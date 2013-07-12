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

/**
 * BookFindByIdEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Used to ask the server for a book by its id<br/>
 * 
 * <p>
 * Fired By: NewBookPage class<br/>
 * Observed By: BookFindService class<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 8, 2013
 *
 */
@Portable
@Conversational
public class BookFindByIdEvent {
	private String bookId;

	/**
	 * Constructor
	 *
	 */
	public BookFindByIdEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 */
	public BookFindByIdEvent(String bookId) {
		super();
		this.bookId = bookId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
}
