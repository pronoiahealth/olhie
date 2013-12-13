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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookSelectedService.java<br/>
 * Responsibilities:<br/>
 * 1. Watches for a BookListBookSelectedEvent and returns a
 * BookListBookSelectedResponseEvent. The response will indicate if the
 * currently logged in user is the creator or co-author of this book.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 26, 2013
 * 
 */
@RequestScoped
public class BookSelectedService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken serverToken;

	@Inject
	private TempThemeHolder holder;

	@Inject
	private Event<BookListBookSelectedResponseEvent> bookListBookSelectedResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public BookSelectedService() {
	}

	/**
	 * Checks the UserBookRelationship to see if the current user is an author
	 * or co-author of the book for the bookId received in the observed event.
	 * It will return true or false in the BookListBookSelectedResponseEvent.
	 * 
	 * @param bookListBookSelectedEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR, SecurityRoleEnum.REGISTERED,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesBookListBookSelectedEvent(
			@Observes BookListBookSelectedEvent bookListBookSelectedEvent) {
		try {
			String bookId = bookListBookSelectedEvent.getBookId();
			String userId = serverToken.getUserId();
			boolean loggedIn = serverToken.getLoggedIn();

			// If the user has logged in
			// relationship. Otherwise he is an anonymous user and has no
			// relationship

			// Get User Book relationships
			Set<UserBookRelationshipEnum> rels = bookDAO
					.getActiveBookRealtionshipForUser(userId, loggedIn, bookId);

			boolean authorSelected = bookDAO.isAuthorSelected(userId, bookId, rels);
			
			// Get the Book
			BookDisplay bookDisplay = bookDAO.getBookDisplayById(bookId,
					userId, holder, true);

			// Update the last viewed date in the UserBookRelationship
			bookDAO.setLastViewedOnUserBookRelationship(userId,
					bookId, new Date());

			// Fire the event
			bookListBookSelectedResponseEvent
					.fire(new BookListBookSelectedResponseEvent(bookId,
							authorSelected, bookDisplay, rels));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}
}
