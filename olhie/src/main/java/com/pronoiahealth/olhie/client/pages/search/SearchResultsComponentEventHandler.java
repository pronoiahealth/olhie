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
package com.pronoiahealth.olhie.client.pages.search;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;

import com.pronoiahealth.olhie.client.shared.constants.SearchPageActionEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.SearchPageNavigationResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientBookSearchEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SearchPageLoadedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.widgets.booklist3d.BookListDiv3DEventObserver;

@Dependent
public class SearchResultsComponentEventHandler {

	private SearchResultsComponent searchResultsComponent;

	@Inject
	private Instance<BookListDiv3DEventObserver> bookListObserverFac;

	@Inject
	private Disposer<BookListDiv3DEventObserver> bookListObserverDisposer;

	private BookListDiv3DEventObserver bookListObserver;

	/**
	 * Constructor
	 * 
	 */
	public SearchResultsComponentEventHandler() {
	}

	@PostConstruct
	protected void postConstruct() {
		bookListObserver = bookListObserverFac.get();
	}

	@PreDestroy
	protected void preDestroy() {
		if (bookListObserver != null) {
			bookListObserverDisposer.dispose(bookListObserver);
		}

	}

	protected void attach(SearchResultsComponent searchResultsComponent) {
		this.searchResultsComponent = searchResultsComponent;
	}

	/**
	 * Observes the results from a book search query.
	 * 
	 * @param event
	 */
	public void observesBookSearchResponseEvent(
			@Observes BookSearchResponseEvent event) {

		// Hide spinner
		searchResultsComponent.setSpinnerVisibility(false);

		// Clear results container
		searchResultsComponent.clearResultsContainer();

		// Display the books, if nothing is returned the display a message
		List<BookDisplay> lst = event.getBookDisplayList();
		if (lst != null && lst.size() > 0) {
			searchResultsComponent.setNewCurrentBookList3D(lst,
					bookListObserver);
		} else {
			searchResultsComponent.setNoResultsLabel();
		}
	}

	/**
	 * Similar actions to the observesBookSearchResponseEvent except the
	 * SearchPageNavigationResponseEvent as a result of the user navigating
	 * through an already returned search result set.
	 * 
	 * 
	 * @param searchPageNavigationResponseEvent
	 */
	protected void observesSearchPageNavigationResponseEvent(
			@Observes SearchPageNavigationResponseEvent searchPageNavigationResponseEvent) {
		// This component does not handle the UPDATE_STATE action
		if (searchPageNavigationResponseEvent.getRequestedAction() != SearchPageActionEnum.UPDATE_STATE) {
			// Hide spinner
			searchResultsComponent.setSpinnerVisibility(false);

			// Clear results container
			searchResultsComponent.clearResultsContainer();

			// Display the books, if nothing is returned the display a message
			List<BookDisplay> lst = searchPageNavigationResponseEvent
					.getBookDisplays();
			if (lst != null && lst.size() > 0) {
				searchResultsComponent.setNewCurrentBookList3D(lst,
						bookListObserver);
			} else {
				searchResultsComponent.setNoResultsLabel();
			}
		}
	}

	/**
	 * @param bookSearchEvent
	 */
	protected void observesClientBookSearchEvent(
			@Observes ClientBookSearchEvent bookSearchEvent) {
		// Show spinner
		searchResultsComponent.setSpinnerVisibility(true);
	}

	/**
	 * Hide the spinner on an error
	 * 
	 * @param serviceErrorEvent
	 */
	protected void observesServiceErrorEvent(
			@Observes ServiceErrorEvent serviceErrorEvent) {
		// hide spinner
		searchResultsComponent.setSpinnerVisibility(false);

		// Clear results container
		searchResultsComponent.clearResultsContainer();
	}

	/**
	 * Hide the spinner on a error
	 * 
	 * @param clientErrorEvent
	 */
	protected void observesClientErrorEvent(
			@Observes ClientErrorEvent clientErrorEvent) {
		// hide spinner
		searchResultsComponent.setSpinnerVisibility(false);

		// Clear results container
		searchResultsComponent.clearResultsContainer();
	}

	/**
	 * Observes when the parent window has been loaded. This component will
	 * adjust its size at that point.
	 * 
	 * @param event
	 */
	public void observesSearchPageLoadedEvent(
			@Observes SearchPageLoadedEvent event) {
		searchResultsComponent.adjustSize();
	}

	/**
	 * Need to adjust the search results panel dynamically
	 * 
	 * @param event
	 */
	public void observesWindowResizeEvent(@Observes WindowResizeEvent event) {
		searchResultsComponent.adjustSize();
	}

	/**
	 * Client is logging out so clear the returned books
	 * 
	 * @param clientLogoutRequestEvent
	 */
	protected void observesClientLogoutRequestEvent(
			@Observes ClientLogoutRequestEvent clientLogoutRequestEvent) {
		// Clear results container
		searchResultsComponent.clearResultsContainer();
	}
}
