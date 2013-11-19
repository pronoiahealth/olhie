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

import com.pronoiahealth.olhie.client.shared.events.book.BookdescriptionDetailRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookdescriptionDetailResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;

/**
 * BookdescriptionDetailService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 18, 2013
 * 
 */
@RequestScoped
public class BookdescriptionDetailService {
	@Inject
	private Logger log;

	@Inject
	private Event<BookdescriptionDetailResponseEvent> bookdescriptionDetailResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	/**
	 * Constructor
	 * 
	 */
	public BookdescriptionDetailService() {
	}

	/**
	 * Observes the BookdescriptionDetailRequestEvent that originates from the
	 * NewBookPage. Fires a BookdescriptionDetailResponseEvent which is observed
	 * by the BookDescriptionDetailDialog.
	 * 
	 * @param bookdescriptionDetailRequestEvent
	 */
	protected void observesBookdescriptionDetailRequestEvent(
			@Observes BookdescriptionDetailRequestEvent bookdescriptionDetailRequestEvent) {

		try {

			// Fire the event
			bookdescriptionDetailResponseEvent.fire(new BookdescriptionDetailResponseEvent());
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}
}
