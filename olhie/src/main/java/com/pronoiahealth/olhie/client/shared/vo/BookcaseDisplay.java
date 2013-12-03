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
package com.pronoiahealth.olhie.client.shared.vo;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * BookcaseDisplay.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Dec 2, 2013
 *
 */
@Portable
public class BookcaseDisplay {
	private String bookId;
	private String userBookRelationshipId;

	/**
	 * Constructor
	 * 
	 */
	public BookcaseDisplay() {
	}

	/**
	 * Constructor
	 * 
	 * @param smallBookImageUrl
	 * @param bookId
	 */
	public BookcaseDisplay(String bookId, String userBookRelationshipId) {
		super();
		this.bookId = bookId;
		this.userBookRelationshipId = userBookRelationshipId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getUserBookRelationshipId() {
		return userBookRelationshipId;
	}

	public void setUserBookRelationshipId(String userBookRelationshipId) {
		this.userBookRelationshipId = userBookRelationshipId;
	}
}
