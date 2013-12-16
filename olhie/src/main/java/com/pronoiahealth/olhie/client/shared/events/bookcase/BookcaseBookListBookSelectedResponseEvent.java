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

import java.util.Set;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;

/**
 * BookcaseBookListBookSelectedResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Dec 15, 2013
 *
 */
@Portable
@Conversational
public class BookcaseBookListBookSelectedResponseEvent {

	private String bookId;
	private boolean authorSelected;
	private BookDisplay bookDisplay;
	private Set<UserBookRelationshipEnum> rels;

	/**
	 * Constructor
	 *
	 */
	public BookcaseBookListBookSelectedResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 * @param authorSelected
	 * @param bookDisplay
	 * @param rels
	 */
	public BookcaseBookListBookSelectedResponseEvent(String bookId,
			boolean authorSelected, BookDisplay bookDisplay,
			Set<UserBookRelationshipEnum> rels) {
		super();
		this.bookId = bookId;
		this.authorSelected = authorSelected;
		this.bookDisplay = bookDisplay;
		this.rels = rels;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public boolean isAuthorSelected() {
		return authorSelected;
	}

	public void setAuthorSelected(boolean authorSelected) {
		this.authorSelected = authorSelected;
	}

	public BookDisplay getBookDisplay() {
		return bookDisplay;
	}

	public void setBookDisplay(BookDisplay bookDisplay) {
		this.bookDisplay = bookDisplay;
	}

	public Set<UserBookRelationshipEnum> getRels() {
		return rels;
	}

	public void setRels(Set<UserBookRelationshipEnum> rels) {
		this.rels = rels;
	}

}
