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

import com.pronoiahealth.olhie.client.shared.events.local.MenuPageVisibleEvent;
import com.pronoiahealth.olhie.client.utils.Utils;

/**
 * MenuSyncSecureAbstractPage.java<br/>
 * Responsibilities:<br/>
 * 1. Subclasses will fire the MenuPageVisibleEvent and test the users role
 * against the page security when the @PageShown annotated method in th
 * eAbstarctPage class is called.</br>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 31, 2013
 * 
 */
public abstract class MenuSyncSecureAbstractPage extends SecureAbstractPage {

	@Inject
	protected Event<MenuPageVisibleEvent> menuPageVisibleEvent;

	/**
	 * Constructor
	 *
	 */
	public MenuSyncSecureAbstractPage() {
	}

	/**
	 * Calls the parent classes implementation of this method which checks the
	 * page security. Then calls menuPageVisibleEvent() to sync the page with
	 * the side bar nav menu.
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.SecureAbstractPage#whenPageShownCalled()
	 */
	@Override
	protected boolean whenPageShownCalled() {
		if (super.whenPageShownCalled() == true) {
			menuPageVisibleEvent();
			return true;
		} else {
			return false;
		}
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
}
