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
package com.pronoiahealth.olhie.client.shared.events.eventcalendar;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.vo.CalendarRequest;

/**
 * CalendarRequestSaveEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired From : EventCalendarRequestDialog<br/>
 * Observed By: EventCalendarService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 8, 2013
 * 
 */
@Portable
@Conversational
public class CalendarRequestSaveEvent {
	private CalendarRequest calendarRequest;

	/**
	 * Constructor
	 * 
	 */
	public CalendarRequestSaveEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param calendarRequest
	 */
	public CalendarRequestSaveEvent(CalendarRequest calendarRequest) {
		super();
		this.calendarRequest = calendarRequest;
	}

	public CalendarRequest getCalendarRequest() {
		return calendarRequest;
	}

	public void setCalendarRequest(CalendarRequest calendarRequest) {
		this.calendarRequest = calendarRequest;
	}
}
