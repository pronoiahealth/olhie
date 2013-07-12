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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.cdi.server.events.EventConversationContext;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.dataaccess.vo.Password;
import com.pronoiahealth.olhie.server.security.SecurityUtils;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.PasswordDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.UserDAO;

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
			try {
				user = UserDAO.getUserByUserId(loginRequestEvent.getUserID(),
						ooDbTx);
			} catch (Exception e) {
				loginErrorEvent
						.fire(new LoginErrorEvent("User ID is not valid"));
			}

			// Password check
			// 1. Look up pwd
			Password pwd = null;
			try {
				pwd = PasswordDAO.getPwdByUserId(user.getUserId(), ooDbTx);
			} catch (Exception e) {
				// Fire an error response
				loginErrorEvent
						.fire(new LoginErrorEvent(
								"Could not find a password for you. Please contact the administrator."));
			}

			if (SecurityUtils.validatePassword(loginRequestEvent.getPwd(),
					pwd.getPwdDigest(), pwd.getPwdSalt(), false) == false) {
				loginErrorEvent.fire(new LoginErrorEvent(
						"Password does not match."));
			} else {
				String firstName = user.getFirstName();
				String lastName = user.getLastName();
				// Set up token
				userToken.setUserFirstName(firstName);
				userToken.setUserLastName(lastName);
				userToken.setLoggedIn(true);
				userToken.setUserId(user.getUserId());
				userToken.setRole(user.getRole());

				// Update the sessionTracker
				String sessionId = EventConversationContext.get()
						.getSessionId();
				sessionTracker.trackUserSession(user.getUserId(), lastName,
						firstName, sessionId);

				// Fire a positive response
				loginResponseEvent.fire(new LoginResponseEvent(user));
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);

			// Tell the user what the exception was
			loginErrorEvent.fire(new LoginErrorEvent(e.getMessage()));
		}
	}
}
