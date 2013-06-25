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
package com.pronoiahealth.olhie.client.shared.events;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.vo.BookForDisplay;

/**
 * BookSearchResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Returned with a book<br/>
 * 
 * <p>
 * Fired By: BookSearchService <br/>
 * Observed By: NewBookPage class <br/>
 * </p>
 * 
 * @author Alex Roman
 * @version 1.0
 * @since Jun 25, 2013
 * 
 */
@Portable
@Conversational
public class BookSearchResponseEvent {
	
	private List<BookForDisplay> bookForDisplayList;

	/**
	 * Constructor
	 * 
	 */
	public BookSearchResponseEvent() {
	}

	/**
	 * @param bookList
	 */
	public BookSearchResponseEvent(List<BookForDisplay> bookForDisplayList) {
		super();
		this.bookForDisplayList = bookForDisplayList;
	}

	public List<BookForDisplay> getBookForDisplayList() {
		return bookForDisplayList;
	}

	public void setBookForDisplayList(List<BookForDisplay> bookForDisplayList) {
		this.bookForDisplayList = bookForDisplayList;
	}

	
}
