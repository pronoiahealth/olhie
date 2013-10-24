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

import com.pronoiahealth.olhie.client.shared.vo.Book;

/**
 * BookUpdateEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Updates a book.<br/>
 * 
 * <p>
 * Fired By: NewBookDialog class<br/>
 * Observed By: BookUpdateService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 7, 2013
 * 
 */
@Portable
@Conversational
public class BookUpdateEvent {
	
	private Book book;

	/**
	 * Constructor
	 * 
	 */
	public BookUpdateEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param book
	 */
	public BookUpdateEvent(Book book) {
		super();
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	

}
