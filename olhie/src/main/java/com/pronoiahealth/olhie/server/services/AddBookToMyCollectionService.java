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
import com.pronoiahealth.olhie.client.shared.events.book.BookFindResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.AddBookToMyCollectionEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.AddBookToMyCollectionEvent.ADD_RESPONSE_TYPE;
import com.pronoiahealth.olhie.client.shared.events.bookcase.AddBookToMyCollectionResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * AddBookToMyCollectionService.java<br/>
 * Responsibilities:<br/>
 * 1. Watches for the AddBookToMyCollectionEvent. If the book is not currently
 * in your collection then its added the UserBookRelationship entity as a my
 * collection relationship. If you are the creator or co-author, you can't also
 * be related to the book as a my collection relationship. Also, since Anonymous
 * users don't have any collections, they can't have relationships with book.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 29, 2013
 * 
 */
@RequestScoped
public class AddBookToMyCollectionService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private TempThemeHolder holder;

	@Inject
	private Event<BookFindResponseEvent> bookFindResponseEvent;

	@Inject
	private Event<AddBookToMyCollectionResponseEvent> addBookToMyCollectionResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public AddBookToMyCollectionService() {
	}

	/**
	 * Watches for the AddBookToMyCollectionEvent.
	 * 
	 * @param addBookToMyCollectionEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesAddBookToMyCollectionEvent(
			@Observes AddBookToMyCollectionEvent addBookToMyCollectionEvent) {

		try {
			// What is the session users current role?
			String role = userToken.getRole();
			String userId = userToken.getUserId();
			boolean loggedIn = userToken.getLoggedIn();
			String bookId = addBookToMyCollectionEvent.getBookId();
			Book book = null;

			if (!role.equals(SecurityRoleEnum.ANONYMOUS.toString())
					&& loggedIn == true && userId.length() > 0) {

				// Need the Book
				book = bookDAO.getBookById(bookId);

				// Get UserBookRelatioship
				Set<UserBookRelationshipEnum> rResult = bookDAO
						.getUserBookRelationshipByUserIdBookId(bookId, userId,
								true);

				// If the user already has an active relationship of the
				// following
				// types don't add it.
				boolean addRelationship = true;
				if (rResult.contains(UserBookRelationshipEnum.CREATOR)
						|| rResult.contains(UserBookRelationshipEnum.COAUTHOR) == true
						|| rResult
								.contains(UserBookRelationshipEnum.MYCOLLECTION) == true) {
					addRelationship = false;
				}

				// Add the relationship if the user doesn't currently have an
				// active appropriate relationship
				if (addRelationship == true) {
					Date now = new Date();
					bookDAO.addBookToUserCollection(now, true,
							UserBookRelationshipEnum.MYCOLLECTION, bookId,
							userId, now, now);
				}

				if (addBookToMyCollectionEvent.getResponseType() == ADD_RESPONSE_TYPE.FIND_ADD_RESPONSE) {
					// Return a BookFindResponseEvent
					// Get the book display
					BookDisplay bookDisplay = bookDAO.getBookDisplayByBook(
							book, userId, holder, true);

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
					addBookToMyCollectionResponseEvent
							.fire(new AddBookToMyCollectionResponseEvent(bookId));
				}
			} else {
				serviceErrorEvent
						.fire(new ServiceErrorEvent(
								"You can't create a relationship with this book. Please sign-in."));
			}

		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}
}
