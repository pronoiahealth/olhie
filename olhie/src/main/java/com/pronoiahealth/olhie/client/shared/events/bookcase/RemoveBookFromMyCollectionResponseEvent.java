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

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * RemoveBookFromMyCollectionResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 *<p>
 * Fired From: RemoveBookFromMyCollectionService<br/>
 * Observed By: Book3d_3<br/>
 * </p>
 * @author John DeStefano
 * @version 1.0
 * @since Oct 17, 2013
 *
 */
@Portable
@Conversational
public class RemoveBookFromMyCollectionResponseEvent {
	private String bookId;

	/**
	 * Constructor
	 *
	 */
	public RemoveBookFromMyCollectionResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 */
	public RemoveBookFromMyCollectionResponseEvent(String bookId) {
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
