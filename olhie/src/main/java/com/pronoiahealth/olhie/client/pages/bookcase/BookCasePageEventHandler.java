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
package com.pronoiahealth.olhie.client.pages.bookcase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;
import org.jboss.errai.ioc.client.container.SyncBeanManager;

import com.pronoiahealth.olhie.client.shared.constants.BookcaseEnum;
import com.pronoiahealth.olhie.client.shared.events.bookcase.BookcaseBookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.MyBooksForBookcaseSmallIconResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookcaseDisplay;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3DEventObserver;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3D_3;

/**
 * BookcasePageEventHandler.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 16, 2013
 * 
 */
@Dependent
public class BookCasePageEventHandler {
	@Inject
	private SyncBeanManager syncManager;

	private BookCasePage bookCasePage;

	@Inject
	private Instance<BookList3DEventObserver> bookListEventObserverFac;

	@Inject
	private Disposer<BookList3DEventObserver> bookListEventObserverDisposer;

	private BookList3DEventObserver currentBookList3DEventObserver;

	/**
	 * Constructor
	 * 
	 */
	public BookCasePageEventHandler() {
	}

	@PreDestroy
	protected void preDestroy() {
		if (currentBookList3DEventObserver != null) {
			syncManager.destroyBean(currentBookList3DEventObserver);
		}
	}

	/**
	 * Attach a BookCasePage instance
	 * 
	 * @param bookCasePage
	 */
	public void attach(BookCasePage bookCasePage) {
		this.bookCasePage = bookCasePage;
	}

	protected void observesMyBooksForBookcaseSmallIconResponseEvent(
			@Observes MyBooksForBookcaseSmallIconResponseEvent myBooksForBookcaseSmallIconResponseEvent) {

		// Get the parameters
		List<BookcaseDisplay> lst = myBooksForBookcaseSmallIconResponseEvent
				.getBookCaseDisplayLst();
		BookcaseEnum requestedTab = myBooksForBookcaseSmallIconResponseEvent
				.getRequestedTab();

		// Create widget
		if (lst != null && lst.size() > 0) {
			// Protects against back to back call so this method
			bookCasePage.disposeTabs();
			bookCasePage.loadNewContainerWidget(requestedTab, lst);
		}

		// Set the spinner state
		bookCasePage.showBookcaseSpinner(false);
	}

	/**
	 * Observes for the response events from the selecting of a book. Once the
	 * BookDisplay is returned it will be set in the BookDetail Component
	 * 
	 * @param bookListBookSelectedResponseEvent
	 */
	protected void observesBookcaseBookListBookSelectedResponseEvent(
			@Observes BookcaseBookListBookSelectedResponseEvent bookListBookSelectedResponseEvent) {

		// Get list
		BookDisplay bookDisplay = bookListBookSelectedResponseEvent
				.getBookDisplay();

		// Clean up panel
		bookCasePage.destroyVisibleBookList();

		if (bookDisplay != null) {
			// Create a new book list
			// Add a book list3D to the container
			List<BookDisplay> bookDisplayList = new ArrayList<BookDisplay>();
			bookDisplayList.add(bookDisplay);
			BookList3D_3 currentBookList3D_3 = bookCasePage
					.addBookListToCurrentBookcaseContainer(bookDisplayList);
			getCurrentBookList3DEventObserver().attachBookList(
					currentBookList3D_3);
		}
	}

	/**
	 * Hide the spinner on an error
	 * 
	 * @param serviceErrorEvent
	 */
	protected void observesServiceErrorEvent(
			@Observes ServiceErrorEvent serviceErrorEvent) {
		// Set the spinner visible
		bookCasePage.showBookcaseSpinner(false);

		// Clean up tabs
		bookCasePage.disposeTabs();
	}

	/**
	 * Hide the spinner on a error
	 * 
	 * @param clientErrorEvent
	 */
	protected void observesClientErrorEvent(
			@Observes ClientErrorEvent clientErrorEvent) {
		// Set the spinner visible
		bookCasePage.showBookcaseSpinner(false);

		// Clean up tabs
		bookCasePage.disposeTabs();
	}

	private BookList3DEventObserver getCurrentBookList3DEventObserver() {
		if (currentBookList3DEventObserver == null) {
			currentBookList3DEventObserver = bookListEventObserverFac.get();
		}
		return currentBookList3DEventObserver;
	}
}
