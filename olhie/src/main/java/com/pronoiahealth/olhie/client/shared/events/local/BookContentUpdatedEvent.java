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
 * BookContentUpdatedEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Signals that the content of a book has been updated. This would happen
 * during the creation or updating of a book.<br/>
 * 
 * <p>
 * Fired By: AddFileDialog class<br/>
 * Observed By: NewBookPage class <br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 13, 2013
 * 
 */
@Local
public class BookContentUpdatedEvent {
	private String bookId;

	/**
	 * Constructor
	 * 
	 */
	public BookContentUpdatedEvent() {
	}

	public BookContentUpdatedEvent(String bookId) {
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
