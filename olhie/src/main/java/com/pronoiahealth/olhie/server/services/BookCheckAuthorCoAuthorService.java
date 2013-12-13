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
import com.pronoiahealth.olhie.client.shared.events.book.CheckBookIsAuthorRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.book.CheckBookIsAuthorResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookCheckAuthorCoAuthor.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 1, 2013
 * 
 */
@RequestScoped
public class BookCheckAuthorCoAuthorService {
	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private Event<CheckBookIsAuthorResponseEvent> checkBookIsAuthorResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@DAO
	private BookDAO bookDAO;

	/**
	 * Constructor
	 * 
	 */
	public BookCheckAuthorCoAuthorService() {
	}

	/**
	 * This method catches the CheckBookIsAuthorRequestEvent which is fired by
	 * the BookList3D when the user clicks the book. The method will fire the
	 * CheckBookIsAuthorResponseEvent id the Book for the provided bookId was
	 * authored by or co-authored by the user as determined by the
	 * userSessionToken.
	 * 
	 * @param checkBookIsAuthorRequestEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR, SecurityRoleEnum.REGISTERED, 
			SecurityRoleEnum.ANONYMOUS })
	protected void observesCheckIsAuthorRequestEvent(
			@Observes CheckBookIsAuthorRequestEvent checkBookIsAuthorRequestEvent) {

		try {
			String bookId = checkBookIsAuthorRequestEvent.getBookId();
			String userId = userToken.getUserId();

			boolean isUserAuthorOrCoAuthor = false;
			if (userId != null && userToken.getLoggedIn() == true) {
				isUserAuthorOrCoAuthor = bookDAO
						.isUserAuthorOrCoauthorOfBook(userId, bookId);
			}
			
			checkBookIsAuthorResponseEvent
			.fire(new CheckBookIsAuthorResponseEvent(
					isUserAuthorOrCoAuthor, bookId));
			
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

}
