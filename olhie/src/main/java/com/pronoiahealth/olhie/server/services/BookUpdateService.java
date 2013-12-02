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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.lowagie.text.pdf.codec.Base64;
import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.annotations.Update;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookUpdateCommittedEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookUpdateEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.BookCoverImageService.Cover;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookUpdateService.java<br/>
 * Responsibilities:<br/>
 * 1. Handle book adds and updates<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 7, 2013
 * 
 */
// TODO: Need to track update history. Not sure to what detail. This could be an
// additional attribute of the Book class
@RequestScoped
public class BookUpdateService {

	@Inject
	private Logger log;

	@Inject
	private BookCoverImageService imgService;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private TempThemeHolder holder;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private Event<BookUpdateCommittedEvent> bookUpdateCommittedEvent;

	// @Resource(mappedName = "queue/testQueue")
	// private Queue queue;

	/**
	 * Optionally use a pooled connection factory for better performance
	 * http://www
	 * .mastertheboss.com/jboss-jms/jboss-as-7-sending-jms-messages-across
	 * -java-ee-components
	 */
	// @Resource(mappedName = "java:/ConnectionFactory")
	// private ConnectionFactory cf;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public BookUpdateService() {
	}

	/**
	 * Watches for a new book
	 * 
	 * @param bookUpdateEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesNewBookUpdateEvent(
			@Observes @Update BookUpdateEvent bookUpdateEvent) {
		try {
			// Find the user. The user sending this request should be the same
			// one in the session
			String sessionUserId = userToken.getUserId();

			// Compare the session user and the one sending the request
			if (!sessionUserId.equals(bookUpdateEvent.getBook().getAuthorId())) {
				throw new Exception(
						"The current user and the author don't match.");
			}

			// Get the book
			Book book = bookUpdateEvent.getBook();

			// Get current book from db
			Book currentBook = bookDAO.getBookById(book.getId());

			// Make the images
			BookCategory cat = holder.getCategoryByName(book.getCategory());
			BookCover cover = holder.getCoverByName(book.getCoverName());
			String authorName = bookDAO.getAuthorName(book.getAuthorId());

			// Is there a logo on the current book
			String logoStr = currentBook.getBase64LogoData();

			// Is there currently a logo
			// TODO: Don't recreate the images if the data in the image hasn't
			// changed.
			byte[] logo = null;
			if (logoStr != null && logoStr.length() > 0) {
				logo = Base64.decode(logoStr);
			}

			Map<Cover, String> coverMap = imgService.createDefaultBookCovers(
					book, cat, cover, logo, authorName);

			// Update basic book data
			currentBook.setBookTitle(book.getBookTitle());
			currentBook.setIntroduction(book.getIntroduction());
			currentBook.setKeywords(book.getKeywords());
			currentBook.setCategory(book.getCategory());
			currentBook.setCoverName(book.getCoverName());
			currentBook.setInterfacePlatform(book.getInterfacePlatform());
			currentBook.setInterfaceSendingSystem(book
					.getInterfaceSendingSystem());
			currentBook.setInterfaceRecievingSystem(book
					.getInterfaceRecievingSystem());

			// Update the active
			Date now = new Date();
			boolean active = book.getActive();
			currentBook.setActive(active);
			if (active == true) {
				currentBook.setActDate(now);
			} else {
				currentBook.setActDate(null);
			}

			// image data
			currentBook.setBase64FrontCover(coverMap.get(Cover.FRONT));
			currentBook.setBase64BackCover(coverMap.get(Cover.BACK));
			currentBook.setBase64SmallFrontCover(coverMap
					.get(Cover.SMALL_FRONT));
			currentBook.setLastUpdated(now);

			// Solr updating
			currentBook.setSolrUpdate(null);

			// Save it
			currentBook = bookDAO.addBook(currentBook);

			// Return the result
			BookUpdateCommittedEvent event = new BookUpdateCommittedEvent(
					currentBook.getId());
			bookUpdateCommittedEvent.fire(event);
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

	/**
	 * Watches for a new book
	 * 
	 * @param bookUpdateEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesUpdateBookUpdateEvent(
			@Observes @New BookUpdateEvent bookUpdateEvent) {
		try {
			// Find the user. The user sending this request should be the same
			// one in the session
			String sessionUserId = userToken.getUserId();

			// Compare the session user and the one sending the request
			if (!sessionUserId.equals(bookUpdateEvent.getBook().getAuthorId())) {
				throw new Exception(
						"The current user and the author don't match.");
			}

			// Get the book
			Book book = bookUpdateEvent.getBook();

			// Make the images
			BookCategory cat = holder.getCategoryByName(book.getCategory());
			BookCover cover = holder.getCoverByName(book.getCoverName());
			String authorName = bookDAO.getAuthorName(sessionUserId);
			Map<Cover, String> coverMap = imgService.createDefaultBookCovers(
					book, cat, cover, null, authorName);

			// Add the book
			Date now = new Date();
			book.setCreatedDate(now);
			book.setBase64FrontCover(coverMap.get(Cover.FRONT));
			book.setBase64BackCover(coverMap.get(Cover.BACK));
			book.setBase64SmallFrontCover(coverMap.get(Cover.SMALL_FRONT));
			book.setLastUpdated(now);
			book.setSolrUpdate(null);
			book.setAuthorId(userToken.getUserId());
			if (book.getActive() != null
					&& book.getActive().booleanValue() == true) {
				book.setActDate(now);
			}

			// Save the book
			book = bookDAO.addBook(book);

			// Add the UserBookRelationship
			bookDAO.addUserBookRelationship(book, true, sessionUserId,
					UserBookRelationshipEnum.CREATOR);

			// Return the result
			BookUpdateCommittedEvent event = new BookUpdateCommittedEvent(
					book.getId());
			bookUpdateCommittedEvent.fire(event);
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}
}
