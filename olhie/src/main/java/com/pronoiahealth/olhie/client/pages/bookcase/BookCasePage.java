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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.ui.nav.client.local.Page;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.RegisteredRole;
import com.pronoiahealth.olhie.client.pages.AppSelectors;
import com.pronoiahealth.olhie.client.pages.MenuSyncSecureAbstractPage;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.vo.BookForDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCollection;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.client.shared.vo.BookState;
import com.pronoiahealth.olhie.client.shared.vo.Bookcase;
import com.pronoiahealth.olhie.client.widgets.bookcase.BookcaseWidget;

/**
 * BookCasePage<br/>
 * Responsibilities:<br/>
 * 1. Displays Bookcase view for a user<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Page(role = {RegisteredRole.class})
public class BookCasePage extends MenuSyncSecureAbstractPage {

	@Inject
	UiBinder<Widget, BookCasePage> binder;

	@UiField
	public HTMLPanel bookcaseContainer;

	@Inject
	private MessageBus bus;

	private Bookcase currentBookcase;

	private BookcaseWidget bWidget;

	public BookCasePage() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		this.currentBookcase = getBookcase();

		// Build the widget but don't attach yet
		// There are some issues with the javascript and
		// when you call attach.
		// See onAttach below.
		bWidget = new BookcaseWidget(currentBookcase, ".center-background");// ".center-background");

		// Bind a listener to BookCaseWidgetBookClicked
		bus.subscribeLocal("BookCaseWidgetBookClicked", new MessageCallback() {
			@Override
			public void callback(Message message) {
				String bookId = message.get(String.class, "bookId");
				Multimap<String, Object> map = ArrayListMultimap.create();
				map.put("bookId", bookId);
				nav.performTransition(NavEnum.BookReviewPage.toString(), map);
			}
		});
	}

	@Override
	public void onLoad() {
		super.onLoad();
		setPageBackgroundClass("ph-Bookcase-Background");
		setContainerSize();
		bookcaseContainer.add(bWidget);
	}

	private void setContainerSize() {
		GQuery gObj = AppSelectors.INSTANCE.getCenterBackground().first();
		int h = gObj.height();
		this.bookcaseContainer.setHeight("" + h + "px");
	}

	private Bookcase getBookcase() {
		Bookcase bookcase = new Bookcase();

		BookCollection bk1 = new BookCollection();
		bk1.setCollectionName("Authored by Me");

		for (int i = 1; i <= 100; i++) {
			BookForDisplay book1 = new BookForDisplay("id", "Title 1", "John D", 4, "Test introduction",
					"TOC", "06/26/1958", "400", "Book 1 Book2",
					BookState.BOOK_STATE_INVISIBLE, new BookCategory("yellow", "Legal"),
					new BookCover("Olhie/images/p1.png", "Paper"));
			bk1.addBook(book1);
		}
		bookcase.addCollection(bk1);

		BookForDisplay book2 = new BookForDisplay("id", "Title 1", "John D", 4, "Test introduction",
				"TOC", "06/26/1958", "400", "Book 1 Book2",
				BookState.BOOK_STATE_INVISIBLE, new BookCategory("yellow", "Legal"),
				new BookCover("Olhie/images/p1.png", "Paper"));

		BookForDisplay book3 = new BookForDisplay("id", "Title 1", "John D", 4, "Test introduction",
				"TOC", "06/26/1958", "400", "Book 1 Book2",
				BookState.BOOK_STATE_INVISIBLE, new BookCategory("yellow", "Legal"),
				new BookCover("Olhie/images/p1.png", "Paper"));

		BookCollection bk2 = new BookCollection();
		bk2.setCollectionName("My Favorites");
		bk2.addBook(book2);
		bk2.addBook(book3);
		bookcase.addCollection(bk2);

		return bookcase;
	}
}
