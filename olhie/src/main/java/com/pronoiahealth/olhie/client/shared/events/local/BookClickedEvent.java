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
import org.jboss.errai.common.client.api.annotations.NonPortable;

/**
 * Fired when a book is clicked. Carries the given id.
 */
/**
 * BookClickedEvent.java<br/>
 * Responsibilities:<br/>
 * 1. A local event fired when a Book is clicked (search and bookcase screens)<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Local
@NonPortable
public class BookClickedEvent {
	String bookId;

	/**
	 * Constructor
	 *
	 */
	public BookClickedEvent() {
	}

	public BookClickedEvent(String bookId) {
		this.bookId = bookId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
}
