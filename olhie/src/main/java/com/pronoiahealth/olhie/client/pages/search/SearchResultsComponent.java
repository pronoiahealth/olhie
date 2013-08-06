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
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.pages.AbstractComposite;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SearchPageLoadedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.widgets.booklist3d.BookList3D;
import com.pronoiahealth.olhie.client.widgets.booklist3d.BookSelectCallBack;

/**
 * SearchResultsComponent.java<br/>
 * Responsibilities:<br/>
 * 1. Show the results of a search query on the Search page<br/>
 * 2. Observes for the SearchPageLoadedEvent<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public class SearchResultsComponent extends AbstractComposite {

	@Inject
	UiBinder<Widget, SearchResultsComponent> binder;

	@Inject
	private PageNavigator nav;

	// @UiField
	// public Pagination searchResultsPager;

	@UiField
	public FluidRow searchResultsHeader;

	@UiField
	public ScrollPanel searchResultsContainerList;

	@UiField
	public Label resultsLbl;

	private Image spinner;

	private BookSelectCallBack bookSelectCallBack;

	/**
	 * Constructor
	 * 
	 */
	public SearchResultsComponent() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Header for list
		searchResultsHeader.addStyleName("ph-SearchResults-Header");

		// Set search label
		resultsLbl.setText(SearchConstants.INSTANCE.searchResults());

		// Navigate to the NewBookPage. The edit mode will be set by logic on
		// that page.
		bookSelectCallBack = new BookSelectCallBack() {
			@Override
			public void onBookSelect(String bookId) {
				Multimap<String, Object> map = ArrayListMultimap.create();
				map.put("bookId", bookId);
				nav.performTransition(NavEnum.NewBookPage.toString(), map);
			}
		};
	}

	/**
	 * Observes the results from a book search query.
	 * 
	 * @param event
	 */
	public void observesBookSearchResponseEvent(
			@Observes BookSearchResponseEvent event) {

		// Remove the spinner
		removeSpinner();
		
		// Display the books, if nothing is returned the display a message
		List<BookDisplay> lst = event.getBookDisplayList();
		if (lst != null && lst.size() > 0) {
			int total = event.getTotalInResultSet();
			int returned = lst.size();
			resultsLbl.setText(SearchMessages.INSTANCE.searchResultsReturned(
					returned, total));
			BookList3D bookLst = new BookList3D(lst, bookSelectCallBack);
			searchResultsContainerList.add(bookLst);
		} else {
			resultsLbl.setText(SearchConstants.INSTANCE.searchResults());
			Label noBooksLbl = new Label(SearchConstants.INSTANCE.noResults());
			noBooksLbl.setStyleName("ph-SearchResults-NoResults-Lbl");
			searchResultsContainerList.add(noBooksLbl);
		}
	}

	/**
	 * @param bookSearchEvent
	 */
	protected void observesBookSearchEvent(
			@Observes BookSearchEvent bookSearchEvent) {

		// Clear the current display
		searchResultsContainerList.clear();

		// Set the spinner visible
		addSpinner();
	}

	protected void observesServiceErrorEvent(
			@Observes ServiceErrorEvent serviceErrorEvent) {
		// Set the spinner visible
		removeSpinner();
	}

	protected void observesClientErrorEvent(
			@Observes ClientErrorEvent clientErrorEvent) {
		// Set the spinner visible
		removeSpinner();
	}

	/**
	 * Removes the spinner
	 */
	private void removeSpinner() {
		if (spinner != null) {
			spinner.removeFromParent();
			spinner = null;
		}
	}

	/**
	 * Adds a spinner to search results container
	 */
	private void addSpinner() {
		spinner = new Image("Olhie/images/loading.gif");
		spinner.getElement().setAttribute("style", "ph-SearchResults-Spinner");
		searchResultsContainerList.add(spinner);
	}

	/**
	 * Observes when the parent window has been loaded. This component will
	 * adjust its size at that point.
	 * 
	 * @param event
	 */
	public void observesSearchPageLoadedEvent(
			@Observes SearchPageLoadedEvent event) {
		adjustSize();
	}

	/**
	 * Need to adjust the search results panel dynamically
	 * 
	 * @param event
	 */
	public void observesWindowResizeEvent(@Observes WindowResizeEvent event) {
		adjustSize();
	}

	/**
	 * Client is logging out so clear the returned books
	 * 
	 * @param clientLogoutRequestEvent
	 */
	protected void observesClientLogoutRequestEvent(
			@Observes ClientLogoutRequestEvent clientLogoutRequestEvent) {
		searchResultsContainerList.clear();
	}

	private void adjustSize() {
		if (isAttached() == true) {
			// Difference from window height
			int wndHeight = this.getPageContainerHeight();

			// 128 is search at top + Search results label + footer at bottom
			int newSearchResultsScrollerHeight = wndHeight - 128;
			searchResultsContainerList.setSize("100%", ""
					+ newSearchResultsScrollerHeight + "px");
		}
	}

}
