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

import com.pronoiahealth.olhie.client.shared.events.BookCoverListRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCoverListResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;

/**
 * BookCoverService.java<br/>
 * Responsibilities:<br/>
 * 1. Returns book covers
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 7, 2013
 *
 */
@RequestScoped
public class BookCoverService {

	@Inject
	private Logger log;

	@Inject
	private Event<BookCoverListResponseEvent> bookCoverListResponseEvent;

	/**
	 * Constructor
	 * 
	 */
	public BookCoverService() {
	}

	/**
	 * Observes for a book cover request
	 * 
	 * @param bookCoverListRequestEvent
	 */
	// TODO: Store in database
	protected void observesBookCoverListRequestEvent(
			@Observes BookCoverListRequestEvent bookCoverListRequestEvent) {
		List<BookCover> bookCovers = new ArrayList<BookCover>();
		bookCovers.add(new BookCover("Olhie/images/p1.png", "Paper"));
		bookCovers.add(new BookCover("Olhie/images/paper.png", "Paper 1"));
		bookCovers.add(new BookCover("Olhie/images/paper1.png", "Paper 2"));

		bookCoverListResponseEvent.fire(new BookCoverListResponseEvent(
				bookCovers));
	}
}
