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

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
				Iterator<UserSessionToken> entryIterator = activeSessions
						.values().iterator();
				while (entryIterator.hasNext()) {
					UserSessionToken user = entryIterator.next();
					if (user.isTimedout()) {
						entryIterator.remove();
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
	public void trackUserSession(String userId, String erraiSessionId) {
		UserSessionToken user = new UserSessionToken(userId, erraiSessionId);
		activeSessions.put(erraiSessionId, user);
	}

	/**
	 * Stop tracking a session
	 * 
	 * @param erraiSessionId
	 */
	public void stopTrackingUserSession(String erraiSessionId) {
		activeSessions.remove(erraiSessionId);
	}

}
