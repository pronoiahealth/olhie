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

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.GetMyBookcaseEvent;
import com.pronoiahealth.olhie.client.shared.events.GetMyBookcaseResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;

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
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private TempCoverBinderHolder holder;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

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
			if (!userId.equals(sessionId)) {
				throw new Exception(
						"The provided user does not match the logged in user.");
			}

			// Create return type
			Map<UserBookRelationshipEnum, List<BookDisplay>> retMap = new HashMap<UserBookRelationshipEnum, List<BookDisplay>>();

			// Must have an active relationship
			OSQLSynchQuery<UserBookRelationship> bQuery = new OSQLSynchQuery<UserBookRelationship>(
					"select from UserBookRelationship where userId = :uId and activeRelationship = true");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("uId", userId);
			List<UserBookRelationship> bResult = ooDbTx.command(bQuery)
					.execute(bparams);
			for (UserBookRelationship rel : bResult) {
				// Get the relationship
				String userRel = rel.getUserRelationship();
				UserBookRelationshipEnum catEnum = UserBookRelationshipEnum
						.valueOf(userRel);
				User currentUser = rel.getTheUser();
				Book currentBook = rel.getTheBook();

				// is the book active or is the user the creator or co-author
				if (currentBook.getActive() == true
						|| catEnum.equals(UserBookRelationshipEnum.COAUTHOR)
						|| catEnum.equals(UserBookRelationshipEnum.CREATOR)) {

					// See if its in the list, if not add it
					List<BookDisplay> books = retMap.get(catEnum);
					if (books == null) {
						books = new ArrayList<BookDisplay>();
						retMap.put(catEnum, books);
					}

					// Build the BookDisplay
					BookDisplay retDisplay = new BookDisplay();

					// Full name
					String fullName = String.format("%s %s",
							currentUser.getFirstName(),
							currentUser.getLastName());
					retDisplay.setAuthorFullName(fullName);

					// Book cover
					String bookCover = currentBook.getCoverName();
					BookCover cover = holder.getCoverByName(bookCover);
					retDisplay.setBookCover(cover);

					// Book Category
					String bookCat = currentBook.getCategory();
					BookCategory cat = holder.getCategoryByName(bookCat);
					retDisplay.setBookCategory(cat);

					// Book assets
					// Get a list of Bookassetdescriptions
					OSQLSynchQuery<Bookassetdescription> baQuery = new OSQLSynchQuery<Bookassetdescription>(
							"select from Bookassetdescription where bookId = :bId");
					HashMap<String, String> baparams = new HashMap<String, String>();
					baparams.put("bId", currentBook.getId());
					List<Bookassetdescription> baResults = ooDbTx.command(
							baQuery).execute(baparams);
					if (baResults != null && baResults.size() > 0) {
						List<Bookassetdescription> retDescs = new ArrayList<Bookassetdescription>();
						retDisplay.setBookAssetDescriptions(retDescs);
						for (Bookassetdescription d : baResults) {
							Bookassetdescription retDesc = new Bookassetdescription();
							retDesc.setDescription(d.getDescription());
							retDescs.add(retDesc);
						}
					}

					// Book
					Book retBook = new Book();
					retDisplay.setBook(retBook);
					retBook.setBookTitle(currentBook.getBookTitle());
					retBook.setIntroduction(currentBook.getIntroduction());
					retBook.setId(currentBook.getId());

					// Add to the list
					books.add(retDisplay);
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

}