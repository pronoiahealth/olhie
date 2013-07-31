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

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.UserSession;
import com.pronoiahealth.olhie.server.dataaccess.orient.OrientFactory;
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
	private OrientFactory oFac;

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
			OObjectDatabaseTx ooDbTx = oFac.getUninjectedConnection();
			// if (log.isLoggable(Level.FINEST)) {
			log.log(Level.INFO, "Aquired connection " + ooDbTx.hashCode()
					+ " for bean " + LoggedInHandlerCloseSessionService.class.getName());
			// }
			try {
				// Take care of sync'ing the LoggedInSession
				LoggedInSessionDAO.endActiveSessionsByErraiSessionId(
						erraiSessionId, ooDbTx, true);

				// Close offers
				OfferDAO.closeOfferByUserId(userId, erraiSessionId, ooDbTx,
						true);
			} catch (Exception e) {
				log.log(Level.SEVERE, "Error ending session with session id "
						+ erraiSessionId + " and  user id " + userId, e);
			} finally {
				if (ooDbTx != null) {
					// if (log.isLoggable(Level.FINEST)) {
					log.log(Level.INFO, "Released connection " + ooDbTx.hashCode());
					// }
					
					ooDbTx.close();
				}
			}
		}
	}
}
