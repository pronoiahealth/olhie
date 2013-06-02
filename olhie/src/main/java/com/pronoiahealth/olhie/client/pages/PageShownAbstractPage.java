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

/**
 * PageShownAbstractPage.java<br/>
 * Responsibilities:<br/>
 * 1. Used for @Page annotated pages that don't require security checks.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 31, 2013
 * 
 */
public abstract class PageShownAbstractPage extends AbstractPage {

	@Inject
	protected Event<PageVisibleEvent> pageVisibleEvent;

	/**
	 * Constructor
	 * 
	 */
	public PageShownAbstractPage() {
	}

	/**
	 * When the @PageShown annotated method of the base class is called fire the
	 * PageVisibleEvent.
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.AbstractPage#whenPageShownCalled()
	 */
	@Override
	protected boolean whenPageShownCalled() {
		pageVisibleEvent();
		return true;
	}

}
