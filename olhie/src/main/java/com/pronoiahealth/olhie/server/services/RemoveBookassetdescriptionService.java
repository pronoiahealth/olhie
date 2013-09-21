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
import com.pronoiahealth.olhie.client.shared.events.book.RemoveBookassetdescriptionEvent;
import com.pronoiahealth.olhie.client.shared.events.book.RemoveBookassetdescriptionResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

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
	@DAO
	private BookDAO bookDAO;

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
			// Find the book asset description
			String sessionUserId = userToken.getUserId();
			String bookAssetdescriptionId = removeBookassetdescriptionEvent
					.getBookAssetdescriptionId();

			// inactivate the bad
			Bookassetdescription bad = bookDAO
					.inactivateBookAssetDescriptionFromBook(sessionUserId,
							bookAssetdescriptionId);

			// Return good result
			removeBookassetdescriptionResponseEvent
					.fire(new RemoveBookassetdescriptionResponseEvent(
							bookAssetdescriptionId, bad.getBookId()));
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

}
