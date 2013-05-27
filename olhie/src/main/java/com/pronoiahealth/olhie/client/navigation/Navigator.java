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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.nav.client.local.spi.PageNode;

import com.google.common.collect.Multimap;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.pages.bookcase.BookCasePage;
import com.pronoiahealth.olhie.client.pages.bookreview.BookReviewPage;
import com.pronoiahealth.olhie.client.pages.bulletinboard.BulletinboardPage;
import com.pronoiahealth.olhie.client.pages.search.SearchPage;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.local.ShowCommentsModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowLoginModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowRegisterModalEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;

/**
 * Navigator <br/>
 * Responsibilities:<br/>
 * 1. Handle "Page" navigations<br/>
 * 2. Shows Login, Comments, and Register dialogs<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Singleton
public class Navigator {

	@Inject
	private Navigation nav;

	@Inject
	private ClientUserToken clientUserToken;

	private Map<String, Set<String>> pageRolesMap;

	@Inject
	private TransitionTo<BulletinboardPage> showBulletinboardPage;

	@Inject
	private TransitionTo<BookCasePage> showBookCasePage;

	@Inject
	private TransitionTo<SearchPage> showSearchPage;

	@Inject
	private TransitionTo<BookReviewPage> showBookReviewPage;

	@Inject
	private Event<ShowLoginModalEvent> showLoginEvent;

	@Inject
	private Event<ShowRegisterModalEvent> showRegisterEvent;

	@Inject
	private Event<ShowCommentsModalEvent> showCommentsEvent;

	public Navigator() {
		pageRolesMap = new HashMap<String, Set<String>>();
	}

	public Widget getNavContentPanel() {
		return nav.getContentPanel();
	}

	/**
	 * Creates the pageRolesMap. This will be used in the @PageShown annotated
	 * method to test that the page should be made visible for this user with a
	 * specific role.
	 */
	@PostConstruct
	// TODO: Need to find a better way to do this
	private void initPageRolesMap() {
		// Collect pages
		Set<String> anonPageNames = collectPageNames(nav
				.getPagesByRole(AnonymousRole.class));
		Set<String> registeredPageNames = collectPageNames(nav
				.getPagesByRole(AnonymousRole.class));
		Set<String> authorPageNames = collectPageNames(nav
				.getPagesByRole(AnonymousRole.class));
		Set<String> adminPageNames = collectPageNames(nav
				.getPagesByRole(AnonymousRole.class));
		Set<String> defPages = collectPageNames(nav
				.getPagesByRole(DefaultPage.class));

		// Put in map based on role hierarchy default -> anonymous -> registered
		// -> author -> admin
		pageRolesMap.put(SecurityRoleEnum.ANONYMOUS.getName(),
				addCollections(anonPageNames, defPages));
		pageRolesMap.put(SecurityRoleEnum.REGISTERED.getName(),
				addCollections(registeredPageNames, defPages, anonPageNames));
		pageRolesMap.put(
				SecurityRoleEnum.AUTHOR.getName(),
				addCollections(authorPageNames, defPages, anonPageNames,
						registeredPageNames));
		pageRolesMap.put(
				SecurityRoleEnum.ADMIN.getName(),
				addCollections(adminPageNames, defPages, anonPageNames,
						registeredPageNames, authorPageNames));
	}

	private Set<String> collectPageNames(Collection<PageNode<?>> pageNodes) {
		Set<String> retVal = new TreeSet<String>();
		if (pageNodes != null && pageNodes.size() > 0) {
			for (PageNode<?> node : pageNodes) {
				retVal.add(node.name());
			}
		}
		return retVal;
	}

	private Set<String> addCollections(Set<String> base,
			Set<String>... otherSets) {
		if (otherSets != null) {
			for (Set<String> strSet : otherSets) {
				base.addAll(strSet);
			}
		}
		return base;
	}

	public void performTransition(String navToPage, Multimap state) {
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

		case BookReviewPage: {
			if (state == null) {
				showBookReviewPage.go();
			} else {
				showBookReviewPage.go(state);
			}
			break;
		}
		}
	}

	/**
	 * Shows the page with the role DefaultPage.class
	 */
	public void showDefaultPage() {
		nav.goToWithRole(DefaultPage.class);
	}

	public void showLoginModalEvent() {
		showLoginEvent.fire(new ShowLoginModalEvent());
	}

	public void showCommentsModalEvent() {
		showCommentsEvent.fire(new ShowCommentsModalEvent());
	}

	public void showRegisterModalEvent() {
		showRegisterEvent.fire(new ShowRegisterModalEvent());
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
		} else if (pageRolesMap == null || pageRolesMap.size() == 0) {
			// By pass check when the map is null or has not roles it it
			return true;
		} else {
			// Only show anonymous pages
			return pageRolesMap.get(SecurityRoleEnum.ANONYMOUS.getName())
					.contains(currentPageName);
		}
	}
}
