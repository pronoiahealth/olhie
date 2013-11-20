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

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.UpdateBookassetdescriptionOrderEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookassetdescriptionOrderService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 19, 2013
 * 
 */
@RequestScoped
public class BookassetdescriptionOrderService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public BookassetdescriptionOrderService() {
	}

	/**
	 * Observes for the position change event. If an error occurs a
	 * ServiceErrorEvent is fired. Also checks to make sure the current user is
	 * the author or co-author of the book.
	 * 
	 * @param updateBookassetdescriptionOrderEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observersUpdateBookassetdescriptionOrderEvent(
			@Observes UpdateBookassetdescriptionOrderEvent updateBookassetdescriptionOrderEvent) {
		try {
			// The user must be the author or co-author of the book being
			// updated
			String userId = userToken.getUserId();
			Map<String, Integer> posMap = updateBookassetdescriptionOrderEvent
					.getPosMap();
			if (posMap != null && posMap.size() > 0) {
				// Check entries for authorship
				for (Entry<String, Integer> entry : posMap.entrySet()) {
					String testBadId = entry.getKey();
					Bookassetdescription testBad = bookDAO
							.getBookassetdescription(testBadId);
					String bookId = testBad.getBookId();
					if (bookDAO.isUserAuthorOrCoauthorOfBook(userId, bookId) == false) {
						throw new Exception(
								"You are not the author or co-author of book with id "
										+ bookId + ".");
					}
				}

				// Update checked entries
				bookDAO.updateBookassetdescriptionPosition(posMap);
			}
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}
}
