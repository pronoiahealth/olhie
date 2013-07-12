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

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.vo.BookCover;

/**
 * BookCoverListResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired form: BookCoverService class<br/>
 * Observed by: NewDialog class<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 6, 2013
 *
 */
@Portable
@Conversational
public class BookCoverListResponseEvent {
	private List<BookCover> bookCover;

	/**
	 * Constructor
	 *
	 */
	public BookCoverListResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookCover
	 */
	public BookCoverListResponseEvent(List<BookCover> bookCover) {
		super();
		this.bookCover = bookCover;
	}

	public List<BookCover> getBookCover() {
		return bookCover;
	}

	public void setBookCover(List<BookCover> bookCover) {
		this.bookCover = bookCover;
	}
}
