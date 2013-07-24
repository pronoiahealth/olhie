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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.loggedinsession.UserLogginEvent;
import com.pronoiahealth.olhie.client.shared.events.loggedinsession.UserLogoutEvent;
import com.pronoiahealth.olhie.client.shared.vo.LoggedInSession;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.LoggedInSessionDAO;

/**
 * LoggedInSessionService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 23, 2013
 * 
 */
@RequestScoped
public class LoggedInSessionService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public LoggedInSessionService() {
	}

	/**
	 * Handle a user log in.
	 * 
	 * @param userLogginEvent
	 */
	protected void observesUserLogginEvent(
			@Observes UserLogginEvent userLogginEvent) {
		try {
			String userId = userLogginEvent.getUserId();
			String erraiSessionId = userLogginEvent.getErraiSessionId();
			String userLastName = userLogginEvent.getUserLastName();
			String userFirstName = userLogginEvent.getUserFirstName();
			
			LoggedInSession sess = LoggedInSessionDAO.addSession(userId,
					erraiSessionId, userFirstName, userLastName, new Date(),
					ooDbTx, true);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}

	/**
	 * @param userLogoutEvent
	 */
	protected void observesUserLogoutEvent(
			@Observes UserLogoutEvent userLogoutEvent) {
		try {
			String erraiSessionId = userLogoutEvent.getErraiSessionId();
			LoggedInSessionDAO.endActiveSessionsByErraiSessionId(
					erraiSessionId, ooDbTx, true);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * Close expired sessions. Will be called from the session tracker. This
	 * should probably be turned into a message bus listener
	 * 
	 * @param expiredErraiSessionIds
	 */
	public static void sessionsExpired(List<String> expiredErraiSessionIds) {

	}

}
