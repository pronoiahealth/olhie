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
 * CheckIsAuthorRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired By: BookList3D_3<br/>
 * Observed By: BookCheckAuthorCoAuthorService<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Dec 1, 2013
 *
 */
@Portable
@Conversational
public class CheckBookIsAuthorRequestEvent {
	private String bookId;
	
	/**
	 * Constructor
	 *
	 */
	public CheckBookIsAuthorRequestEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 */
	public CheckBookIsAuthorRequestEvent(String bookId) {
		super();
		this.bookId = bookId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

}
