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

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.RemoveBookFromMyCollectionEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.RemoveBookFromMyCollectionEvent.REMOVE_RESPONSE_TYPE;
import com.pronoiahealth.olhie.client.shared.events.bookcase.RemoveBookFromMyCollectionResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * RemoveBookFromMyCollectionService.java<br/>
 * Responsibilities:<br/>
 * 1. Removes mycollection relationship<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 30, 2013
 * 
 */
@RequestScoped
public class RemoveBookFromMyCollectionService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private Event<BookFindResponseEvent> bookFindResponseEvent;

	@Inject
	private Event<RemoveBookFromMyCollectionResponseEvent> removeBookFromMyCollectionResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private TempThemeHolder holder;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public RemoveBookFromMyCollectionService() {
	}

	/**
	 * Watches for the remove event and inactivates the relationship. Will send
	 * back a BookFindResponseEvent to resync the display
	 * 
	 * @param emoveBookFromMyCollectionEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesRemoveBookFromMyCollectionEvent(
			@Observes RemoveBookFromMyCollectionEvent removeBookFromMyCollectionEvent) {

		try {
			// Remove it
			String userId = userToken.getUserId();
			String bookId = removeBookFromMyCollectionEvent.getBookId();
			bookDAO.removeBookFromMyCollection(userId, bookId);

			if (removeBookFromMyCollectionEvent.getResponseType() == REMOVE_RESPONSE_TYPE.FIND_REMOVE_RESPONSE) {
				// Return a BookFindResponseEvent
				// Get the book display
				BookDisplay bookDisplay = bookDAO.getBookDisplayById(bookId,
						userId, holder, true);

				// Get the user relations
				Set<UserBookRelationshipEnum> rels = bookDAO
						.getActiveBookRealtionshipForUser(userId,
								userToken.getLoggedIn(), bookId);

				// Is the user asking for the book the author or co-author
				boolean authorSelected = bookDAO.isAuthorSelected(userId,
						bookId, rels);

				// Fire the event
				bookFindResponseEvent.fire(new BookFindResponseEvent(
						bookDisplay, rels, authorSelected));
			} else {
				removeBookFromMyCollectionResponseEvent
						.fire(new RemoveBookFromMyCollectionResponseEvent(
								bookId));
			}
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}
}
