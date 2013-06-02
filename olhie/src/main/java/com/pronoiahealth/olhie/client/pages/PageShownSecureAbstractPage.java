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

import com.pronoiahealth.olhie.client.shared.events.local.PageVisibleEvent;

public abstract class PageShownSecureAbstractPage extends SecureAbstractPage {

	@Inject
	protected Event<PageVisibleEvent> pageVisibleEvent;

	/**
	 * Constructor
	 *
	 */
	public PageShownSecureAbstractPage() {
	}

	/**
	 * First calls the super class implementation of this method to run a
	 * security check on the page. If true is returned then the PageVisibleEvent
	 * is fired.</br>
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.SecureAbstractPage#whenPageShownCalled()
	 */
	@Override
	protected boolean whenPageShownCalled() {
		if (super.whenPageShownCalled() == true) {
			pageVisibleEvent();
			return true;
		} else {
			return false;
		}
	}

}
