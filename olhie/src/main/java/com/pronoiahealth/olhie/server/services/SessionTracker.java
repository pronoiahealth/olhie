/*******************************************************************************
 * Copyright (c) 2013 Pronoia Health LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pronoia Health LLC - initial API and implementation
 *******************************************************************************/
package com.pronoiahealth.olhie.server.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.cdi.server.events.EventConversationContext;

import com.pronoiahealth.olhie.client.shared.events.loginout.LoggedInPingEvent;
import com.pronoiahealth.olhie.client.shared.vo.UserSession;

/**
 * SessionTracker.java<br/>
 * Responsibilities:<br/>
 * 1. Tracks logged in users sessions for the purposes of various user
 * communication features such as chat.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 1, 2013
 * 
 */
@ApplicationScoped
public class SessionTracker {

	@Inject
	private RequestDispatcher dispatcher;

	private static final String ExpireListener = "LoggedInHandleExpiredSessionsService";
	
	private static final String CloseListener = "LoggedInHandlerCloseSessionService";
	
	private static final String LoginListener = "LoginHandlerLoginService";

	private final ScheduledExecutorService service = Executors
			.newScheduledThreadPool(1);

	/**
	 * Tracks sessionId and the user associated with them
	 */
	private final Map<String, UserSessionToken> activeSessions = new ConcurrentHashMap<String, UserSessionToken>();

	public SessionTracker() {
	}

	/**
	 * Cleans up the active session every 10 seconds
	 */
	@PostConstruct
	private void postConstruct() {

		// Run every 10 seconds
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (activeSessions.size() > 0) {
					// Create list of expired sessions
					List<UserSession> eSess = new ArrayList<UserSession>();

					Iterator<UserSessionToken> entryIterator = activeSessions
							.values().iterator();
					while (entryIterator.hasNext()) {
						UserSessionToken user = entryIterator.next();
						if (user.isSessionTimedout()) {
							// Remove user session
							String sessionId = user.getErraiSessionId();
							String userId = user.getUserId();

							// Add to list
							eSess.add(new UserSession(userId, sessionId));

							// Remove the session from the active sessions map
							entryIterator.remove();
						}
					}

					// Expire any collected sessions
					if (eSess.size() > 0) {
						expireSessions(eSess);
					}
				}
			}
		}, 0, 10, TimeUnit.SECONDS);
	}

	@PreDestroy
	private void preDestroy() {
		service.shutdownNow();
	}

	/**
	 * Gets the UserSessionToken form the context of the current event.
	 * 
	 * @return
	 */
	private UserSessionToken getUser() {
		final String sessionId = EventConversationContext.get().getSessionId();
		return activeSessions.get(sessionId);
	}

	/**
	 * Observes for the LoggedInPingEvent. When it is gotten the
	 * UserSessionToken is updated.
	 * 
	 * @param ping
	 */
	protected void observesPing(@Observes LoggedInPingEvent ping) {
		final UserSessionToken user = getUser();
		if (user != null) {
			user.activity();
		}
	}

	/**
	 * Start tracking the users session
	 * 
	 * @param userId
	 * @param erraiSessionId
	 */
	public void trackUserSession(String userId, String userLastName,
			String userFirstName, String erraiSessionId) {
		UserSessionToken user = new UserSessionToken(userId, userLastName,
				userFirstName, erraiSessionId);

		// Add an active session
		activeSessions.put(erraiSessionId, user);
		
		// Tell the LoggInSession Entity about it
		loginSession(new UserSession(userId, erraiSessionId));
	}

	/**
	 * Stop tracking a session
	 * 
	 * @param erraiSessionId
	 */
	public void stopTrackingUserSession(String erraiSessionId) {
		// Remove from active session
		UserSessionToken token = activeSessions.get(erraiSessionId);
		activeSessions.remove(erraiSessionId);

		// Expire the session
		closeSession(new UserSession(token.getUserId(),
				token.getErraiSessionId()));
	}

	/**
	 * Is the sessionId still in the map;
	 * 
	 * @param sessionId
	 * @return
	 */
	public boolean isSessionActive(String sessionId) {
		return activeSessions.containsKey(sessionId);
	}

	/**
	 * Send the list of expired session to the
	 * LoggedInHandleExpiredSessionsService
	 * 
	 * @param eSess
	 */
	private void expireSessions(List<UserSession> eSess) {
		MessageBuilder.createMessage().toSubject(ExpireListener).signalling()
				.with("ExpiredSessionsList", eSess).noErrorHandling()
				.sendNowWith(dispatcher);
	}

	/**
	 * Closes a LoggedInSession in the db
	 * 
	 * @param sess
	 */
	private void closeSession(UserSession sess) {
		MessageBuilder.createMessage().toSubject(CloseListener).signalling()
				.with("UserSession", sess).noErrorHandling()
				.sendNowWith(dispatcher);
	}
	
	/**
	 * Causes the creation of a row in the db
	 * 
	 * @param sess
	 */
	private void loginSession(UserSession sess) {
		MessageBuilder.createMessage().toSubject(LoginListener).signalling()
		.with("UserSession", sess).noErrorHandling()
		.sendNowWith(dispatcher);
	}

}
