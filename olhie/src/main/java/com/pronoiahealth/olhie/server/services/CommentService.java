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

import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.events.comments.CommentEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * CommentService.java<br/>
 * Responsibilities:<br/>
 * 1. Saves comments by responding to the CommentEvent<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@RequestScoped
public class CommentService {

	@Inject
	private Logger log;

	@Inject
	@DAO
	private BookDAO bookDAO;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	/**
	 * Constructor
	 * 
	 */
	public CommentService() {
	}

	/**
	 * When a new comment is received add it to the database
	 * 
	 * @param commentEvent
	 */
	protected void observesCommentEvent(@Observes @New CommentEvent commentEvent) {
		try {
			bookDAO.saveComment(commentEvent.getComment());
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}
}
