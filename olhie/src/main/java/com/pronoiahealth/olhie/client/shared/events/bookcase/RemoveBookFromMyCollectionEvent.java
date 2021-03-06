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

/**
 * RemoveBookFromMyCollectionEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired From: NewBookPage<br/>
 * Observed By: RemoveBookFromMyCollectionService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 30, 2013
 *
 */
@Portable
@Conversational
public class RemoveBookFromMyCollectionEvent {
	public enum REMOVE_RESPONSE_TYPE {FIND_REMOVE_RESPONSE, REMOVE_RESPONSE};
	private String bookId;
	private REMOVE_RESPONSE_TYPE responseType;

	/**
	 * Constructor
	 *
	 */
	public RemoveBookFromMyCollectionEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 */
	public RemoveBookFromMyCollectionEvent(String bookId, REMOVE_RESPONSE_TYPE responseType) {
		super();
		this.bookId = bookId;
		this.responseType = responseType;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public REMOVE_RESPONSE_TYPE getResponseType() {
		return responseType;
	}

	public void setResponseType(REMOVE_RESPONSE_TYPE responseType) {
		this.responseType = responseType;
	}
}
