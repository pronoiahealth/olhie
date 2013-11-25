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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.BookAssetActionType;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;
import com.pronoiahealth.olhie.client.shared.events.book.AddBookYouTubeAssetEvent;
import com.pronoiahealth.olhie.client.shared.events.book.FindAuthorsBookByIdEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookYouTubeAssetService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 23, 2013
 * 
 */
@RequestScoped
public class BookYouTubeAssetService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	@DAO
	private BookDAO bookDAO;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private Event<FindAuthorsBookByIdEvent> findAuthorsBookByIdEvent;

	/**
	 * Constructor
	 * 
	 */
	public BookYouTubeAssetService() {
	}

	/**
	 * This method processes the AddBookYouTubeAssetEvent:<br/>
	 * 1. Checks that the current session user is the author or co-author of the
	 * book.<br/>
	 * 2. Adds a new Bookassetdescription and Booasset to the database.<br/>
	 * 3. Fires the FindAuthorsBookByIdEvent. This will be processed by the
	 * BookFindService which in turn will fire a BookFindResponseEvent. This
	 * event is listened for by the NewBookPage. The result will be that the
	 * NewBookPage will be updated if it is listenening.
	 * 
	 * @param addBookYouTubeAssetEvent
	 */
	protected void observersAddBookYouTubeAssetEvent(
			@Observes AddBookYouTubeAssetEvent addBookYouTubeAssetEvent) {
		try {
			// Get required data
			String userId = userToken.getUserId();
			String bookId = addBookYouTubeAssetEvent.getBookId();
			String assetDescription = addBookYouTubeAssetEvent
					.getAssetDescription();
			String youTubeLink = addBookYouTubeAssetEvent.getYouTubeLink();
			int hoursOfWork = addBookYouTubeAssetEvent.getHoursOfWork();

			// Check to make sure the sender of the info is the author or
			// co-author of the book
			boolean isAuthor = bookDAO.isUserAuthorOrCoauthorOfBook(userId,
					bookId);
			if (isAuthor == false) {
				throw new Exception("The user " + userId
						+ " is not the author of the book.");
			}

			// Add a new Bookassetdescriton and associated Bookasset to the
			// book
			bookDAO.addUpdateBookasset(assetDescription, bookId,
					BookAssetDataType.YOUTUBE.toString(),
					BookAssetDataType.LINK.toString(), null,
					BookAssetActionType.NEW.name(), null, youTubeLink, null, 0,
					hoursOfWork, userId);

			// Fire back an update
			findAuthorsBookByIdEvent.fire(new FindAuthorsBookByIdEvent(bookId));
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}
}
