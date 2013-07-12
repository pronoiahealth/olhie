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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.events.comments.CommentEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Comment;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.ServerUserToken;

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
	private ServerUserToken userToken;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

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
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			Comment comment = commentEvent.getComment();
			comment.setSubmittedDate(new Date());
			ooDbTx.save(comment);
			ooDbTx.commit();
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(e.getMessage()));
		}
	}
}
