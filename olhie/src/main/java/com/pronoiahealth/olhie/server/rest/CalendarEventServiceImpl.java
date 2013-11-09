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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.vo.CalendarEvent;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.services.dbaccess.CalendarEventDAO;

/**
 * CalendarEventServiceImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 22, 2013
 * 
 */
@Path("/calendar-event")
public class CalendarEventServiceImpl implements CalendarEventService {

	/**
	 * Protect non thread safe SimpleDateFormat that returns and ISO date format
	 */
	private static final ThreadLocal<SimpleDateFormat> dtFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		}
	};

	@Inject
	private Logger log;

	@Inject
	@DAO
	private CalendarEventDAO calendarEventDAO;

	/**
	 * Constructor
	 * 
	 */
	@Inject
	public CalendarEventServiceImpl() {
	}

	@Override
	@GET
	@Path("/events")
	@Produces({ "application/json" })
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	public Response getCalendarEvents(@QueryParam("start") String start,
			@QueryParam("end") String end) throws ServletException {

		try {
			long startUnixTimestamp = Long.parseLong(start);
			long endUnixTimestamp = Long.parseLong(end);
			Date startDate = new Date(startUnixTimestamp * 1000L);
			Date endDate = new Date(endUnixTimestamp * 1000L);

			// Get the list of events
			List<CalendarEvent> events = calendarEventDAO
					.getCalendarEventsBetweenDates(startDate, endDate);

			// Build and return response
			return Response.ok(this.convertToJSON(events),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"Throwing servlet exception for unhandled exception", e);
			if (e instanceof ServletException) {
				throw (ServletException) e;
			} else {
				throw new ServletException(e.getMessage());
			}
		}
	}

	/**
	 * Convert the list to a custom json string
	 * 
	 * @param events
	 * @return
	 */
	private String convertToJSON(List<CalendarEvent> events) {
		Gson gson = new Gson();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (CalendarEvent evt : events) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", evt.getTitle());
			map.put("allDay", evt.getAllDay());
			map.put("start", dtFormat.get().format(evt.getStart()));
			map.put("end", dtFormat.get().format(evt.getEnd()));
			mapList.add(map);
		}
		String json = gson.toJson(mapList);
		return json;
	}
}
