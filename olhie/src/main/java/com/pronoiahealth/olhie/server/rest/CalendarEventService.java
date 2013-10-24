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
package com.pronoiahealth.olhie.server.rest;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;

/**
 * CalendarEventService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 22, 2013
 * 
 */
public interface CalendarEventService {

	/**
	 * Respond with calendar events
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @throws ServletException
	 */
	public Response getCalendarEvents(String start, String end)
			throws ServletException;

}
