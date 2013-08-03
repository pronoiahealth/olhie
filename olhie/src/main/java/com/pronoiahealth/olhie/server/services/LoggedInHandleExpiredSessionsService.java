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

import java.util.List;
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
 * LoggedInHandleExpiredSessionsService.java<br/>
 * Responsibilities:<br/>
 * 1. Part of the session tracking function. Handles messages that cause any
 * chat outstanding offers to be closed and the LoggedInSession matching an
 * errai session id to be marked closed.<br/>
 * 
 * <p>
 * Notice that OrientFactory is injected and used to directly get database
 * connections. As the connections are not injected they must be closed in the
 * code. This component is not a RequestScoped components as it can be invoked
 * outside of a request.
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 23, 2013
 * 
 */
@Service
public class LoggedInHandleExpiredSessionsService implements MessageCallback {
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
	public LoggedInHandleExpiredSessionsService() {
	}

	/**
	 * Expire sessions based on Errai Session Id. This can happen if a logged in
	 * user closes the browser without logging off first. This method will mark
	 * the LoggedInSession as closed and will mark any outstanding CHAT offers
	 * as exoired.
	 * 
	 * @see org.jboss.errai.bus.client.api.messaging.MessageCallback#callback(org.jboss.errai.bus.client.api.messaging.Message)
	 */
	@Override
	public void callback(Message message) {
		List expiredSessionsList = message.get(List.class,
				"ExpiredSessionsList");
		if (expiredSessionsList != null && expiredSessionsList.size() > 0) {
			for (Object obj : expiredSessionsList) {
				UserSession us = (UserSession) obj;
				String userId = us.getUserId();
				String erraiSessionId = us.getSessionId();
				OObjectDatabaseTx ooDbTx = oFac.getUninjectedConnection();
				if (log.isLoggable(Level.FINEST)) {
					log.log(Level.INFO,
							"Aquired connection "
									+ ooDbTx.hashCode()
									+ " for bean "
									+ LoggedInHandleExpiredSessionsService.class
											.getName());
				}

				try {
					// Take care of sync'ing the LoggedInSession
					LoggedInSessionDAO.endActiveSessionsByErraiSessionId(
							erraiSessionId, ooDbTx, true);

					// Expire any outstanding offers
					OfferDAO.expireOfferByUserId(userId, ooDbTx, true);

				} catch (Exception e) {
					log.log(Level.SEVERE,
							"Error ending session with session id "
									+ erraiSessionId + " and  user id "
									+ userId, e);
				} finally {
					if (ooDbTx != null) {
						if (log.isLoggable(Level.FINEST)) {
							log.log(Level.INFO,
									"Released connection " + ooDbTx.hashCode());
						}
						ooDbTx.close();
					}
				}
			}
		}
	}

}
