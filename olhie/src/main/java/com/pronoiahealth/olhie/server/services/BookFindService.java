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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.annotations.NewBook;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.BookFindByIdEvent;
import com.pronoiahealth.olhie.client.shared.events.BookFindResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;

/**
 * BookFindService.java<br/>
 * Responsibilities:<br/>
 * 1. Use to return a book based on the books id<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 8, 2013
 * 
 */
@RequestScoped
public class BookFindService {
	@Inject
	private Logger log;

	@Inject
	private Event<BookFindResponseEvent> bookFindResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private TempCoverBinderHolder holder;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public BookFindService() {
	}

	/**
	 * Finds a book and then fires a response with the found book.
	 * 
	 * @param bookFindByIdEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesBookNewFindByIdEvent(
			@Observes @NewBook BookFindByIdEvent bookFindByIdEvent) {
		try {
			String bookId = bookFindByIdEvent.getBookId();
			// Find Book
			OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
					"select from Book where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", bookId);
			List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);
			Book book = ooDbTx.detach(bResult.get(0), true);

			// Find author
			OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
					"select from User where userId = :uId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("uId", book.getAuthorId());
			List<User> uResult = ooDbTx.command(uQuery).execute(uparams);
			User user = uResult.get(0);
			String authorName = user.getFirstName() + " " + user.getLastName();

			// Find the cover and the category
			BookCover cover = holder.getCoverByName(book.getCoverName());
			BookCategory cat = holder.getCategoryByName(book.getCategory());

			// Get a list of Bookassetdescriptions
			OSQLSynchQuery<Bookassetdescription> baQuery = new OSQLSynchQuery<Bookassetdescription>(
					"select from Bookassetdescription where bookId = :bId");
			HashMap<String, String> baparams = new HashMap<String, String>();
			baparams.put("bId", bookId);
			List<Bookassetdescription> baResult = ooDbTx.command(baQuery)
					.execute(baparams);
			List<Bookassetdescription> retBaResults = new ArrayList<Bookassetdescription>();
			// Need to detach them. We don't want to pull back the entire object
			// tree
			if (baResult != null) {
				for (Bookassetdescription bad : baResult) {
					if (bad.getRemoved().booleanValue() == false) {
						Bookassetdescription retBad = new Bookassetdescription();
						retBad.setBookId(bad.getBookId());
						retBad.setCreatedDate(bad.getCreatedDate());
						retBad.setDescription(bad.getDescription());
						Bookasset ba = bad.getBookAssets().get(0);
						Bookasset retBa = new Bookasset();
						retBa.setId(ba.getId());
						ArrayList<Bookasset> retbookAssets = new ArrayList<Bookasset>();
						retbookAssets.add(retBa);
						retBad.setBookAssets(retbookAssets);
						retBaResults.add(retBad);
					}
				}
			}
			
			BookDisplay bookDisplay = new BookDisplay(book, cat, cover, authorName, retBaResults);

			// Fire the event
			bookFindResponseEvent.fire(new BookFindResponseEvent(bookDisplay));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}

}
