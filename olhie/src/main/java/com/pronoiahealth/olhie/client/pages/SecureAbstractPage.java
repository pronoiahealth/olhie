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
import com.google.gwt.user.client.Command;
import com.pronoiahealth.olhie.client.shared.events.local.NavigationErrorEvent;

/**
 * SecureAbstractPage.java<br/>
 * Responsibilities:<br/>
 * 1. A page which extends this page will check the security on the page to
 * allow it to be viewed, or not. See the allowPageShown method of the Navigator
 * class and the getPageRoleMap method of the ClientFactory class. The Page
 * roles are hierarchical (Anonymous -> Registered -> Author -> Admin). Pages
 * should be secured with the minimum role required to access them as in
 * 
 * @Page(role = {RegisteredRole.class}).<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 31, 2013
 * 
 */
public abstract class SecureAbstractPage extends AbstractPage {

	@Inject
	protected Event<NavigationErrorEvent> navigationErrorEvent;

	public SecureAbstractPage() {
	}

	/**
	 * Performs, with the help of the Navigation object, a security check that
	 * matches the @Page role with the users security. Returning true indicates
	 * its OK to proceed.
	 * 
	 * @return
	 */
	protected boolean defaultSecurityCheck() {
		if (nav.allowPageFromShownMethod() == false) {
			nav.showDefaultPage();

			Scheduler.get().scheduleDeferred(new Command() {
				public void execute() {
					navigationErrorEvent
							.fire(new NavigationErrorEvent(
									"You tried to access a page your don't have access to."));
				}
			});

			return false;
		} else {
			return true;
		}
	}

	/**
	 * When the @PageShown annotated method of the base class is called do a
	 * security check on the users rights to see the page.
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.AbstractPage#whenPageShownCalled()
	 */
	@Override
	protected boolean whenPageShownCalled() {
		return defaultSecurityCheck();
	}
}
