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

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.eventcalendar.CalendarRequestSaveEvent;
import com.pronoiahealth.olhie.client.shared.events.eventcalendar.UserEmailRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.eventcalendar.UserEmailResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.CalendarRequest;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.CalendarEventDAO;

/**
 * EventCalendarService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 8, 2013
 * 
 */
@RequestScoped
public class EventCalendarService {

	@Inject
	private Logger log;

	@Inject
	@DAO
	private CalendarEventDAO calendarEventDAO;

	@Inject
	private Event<UserEmailResponseEvent> userEmailResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private ServerUserToken userToken;

	/**
	 * Constructor
	 * 
	 */
	public EventCalendarService() {
	}

	/**
	 * Get the currently logged in users email and fire back in a
	 * UserEmailResponseEvent
	 * 
	 * @param userEmailRequestEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR, SecurityRoleEnum.REGISTERED })
	protected void observersUserEmailRequestEvent(
			@Observes UserEmailRequestEvent userEmailRequestEvent) {
		try {
			User user = calendarEventDAO.getUserByUserId(userToken.getUserId());
			userEmailResponseEvent.fire(new UserEmailResponseEvent(user
					.getEmail()));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * Saves a new calendar event request
	 * 
	 * @param calendarRequestSaveEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR, SecurityRoleEnum.REGISTERED })
	protected void observesCalendarRequestSaveEvent(
			@Observes CalendarRequestSaveEvent calendarRequestSaveEvent) {
		try {
			// Quick check to see if there is any funny business
			String userId = userToken.getUserId();
			CalendarRequest cr = calendarRequestSaveEvent.getCalendarRequest();
			if (!userId.equals(cr.getRequestorUserId()) == true) {
				throw new Exception(
						"Logged in user is not the user who sent the request?");
			}

			// Process the save
			calendarEventDAO.saveNewCalendarRequest(cr);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

}
