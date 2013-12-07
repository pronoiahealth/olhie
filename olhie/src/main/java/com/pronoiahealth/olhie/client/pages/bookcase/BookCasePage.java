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

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.RegisteredRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;
import com.pronoiahealth.olhie.client.pages.AppSelectors;
import com.pronoiahealth.olhie.client.pages.bookcase.widgets.BookCaseContainerWidget;
import com.pronoiahealth.olhie.client.shared.constants.BookcaseEnum;
import com.pronoiahealth.olhie.client.shared.events.bookcase.GetMyBookcaseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.MyBooksForBookcaseSmallIconRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.MyBooksForBookcaseSmallIconResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookcaseDisplay;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.widgets.GlassPanelSpinner;

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

	private GlassPanelSpinner gSpinner;

	@Inject
	private ClientUserToken clientToken;

	@Inject
	private Event<GetMyBookcaseEvent> getMyBookcaseEvent;

	@Inject
	private Event<MyBooksForBookcaseSmallIconRequestEvent> myBooksForBookcaseSmallIconRequestEvent;

	private boolean responseRet = false;

	@Inject
	private Instance<BookCaseContainerWidget> bookCaseContainerWidgetFac;

	@Inject
	private Disposer<BookCaseContainerWidget> bookCaseContainerWidgetDisposer;

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

		// Bind tab panel selection event
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				int tabIdx = event.getSelectedItem();
				BookcaseEnum tabVal = null;
				switch (tabIdx) {
				case 0:
					tabVal = BookcaseEnum.AUTHOR;
					break;
				case 1:
					tabVal = BookcaseEnum.COAUTHOR;
					break;
				case 2:
					tabVal = BookcaseEnum.MYCOLLECTION;
					break;
				}

				// Fire event
				myBooksForBookcaseSmallIconRequestEvent
						.fire(new MyBooksForBookcaseSmallIconRequestEvent(
								clientToken.getUserId(), tabVal));
			}
		});

	}

	/**
	 * When the page is shown ask the server for the list of books
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.MenuSyncSecureAbstractPage#whenPageShownCalled()
	 */
	@PageShowing
	protected void pageShowing() {
		// Load the author books by default
		gSpinner.setVisible(true);
		myBooksForBookcaseSmallIconRequestEvent
				.fire(new MyBooksForBookcaseSmallIconRequestEvent(clientToken
						.getUserId(), BookcaseEnum.AUTHOR));
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

	@Override
	public void onUnload() {
		super.onUnload();
		disposeTabs();
		bookcaseContainer.clear();
	}

	/**
	 * Adjusts the container height
	 */
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

	protected void observesMyBooksForBookcaseSmallIconResponseEvent(
			@Observes MyBooksForBookcaseSmallIconResponseEvent myBooksForBookcaseSmallIconResponseEvent) {

		// Get the parameters
		List<BookcaseDisplay> lst = myBooksForBookcaseSmallIconResponseEvent
				.getBookCaseDisplayLst();
		BookcaseEnum requestedTab = myBooksForBookcaseSmallIconResponseEvent
				.getRequestedTab();

		// Clear all tabs
		disposeTabs();

		// Create widget
		BookCaseContainerWidget widget = null;
		if (lst != null && lst.size() > 0) {
			widget = bookCaseContainerWidgetFac.get();
			widget.loadDataAndInit(lst);
		}

		switch (requestedTab) {
		// Load the appropriate list
		case AUTHOR:
			myBooksTab.add(widget);
			break;
		case COAUTHOR:
			myCoBooksTab.add(widget);
			break;
		case MYCOLLECTION:
			myCollectionTab.add(widget);
			break;
		}

		// Set the spinner state
		gSpinner.setVisible(false);
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

	private void disposeTabs() {
		// Destroy current widgets
		// The tabs should only contain 1 widget
		if (myBooksTab != null && myBooksTab.getWidgetCount() > 0) {
			BookCaseContainerWidget w = (BookCaseContainerWidget) myBooksTab
					.getWidget(0);
			bookCaseContainerWidgetDisposer.dispose(w);
			myBooksTab.clear();
		}

		if (myCoBooksTab != null && myCoBooksTab.getWidgetCount() > 0) {
			BookCaseContainerWidget w = (BookCaseContainerWidget) myCoBooksTab
					.getWidget(0);
			bookCaseContainerWidgetDisposer.dispose(w);
			myCoBooksTab.clear();
		}

		if (myCollectionTab != null && myCollectionTab.getWidgetCount() > 0) {
			BookCaseContainerWidget w = (BookCaseContainerWidget) myCollectionTab
					.getWidget(0);
			bookCaseContainerWidgetDisposer.dispose(w);
			myCollectionTab.clear();
		}
	}
}
