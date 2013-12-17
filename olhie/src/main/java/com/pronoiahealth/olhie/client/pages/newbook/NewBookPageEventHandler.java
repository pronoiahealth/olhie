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
package com.pronoiahealth.olhie.client.pages.newbook;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.events.book.BookFindResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.FindAuthorsBookByIdEvent;
import com.pronoiahealth.olhie.client.shared.events.local.BookContentUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;

/**
 * NewBookEventHandler.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Dec 15, 2013
 *
 */
@Dependent
public class NewBookPageEventHandler {

	private NewBookPage_2 newBookPage;

	@Inject
	private Event<FindAuthorsBookByIdEvent> /**
	 * @param bookListBookSelectedResponseEvent
	 */
	bookFindByIdEvent;

	public NewBookPageEventHandler() {
	}

	public void attach(NewBookPage_2 newBookPage) {
		this.newBookPage = newBookPage;
	}

	/**
	 * Looks for the BookListBookSelectedResponseEvent.
	 * 
	 * @param bookListBookSelectedResponseEvent
	 */
	protected void observesBookListBookSelectedResponseEvent(
			@Observes BookListBookSelectedResponseEvent bookListBookSelectedResponseEvent) {

		// Set the book in display
		newBookPage.setCurrentBookInDisplay(bookListBookSelectedResponseEvent
				.getBookDisplay());
	}

	/**
	 * Watches for the book content update event. If the book id matches the one
	 * showing then asks for the new data.
	 * 
	 * @param bookContentUpdatedEvent
	 */
	protected void observesBookContentUpdatedEvent(
			@Observes BookContentUpdatedEvent bookContentUpdatedEvent) {
		String id = bookContentUpdatedEvent.getBookId();
		callBookFindById(id);
	}

	/**
	 * From the bookId a request is made for the book from the
	 * whenPageShownCalled method. The response is received here and processed.
	 * If in the view mode the user-book relationship is checked to see if the
	 * addToMycollections button should be made visible.
	 * 
	 * @param bookFindResponseEvent
	 */
	protected void observesBookFindResponse(
			@Observes BookFindResponseEvent bookFindResponseEvent) {

		// Set page background to the book cover background
		newBookPage.setCurrentBookInDisplay(bookFindResponseEvent
				.getBookDisplay());
	}

	/**
	 * Need to adjust the Heros dynamically
	 * 
	 * @param event
	 */
	protected void observesWindowResizeEvent(@Observes WindowResizeEvent event) {
		newBookPage.adjustSize();
	}

	/**
	 * observersBookContentUpdatedEvent
	 * 
	 * @param id
	 */
	public void callBookFindById(String id) {
		// Test to see if the book id's match
		// If so ask for updated data
		if (id != null) {
			bookFindByIdEvent.fire(new FindAuthorsBookByIdEvent(id));
		}
	}

}
