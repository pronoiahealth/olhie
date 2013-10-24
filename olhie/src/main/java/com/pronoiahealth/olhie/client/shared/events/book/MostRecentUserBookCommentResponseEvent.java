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

import com.pronoiahealth.olhie.client.shared.vo.Bookcomment;

/**
 * MostRecentUserBookCommentResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired By: BookCommentRatingService class<br/>
 * Observed By: AddBookCommentDialog <br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Oct 23, 2013
 *
 */
@Portable
@Conversational
public class MostRecentUserBookCommentResponseEvent {
	private String bookId;
	private Bookcomment comment;

	/**
	 * Constructor
	 *
	 */
	public MostRecentUserBookCommentResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 * @param comment
	 */
	public MostRecentUserBookCommentResponseEvent(String bookId,
			Bookcomment comment) {
		super();
		this.bookId = bookId;
		this.comment = comment;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public Bookcomment getComment() {
		return comment;
	}

	public void setComment(Bookcomment comment) {
		this.comment = comment;
	}

}
