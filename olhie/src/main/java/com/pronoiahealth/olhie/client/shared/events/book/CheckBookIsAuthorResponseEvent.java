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
 * CheckIsAuthorResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired By: BookCheckAuthorCoAuthorService<br/>
 * Observed By: BookList3D_3<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 1, 2013
 * 
 */
@Portable
@Conversational
public class CheckBookIsAuthorResponseEvent {
	private boolean isAuthorCoAuthor;
	private String bookId;

	/**
	 * Constructor
	 * 
	 */
	public CheckBookIsAuthorResponseEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param isAuthorCoAuthor
	 */
	public CheckBookIsAuthorResponseEvent(boolean isAuthorCoAuthor,
			String bookId) {
		super();
		this.isAuthorCoAuthor = isAuthorCoAuthor;
		this.bookId = bookId;
	}

	public boolean isAuthorCoAuthor() {
		return isAuthorCoAuthor;
	}

	public void setAuthorCoAuthor(boolean isAuthorCoAuthor) {
		this.isAuthorCoAuthor = isAuthorCoAuthor;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
}
