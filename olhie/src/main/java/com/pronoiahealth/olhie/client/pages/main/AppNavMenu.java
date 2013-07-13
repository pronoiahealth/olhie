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
package com.pronoiahealth.olhie.client.pages.main;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.common.collect.Multimap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SyncMenuWithViewEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SyncPageToMenuEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.widgets.sidebarnav.SideBarNavWidget;
import com.pronoiahealth.olhie.client.widgets.sidebarnav.UnorderedListWidget;

/**
 * Application navigation menu that appears on the west side of the screen.<br/>
 * Responsibilities:<br/>
 * 1. Shows side bar naviagtion menu<br/>
 * 2. Responds to SyncMenuWithViewEvent<br>
 * 3. Responds to MenuPageVisibleEvent<br>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since 5/8/2013
 * 
 */
public class AppNavMenu extends UnorderedListWidget {
	@Inject
	private ClientUserToken clientUserToken;

	@Inject
	private PageNavigator navigator;

	@Inject
	private Event<SyncMenuWithViewEvent> syncMenuWithViewEvent;

	private ArrayList<SideBarNavWidget> sWidgetLst;

	/**
	 * A hack to get around the navigation components initializing "pages"
	 * before the menu gets initialized. This only happens on start-up.
	 */
	public static String currentPage;

	public AppNavMenu() {
	}

	/**
	 * Create default nav menu
	 */
	@PostConstruct
	public void postConstruct() {
		sWidgetLst = new ArrayList<SideBarNavWidget>();

		// Create desktop screen and search
		clear();
		
		// Bulletin board
		SideBarNavWidget navWidget = new SideBarNavWidget(
				NavEnum.BulletinboardPage.toString(), "", "", "icon-calendar",
				"Bulletin Board");
		addSideBarNavWidget(navWidget);
		
		// Search screen
		navWidget = new SideBarNavWidget(NavEnum.SearchPage.toString(), "", "",
				"icon-search", "Find a Book");
		addSideBarNavWidget(navWidget);

		// See the SideBarNavWidget doc and currentPage
		syncMenuWithPage(currentPage);
	}

	public void observesCleintUserTokenUpdatedEvent(
			@Observes ClientUserUpdatedEvent clientUserUpdatedEvent) {

		if (clientUserToken.isLoggedIn() == true) {
			// Client has logged in
			String role = clientUserToken.getRole();
			SecurityRoleEnum secRole = SecurityRoleEnum.valueOf(role);
			if (secRole != SecurityRoleEnum.ANONYMOUS) {

				switch (secRole) {
				case ADMIN:

				case AUTHOR:

				case REGISTERED:
					SideBarNavWidget navWidget = new SideBarNavWidget(
							NavEnum.BookCasePage.toString(), "", "",
							"icon-book", "Book Case", SecurityRoleEnum.REGISTERED);
					addSideBarNavWidget(navWidget);
					break;
				}
			}
		} else {
			// client has Logged out
			removeAllButAnonymousItems();
		}
	}

	@Override
	public void clear() {
		super.clear();
		sWidgetLst.clear();
	}

	/**
	 * Add widget to menu and internal arraylist
	 * 
	 * @param sWidget
	 */
	public void addSideBarNavWidget(SideBarNavWidget sWidget) {
		super.add(sWidget);
		HandlerRegistration handler = sWidget
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						SideBarNavWidget sWidget = (SideBarNavWidget) event
								.getSource();
						syncMenuWithViewEvent.fire(new SyncMenuWithViewEvent(
								sWidget, null));
					}
				});
		sWidget.setClickHandlerReg(handler);
		sWidgetLst.add(sWidget);
	}
	
	/**
	 * Removes nav widgets that are not of securityRole type anonymous
	 */
	private void removeAllButAnonymousItems() {
		for (SideBarNavWidget w : sWidgetLst) {
			if (w.getSecurityRole() != SecurityRoleEnum.ANONYMOUS) {
				HandlerRegistration reg = w.getClickHandlerReg();
				reg.removeHandler();
				this.removeSideBarNavWidget(w);
			}
		}
	}

	/**
	 * Removes the menu item from the list and clears its click handler
	 * 
	 * @param sWidget
	 */
	public void removeSideBarNavWidget(SideBarNavWidget sWidget) {
		sWidget.getClickHandlerReg().removeHandler();
		super.remove(sWidget);
		sWidget = null;
	}

	/**
	 * Performs a transition to the page that corresponds to the navigation
	 * widget selection. It is up to the page to update the menu selection so it
	 * displays in the "current". See the observesPageVisibleEvent method in
	 * this class.
	 * 
	 * @param event
	 */
	public void observesSideBarNavWidgetClick(
			@Observes SyncMenuWithViewEvent event) {
		SideBarNavWidget sWidget = event.getWidget();
		Multimap<String, Object> map = event.getState();
		navigator.performTransition(sWidget.getNavToPageName(),
				map == null ? null : map);
	}

	/**
	 * Watches for page visible events from "pages"
	 * 
	 * @param event
	 */
	public void observesSyncPageToMenuEvent(
			@Observes SyncPageToMenuEvent event) {
		currentPage = event.getPageName();

		SideBarNavWidget cWidget = this
				.getCorespondingSideBarNavWidget(currentPage);
		if (cWidget != null) {
			clearCurrentSelection();
			cWidget.setCurrent(true);
		}
	}

	/**
	 * Find the page name in the list of widgets Assumes the page name are for a
	 * single sidebarnavwidget
	 * 
	 * @param pageName
	 * @return
	 */
	protected SideBarNavWidget getCorespondingSideBarNavWidget(String pageName) {
		if (sWidgetLst != null && sWidgetLst.size() > 0) {
			for (SideBarNavWidget widget : sWidgetLst) {
				if (widget.getNavToPageName().equals(currentPage)) {
					return widget;
				}
			}
		}

		// List is empty or widget not in list
		return null;
	}

	/**
	 * Puts the widget in the selected or "current" display mode and clears the
	 * previously highlighted menu selection.
	 * 
	 * @param widget
	 */
	protected void syncMenuWithPage(SideBarNavWidget widget) {
		if (widget != null) {
			clearCurrentSelection();
			widget.setCurrent(true);
		}
	}

	/**
	 * Puts the widget in the selected or "current" display mode and clears the
	 * previously highlighted menu selection.
	 * 
	 * @param widget
	 */
	protected void syncMenuWithPage(String pageName) {
		if (pageName != null && pageName.length() > 0) {
			syncMenuWithPage(getCorespondingSideBarNavWidget(pageName));
		}
	}

	/**
	 * Puts all attached widgets in the non-current mode. They will not appear
	 * selected.
	 */
	protected void clearCurrentSelection() {
		for (SideBarNavWidget sWidget : sWidgetLst) {
			sWidget.setCurrent(false);
		}
	}

}
