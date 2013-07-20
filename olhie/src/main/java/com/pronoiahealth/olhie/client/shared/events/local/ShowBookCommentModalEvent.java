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
package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;

/**
 * ShowBookCommentModalEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Show the BookComment Modal Dialog<br/>
 *
 * <p>
 * Fired By: <br/>
 * Observed By: <br/>
 * </p>
 * @author John DeStefano
 * @version 1.0
 * @since Jul 20, 2013
 *
 */
@Local
public class ShowBookCommentModalEvent {
	private String bookId;

	/**
	 * Constructor
	 *
	 */
	public ShowBookCommentModalEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 */
	public ShowBookCommentModalEvent(String bookId) {
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
