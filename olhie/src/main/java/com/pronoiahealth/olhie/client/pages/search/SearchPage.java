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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageHidden;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.AnonymousRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;
import com.pronoiahealth.olhie.client.shared.events.book.LeavingSearchPageEvent;
import com.pronoiahealth.olhie.client.shared.events.local.DestroyPageWhenHiddenEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SearchPageLoadedEvent;

/**
 * SearchPage.java<br/>
 * Responsibilities:<br/>
 * 1. Shows Search page<br/>
 * 2. Fires the SearchPageLoadedEvent<br/>
 * 
 * <p>
 * How this works<br/>
 * The user selects this page from the left hand navigation menu. They then
 * enter a search string in the find box and select the find button. The find
 * box and button are part of the SearchComponent widget. Pressing the find
 * button causes a BookSearchEvent to be fire by the SearchComponent. This event
 * is handled by the BookSearchService. The method in that class that handles
 * this event will return a BookSearchResponseEvent containing BookDisplays upto
 * the page search size (defined in the detaspike configuration file
 * (/META-INF/apache-deltaspike.properties). It will also save the state of the
 * search in the session scoped SearchResultHolder. The holder will save the
 * necessary data to respond to future SearchPageNavigationRequestEvent. The
 * BookSearchResponseEvent is also listened for by the SearchPagerWidget. When
 * the widget recieves this event it will fire a
 * SearchPageNavigationRequestEvent with and action of UPDATE_STATE. The
 * receiving method in the BookSearchService will fire a
 * SearchPageNavigationResponseEvent which will be handled by the
 * SearchResultsComponent and the SearchPagerWidget. Because this event will
 * have the original action of UPDATE_STATE the SearchResultsComponent will do
 * nothing with it. However the SearchPagerWidget will update the state of the
 * pager buttons and results display. When buttons are selected on the
 * SearchPagerWidget component the SearchPageNavigationRequestEvent will be
 * fired and handled by the BookSearchService. Depending on what button is
 * pressed the appropriate response will be returned in the
 * SearchPageNavigationResponseEvent. As above this event is observed for by the
 * SearchPagerWidget and the SearchResultsComponent.
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@Dependent
@Page(role = { AnonymousRole.class })
public class SearchPage extends AbstractPage {

	@Inject
	UiBinder<Widget, SearchPage> binder;

	@UiField
	public HTMLPanel searchPlaceHolder;

	@Inject
	public SearchComponent searchComponent;

	@UiField
	public HTMLPanel searchResultsPlaceHolder;

	@Inject
	private SearchResultsComponent searchResultsComponent;

	@Inject
	private Event<SearchPageLoadedEvent> searchPageLoadedEvent;

	@Inject
	private Event<LeavingSearchPageEvent> leavingSearchPageEvent;

	@Inject
	private Event<DestroyPageWhenHiddenEvent> destroyPageWhenHiddenEvent;

	/**
	 * Constructor
	 * 
	 */
	public SearchPage() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		searchPlaceHolder.add(searchComponent);
		searchResultsPlaceHolder.add(searchResultsComponent);
	}

	/**
	 * When the page is hidden the user is navigating away. Fire the
	 * LeavingSearchPageEvent to tell the BookSearchService.
	 */
	@PageHidden
	protected void pageHidden() {
		leavingSearchPageEvent.fire(new LeavingSearchPageEvent());
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		setPageBackgroundClass("ph-SearchPage-Background");
		searchPageLoadedEvent.fire(new SearchPageLoadedEvent());
		searchComponent.setFocusOnSearchQryBox();
	}
}
