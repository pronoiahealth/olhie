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

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.RegisteredRole;
import com.pronoiahealth.olhie.client.pages.AppSelectors;
import com.pronoiahealth.olhie.client.pages.MenuSyncSecureAbstractPage;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.GetMyBookcaseEvent;
import com.pronoiahealth.olhie.client.shared.events.GetMyBookcaseResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.widgets.booklist3d.BookList3D;
import com.pronoiahealth.olhie.client.widgets.booklist3d.BookSelectCallBack;

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
@Page(role = { RegisteredRole.class })
public class BookCasePage extends MenuSyncSecureAbstractPage {

	@Inject
	UiBinder<Widget, BookCasePage> binder;

	@UiField
	public HTMLPanel bookcaseContainer;

	@UiField
	public HTMLPanel myBooksTab;

	@UiField
	public HTMLPanel myCoBooksTab;

	@UiField
	public HTMLPanel myCollectionTab;

	@UiField
	public TabLayoutPanel tabPanel;

	private BookList3D bookList;

	@Inject
	private ClientUserToken clientToken;

	@Inject
	private Event<GetMyBookcaseEvent> getMyBookcaseEvent;

	private BookSelectCallBack bookSelectCallBack;

	public BookCasePage() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// create selector - This will fire the bookId to the server to see what
		// the users relationship with the book is. If he's the author or
		// co-author he will be directed to the NewBookPage to edit the book. If
		// not he will be directed to the book review page.
		bookSelectCallBack = new BookSelectCallBack() {
			@Override
			public void onBookSelect(String bookId) {
				if (bookId != null) {
					Window.alert("Called");
				}
			}
		};

	}

	/**
	 * When the page is shown ask the server for the list of books
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.MenuSyncSecureAbstractPage#whenPageShownCalled()
	 */
	@Override
	protected boolean whenPageShownCalled() {
		boolean ret = super.whenPageShownCalled();
		if (ret == true) {
			getMyBookcaseEvent.fire(new GetMyBookcaseEvent(clientToken
					.getUserId()));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see com.google.gwt.user.client.ui.Widget#onLoad()
	 */
	@Override
	public void onLoad() {
		super.onLoad();
		setPageBackgroundClass("ph-Bookcase-Background");
		setContainerSize();
		getMyBookcaseEvent
				.fire(new GetMyBookcaseEvent(clientToken.getUserId()));
	}

	private void setContainerSize() {
		GQuery gObj = AppSelectors.INSTANCE.getCenterBackground().first();
		int h = gObj.height();
		this.bookcaseContainer.setHeight("" + h + "px");
	}

	/**
	 * Need to adjust the search results panel dynamically
	 * 
	 * @param event
	 */
	protected void observesWindowResizeEvent(@Observes WindowResizeEvent event) {
		setContainerSize();
	}

	/**
	 * Watches for the list of books to display
	 * 
	 * @param getMyBookcaseResponseEvent
	 */
	protected void observesGetMyBookcaseResponseEvent(
			@Observes GetMyBookcaseResponseEvent getMyBookcaseResponseEvent) {
		Map<UserBookRelationshipEnum, List<BookDisplay>> bookMap = getMyBookcaseResponseEvent
				.getDisplayMap();

		// Get the data and put it in the tabs
		for (Map.Entry<UserBookRelationshipEnum, List<BookDisplay>> entry : bookMap
				.entrySet()) {
			UserBookRelationshipEnum key = entry.getKey();
			List<BookDisplay> lst = entry.getValue();
			BookList3D bookLst = new BookList3D(lst, bookSelectCallBack);

			switch (key) {
			case CREATOR:
				myBooksTab.clear();
				myBooksTab.add(bookLst);
				break;

			case COAUTHOR:
				myCoBooksTab.clear();
				myCoBooksTab.add(bookLst);
				break;

			case MYCOLLECTION:
				myCollectionTab.clear();
				myCollectionTab.add(bookLst);
				break;
			}
		}
	}
}
