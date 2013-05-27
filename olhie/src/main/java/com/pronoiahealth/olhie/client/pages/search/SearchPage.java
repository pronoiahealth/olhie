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
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShown;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.AnonymousRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;
import com.pronoiahealth.olhie.client.shared.events.local.SearchPageLoadedEvent;

/**
 * SearchPage.java<br/>
 * Responsibilities:<br/>
 * 1. Shows Search page<br/>
 * 2. Fires the SearchPageLoadedEvent<br/> 
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Page(role={AnonymousRole.class})
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
	public SearchResultsComponent searchResultsComponent;

	@Inject
	private Event<SearchPageLoadedEvent> searchPageLoadedEvent;

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
	
	@PageShown
	protected void pageShown() {
		menuPageVisibleEvent();
		defaultSecurityCheck();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		setPageBackgroundClass("ph-SearchPage-Background");
		searchPageLoadedEvent.fire(new SearchPageLoadedEvent());
	}
}
