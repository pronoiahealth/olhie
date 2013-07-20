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
 * AddBookCommentEvent.java<br/>
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
 * @since Jul 20, 2013
 * 
 */
@Portable
@Conversational
public class AddBookCommentEvent {
	private String bookId;
	private String bookComment;

	/**
	 * Constructor
	 *
	 */
	public AddBookCommentEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 * @param bookComment
	 */
	public AddBookCommentEvent(String bookId, String bookComment) {
		super();
		this.bookId = bookId;
		this.bookComment = bookComment;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getBookComment() {
		return bookComment;
	}

	public void setBookComment(String bookComment) {
		this.bookComment = bookComment;
	}
}
