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
package com.pronoiahealth.olhie.server.services.dbaccess.orient;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.pronoiahealth.olhie.client.shared.vo.CalendarEvent;
import com.pronoiahealth.olhie.server.services.dbaccess.CalendarEventDAO;

/**
 * OrientCalendarEventDAOImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Oct 22, 2013
 *
 */
public class OrientCalendarEventDAOImpl extends OrientBaseTxDAO implements CalendarEventDAO {

	/**
	 * Constructor
	 *
	 */
	public OrientCalendarEventDAOImpl() {
	}

	/**
	 * 
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.CalendarEventDAO#getCalendarEventsBetweenDates(java.util.Date, java.util.Date)
	 */
	@Override
	public List<CalendarEvent> getCalendarEventsBetweenDates(Date startDate,
			Date endDate) throws Exception {
		OSQLSynchQuery<CalendarEvent> baQuery = new OSQLSynchQuery<CalendarEvent>(
				"select from CalendarEvent where start BETWEEN :startDate and :endDate");
		HashMap<String, Object> baparams = new HashMap<String, Object>();
		baparams.put("startDate", startDate);
		baparams.put("endDate", endDate);
		List<CalendarEvent> baResult = ooDbTx.command(baQuery).execute(
				baparams);
		if (baResult != null && baResult.size() > 0) {
			return createDetachedRetLst(baResult);
		} else {
			return baResult;
		}
		
	}

}
