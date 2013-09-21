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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.NewStarRatingEvent;
import com.pronoiahealth.olhie.client.shared.events.book.NewStarRatingResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Bookstarrating;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * RemoveBookassetdescriptionService.java<br/>
 * Responsibilities:<br/>
 * 1. In-activate a book asset description<br/>
 * 
 * @author Alex Roman
 * @version 1.0
 * @since Jul 07, 2013
 * 
 */
@RequestScoped
public class StarRatingService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private Event<NewStarRatingResponseEvent> newStarRatingEventResponseEvent;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public StarRatingService() {
	}

	/**
	 * Observes for the NewStarRatingEvent and in-activates the description.
	 * 
	 * @param newStarRatingEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesNewStarRatingEvent(
			@Observes NewStarRatingEvent newStarRatingEvent) {

		try {
			String userId = userToken.getUserId();
			int rating = newStarRatingEvent.getStars();
			String bookId = newStarRatingEvent.getBookId();

			// add/update the rating
			Bookstarrating current = bookDAO.addUpdateBookRating(userId,
					bookId, rating);

			// Return good result
			newStarRatingEventResponseEvent
					.fire(new NewStarRatingResponseEvent(current.getStars(),
							current.getUserId(), current.getBookId()));
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

}
