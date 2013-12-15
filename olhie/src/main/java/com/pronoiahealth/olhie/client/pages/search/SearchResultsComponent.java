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

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.pages.AbstractComposite;
import com.pronoiahealth.olhie.client.shared.constants.SearchPageActionEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.SearchPageNavigationResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SearchPageLoadedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.widgets.GlassPanelSpinner;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3DEventObserver;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3D_3;

//import com.pronoiahealth.olhie.client.widgets.booklist3d.BookList3D_2;

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
@Dependent
public class SearchResultsComponent extends AbstractComposite {

	@Inject
	UiBinder<Widget, SearchResultsComponent> binder;

	@Inject
	private PageNavigator nav;

	@UiField
	public HTMLPanel searchContainer;

	@UiField
	public FluidRow searchResultsHeader;

	@UiField
	public Column pagerContainerCol;

	@UiField
	public HTMLPanel pagerContainer;

	@UiField
	public ScrollPanel searchResultsContainerList;

	@UiField
	public Label resultsLbl;

	private GlassPanelSpinner gSpinner;

	@Inject
	private Instance<BookList3D_3> bookList3DFac;

	private BookList3D_3 currentInstanceBookList3D_3;

	@Inject
	private Disposer<BookList3D_3> bookList3DDisposer;

	@Inject
	private SearchPagerWidget pagerWidget;

	@Inject
	private BookList3DEventObserver bookListObserver;

	@Inject
	private SyncBeanManager syncManger;

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

		// Header for list
		searchResultsHeader.addStyleName("ph-SearchResults-Header");

		// Set search label
		resultsLbl.setText(SearchConstants.INSTANCE.searchResults());

		// Construct spinner
		gSpinner = new GlassPanelSpinner();
		gSpinner.setVisible(false);
		searchContainer.add(gSpinner);

		// Set pager column style
		pagerContainerCol.getElement().setAttribute("style",
				"text-align: center;");

		// Attach the pager widget
		pagerContainer.getElement().appendChild(pagerWidget.configure());
	}

	/**
	 * Clears any remaining book list widget
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#onUnload()
	 */
	@Override
	protected void onUnload() {
		// TODO Auto-generated method stub
		super.onUnload();
		clearResultsContainer();
		
		// Destroy the event observer
		syncManger.destroyBean(bookListObserver);
	}

	/**
	 * Observes the results from a book search query.
	 * 
	 * @param event
	 */
	public void observesBookSearchResponseEvent(
			@Observes BookSearchResponseEvent event) {

		// Hide spinner
		gSpinner.setVisible(false);

		// Clear results container
		clearResultsContainer();

		// Display the books, if nothing is returned the display a message
		List<BookDisplay> lst = event.getBookDisplayList();
		if (lst != null && lst.size() > 0) {
			setNewCurrentBookList3D(lst);
		} else {
			resultsLbl.setText(SearchConstants.INSTANCE.searchResults());
			Label noBooksLbl = new Label(SearchConstants.INSTANCE.noResults());
			noBooksLbl.setStyleName("ph-SearchResults-NoResults-Lbl");
			searchResultsContainerList.add(noBooksLbl);
		}
	}

	/**
	 * Similar actions to the observesBookSearchResponseEvent except the
	 * SearchPageNavigationResponseEvent as a result of the user navigating
	 * through an already returned search result set.
	 * 
	 * 
	 * @param searchPageNavigationResponseEvent
	 */
	protected void observesSearchPageNavigationResponseEvent(
			@Observes SearchPageNavigationResponseEvent searchPageNavigationResponseEvent) {
		// This component does not handle the UPDATE_STATE action
		if (searchPageNavigationResponseEvent.getRequestedAction() != SearchPageActionEnum.UPDATE_STATE) {
			// Hide spinner
			gSpinner.setVisible(false);

			// Clear results container
			clearResultsContainer();

			// Display the books, if nothing is returned the display a message
			List<BookDisplay> lst = searchPageNavigationResponseEvent
					.getBookDisplays();
			if (lst != null && lst.size() > 0) {
				setNewCurrentBookList3D(lst);
			} else {
				resultsLbl.setText(SearchConstants.INSTANCE.searchResults());
				Label noBooksLbl = new Label(
						SearchConstants.INSTANCE.noResults());
				noBooksLbl.setStyleName("ph-SearchResults-NoResults-Lbl");
				searchResultsContainerList.add(noBooksLbl);
			}
		}
	}

	/**
	 * @param bookSearchEvent
	 */
	protected void observesBookSearchEvent(
			@Observes BookSearchEvent bookSearchEvent) {
		// Show spinner
		gSpinner.setVisible(true);
	}

	/**
	 * Hide the spinner on an error
	 * 
	 * @param serviceErrorEvent
	 */
	protected void observesServiceErrorEvent(
			@Observes ServiceErrorEvent serviceErrorEvent) {
		// hide spinner
		gSpinner.setVisible(false);

		// Clear results container
		clearResultsContainer();
	}

	/**
	 * Hide the spinner on a error
	 * 
	 * @param clientErrorEvent
	 */
	protected void observesClientErrorEvent(
			@Observes ClientErrorEvent clientErrorEvent) {
		// hide spinner
		gSpinner.setVisible(false);

		// Clear results container
		clearResultsContainer();
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

	/**
	 * Client is logging out so clear the returned books
	 * 
	 * @param clientLogoutRequestEvent
	 */
	protected void observesClientLogoutRequestEvent(
			@Observes ClientLogoutRequestEvent clientLogoutRequestEvent) {
		// Clear results container
		clearResultsContainer();
	}

	/**
	 * Dynamically adjusts the page size
	 */
	private void adjustSize() {
		if (isAttached() == true) {
			// Difference from window height
			int wndHeight = this.getPageContainerHeight();

			// 128 is search at top + Search results label + footer at bottom
			int newSearchResultsScrollerHeight = wndHeight - 188;
			searchResultsContainerList.setSize("100%", ""
					+ newSearchResultsScrollerHeight + "px");
		}
	}

	private void setNewCurrentBookList3D(List<BookDisplay> lst) {
		currentInstanceBookList3D_3 = bookList3DFac.get();
		currentInstanceBookList3D_3.build(lst, false);
		bookListObserver.attachBookList(currentInstanceBookList3D_3, "SearchResultsComponent");
		searchResultsContainerList.add(currentInstanceBookList3D_3);
	}

	private void clearResultsContainer() {
		if (searchResultsContainerList.getWidget() != null) {
			searchResultsContainerList.clear();
			if (currentInstanceBookList3D_3 != null) {
				bookList3DDisposer.dispose(currentInstanceBookList3D_3);
				currentInstanceBookList3D_3 = null;
			}
		}

		// Are there any other reference to the booklist
		// This is a hack for now.
		Collection<IOCBeanDef<BookList3D_3>> book3DList = syncManger
				.lookupBeans(BookList3D_3.class);
		if (book3DList != null && book3DList.size() > 0) {
			for (IOCBeanDef<BookList3D_3> b : book3DList) {
				syncManger.destroyBean(b.getInstance());
			}
		}
	}
}
