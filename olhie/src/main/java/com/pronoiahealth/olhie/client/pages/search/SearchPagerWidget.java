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

import static com.arcbees.gquery.tooltip.client.Tooltip.Tooltip;
import static com.google.gwt.query.client.GQuery.$;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.pronoiahealth.olhie.client.shared.constants.SearchPageActionEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.SearchPageNavigationRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.book.SearchPageNavigationResponseEvent;

/**
 * SearchPagerWidget.java<br/>
 * Responsibilities:<br/>
 * 1. Controls the state of the pager buttons and display.<br/>
 * 2. Signals the service (BookSearchService) when the user wants to navigate
 * the result set.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 27, 2013
 * 
 */
@Templated("#pager")
public class SearchPagerWidget extends Composite {

	@DataField
	private Element pager = DOM.createDiv();

	GQuery pagerQry;
	GQuery firstButton;
	GQuery previousButton;
	GQuery nextButton;
	GQuery lastButton;
	GQuery currentPageDisplay;

	@Inject
	private javax.enterprise.event.Event<SearchPageNavigationRequestEvent> searchPageNavigationRequestEvent;

	/**
	 * Required CDI no-args Constructor
	 * 
	 */
	public SearchPagerWidget() {
	}

	/**
	 * Add some gwtQuery tooltips and attach click handlers to the buttons. on
	 * the buttons.
	 */
	@PostConstruct
	protected void postConstruct() {
		// Add the buttons
		pagerQry = $(pager);

		// First Button
		firstButton = pagerQry.find(".searchPagerFirstPage").first();
		firstButton.bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				if (!firstButton.hasClass("disabled")) {
					searchPageNavigationRequestEvent
							.fire(new SearchPageNavigationRequestEvent(
									SearchPageActionEnum.FIRST));
				}
				return false;
			}
		});

		// previous button
		previousButton = pagerQry.find(".searchPagerPreviousPage").first();
		previousButton.bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				if (!previousButton.hasClass("disabled")) {
					searchPageNavigationRequestEvent
							.fire(new SearchPageNavigationRequestEvent(
									SearchPageActionEnum.PREVIOUS));
				}
				return false;
			}
		});

		// Next button
		nextButton = pagerQry.find(".searchPagerNextPage").first();
		nextButton.bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				if (!nextButton.hasClass("disabled")) {
					searchPageNavigationRequestEvent
							.fire(new SearchPageNavigationRequestEvent(
									SearchPageActionEnum.NEXT));
				}
				return false;
			}
		});

		// Last button
		lastButton = pagerQry.find(".searchPagerLastPage").first();
		lastButton.bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				if (!lastButton.hasClass("disabled")) {
					searchPageNavigationRequestEvent
							.fire(new SearchPageNavigationRequestEvent(
									SearchPageActionEnum.LAST));
				}
				return false;
			}
		});

		// searchPagerPageDisplay
		currentPageDisplay = pagerQry.find(".searchPagerPageDisplay").first();

		// Initially hide the widget
		pagerQry.hide();
		
		// Add the tool tips
		$("[rel=tooltip]", pager).as(Tooltip).tooltip();
	}

	/**
	 * Returns the pager element for attachment to the DOM.
	 * 
	 * @return
	 */
	public Element configure() {
		return pager;
	}

	/**
	 * Observes for responses to the navigation event and updates the display.
	 * 
	 * @param searchPageNavigationResponseEvent
	 */
	protected void observesSearchPageNavigationResponseEvent(
			@Observes SearchPageNavigationResponseEvent searchPageNavigationResponseEvent) {

		int endIdx = searchPageNavigationResponseEvent.getEndIndex();
		int startIdx = searchPageNavigationResponseEvent.getStartIndex();
		int total = searchPageNavigationResponseEvent.getTotalInResultsSet();
		boolean isStartPage = searchPageNavigationResponseEvent.isFirstPage();
		boolean isLastPage = searchPageNavigationResponseEvent.isLastPage();
		boolean isFitsOnOnePage = searchPageNavigationResponseEvent
				.isFitsOnOnePage();

		if (total > 0) {
			pagerQry.show();

			// Set the searchPagerPageDisplay
			currentPageDisplay.text(SearchMessages.INSTANCE
					.searchPagerResultsReturned(startIdx, endIdx, total));

			// Set the buttons
			// disable all the buttons
			if (isFitsOnOnePage == true) {
				setButtonState(firstButton, false);
				setButtonState(previousButton, false);
				setButtonState(nextButton, false);
				setButtonState(lastButton, false);
			} else {
				// disable the First and previous button
				if (isStartPage == true) {
					setButtonState(firstButton, false);
					setButtonState(previousButton, false);
				} else {
					setButtonState(firstButton, true);
					setButtonState(previousButton, true);
				}

				if (isLastPage == true) {
					setButtonState(nextButton, false);
					setButtonState(lastButton, false);
				} else {
					setButtonState(nextButton, true);
					setButtonState(lastButton, true);
				}
			}

		} else {
			pagerQry.hide();
		}
	}

	/**
	 * This event is fired from the BookSearchService. It indicates that a new
	 * search has been performed. This component will fire a
	 * SearchPageNavigationRequestEvent with an action of UPDATE_STATE to gets
	 * its initial display state.
	 * 
	 * @param bookSearchResponseEvent
	 */
	protected void observesBookSearchResponseEvent(
			@Observes BookSearchResponseEvent bookSearchResponseEvent) {
		searchPageNavigationRequestEvent
				.fire(new SearchPageNavigationRequestEvent(
						SearchPageActionEnum.UPDATE_STATE));
	}

	/**
	 * Helper to set button state
	 * 
	 * @param button
	 * @param enabled
	 */
	private void setButtonState(GQuery button, boolean enabled) {
		if (enabled == true) {
			button.removeClass("disabled");
			button.css("opacity", "1.0");
		} else {
			button.addClass("disabled");
			button.css("opacity", "0.5");
		}
	}

}
