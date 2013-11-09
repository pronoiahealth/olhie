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
package com.pronoiahealth.olhie.client.shared.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

 /**
 * CalendarEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Oct 22, 2013
 *
 */
@Portable
public class CalendarEvent {
	
	@OId
	private String id;

	@OVersion
	private Long version;
	
	@NotNull
	@Size(min = 1, max = 300, message = "Must be between 1 and 300 characters")
	private String title;	
	
	private Boolean allDay;	
	
	@NotNull
	private Date start;	
	
	@NotNull
	private Date end;	

	/**
	 * Constructor
	 *
	 */
	public CalendarEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param title
	 * @param allDay
	 * @param start
	 * @param end
	 */
	public CalendarEvent(String title, Boolean allDay, Date start, Date end) {
		super();
		this.title = title;
		this.allDay = allDay;
		this.start = start;
		this.end = end;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getId() {
		return id;
	}

	public Boolean getAllDay() {
		return allDay;
	}

	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}

}
