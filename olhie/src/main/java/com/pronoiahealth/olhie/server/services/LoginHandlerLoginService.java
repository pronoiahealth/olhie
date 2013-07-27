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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.bus.server.annotations.Service;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.client.shared.vo.UserSession;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.services.dbaccess.LoggedInSessionDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.UserDAO;

@Service
public class LoginHandlerLoginService implements MessageCallback {
	@Inject
	private Logger log;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	private RequestDispatcher dispatcher;

	/**
	 * Constructor
	 * 
	 * @param dispatcher
	 */
	@Inject
	public LoginHandlerLoginService(RequestDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * ECreates a new session in the LoggedInSession entity
	 * 
	 * @see org.jboss.errai.bus.client.api.messaging.MessageCallback#callback(org.jboss.errai.bus.client.api.messaging.Message)
	 */
	@Override
	public void callback(Message message) {
		UserSession us = message.get(UserSession.class, "UserSession");
		if (us != null) {
			String userId = us.getUserId();
			String erraiSessionId = us.getSessionId();
			try {
				// Get the user
				User user = UserDAO.getUserByUserId(userId, ooDbTx);

				// Create the row
				LoggedInSessionDAO.addSession(userId, erraiSessionId,
						user.getFirstName(), user.getLastName(), new Date(),
						ooDbTx, true);

			} catch (Exception e) {
				log.log(Level.SEVERE, "Error creating session with session id "
						+ erraiSessionId + " and  user id " + userId, e);
			}
		}
	}
}
