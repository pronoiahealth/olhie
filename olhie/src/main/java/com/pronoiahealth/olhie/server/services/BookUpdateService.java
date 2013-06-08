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
import com.pronoiahealth.olhie.client.shared.events.BookUpdateCommittedEvent;
import com.pronoiahealth.olhie.client.shared.events.BookUpdateEvent;
import com.pronoiahealth.olhie.client.shared.events.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.ServerUserToken;

/**
 * BookUpdateService.java<br/>
 * Responsibilities:<br/>
 * 1. Handle book adds and updates<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 7, 2013
 * 
 */
@RequestScoped
public class BookUpdateService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private Event<BookUpdateCommittedEvent> bookUpdateCommittedEvent;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public BookUpdateService() {
	}

	/**
	 * Watches for a new book
	 * 
	 * @param bookUpdateEvent
	 */
	protected void observesNewBookUpdateEvent(
			@Observes @New BookUpdateEvent bookUpdateEvent) {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			Book book = bookUpdateEvent.getBook();
			book.setPublishedDate(new Date());
			book.setAuthorId(userToken.getUserId());
			ooDbTx.save(book);
			ooDbTx.commit();
			bookUpdateCommittedEvent.fire(new BookUpdateCommittedEvent());
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

}
