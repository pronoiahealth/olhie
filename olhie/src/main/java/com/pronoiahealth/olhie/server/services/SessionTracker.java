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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.errai.cdi.server.events.EventConversationContext;

import com.pronoiahealth.olhie.client.shared.events.LoggedInPingEvent;

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

	private final ScheduledExecutorService service = Executors
			.newScheduledThreadPool(1);

	/**
	 * Tracks sessionId and the user associated with them
	 */
	private final Map<String, UserSessionToken> activeSessions = new ConcurrentHashMap<String, UserSessionToken>();

	/**
	 * Tracks users and the session they have, a user may have more than one
	 * browser open.
	 */
	private final Map<String, ConcurrentSkipListSet<String>> activeUsers = new ConcurrentHashMap<String, ConcurrentSkipListSet<String>>();

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
					Iterator<UserSessionToken> entryIterator = activeSessions
							.values().iterator();
					while (entryIterator.hasNext()) {
						UserSessionToken user = entryIterator.next();
						if (user.isSessionTimedout()) {
							// Remove user session
							String sessionId = user.getErraiSessionId();
							String userKey = getActiveUserKey(user);
							Set<String> userSessions = activeUsers.get(userKey);
							if (userSessions != null && userSessions.size() > 0) {
								userSessions.remove(sessionId);
							}

							// If user has no session remove them from the
							// active users map
							if (userSessions.size() == 0) {
								activeUsers.remove(userKey);
							}

							// Remove the session from the active sessions map
							entryIterator.remove();
						}
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

		// Add a session the active users session
		ConcurrentSkipListSet<String> sessions = activeUsers.get(getActiveUserKey(user));
		if (sessions == null) {
			sessions = new ConcurrentSkipListSet<String>();
			activeUsers.put(getActiveUserKey(user), sessions);
		}
		sessions.add(erraiSessionId);
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

		// Remove from user sessions
		ConcurrentSkipListSet<String> sessions = activeUsers.get(getActiveUserKey(token));
		if (sessions != null) {
			sessions.remove(erraiSessionId);
		}

		// If the user has no more sessions remove the user from the user
		// sessions
		if (sessions.size() == 0) {
			activeUsers.remove(getActiveUserKey(token));
		}
	}

	/**
	 * Gets the key to use in the activeUsers map.
	 * 
	 * @param tok
	 * @return
	 */
	private String getActiveUserKey(UserSessionToken tok) {
		StringBuilder sb = new StringBuilder();
		return sb.append(tok.getUserFirstName()).append(" ")
				.append(tok.getUserLastName()).append(" (")
				.append(tok.getUserId()).append(")").toString();
	}

	/**
	 * Returns userKey from activeUsers if it starts with the qry string
	 * 
	 * @param qry
	 * @return
	 */
	public List<String> getMatchingActiveUsers(String qry) {
		List<String> retLst = new ArrayList<String>();
		for (String userKey : activeUsers.keySet()) {
			if (userKey.startsWith(qry)) {
				retLst.add(userKey);
			}
		}
		return retLst;
	}

}
