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

import com.pronoiahealth.olhie.client.shared.constants.SearchPageActionEnum;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.LeavingSearchPageEvent;
import com.pronoiahealth.olhie.client.shared.events.book.SearchPageNavigationRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.book.SearchPageNavigationResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.serverfactories.SearchPageSize;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookSearchService.java<br/>
 * Responsibilities:<br/>
 * 1. Use to return a book(s) based on a search query<br/>
 * 2. Receives events to navigate within an already returned results set<br/>
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
 *        1. Changes: Added code to only return active books - 6/27/13 - jjd<br/>
 *        2. Changes: Added code to handle navigation with a result set -
 *        11/30/13 - jjd<br/>
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
	private SearchResultHolder srchRsltHolder;

	@Inject
	@SearchPageSize
	private int searchPageSize;

	@Inject
	private Event<BookSearchResponseEvent> bookSearchResponseEvent;

	@Inject
	private Event<SearchPageNavigationResponseEvent> searchPageNavigationResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private SolrSearchService solrSearchService;

	@Inject
	private TempThemeHolder holder;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public BookSearchService() {
	}

	/**
	 * Finds a book and then fires a response with the found book. This is fired
	 * when the user selects the find button on the search page. This indicates
	 * a new search is requested and thus the state held by the srchRsltHolder
	 * should be cleared. The BookSearchResponseEvent is observed by the
	 * SearchComponent and the SearchPagerWidget. When received by the
	 * SearchPagerWidget the widget will request the initial state of the
	 * srchRsltHolder by firing a SearchPageNavigationRequestEvent with ab
	 * action parameter of UPDATE_STATE.
	 * 
	 * @param bookFindByIdEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesBookSearchEvent(
			@Observes BookSearchEvent bookSearchEvent) {
		try {
			// Clear current search results state in any.
			srchRsltHolder.clear();

			// Query request
			String searchText = bookSearchEvent.getSearchText();
			List<BookDisplay> bookDisplayList = new ArrayList<BookDisplay>();

			int rows = bookSearchEvent.getRows();
			// solrSearchService.searchSolr(searchText);
			List<String> solrBookIdList = solrSearchService.searchSolr(
					searchText.split("\\s+"));

			// Find Book
			// OSQLSynchQuery<Book> bQuery = new
			// OSQLSynchQuery<Book>( //
			// "select from Book where @rid in :idlist and active = true");
			// "select from Book where @rid in " + bookIdList +
			// " and active = true"); HashMap<String, Object> bparams = new
			// HashMap<String, Object>(); // bparams.put("idlist", bookIdList);
			// List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);

			/*
			 * OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
			 * "select from Book where bookTitle.toLowerCase() like :title and active = true"
			 * ); HashMap<String, String> bparams = new HashMap<String,
			 * String>(); bparams.put("title", "%" + searchText.toLowerCase() +
			 * "%"); List<Book> bResult =
			 * ooDbTx.command(bQuery).execute(bparams);
			 */

			List<String> bookIdxLst = new ArrayList<String>();
			List<Book> bResult = bookDAO.getBooksByIdLst(solrBookIdList, true);
			//List<Book> bResult = bookDAO.getActiveBooksByTitle(searchText, 0,
			//		rows);
			String userId = userToken.getUserId();
			int cnt = 0;
			for (Book book : bResult) {
				BookDisplay bookDisplay = bookDAO.getBookDisplayByBook(book,
						userId, holder, true);

				// Only return the first page on the initial query but load all
				// results into the book id's list for use by the srchRsltHolder
				if (bookDisplay != null && cnt < searchPageSize) {
					bookDisplayList.add(bookDisplay);
					bookIdxLst.add(bookDisplay.getBook().getId());
					cnt++;
				} else {
					bookIdxLst.add(bookDisplay.getBook().getId());
				}
			}

			if (bResult.size() > 0) {
				// Set initial state of session token
				srchRsltHolder.setSearchResultHolder(bookIdxLst, searchText,
						searchPageSize);
			} else {
				srchRsltHolder.clear();
			}

			// Fire search response the event
			bookSearchResponseEvent.fire(new BookSearchResponseEvent(
					bookDisplayList, bookDisplayList.size(), searchPageSize));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * Watches for search page result set navigation events. They are fired from
	 * the SearchPagerWidget when the user is requesting a new page of results.
	 * This method will send back the requested results and information about
	 * the current state of the search (total number of results, current results
	 * returned, etc..). This method uses the session scoped SearchResultHolder
	 * to track the results.
	 * 
	 * 
	 * @param searchPageNavigationRequestEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesSearchPageNavigationRequestEvent(
			@Observes SearchPageNavigationRequestEvent searchPageNavigationRequestEvent) {
		try {
			// What action is requested
			SearchPageActionEnum action = searchPageNavigationRequestEvent
					.getRequestedAction();

			// Results to return
			List<BookDisplay> bookDisplays = null;

			// Switch
			switch (action) {
			case FIRST:
				// Position the page
				srchRsltHolder.setCurrentPageToFirst();
				bookDisplays = this.getBooksForResultLstToDisplay(true);
				break;
			case PREVIOUS:
				srchRsltHolder.setCurrentPageToPrevious();
				bookDisplays = this.getBooksForResultLstToDisplay(true);
				break;
			case NEXT:
				srchRsltHolder.setCurrentPageToNext();
				bookDisplays = this.getBooksForResultLstToDisplay(true);
				break;
			case LAST:
				srchRsltHolder.setCurrentPageToLast();
				bookDisplays = this.getBooksForResultLstToDisplay(true);
				break;
			case UPDATE_STATE:
				break;
			default:
				throw new Exception("Unknow navigation action requested.");
			}

			// Send response to client
			// An update action will not send a list back
			searchPageNavigationResponseEvent
					.fire(new SearchPageNavigationResponseEvent(bookDisplays,
							srchRsltHolder.getCurrentPageStartIdx(),
							srchRsltHolder.getCurrentPageEndIdx(),
							srchRsltHolder.getResultCnt(), srchRsltHolder
									.isFirstPage(),
							srchRsltHolder.isLastPage(), srchRsltHolder
									.isFitsOnOnePage(),
							searchPageNavigationRequestEvent
									.getRequestedAction()));

		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}

	/**
	 * Fired when the user navigates away from the SearchPage. When this happens
	 * the users session scoped SearchResultHolder token is cleared.
	 * 
	 * @param leavingSearchPageEvent
	 */
	protected void observesLeavingSearchPageEvent(
			@Observes LeavingSearchPageEvent leavingSearchPageEvent) {
		srchRsltHolder.clear();
	}

	/**
	 * Used by the navigation observer method to get a list of BookDisplays to
	 * return
	 * 
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	private List<BookDisplay> getBooksForResultLstToDisplay(boolean activeOnly) throws Exception {
		String userId = userToken.getUserId();
		List<String> srchLst = srchRsltHolder.getResultsLstForCurrentPage();
		List<Book> bResult = bookDAO.getBooksByIdLst(srchLst, activeOnly);

		List<BookDisplay> bookDisplayList = new ArrayList<BookDisplay>();
		for (Book book : bResult) {
			BookDisplay bookDisplay = bookDAO.getBookDisplayByBook(book,
					userId, holder, true);

			// Only return the first page on the initial query
			if (bookDisplay != null) {
				bookDisplayList.add(bookDisplay);
			}
		}

		return bookDisplayList;
	}

}
