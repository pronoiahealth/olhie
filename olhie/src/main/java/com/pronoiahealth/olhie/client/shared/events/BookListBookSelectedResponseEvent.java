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
package com.pronoiahealth.olhie.client.shared.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * BookListBookSelectedResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Response to the BookListBookSelectedEvent<br/>
 * 
 * <p>
 * Fired By: BookSelectedService<br/>
 * Observed By: BookcasePage<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 26, 2013
 *
 */
@Portable
@Conversational
public class BookListBookSelectedResponseEvent {
	private String bookId;
	private boolean authorSelected;

	public BookListBookSelectedResponseEvent() {
	}

	public BookListBookSelectedResponseEvent(String bookId,
			boolean authorSelected) {
		super();
		this.bookId = bookId;
		this.authorSelected = authorSelected;
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

}
