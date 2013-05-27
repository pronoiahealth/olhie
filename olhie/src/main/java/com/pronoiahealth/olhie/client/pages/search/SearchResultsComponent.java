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

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.pages.AbstractComposite;
import com.pronoiahealth.olhie.client.shared.events.local.SearchPageLoadedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookBackgroundPattern;
import com.pronoiahealth.olhie.client.shared.vo.BookCatagory;
import com.pronoiahealth.olhie.client.shared.vo.BookState;
import com.pronoiahealth.olhie.client.widgets.booklist.BookListResultWidget;
import com.watopi.chosen.client.gwt.ChosenListBox;

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

	// @UiField
	// public Pagination searchResultsPager;
	
	@UiField
	public FluidRow searchResultsHeader;

	@UiField
	public ChosenListBox resultListCnt;

	@UiField
	public HTMLPanel searchResultsContainerList;

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

		// Hearder for list
		searchResultsHeader.addStyleName("ph-SearchResults-Header");
		
		// Set width of dropdown
		resultListCnt.setWidth("100px");
		
		// add a book for test
		BookListResultWidget widget = new BookListResultWidget(new Book("1",
				"Book 1 Test Test Test Test Test Test Test", "John DeStefano", 4, "Test introduction",
				"Need some help here", "06/26/2011", "400", "Book 3, Book 4",
				BookBackgroundPattern.PAPER, BookState.BOOK_STATE_INVISIBLE, BookCatagory.INTERFACE, "green"));
		if (widget.getBook().getBookState() == BookState.BOOK_STATE_INVISIBLE) {
			widget.getBookImagePanelWidget().setVisible(false);
			searchResultsContainerList.add(widget);
		}

		widget = new BookListResultWidget(new Book("2", "Book 2",
				"John DeStefano", 4, "Test introduction",
				"Need some help here", "06/26/2011", "400", "Book 3, Book 4",
				BookBackgroundPattern.PAPER, BookState.BOOK_STATE_VISIBLE, BookCatagory.LEGAL,"orange"));
		if (widget.getBook().getBookState() == BookState.BOOK_STATE_VISIBLE) {
			searchResultsContainerList.add(widget);
		}

		widget = new BookListResultWidget(new Book("3", "Book 3",
				"John DeStefano", 4, "Test introduction",
				"Need some help here", "06/26/2011", "400", "Book 3, Book 4",
				BookBackgroundPattern.PAPER, BookState.BOOK_STATE_VISIBLE, BookCatagory.LEGAL, "black"));
		if (widget.getBook().getBookState() == BookState.BOOK_STATE_VISIBLE) {
			searchResultsContainerList.add(widget);
		}

		widget = new BookListResultWidget(new Book("4", "Book 2",
				"John DeStefano", 4, "Test introduction",
				"Need some help here", "06/26/2011", "400", "Book 3, Book 4",
				BookBackgroundPattern.PAPER, BookState.BOOK_STATE_VISIBLE, BookCatagory.LEGAL, "lime"));
		if (widget.getBook().getBookState() == BookState.BOOK_STATE_VISIBLE) {
			searchResultsContainerList.add(widget);
		}

		widget = new BookListResultWidget(new Book("5", "Book 3",
				"John DeStefano", 4, "Test introduction",
				"Need some help here", "06/26/2011", "400", "Book 3, Book 4",
				BookBackgroundPattern.PAPER, BookState.BOOK_STATE_VISIBLE, BookCatagory.LEGAL, "green"));
		if (widget.getBook().getBookState() == BookState.BOOK_STATE_VISIBLE) {
			searchResultsContainerList.add(widget);
		}
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

	private void adjustSize() {
		if (isAttached() == true) {
			// Difference from window height
			int wndHeight = this.getPageContainerHeight();

			// 128 is search at top + Search results label + footer at bottom
			int newSearchResultsScrollerHeight = wndHeight - 128;
			searchResultsContainerList.setHeight(""
					+ newSearchResultsScrollerHeight + "px");
		}
	}

}
