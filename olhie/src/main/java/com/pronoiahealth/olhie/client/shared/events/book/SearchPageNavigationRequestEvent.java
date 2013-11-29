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
package com.pronoiahealth.olhie.client.shared.events.book;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.constants.SearchPageActionEnum;

/**
 * SearchPageNavigationRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * 
 * <p>
 * Fired By: SearchPagerWidget<br/>
 * Observed By: BookSearchService and SearchResultsComponent<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 29, 2013
 * 
 */
@Portable
@Conversational
public class SearchPageNavigationRequestEvent {
	private SearchPageActionEnum requestedAction;

	/**
	 * Constructor
	 * 
	 */
	public SearchPageNavigationRequestEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param requestedAction
	 */
	public SearchPageNavigationRequestEvent(SearchPageActionEnum requestedAction) {
		super();
		this.requestedAction = requestedAction;
	}

	public SearchPageActionEnum getRequestedAction() {
		return requestedAction;
	}

	public void setRequestedAction(SearchPageActionEnum requestedAction) {
		this.requestedAction = requestedAction;
	}

}
