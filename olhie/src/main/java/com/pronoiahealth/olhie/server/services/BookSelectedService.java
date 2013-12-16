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
import com.pronoiahealth.olhie.client.shared.events.bookcase.BookcaseBookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.BookcaseBookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookSelectedService.java<br/>
 * Responsibilities:<br/>
 * 1. Watches for a BookListBookSelectedEvent and
 * BookcaseBookListBookSelectedEvent and returns a
 * BookListBookSelectedResponseEvent or
 * BookcaseBookListBookSelectedResponseEvent. The response will indicate if the
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
	private Event<BookcaseBookListBookSelectedResponseEvent> bookcaseBookListBookSelectedResponseEvent;

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
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED, SecurityRoleEnum.ANONYMOUS })
	protected void observesBookListBookSelectedEvent(
			@Observes BookListBookSelectedEvent bookListBookSelectedEvent) {
		try {
			processRequest(bookListBookSelectedEvent.getBookId(), false);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * @param bookListBookSelectedEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED, SecurityRoleEnum.ANONYMOUS })
	protected void observesBookcaseBookListBookSelectedEvent(
			@Observes BookcaseBookListBookSelectedEvent bookListBookSelectedEvent) {
		try {
			processRequest(bookListBookSelectedEvent.getBookId(), true);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}

	/**
	 * Processes the request. Basically the same processing for both events.
	 * Done this way to get around CDI event issues on front end where 2
	 * functionally different components might receive the same message before
	 * one has a chance to be destroyed.
	 * 
	 * @param bookId
	 * @return
	 * @throws Exception
	 */
	private void processRequest(String bookId, boolean fireForBookcase)
			throws Exception {
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
		BookDisplay bookDisplay = bookDAO.getBookDisplayById(bookId, userId,
				holder, true);

		// Update the last viewed date in the UserBookRelationship
		bookDAO.setLastViewedOnUserBookRelationship(userId, bookId, new Date());

		// Return for bookcase or booklist
		if (fireForBookcase == true) {
			// Fire the event
			bookcaseBookListBookSelectedResponseEvent
					.fire(new BookcaseBookListBookSelectedResponseEvent(bookId,
							authorSelected, bookDisplay, rels));
		} else {
			// Fire the event
			bookListBookSelectedResponseEvent
					.fire(new BookListBookSelectedResponseEvent(bookId,
							authorSelected, bookDisplay, rels));
		}
	}

}
