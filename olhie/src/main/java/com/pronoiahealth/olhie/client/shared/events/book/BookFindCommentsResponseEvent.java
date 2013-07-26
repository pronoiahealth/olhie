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

/**
 * BookFindCommentsResponseEvent.java<br/>
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
 * @since Jul 25, 2013
 * 
 */
@Portable
@Conversational
public class BookFindCommentsResponseEvent {
	private String bookId;
	private List<String> comments;

	/**
	 * Constructor
	 * 
	 */
	public BookFindCommentsResponseEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param bookId
	 * @param comments
	 */
	public BookFindCommentsResponseEvent(String bookId, List<String> comments) {
		super();
		this.bookId = bookId;
		this.comments = comments;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}
}
