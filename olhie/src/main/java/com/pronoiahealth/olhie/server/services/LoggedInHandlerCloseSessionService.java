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

import javax.inject.Inject;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.server.annotations.Service;

import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.vo.UserSession;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.services.dbaccess.LoggedInSessionDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.OfferDAO;

/**
 * LoggedInHandlerCloseSessionService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 23, 2013
 * 
 */
@Service
public class LoggedInHandlerCloseSessionService implements MessageCallback {
	@Inject
	private Logger log;

	@Inject
	@DAO
	private OfferDAO offerDAO;

	@Inject
	@DAO
	private LoggedInSessionDAO loggedInSessionDAO;

	/**
	 * Constructor
	 * 
	 * @param dispatcher
	 */
	@Inject
	public LoggedInHandlerCloseSessionService() {
	}

	/**
	 * Expire sessions based on Errai Session Id
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
				// Take care of sync'ing the LoggedInSession
				loggedInSessionDAO
						.endActiveSessionsByErraiSessionId(erraiSessionId);

				// Close offers
				offerDAO.closeOfferByUserId(userId, erraiSessionId,
						OfferTypeEnum.CHAT);
			} catch (Exception e) {
				log.log(Level.SEVERE, "Error ending session with session id "
						+ erraiSessionId + " and  user id " + userId, e);
			}
		}
	}
}
