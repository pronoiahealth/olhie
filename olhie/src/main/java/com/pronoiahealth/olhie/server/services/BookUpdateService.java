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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.BookUpdateCommittedEvent;
import com.pronoiahealth.olhie.client.shared.events.BookUpdateEvent;
import com.pronoiahealth.olhie.client.shared.events.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
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
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesNewBookUpdateEvent(
			@Observes @New BookUpdateEvent bookUpdateEvent) {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			// Find the user. The user sending this request should be the same
			// one in the session
			String sessionUserId = userToken.getUserId();
			OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
					"select from User where userId = :uId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("uId", sessionUserId);
			List<User> uResult = ooDbTx.command(uQuery).execute(uparams);
			User currentUser = null;
			if (uResult != null && uResult.size() == 1) {
				// Got the user
				currentUser = uResult.get(0);
			} else {
				throw new Exception(
						"Can't find the book creator in the database.");
			}

			// Compare the session user and the one sending the request
			String currentUserId = currentUser.getUserId();
			if (!currentUserId.equals(bookUpdateEvent.getBook().getAuthorId())) {
				throw new Exception(
						"The current user and the author don't match.");
			}

			// Add the book
			Date now = new Date();
			Book book = bookUpdateEvent.getBook();
			book.setCreatedDate(now);
			book.setAuthorId(userToken.getUserId());
			if (book.getActive() == true) {
				book.setActDate(now);
			}
			book = ooDbTx.save(book);
			ooDbTx.commit();

			// Add the UserBookRelationship
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			String bookId = book.getId();
			UserBookRelationship ubRel = new UserBookRelationship();
			ubRel.setActiveRelationship(true);
			ubRel.setBookId(bookId);
			ubRel.setTheBook(book);
			ubRel.setTheUser(currentUser);
			ubRel.setUserId(currentUserId);
			ubRel.setUserRelationship(UserBookRelationshipEnum.CREATOR
					.toString());
			ooDbTx.save(ubRel);
			ooDbTx.commit();

			// Return the result
			BookUpdateCommittedEvent event = new BookUpdateCommittedEvent(
					bookId);
			bookUpdateCommittedEvent.fire(event);
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

}
