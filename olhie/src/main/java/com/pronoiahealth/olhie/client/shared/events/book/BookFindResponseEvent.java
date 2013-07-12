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

import java.util.Set;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;

/**
 * BookFindResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Returned with a book<br/>
 * 
 * <p>
 * Fired By: BookFindService <br/>
 * Observed By: NewBookPage class <br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 8, 2013
 * 
 */
@Portable
@Conversational
public class BookFindResponseEvent {

	private BookDisplay bookDisplay;
	private Set<UserBookRelationshipEnum> rels;

	/**
	 * Constructor
	 * 
	 */
	public BookFindResponseEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param bookDisplay
	 * @param bookCategory
	 * @param bookCover
	 * @param authorFullName
	 * @param bookAssetDescriptions
	 */
	public BookFindResponseEvent(BookDisplay bookDisplay,
			Set<UserBookRelationshipEnum> rels) {
		super();
		this.bookDisplay = bookDisplay;
		this.rels = rels;
	}

	/**
	 * @return
	 */
	public BookDisplay getBookDisplay() {
		return bookDisplay;
	}

	/**
	 * @param bookDisplay
	 */
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
