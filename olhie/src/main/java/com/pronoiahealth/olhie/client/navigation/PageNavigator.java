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
package com.pronoiahealth.olhie.client.navigation;

import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.nav.client.local.NavigationEvent;
import org.jboss.errai.ui.nav.client.local.TransitionTo;

import com.google.common.collect.Multimap;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.clientfactories.DefaultAppPage;
import com.pronoiahealth.olhie.client.clientfactories.PageRoleMap;
import com.pronoiahealth.olhie.client.pages.bookcase.BookCasePage;
import com.pronoiahealth.olhie.client.pages.bulletinboard.BulletinboardPage;
import com.pronoiahealth.olhie.client.pages.calendar.EventsPage;
import com.pronoiahealth.olhie.client.pages.newbook.NewBookPage_2;
import com.pronoiahealth.olhie.client.pages.search.SearchPage;
import com.pronoiahealth.olhie.client.pages.tv.TVPage;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.local.SyncPageToMenuEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;

/**
 * Navigator <br/>
 * Responsibilities:<br/>
 * 1. Handle "Page" navigations<br/>
 * 2. Shows Login, Comments, and Register dialogs<br/>
 * 3. Central control of page security<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@ApplicationScoped
public class PageNavigator {

	@Inject
	private Navigation nav;

	@Inject
	@PageRoleMap
	private Map<String, Set<String>> pageRolesMap;

	@Inject
	@DefaultAppPage
	private String defAppPageName;

	@Inject
	private ClientUserToken clientUserToken;

	@Inject
	private TransitionTo<BulletinboardPage> showBulletinboardPage;

	@Inject
	private TransitionTo<EventsPage> showEventsPage;

	@Inject
	private TransitionTo<BookCasePage> showBookCasePage;

	@Inject
	private TransitionTo<SearchPage> showSearchPage;

	@Inject
	private TransitionTo<NewBookPage_2> showNewBookPage;

	@Inject
	private TransitionTo<TVPage> showTVPage;

	@Inject
	private Event<SyncPageToMenuEvent> syncPageToMenuEvent;

	@Inject
	public PageNavigator() {
	}

	public Widget getNavContentPanel() {
		return nav.getContentPanel().asWidget();
	}

	/**
	 * Tests the name of the current page being shown against a value.
	 * 
	 * @param pageInQuestionName
	 * @return
	 */
	public boolean isCurrentPage(String pageInQuestionName) {
		String currentPageName = nav.getCurrentPage().name();
		if (currentPageName.equals(pageInQuestionName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Performs various transitions to pages.
	 * 
	 * @param navToPage
	 * @param state
	 */
	public void performTransition(String navToPage,
			Multimap<String, String> state) {
		NavEnum page = NavEnum.valueOf(navToPage);
		switch (page) {
		case BulletinboardPage: {
			if (state == null) {
				showBulletinboardPage.go();
			} else {
				showBulletinboardPage.go(state);
			}
			break;
		}

		case EventsPage: {
			if (state == null) {
				showEventsPage.go();
			} else {
				showEventsPage.go(state);
			}
			break;
		}

		case SearchPage: {
			if (state == null) {
				showSearchPage.go();
			} else {
				showSearchPage.go(state);
			}
			break;
		}

		case BookCasePage: {
			if (state == null) {
				showBookCasePage.go();
			} else {
				showBookCasePage.go(state);
			}
			break;
		}

		// case NewBookPage: {
		case NewBookPage_2: {
			if (state == null) {
				showNewBookPage.go();
			} else {
				showNewBookPage.go(state);
			}
			break;
		}

		// case NewBookPage: {
		case TVPage: {
			if (state == null) {
				showTVPage.go();
			} else {
				showTVPage.go(state);
			}
			break;
		}
		}
	}

	/**
	 * Shows the page with the role DefaultPage.class
	 */
	public void showDefaultPage() {
		nav.goTo(defAppPageName);
	}

	protected void observesNavigationEvent(
			@Observes NavigationEvent navigationEvent) {
		String pageName = navigationEvent.getHistoryToken().getPageName();
		if (pageName.length() > 0) {
			// Check Can navigate to the page security
			boolean canNavTo = false;
			if (clientUserToken.isLoggedIn()) {
				canNavTo = pageRolesMap.get(
						SecurityRoleEnum.valueOf(clientUserToken.getRole())
								.getName()).contains(pageName);
			} else {
				// Only show anonymous pages
				canNavTo = pageRolesMap.get(
						SecurityRoleEnum.ANONYMOUS.getName())
						.contains(pageName);
			}

			if (canNavTo == false) {
				showDefaultPage();
			}

			// Fire SyncPageToMenuEvent
			syncPageToMenuEvent.fire(new SyncPageToMenuEvent(pageName));
		}
	}

	/**
	 * Based on the current users role and the role of the page return a boolean
	 * value. If the role is valid for the page return true other wise false.
	 * Important - This method should only be called in the @PageShown annotated
	 * method to assure the current page is the one your testing.
	 * <p>
	 * This might be a brittle method to allow showing pages. Look into other
	 * methods
	 * </p>
	 * 
	 * @return
	 */
	public boolean allowPageFromShownMethod() {
		String currentPageName = nav.getCurrentPage().name();

		if (clientUserToken.isLoggedIn()) {
			return pageRolesMap.get(
					SecurityRoleEnum.valueOf(clientUserToken.getRole())
							.getName()).contains(currentPageName);
		} else {
			// Only show anonymous pages
			return pageRolesMap.get(SecurityRoleEnum.ANONYMOUS.getName())
					.contains(currentPageName);
		}
	}
}
