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
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
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
import com.pronoiahealth.olhie.client.shared.events.local.DestroyPageWhenHiddenEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookcaseDisplay;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.widgets.GlassPanelSpinner;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3D_3;

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
@Dependent
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

	@Inject
	private Event<DestroyPageWhenHiddenEvent> destroyPageWhenHiddenEvent;

	@Inject
	private Instance<BookCaseContainerWidget> bookCaseContainerWidgetFac;

	@Inject
	private Disposer<BookCaseContainerWidget> bookCaseContainerWidgetDisposer;

	private BookCaseContainerWidget currentBookCaseContainerWidget;

	@Inject
	private Instance<BookCasePageEventHandler> bookCasePageEventHandlerFac;

	@Inject
	private Disposer<BookCasePageEventHandler> bookCasePageEventHandlerDisposer;

	private BookCasePageEventHandler currentBookCasePageEventHandler;

	public BookCasePage() {
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
		if (currentBookCasePageEventHandler != null) {
			bookCasePageEventHandlerDisposer
					.dispose(currentBookCasePageEventHandler);
		}
		disposeTabs();
		bookcaseContainer.clear();
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Create and attach event listener
		currentBookCasePageEventHandler = bookCasePageEventHandlerFac.get();
		currentBookCasePageEventHandler.attach(this);

		// Spinner
		gSpinner = new GlassPanelSpinner();
		gSpinner.setVisible(false);
		bookcaseContainer.add(gSpinner);

		// Bind tab panel selection event
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				// First dispose of any existing tabs
				disposeTabs();

				// Now send a request for new data
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

	/**
	 * Clean up tabs
	 */
	protected void disposeTabs() {
		// Destroy current widgets
		// The tabs should only contain 1 widget
		if (this.currentBookCaseContainerWidget != null) {
			// HTMLPanel activeTabContainer = (HTMLPanel)
			// currentBookCaseContainerWidget
			// .getParent();
			bookCaseContainerWidgetDisposer
					.dispose(currentBookCaseContainerWidget);
			currentBookCaseContainerWidget.removeFromParent();
			Element el = currentBookCaseContainerWidget.getElement();
			Node firstNode = null;
			while ((firstNode = el.getFirstChild()) != null) {
				el.removeChild(firstNode);
			}
			currentBookCaseContainerWidget = null;
			// activeTabContainer.clear();
		}
	}

	protected void loadNewContainerWidget(BookcaseEnum tab,
			List<BookcaseDisplay> lst) {
		currentBookCaseContainerWidget = bookCaseContainerWidgetFac.get();
		currentBookCaseContainerWidget.loadDataAndInit(lst);

		switch (tab) {
		case AUTHOR:
			myBooksTab.add(currentBookCaseContainerWidget);
			break;
		case COAUTHOR:
			myCoBooksTab.add(currentBookCaseContainerWidget);
			break;
		case MYCOLLECTION:
			myCollectionTab.add(currentBookCaseContainerWidget);
			break;
		}
	}

	protected void showBookcaseSpinner(boolean show) {
		gSpinner.setVisible(show);
	}

	protected void destroyVisibleBookList() {
		currentBookCaseContainerWidget.disposeBookList();
	}

	protected BookList3D_3 addBookListToCurrentBookcaseContainer(
			List<BookDisplay> bookDisplayList) {
		return currentBookCaseContainerWidget
				.attachNewBookList(bookDisplayList);
	}
}
