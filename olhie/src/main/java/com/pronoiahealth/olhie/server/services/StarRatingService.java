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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.NewStarRatingEvent;
import com.pronoiahealth.olhie.client.shared.events.NewStarRatingResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Bookstarrating;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;

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
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public StarRatingService() {
	}

	/**
	 * Observes for the NewStarRatingEvent and in-activates the
	 * description.
	 * 
	 * @param newStarRatingEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesNewStarRatingEvent(
			@Observes NewStarRatingEvent newStarRatingEvent) {

		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			// Find the book stars
			String sessionUserId = userToken.getUserId();
			int star = newStarRatingEvent.getStars();
			String bookId = newStarRatingEvent.getBookId();
			OSQLSynchQuery<Bookstarrating> query = new OSQLSynchQuery<Bookstarrating>(
					"select from Bookstarrating where bookId = :bookId and userId = :userId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("bookId", bookId);
			uparams.put("userId", sessionUserId);
			List<Bookstarrating> resultList = ooDbTx.command(query)
					.execute(uparams);
			Bookstarrating current = null;
			if (resultList != null && resultList.size() == 1) {
				// Got the book star rating
				current = resultList.get(0);
				current.setUpdatedDate(new Date());
				current.setStars(star);
			} else {
				current = new Bookstarrating();
				current.setBookId(bookId);
				current.setUserId(sessionUserId);
				current.setStars(star);
			}

			// Now save it
			current.setUpdatedDate(new Date());
			current = ooDbTx.save(current);
			ooDbTx.commit();

			// Return good result
			newStarRatingEventResponseEvent
					.fire(new NewStarRatingResponseEvent(
							current.getStars(), current.getUserId(), current.getBookId()));
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

}
