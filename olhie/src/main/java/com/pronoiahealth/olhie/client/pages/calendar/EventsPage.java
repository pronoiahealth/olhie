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
package com.pronoiahealth.olhie.client.pages.calendar;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.pronoiahealth.olhie.client.navigation.AnonymousRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;

/**
 * EventsPage.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 21, 2013
 * 
 */
@Templated("#root")
@Page(role = { AnonymousRole.class })
public class EventsPage extends AbstractPage {

	/**
	 * Constructor
	 * 
	 */
	public EventsPage() {
	}

	/**
	 * Always call the super method. Adds the jQuery calendar widget to the
	 * screen and initializes it.
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.AbstractPage#onLoad()
	 */
	@Override
	protected void onLoad() {
		super.onLoad();

		// Add background
		setPageBackgroundClass("ph-Events-Background");

		// Add scrolling
		addFullPageScrolling();

		// Load calendar
		addCalendar();
	}

	/**
	 * Destroys the jQuery calendar object
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.AbstractPage#onUnload()
	 */
	@Override
	protected void onUnload() {
		super.onUnload();

		// Remove the calendar
		destroyCalendar();
	}

	/**
	 * Native method to attach calendar
	 */
	private native void addCalendar() /*-{
		$wnd.jQuery('#olhieEventsCalendar').fullCalendar({
			header : {
				left : "prev,next today",
				center : "title",
				right : "month,basicWeek,basicDay"
			},
			editable : false,
			events : "rest/calendar-event/events"
		});

	}-*/;

	/**
	 * Native method to destroy calendar
	 */
	private native void destroyCalendar() /*-{
		$wnd.jQuery('#olhieEventsCalendar').fullCalendar("destroy");
	}-*/;

}
