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

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.book.AddBookToMyCollectionEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.UserBookRelationshipDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.UserDAO;

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
	private TempCoverBinderHolder holder;

	@Inject
	private Event<BookFindResponseEvent> bookFindResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

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
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesAddBookToMyCollectionEvent(
			@Observes AddBookToMyCollectionEvent addBookToMyCollectionEvent) {

		try {
			// What is the session users current role?
			String role = userToken.getRole();
			String userId = userToken.getUserId();
			boolean loggedIn = userToken.getLoggedIn();
			String bookId = addBookToMyCollectionEvent.getBookId();

			if (!role.equals(SecurityRoleEnum.ANONYMOUS.toString())
					&& loggedIn == true && userId.length() > 0) {

				// Need the Book
				Book book = BookDAO.getBookById(bookId, ooDbTx);

				// Find user
				User user = UserDAO.getUserByUserId(book.getAuthorId(), ooDbTx);

				// Get UserBookRelatioship
				Set<UserBookRelationshipEnum> rResult = UserBookRelationshipDAO
						.getUserBookRelationshipByUserIdBookId(bookId, userId,
								true, ooDbTx);

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
					UserBookRelationship rel = new UserBookRelationship();
					rel.setActiveRelationship(true);
					rel.setUserRelationship(UserBookRelationshipEnum.MYCOLLECTION
							.toString());
					rel.setBookId(bookId);
					rel.setTheBook(book);
					rel.setTheUser(user);
					rel.setUserId(userId);
					rel.setEffectiveDate(now);
					rel.setLastViewedDate(now);
					ooDbTx.save(rel);
				}
			}

			// Return a BookFindResponseEvent
			// Get the book display
			BookDisplay bookDisplay = BookDAO.getBookDisplayById(bookId,
					ooDbTx, userId, holder);

			// Get the user relations
			Set<UserBookRelationshipEnum> rels = BookDAO
					.getActiveBookRealtionshipForUser(userId,
							userToken.getLoggedIn(), bookId, ooDbTx);
			
			// Is the user asking for the book the author or co-author
			boolean authorSelected = BookDAO.isAuthorSelected(userId, bookId,
					ooDbTx);

			// Fire the event
			bookFindResponseEvent.fire(new BookFindResponseEvent(bookDisplay,
					rels, authorSelected));

		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}
}
