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
package com.pronoiahealth.olhie.client.widgets.booklist3d;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.shared.GWT;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.events.book.CheckBookIsAuthorResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.AddBookToMyCollectionResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.RemoveBookFromMyCollectionResponseEvent;

/**
 * BookList3DEventObserver.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Dec 13, 2013
 *
 */
@Dependent
public class BookListDiv3DEventObserver {
	@Inject
	protected PageNavigator nav;

	private BookList3D bookList;

	/**
	 * Constructor
	 *
	 */
	public BookListDiv3DEventObserver() {
	}
	
	@PostConstruct
	protected void postConstruct() {
		nav.hashCode();
	}

	public void attachBookList(BookList3D bookList) {
		this.bookList = bookList;
	}

	/**
	 * Watch for events where the book has been added to the users collection
	 * 
	 * @param addBookToMyCollectionResponseEvent
	 */
	protected void observersAddBookToMyCollectionResponseEvent(
			@Observes AddBookToMyCollectionResponseEvent addBookToMyCollectionResponseEvent) {
		try {
			String bookId = addBookToMyCollectionResponseEvent.getBookId();
			BookLIWidget bookListItemWidget = bookList.getBliwMap().get(
					bookId);

			// Toggle the button so the user can now remove the book from his
			// collection
			bookListItemWidget.setAfterAddToMyCollection();
		} catch (Exception e) {
			GWT.log("BookList3DEventObserver: " + e.getMessage());
		}
	}

	/**
	 * Watch for events where the book has been removed from the users
	 * collection
	 * 
	 * @param removeBookFromMyCollectionResponseEvent
	 */
	protected void observesRemoveBookFromMyCollectionResponseEvent(
			@Observes RemoveBookFromMyCollectionResponseEvent removeBookFromMyCollectionResponseEvent) {
		try {
			String bookId = removeBookFromMyCollectionResponseEvent.getBookId();
			BookLIWidget bookListItemWidget = bookList.getBliwMap().get(
					bookId);

			// Toggle the button so the user can now add the book to his
			// collection
			bookListItemWidget.setAfterRemoveFromMyCollection();
		} catch (Exception e) {
			GWT.log("BookList3DEventObserver: " + e.getMessage());
		}
	}

	/**
	 * The corresponding request event is fired when the user clicks the book.
	 * When this happens a check is done to see if the user clicking the book is
	 * the author or co-author of the book. If yes then the user will be
	 * navigated to the NewBookPage where they cab edit the contents of the
	 * book.
	 * 
	 * 
	 * @param checkBookIsAuthorResponseEvent
	 */
	protected void observesCheckBookIsAuthorResponseEvent(
			@Observes CheckBookIsAuthorResponseEvent checkBookIsAuthorResponseEvent) {
		boolean isAuthorCoAuthor = checkBookIsAuthorResponseEvent
				.isAuthorCoAuthor();
		if (isAuthorCoAuthor == true) {
			Multimap<String, String> map = ArrayListMultimap.create();
			map.put("bookId", checkBookIsAuthorResponseEvent.getBookId());
			nav.performTransition(NavEnum.NewBookPage_2.toString(), map);
		}
	}

}
