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
package com.pronoiahealth.olhie.client.widgets.booklist3d.errai;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
public class BookList3DEventObserver {
	@Inject
	protected PageNavigator nav;

	private BookList3D_3 bookList;
	
	private String clazz;

	/**
	 * Constructor
	 *
	 */
	public BookList3DEventObserver() {
	}
	
	@PostConstruct
	protected void postConstruct() {
		nav.hashCode();
	}
	
	@PreDestroy
	protected void preDestroy() {
		nav.getClass();
	}

	public void attachBookList(BookList3D_3 bookList, String clazz) {
		this.bookList = bookList;
		this.clazz = clazz;
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
			BookListItemWidget bookListItemWidget = bookList.getBliwMap().get(
					bookId);

			// Toggle the button so the user can now remove the book from his
			// collection
			bookListItemWidget.getTocWidget()
					.setMyCollectionBtnRemoveFromCollection(false);

			// Set the icon on the front of the book indicating that the book is
			// in
			// the users collection
			bookListItemWidget.getMyCollectionIndicator()
					.setAddToMyCollectionBtn(true);
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
			BookListItemWidget bookListItemWidget = bookList.getBliwMap().get(
					bookId);

			// Toggle the button so the user can now add the book to his
			// collection
			bookListItemWidget.getTocWidget()
					.setMyCollectionBtnAddToCollection(false);

			// Hide the icon on the front of the book indicating that the book
			// is
			// not in
			// the users collection
			bookListItemWidget.getMyCollectionIndicator()
					.setHideMyCollectionBtn();
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
			Multimap<String, Object> map = ArrayListMultimap.create();
			map.put("bookId", checkBookIsAuthorResponseEvent.getBookId());
			nav.performTransition(NavEnum.NewBookPage_2.toString(), map);
		}
	}

}
