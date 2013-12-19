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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;
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
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.widgets.GlassPanelSpinner;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3DEventObserver;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3D_3;

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
	private Instance<SearchResultsComponentEventHandler> searchResultsComponentEventHandlerFac;

	@Inject
	private Disposer<SearchResultsComponentEventHandler> searchResultsComponentEventHandlerDisposer;

	private SearchResultsComponentEventHandler currentSearchResultsComponentEventHandler;

	@Inject
	private SearchPagerWidget pagerWidget;

	@Inject
	private SyncBeanManager syncManager;

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

		// Initalize currentSearchResultsComponentEventHandler
		currentSearchResultsComponentEventHandler = searchResultsComponentEventHandlerFac
				.get();
		currentSearchResultsComponentEventHandler.attach(this);

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
		if (currentSearchResultsComponentEventHandler != null) {
			searchResultsComponentEventHandlerDisposer
					.dispose(currentSearchResultsComponentEventHandler);
		}
	}

	/**
	 * Dynamically adjusts the page size
	 */
	protected void adjustSize() {
		if (isAttached() == true) {
			// Difference from window height
			int wndHeight = this.getPageContainerHeight();

			// 128 is search at top + Search results label + footer at bottom
			int newSearchResultsScrollerHeight = wndHeight - 188;
			searchResultsContainerList.setSize("100%", ""
					+ newSearchResultsScrollerHeight + "px");
		}
	}

	protected void setNewCurrentBookList3D(List<BookDisplay> lst,
			BookList3DEventObserver bookListObserver) {
		currentInstanceBookList3D_3 = bookList3DFac.get();
		currentInstanceBookList3D_3.build(lst, false);
		bookListObserver.attachBookList(currentInstanceBookList3D_3);
		searchResultsContainerList.add(currentInstanceBookList3D_3);
	}

	protected void clearResultsContainer() {
		if (searchResultsContainerList.getWidget() != null) {
			if (currentInstanceBookList3D_3 != null) {
				bookList3DDisposer.dispose(currentInstanceBookList3D_3);
				currentInstanceBookList3D_3 = null;
			}
			searchResultsContainerList.clear();
		}
	}

	protected void setSpinnerVisibility(boolean show) {
		gSpinner.setVisible(show);
	}

	protected void setNoResultsLabel() {
		resultsLbl.setText(SearchConstants.INSTANCE.searchResults());
		Label noBooksLbl = new Label(SearchConstants.INSTANCE.noResults());
		noBooksLbl.setStyleName("ph-SearchResults-NoResults-Lbl");
		searchResultsContainerList.add(noBooksLbl);
	}
}
