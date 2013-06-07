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
package com.pronoiahealth.olhie.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.events.BookCategoryListRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCategoryListResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;

/**
 * BookCatagoryService.java<br/>
 * Responsibilities:<br/>
 * 1. Responds to a BookCatagoryListRequestEvent<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 6, 2013
 * 
 */
@RequestScoped
public class BookCatagoryService {

	@Inject
	private Logger log;

	@Inject
	private Event<BookCategoryListResponseEvent> bookCategoryListResponseEvent;

	/**
	 * Constructor
	 * 
	 */
	public BookCatagoryService() {
	}

	/**
	 * Watches for a book category request and responds with a book category
	 * response
	 * 
	 * @param bookCategoryListRequestEvent
	 */
	// TODO: Store in database
	protected void observesBookCategoryListRequestEvent(
			@Observes BookCategoryListRequestEvent bookCategoryListRequestEvent) {
		List<BookCategory> bookCategories = new ArrayList<BookCategory>();
		bookCategories.add(new BookCategory("black", "Interface"));
		bookCategories.add(new BookCategory("yellow", "Legal"));

		bookCategoryListResponseEvent.fire(new BookCategoryListResponseEvent(bookCategories));
	}
}
