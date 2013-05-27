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
package com.pronoiahealth.olhie.client.pages;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Command;
import com.pronoiahealth.olhie.client.navigation.Navigator;
import com.pronoiahealth.olhie.client.shared.events.local.MenuPageVisibleEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NavigationErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.PageVisibleEvent;
import com.pronoiahealth.olhie.client.utils.Utils;

/**
 * AbstractPage - Extends AbstractComposite<br/>
 * Responsibilities:<br/>
 * 1. Set page backgrounds<br/>
 * 2. Coordinate with AppNavMenu to sync menu with view<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public abstract class AbstractPage extends AbstractComposite {

	@Inject
	protected Navigator nav;

	@Inject
	protected Event<MenuPageVisibleEvent> menuPageVisibleEvent;

	@Inject
	protected Event<PageVisibleEvent> pageVisibleEvent;

	@Inject
	protected Event<NavigationErrorEvent> navigationErrorEvent;

	public AbstractPage() {
	}

	/**
	 * Fire a MenuPageVisibleEvent if required when the @PageShown method is
	 * called. This event will be picked up by the AppNavMenu and the menu item
	 * selection will be updated.
	 */
	protected void menuPageVisibleEvent() {
		menuPageVisibleEvent.fire(new MenuPageVisibleEvent(Utils
				.parseClassSimpleName(this.getClass())));
	}

	/**
	 * Fire a PageVisibleEvent if required when the @PageShown method is called.
	 */
	protected void pageVisibleEvent() {
		pageVisibleEvent.fire(new PageVisibleEvent(Utils
				.parseClassSimpleName(this.getClass())));
	}

	/**
	 * Allows for page background to be set
	 * 
	 * @param backgroundClass
	 */
	protected void setPageBackgroundClass(String backgroundClass) {
		GQuery gObj = AppSelectors.INSTANCE.getCenterBackground();
		if (backgroundClass == null || backgroundClass.length() == 0) {
			gObj.attr("class", "center-background");
		} else {
			gObj.attr("class", "center-background " + backgroundClass);
		}
	}

	protected void defaultSecurityCheck() {
		if (nav.allowPageFromShownMethod() == false) {
			nav.showDefaultPage();

			Scheduler.get().scheduleDeferred(new Command() {
				public void execute() {
					navigationErrorEvent
							.fire(new NavigationErrorEvent(
									"You tries to access a page your don't have access to."));
				}
			});
		}
	}
}
