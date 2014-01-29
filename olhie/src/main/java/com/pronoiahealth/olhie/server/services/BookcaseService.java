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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.BookcaseEnum;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.bookcase.BookcaseBookWidgetReorderEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.GetMyBookcaseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.GetMyBookcaseResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.MyBooksForBookcaseSmallIconRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.MyBooksForBookcaseSmallIconResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookcaseDisplay;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookcaseService.java<br/>
 * Responsibilities:<br/>
 * 1. Handle bookcase events<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 25, 2013
 * 
 */
@RequestScoped
public class BookcaseService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userSessionToken;

	@Inject
	private Event<GetMyBookcaseResponseEvent> getMyBookcaseResponseEvent;

	@Inject
	private Event<MyBooksForBookcaseSmallIconResponseEvent> myBooksForBookcaseSmallIconResponseEvent;

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
	public BookcaseService() {
	}

	/**
	 * Responds to a GetMyBookcaseEvent with a GetMyBookcaseResponseEvent.
	 * Builds a map with the UserBookRelationshipEnum as the key and a list of
	 * Books as the value.
	 * 
	 * @param GetMyBookcaseEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED })
	protected void observesGetMyBookcaseResponseEvent(
			@Observes GetMyBookcaseEvent getMyBookcaseEvent) {

		try {
			// Check user id against session
			String userId = getMyBookcaseEvent.getUserId();
			String sessionId = userSessionToken.getUserId();

			// Create return type
			Map<UserBookRelationshipEnum, List<BookDisplay>> retMap = new HashMap<UserBookRelationshipEnum, List<BookDisplay>>();

			// If this is not the case the return an empty list
			if (userId != null && userId.equals(sessionId)
					&& userSessionToken.getLoggedIn() == true) {

				// Must have an active relationship
				List<UserBookRelationship> bResult = bookDAO
						.getUserBooksRelationshipLstByUserId(userId, true);
				for (UserBookRelationship rel : bResult) {
					// Get the relationship
					String userRel = rel.getUserRelationship();
					UserBookRelationshipEnum catEnum = UserBookRelationshipEnum
							.valueOf(userRel);

					Book currentBook = rel.getTheBook();

					// is the book active or is the user the creator or
					// co-author or
					if (currentBook.getActive() == true
							|| catEnum
									.equals(UserBookRelationshipEnum.COAUTHOR)
							|| catEnum.equals(UserBookRelationshipEnum.CREATOR)) {

						// See if its in the list, if not add it
						List<BookDisplay> books = retMap.get(catEnum);
						if (books == null) {
							books = new ArrayList<BookDisplay>();
							retMap.put(catEnum, books);
						}

						// get display
						BookDisplay retDisplay = bookDAO.getBookDisplayByBook(
								currentBook, userId, holder);

						// Add to the list
						books.add(retDisplay);
					}
				}
			}

			// Fire the event
			getMyBookcaseResponseEvent.fire(new GetMyBookcaseResponseEvent(
					retMap));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * Gets the books that the user is the author of for the Bookcase
	 * 
	 * 
	 * @param myBooksForBookcaseSmallIconRequestEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
		SecurityRoleEnum.REGISTERED })
	protected void observesMyBooksForBookcaseSmallIconRequestEvent(
			@Observes MyBooksForBookcaseSmallIconRequestEvent myBooksForBookcaseSmallIconRequestEvent) {

		try {
			// Check user id against session
			String userId = myBooksForBookcaseSmallIconRequestEvent.getUserId();
			String sessionId = userSessionToken.getUserId();
			BookcaseEnum requestedTab = myBooksForBookcaseSmallIconRequestEvent
					.getRequestedTab();

			// Create the return list
			List<BookcaseDisplay> bookcaseDisplayLst = new ArrayList<BookcaseDisplay>();

			// If this is not the case the return an empty list
			if (userId != null && userId.equals(sessionId)
					&& userSessionToken.getLoggedIn() == true) {

				// Must have an active relationship
				List<UserBookRelationship> bResult = null;
				switch (requestedTab) {
				case AUTHOR:
					bResult = bookDAO
							.getActiveUserBooksRelationshipLstByUserId(userId,
									UserBookRelationshipEnum.CREATOR.toString());
					break;
				case COAUTHOR:
					bResult = bookDAO
							.getActiveUserBooksRelationshipLstByUserId(userId,
									UserBookRelationshipEnum.COAUTHOR
											.toString());
					break;
				case MYCOLLECTION:
					bResult = bookDAO
							.getActiveUserBooksRelationshipLstByUserId(userId,
									UserBookRelationshipEnum.MYCOLLECTION
											.toString());
					break;
				}

				// Load list
				for (UserBookRelationship rel : bResult) {
					Book book = rel.getTheBook();
					bookcaseDisplayLst.add(new BookcaseDisplay(book.getId(),
							rel.getId(), book.getBookTitle()));
				}
			}

			// Fire return
			myBooksForBookcaseSmallIconResponseEvent
					.fire(new MyBooksForBookcaseSmallIconResponseEvent(
							bookcaseDisplayLst, requestedTab));

		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * Reorder the books for the various collections
	 * 
	 * @param bookcaseBookWidgetReorderEvent
	 */
	protected void observesBookcaseBookWidgetReorderEvent(
			@Observes BookcaseBookWidgetReorderEvent bookcaseBookWidgetReorderEvent) {
		try {
			Map<String, Integer> positionMap = bookcaseBookWidgetReorderEvent
					.getWidgetPositionMap();
			String userId = userSessionToken.getUserId();
			bookDAO.updateUserBookRelationshipBookCasePosition(positionMap,
					userId);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}
}