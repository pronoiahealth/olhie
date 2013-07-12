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

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookCoverListRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookCoverListResponseEvent;
import com.pronoiahealth.olhie.server.security.SecureAccess;

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

	@Inject
	private TempCoverBinderHolder holder;

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
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesBookCoverListRequestEvent(
			@Observes BookCoverListRequestEvent bookCoverListRequestEvent) {
		bookCoverListResponseEvent.fire(new BookCoverListResponseEvent(holder
				.getCovers()));
	}
}
