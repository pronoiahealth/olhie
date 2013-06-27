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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.pages.AbstractComposite;
import com.pronoiahealth.olhie.client.shared.events.BookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.BookSearchResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SearchPageLoadedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookState;
import com.pronoiahealth.olhie.client.widgets.booklist.BookListResultWidget;
import com.pronoiahealth.olhie.client.widgets.booklist3d.BookList3D;
import com.pronoiahealth.olhie.client.widgets.booklist3d.BookSelectCallBack;
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

		// Hearder for list
		searchResultsHeader.addStyleName("ph-SearchResults-Header");

		// Set width of dropdown
		resultListCnt.setWidth("100px");
		
		// Test add some books
		/*
		BookListResultWidget widget = new BookListResultWidget(new BookForDisplay("id", "Title 1", "John D", 4, "Test introduction",
				"TOC", "06/26/1958", "400", "Book 1 Book2",
				BookState.BOOK_STATE_INVISIBLE, new BookCategory("yellow", "Legal"),
				new BookCover("Olhie/images/p1.png", "Paper")));
		if (widget.getBook().getBookState() == BookState.BOOK_STATE_INVISIBLE) {
			widget.getBookImagePanelWidget().setVisible(false);
			searchResultsContainerList.add(widget);
		}
		*/

		// create selector - This will fire the bookId to the server to see what
		// the users relationship with the book is. If he's the author or
		// co-author he will be directed to the NewBookPage to edit the book. If
		// not he will be directed to the book review page.
		bookSelectCallBack = new BookSelectCallBack() {
			@Override
			public void onBookSelect(String bookId) {
				/*
				//TODO: implement book select event
				if (bookId != null) {
					bookListBookSelectedEvent
							.fire(new BookListBookSelectedEvent(bookId));
				}
				*/
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

		List<BookDisplay> lst = event.getBookDisplayList();
		
		BookList3D bookLst = new BookList3D(lst, bookSelectCallBack);

		searchResultsContainerList.clear();
		searchResultsContainerList.add(bookLst);
		/*
		 * TODO: replace with the booklist3d widget
		for (BookDisplay bookDisplay : bookDisplayList) {
			BookListResultWidget widget = new BookListResultWidget(bookDisplay);
			if (widget.getBook().getBookState() == BookState.BOOK_STATE_INVISIBLE) {
				widget.getBookImagePanelWidget().setVisible(false);
				searchResultsContainerList.add(widget);
			}
		}
		 */
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
