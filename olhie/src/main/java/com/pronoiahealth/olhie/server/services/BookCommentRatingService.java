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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.AddBookCommentRatingEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindCommentsEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindCommentsResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Bookcomment;
import com.pronoiahealth.olhie.client.shared.vo.Bookstarrating;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookCommentService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 20, 2013
 * 
 */
@RequestScoped
public class BookCommentRatingService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	@DAO
	private BookDAO bookDAO;;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private Event<BookFindCommentsResponseEvent> bookFindCommentsResponseEvent;

	/**
	 * Constructor
	 * 
	 */
	public BookCommentRatingService() {
	}

	/**
	 * Adds a comment to a book
	 * 
	 * @param addBookCommentEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesAddBookCommentEvent(
			@Observes AddBookCommentRatingEvent addBookCommentEvent) {

		try {
			String userId = userToken.getUserId();
			String bookId = addBookCommentEvent.getBookId();
			String comment = addBookCommentEvent.getBookComment();
			int starRating = addBookCommentEvent.getStarRating();

			// Update the comments
			if (comment != null && comment.length() > 0) {
				bookDAO.addBookComment(bookId, userId, comment, true);
			}

			// Update the star rating
			if (starRating != 0) {
				// add/update the rating
				Bookstarrating current = bookDAO.addUpdateBookRating(userId,
						bookId, starRating);
			}
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * Returns the list of comments for a book
	 * 
	 * @param bookFindCommentsEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesBookFindCommentsEvent(
			@Observes BookFindCommentsEvent bookFindCommentsEvent) {
		try {
			// Return list
			List<String> retLst = new ArrayList<String>();

			// Book to find comments for
			String bookId = bookFindCommentsEvent.getBookId();

			// Get the list
			List<Bookcomment> comments = bookDAO
					.getBookCommentsByBookId(bookId);

			// Transfer the comments to the return list
			if (comments != null && comments.size() > 0) {
				for (Bookcomment comment : comments) {
					retLst.add(comment.getComment());
				}
			}

			// Fire the list back
			bookFindCommentsResponseEvent
					.fire(new BookFindCommentsResponseEvent(bookId, retLst));

		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

}
