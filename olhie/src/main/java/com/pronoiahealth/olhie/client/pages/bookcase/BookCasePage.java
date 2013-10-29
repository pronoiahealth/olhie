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
import javax.annotation.PreDestroy;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.RegisteredRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;
import com.pronoiahealth.olhie.client.pages.AppSelectors;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.bookcase.GetMyBookcaseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.GetMyBookcaseResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.widgets.GlassPanelSpinner;
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
public class BookCasePage extends AbstractPage {

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

	//@UiField
	//public Icon spinner;
	
	private GlassPanelSpinner gSpinner;

	@Inject
	private ClientUserToken clientToken;

	@Inject
	private Event<GetMyBookcaseEvent> getMyBookcaseEvent;

	private BookSelectCallBack bookSelectCallBack;

	private boolean responseRet = false;

	public BookCasePage() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		//
		gSpinner = new GlassPanelSpinner();
		gSpinner.setVisible(false);
		bookcaseContainer.add(gSpinner);
		
		// Initial state of spinner
		// spinner.setVisible(false);

		// create selector - This will fire the bookId to the server to see what
		// the users relationship with the book is. If he's the author or
		// co-author he will be directed to the NewBookPage to edit the book. If
		// not he will be directed to the book review page.
		bookSelectCallBack = new BookSelectCallBack() {
			@Override
			public void onBookSelect(String bookId) {
				if (bookId != null) {
					Multimap<String, Object> map = ArrayListMultimap.create();
					map.put("bookId", bookId);
					nav.performTransition(NavEnum.NewBookPage.toString(), map);
				}
			}
		};

		// Bind tab panel selection event
		// tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
		// @Override
		// public void onSelection(SelectionEvent<Integer> event) {
		// }
		// });

	}
	
	@PreDestroy
	protected void preDestroy() {
		gSpinner.getElement();
	}

	/**
	 * When the page is shown ask the server for the list of books
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.MenuSyncSecureAbstractPage#whenPageShownCalled()
	 */
	@PageShowing
	protected void pageShowing() {
		gSpinner.setVisible(true);
		//spinner.setVisible(true);
		getMyBookcaseEvent
				.fire(new GetMyBookcaseEvent(clientToken.getUserId()));
	}

	/**
	 * @see com.google.gwt.user.client.ui.Widget#onLoad()
	 */
	@Override
	public void onLoad() {
		super.onLoad();
		setPageBackgroundClass("ph-Bookcase-Background");
		setContainerSize();
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

		List<BookDisplay> books = null;
		books = bookMap.get(UserBookRelationshipEnum.CREATOR);
		if (books != null && books.size() > 0) {
			myBooksTab.add(new BookList3D(books, bookSelectCallBack));
		}

		books = bookMap.get(UserBookRelationshipEnum.COAUTHOR);
		if (books != null && books.size() > 0) {
			myCoBooksTab.add(new BookList3D(books, bookSelectCallBack));
		}

		books = bookMap.get(UserBookRelationshipEnum.MYCOLLECTION);
		if (books != null && books.size() > 0) {
			myCollectionTab.add(new BookList3D(books, bookSelectCallBack));
		}

		// Set the spinner state
		gSpinner.setVisible(false);
		//spinner.setVisible(false);
	}

	/**
	 * Hide the spinner on an error
	 * 
	 * @param serviceErrorEvent
	 */
	protected void observesServiceErrorEvent(
			@Observes ServiceErrorEvent serviceErrorEvent) {
		// Set the spinner visible
		gSpinner.setVisible(false);
		// spinner.setVisible(false);
	}

	/**
	 * Hide the spinner on a error
	 * 
	 * @param clientErrorEvent
	 */
	protected void observesClientErrorEvent(
			@Observes ClientErrorEvent clientErrorEvent) {
		// Set the spinner visible
		gSpinner.setVisible(false);
	}
}
