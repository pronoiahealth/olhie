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
package com.pronoiahealth.olhie.client.pages.events;

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

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();

		// Add background
		setPageBackgroundClass("ph-Events-Background");

		// Add scrolling
		addFullPageScrolling();

		// Load calendar
		addCalendar();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		
		// Remove the calendar
		destroyCalendar();
	}

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

	private native void destroyCalendar() /*-{
		$wnd.jQuery('#olhieEventsCalendar').fullCalendar("destroy");
	}-*/;

}
