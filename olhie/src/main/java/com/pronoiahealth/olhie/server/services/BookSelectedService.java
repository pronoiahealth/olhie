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

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.BookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.BookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
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
	private TempCoverBinderHolder holder;

	@Inject
	private Event<BookListBookSelectedResponseEvent> bookListBookSelectedResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public BookSelectedService() {
	}

	/**
	 * Checks the UserBookRelationship to see if the current user is an author
	 * or co-author of the book for the bookId received in the observed event.
	 * It will will true or false in the BookListBookSelectedResponseEvent.
	 * 
	 * @param bookListBookSelectedEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesBookListBookSelectedEvent(
			@Observes BookListBookSelectedEvent bookListBookSelectedEvent) {
		try {
			String bookId = bookListBookSelectedEvent.getBookId();
			String userId = serverToken.getUserId();

			// If the user has logged in and has a user check the
			// relationship. Otherwise he is an anonymous user and has no
			// relationship
			boolean authorSelected = false;
			if (userId != null && userId.length() > 0) {
				// Get UserBookRelatioship
				OSQLSynchQuery<UserBookRelationship> bQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where bookId = :bId");
				HashMap<String, String> bparams = new HashMap<String, String>();
				bparams.put("bId", bookId);
				List<UserBookRelationship> bResult = ooDbTx.command(bQuery)
						.execute(bparams);
				if (bResult != null && bResult.size() > 0) {
					for (UserBookRelationship r : bResult) {
						if (r.getUserId().equals(userId)) {
							String relationship = r.getUserRelationship();
							if (relationship
									.equals(UserBookRelationshipEnum.CREATOR
											.name())
									|| relationship
											.equals(UserBookRelationshipEnum.COAUTHOR
													.name())) {
								authorSelected = true;
								break;
							}
						}
					}
				}
			}

			// Get the Book
			BookDisplay bookDisplay = BookDAO.getBookDisplayById(bookId,
					ooDbTx, userId, holder);

			// Get User Book relationships
			Set<UserBookRelationshipEnum> rels = BookDAO
					.getActiveBookRealtionshipForUser(userId,
							serverToken.getLoggedIn(), bookId, ooDbTx);

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
