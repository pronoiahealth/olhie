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
package com.pronoiahealth.olhie.server.services.dbaccess;

import java.util.Date;
import java.util.List;

import com.pronoiahealth.olhie.client.shared.vo.CalendarEvent;
import com.pronoiahealth.olhie.client.shared.vo.CalendarRequest;

/**
 * CalendarEventDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 22, 2013
 * 
 */
public interface CalendarEventDAO extends BaseDAO {

	/**
	 * @param startDate
	 * @param EndDate
	 * @return
	 * @throws Exception
	 */
	public List<CalendarEvent> getCalendarEventsBetweenDates(Date startDate,
			Date endDate) throws Exception;


	/**
	 * @param calReq
	 * @return the saved CalendarRequest
	 * @throws Exception
	 */
	public CalendarRequest saveNewCalendarRequest(CalendarRequest calReq)
			throws Exception;
}
