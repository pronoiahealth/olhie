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
import com.pronoiahealth.olhie.client.shared.events.RemoveBookassetdescriptionEvent;
import com.pronoiahealth.olhie.client.shared.events.RemoveBookassetdescriptionResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;

/**
 * RemoveBookassetdescriptionService.java<br/>
 * Responsibilities:<br/>
 * 1. In-activate a book asset description<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 24, 2013
 * 
 */
@RequestScoped
public class RemoveBookassetdescriptionService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private Event<RemoveBookassetdescriptionResponseEvent> removeBookassetdescriptionResponseEvent;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public RemoveBookassetdescriptionService() {
	}

	/**
	 * Observes for the RemoveBookassetdescriptionEvent and in-activates the
	 * description.
	 * 
	 * @param removeBookassetdescriptionEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesRemoveBookassetdescriptionEvent(
			@Observes RemoveBookassetdescriptionEvent removeBookassetdescriptionEvent) {

		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			// Find the book asset description
			String sessionUserId = userToken.getUserId();
			String bookAssetdescriptionId = removeBookassetdescriptionEvent
					.getBookAssetdescriptionId();
			OSQLSynchQuery<Bookassetdescription> badQuery = new OSQLSynchQuery<Bookassetdescription>(
					"select from Bookassetdescription where @rid = :badId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("badId", bookAssetdescriptionId);
			List<Bookassetdescription> badResult = ooDbTx.command(badQuery)
					.execute(uparams);
			Bookassetdescription currentBad = null;
			if (badResult != null && badResult.size() == 1) {
				// Got the user
				currentBad = badResult.get(0);
			} else {
				throw new Exception(
						"Can't find the book asset description to remove.");
			}

			// Set it to removed and save it
			currentBad.setRemoved(true);
			currentBad.setRemovedDate(new Date());
			currentBad.setRemovedBy(sessionUserId);

			// Now save it
			currentBad = ooDbTx.save(currentBad);
			ooDbTx.commit();

			// Return good result
			removeBookassetdescriptionResponseEvent
					.fire(new RemoveBookassetdescriptionResponseEvent(
							bookAssetdescriptionId, currentBad.getBookId()));
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

}
