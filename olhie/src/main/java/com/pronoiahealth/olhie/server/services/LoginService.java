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

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.cdi.server.events.EventConversationContext;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.events.LoginErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.LoginRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.LoginResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.dataaccess.vo.Password;
import com.pronoiahealth.olhie.server.security.SecurityUtils;
import com.pronoiahealth.olhie.server.security.ServerUserToken;

/**
 * Login Service is a RequestScoped component<br/>
 * Responsibilities:<br/>
 * 1. Respond to the LoginRequestEvent<br/>
 * 2. On successful login fire the LoginResponseEvent<br/>
 * 3. On error fire the LoginErrorEvent<br/>
 * 
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 25, 2013
 * 
 */
@RequestScoped
public class LoginService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private SessionTracker sessionTracker;

	@Inject
	private Event<LoginErrorEvent> loginErrorEvent;

	@Inject
	private Event<LoginResponseEvent> loginResponseEvent;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Default Constructor
	 * 
	 */
	public LoginService() {
	}

	/**
	 * Responds to the LoginRequestEvent. Successful logins result in the
	 * LoginResponseEvent being fired. Errors result in the LoginErrorEvent
	 * being fired.
	 * 
	 * @param loginRequestEvent
	 */
	public void observesLoginRequestEvent(
			@Observes LoginRequestEvent loginRequestEvent) {
		User user = null;
		try {
			// User check
			// 1. Look up user
			OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
					"select from User where userId = :uId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("uId", loginRequestEvent.getUserID());
			List<User> uResult = ooDbTx.command(uQuery).execute(uparams);
			if (uResult != null && uResult.size() == 1) {
				// Got the user
				user = uResult.get(0);
			} else {
				loginErrorEvent
						.fire(new LoginErrorEvent("User ID is not valid"));
			}

			// Password check
			// 1. Look up pwd
			OSQLSynchQuery<Password> query = new OSQLSynchQuery<Password>(
					"select from Password where userId = :uId");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("uId", loginRequestEvent.getUserID());
			List<Password> pResult = ooDbTx.command(query).execute(params);
			if (pResult != null && pResult.size() == 1) {
				// 2. Check password validity
				Password pwd = pResult.get(0);
				if (SecurityUtils.validatePassword(loginRequestEvent.getPwd(),
						pwd.getPwdDigest(), pwd.getPwdSalt(), false) == false) {
					loginErrorEvent.fire(new LoginErrorEvent(
							"Password does not match."));
				} else {
					// Set up token
					userToken.setUserFirstName(user.getFirstName());
					userToken.setUserLastName(user.getLastName());
					userToken.setLoggedIn(true);
					userToken.setUserId(user.getUserId());
					userToken.setRole(user.getRole());

					// Update the sessionTracker
					String sessionId = EventConversationContext.get().getSessionId();
					sessionTracker.trackUserSession(user.getUserId(), sessionId);
					
					// Fire a positive response
					loginResponseEvent.fire(new LoginResponseEvent(user));
				}
			} else {
				// Fire an error response
				loginErrorEvent
						.fire(new LoginErrorEvent(
								"Could not find a password for you. Please contact the administrator."));
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			
			// Tell the user what the exception was
			loginErrorEvent.fire(new LoginErrorEvent(e.getMessage()));
		}
	}
}
