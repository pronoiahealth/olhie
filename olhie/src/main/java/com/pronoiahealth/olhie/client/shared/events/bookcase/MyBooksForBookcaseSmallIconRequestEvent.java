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
package com.pronoiahealth.olhie.client.shared.events.bookcase;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.constants.BookcaseEnum;

/**
 * MyBookcaseSmallIconRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired By: <br/>
 * Observed By: <br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 29, 2013
 *
 */
@Portable
@Conversational
public class MyBooksForBookcaseSmallIconRequestEvent {
	private String userId;
	private BookcaseEnum requestedTab;

	/**
	 * Constructor
	 *
	 */
	public MyBooksForBookcaseSmallIconRequestEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param userId
	 */
	public MyBooksForBookcaseSmallIconRequestEvent(String userId, BookcaseEnum requestedTab) {
		super();
		this.userId = userId;
		this.requestedTab = requestedTab;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BookcaseEnum getRequestedTab() {
		return requestedTab;
	}

	public void setRequestedTab(BookcaseEnum requestedTab) {
		this.requestedTab = requestedTab;
	}
}
