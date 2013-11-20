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

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookdescriptionDetailRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookdescriptionDetailResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookdescriptionDetailService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 18, 2013
 * 
 */
@RequestScoped
public class BookdescriptionDetailService {
	/**
	 * Protect non thread safe SimpleDateFormat that returns and ISO date format
	 */
	private static final ThreadLocal<SimpleDateFormat> dtFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy");
		}
	};

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userSessionToken;

	@Inject
	private Event<BookdescriptionDetailResponseEvent> bookdescriptionDetailResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public BookdescriptionDetailService() {
	}

	/**
	 * Observes the BookdescriptionDetailRequestEvent that originates from the
	 * NewBookPage. Fires a BookdescriptionDetailResponseEvent which is observed
	 * by the BookDescriptionDetailDialog.
	 * 
	 * @param bookdescriptionDetailRequestEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesBookdescriptionDetailRequestEvent(
			@Observes BookdescriptionDetailRequestEvent bookdescriptionDetailRequestEvent) {

		try {
			// Get incoming parameters
			String baId = bookdescriptionDetailRequestEvent.getBookAssetId();
			String badId = bookdescriptionDetailRequestEvent.getBookDescriptionId();
			
			// Lookup data, can't be null
			Bookasset ba = bookDAO.getBookasset(baId);
			Bookassetdescription bad = bookDAO.getBookassetdescription(badId);
			
			// load response
			BookdescriptionDetailResponseEvent response = new BookdescriptionDetailResponseEvent();
			response.setBookDescription(bad.getDescription());
			response.setCreationDate(dtFormat.get().format(ba.getCreatedDate()));
			response.setCreationHrs("" + ba.getHoursOfWork());
			response.setItemType(ba.getItemType());

			// Fire the resoonse event
			bookdescriptionDetailResponseEvent
					.fire(response);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}
}
