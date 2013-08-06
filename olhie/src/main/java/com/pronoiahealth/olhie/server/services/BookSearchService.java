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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookSearchService.java<br/>
 * Responsibilities:<br/>
 * 1. Use to return a book based on a search query<br/>
 * 
 * <p>
 * Book should not be returned from this service unless they are active.
 * </p>
 * 
 * @author Alex Roman
 * @version 1.0
 * @since Jun 25, 2013
 * 
 *        <p>
 *        Changes: Added code to only return active books - 6/27/13 - jjd<br/>
 *        </p>
 * 
 */
@RequestScoped
public class BookSearchService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private Event<BookSearchResponseEvent> bookSearchResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private SolrSearchService solrSearchService;

	@Inject
	private TempCoverBinderHolder holder;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public BookSearchService() {
	}

	/**
	 * Finds a book and then fires a response with the found book.
	 * 
	 * @param bookFindByIdEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesBookSearchEvent(
			@Observes BookSearchEvent bookSearchEvent) {
		try {
			String searchText = bookSearchEvent.getSearchText();
			List<BookDisplay> bookDisplayList = new ArrayList<BookDisplay>();

			// int rows = bookSearchEvent.getRows();
			// List<String> bookIdList =
			//// solrSearchService.searchSolr(searchText);
			// solrSearchService.searchSolr(searchText.split("\\s+"), rows);

			// Find Book
			// OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
			//// "select from Book where @rid in :idlist and active = true");
			// "select from Book where @rid in " + bookIdList + " and active = true");
			// HashMap<String, Object> bparams = new HashMap<String, Object>();
			//// bparams.put("idlist", bookIdList);
			// List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);

			OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
					"select from Book where bookTitle.toLowerCase() like :title and active = true");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("title", "%" + searchText.toLowerCase() + "%");
			List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);

			for (Book book : bResult) {
				BookDisplay bookDisplay = BookDAO.getBookDisplayById(
						book.getId(), ooDbTx, userToken.getUserId(), holder);

				bookDisplayList.add(bookDisplay);
			}

			// Fire the event
			bookSearchResponseEvent.fire(new BookSearchResponseEvent(
					bookDisplayList, bookDisplayList.size()));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}

}
