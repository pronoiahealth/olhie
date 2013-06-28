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
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.BookSearchEvent;
import com.pronoiahealth.olhie.client.shared.events.BookSearchResponseEvent;
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
 * BookSearchService.java<br/>
 * Responsibilities:<br/>
 * 1. Use to return a book based on a search query<br/>
 * 
 * <p>
 * Book should not be returned from this service unless they are active.
 * </p>
 * 
 * @author Alex Roman
 * @version 1.0
 * @since Jun 25, 2013
 * 
 *        <p>
 *        Changes: Added code to only return active books - 6/27/13 - jjd<br/>
 *        </p>
 * 
 */
@RequestScoped
public class BookSearchService {
	@Inject
	private Logger log;

	@Inject
	private Event<BookSearchResponseEvent> bookSearchResponseEvent;

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
	public BookSearchService() {
	}

	/**
	 * Finds a book and then fires a response with the found book.
	 * 
	 * @param bookFindByIdEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	protected void observesBookSearchEvent(
			@Observes BookSearchEvent bookSearchEvent) {
		try {
			String searchText = bookSearchEvent.getSearchText();
			List<BookDisplay> bookDisplayList = new ArrayList<BookDisplay>();

			// Find Book
			OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
					"select from Book where bookTitle.toLowerCase() like :title and active = true");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("title", "%" + searchText.toLowerCase() + "%");
			List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);

			for (Book book : bResult) {
				// Get the title into the object
				// Remember that Orient lazy loads the object attributes so call
				// for the attributes you want to return or detach the whole
				// thing.
				book.getBookTitle();
				book.getIntroduction();

				// Find author
				OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
						"select from User where userId = :uId");
				HashMap<String, String> uparams = new HashMap<String, String>();
				uparams.put("uId", book.getAuthorId());
				List<User> uResult = ooDbTx.command(uQuery).execute(uparams);
				User user = uResult.get(0);
				String authorName = user.getFirstName() + " "
						+ user.getLastName();

				// Find the cover and the category
				BookCover cover = holder.getCoverByName(book.getCoverName());
				BookCategory cat = holder.getCategoryByName(book.getCategory());

				// Get a list of Bookassetdescriptions
				OSQLSynchQuery<Bookassetdescription> baQuery = new OSQLSynchQuery<Bookassetdescription>(
						"select from Bookassetdescription where bookId = :bId");
				HashMap<String, String> baparams = new HashMap<String, String>();
				baparams.put("bId", book.getId());
				List<Bookassetdescription> baResult = ooDbTx.command(baQuery)
						.execute(baparams);
				List<Bookassetdescription> retBaResults = new ArrayList<Bookassetdescription>();
				// Need to detach them. We don't want to pull back the
				// entire
				// object
				// tree
				if (baResult != null) {
					for (Bookassetdescription bad : baResult) {
						if (bad.getRemoved().booleanValue() == false) {
							Bookassetdescription retBad = new Bookassetdescription();
							retBad.setBookId(bad.getBookId());
							retBad.setCreatedDate(bad.getCreatedDate());
							retBad.setDescription(bad.getDescription());
							retBad.setId(bad.getId());
							Bookasset ba = bad.getBookAssets().get(0);
							Bookasset retBa = new Bookasset();
							retBa.setId(ba.getId());
							retBa.setContentType(ba.getContentType());
							retBa.setItemType(ba.getItemType());
							ArrayList<Bookasset> retbookAssets = new ArrayList<Bookasset>();
							retbookAssets.add(retBa);
							retBad.setBookAssets(retbookAssets);
							retBaResults.add(retBad);
						}
					}
				}

				String logoFileName = book.getLogoFileName();
				BookDisplay bookDisplay = new BookDisplay(
						book,
						cat,
						cover,
						authorName,
						retBaResults,
						(logoFileName != null && logoFileName.length() > 0) ? true
								: false);
				bookDisplayList.add(bookDisplay);
			}

			// Fire the event
			bookSearchResponseEvent.fire(new BookSearchResponseEvent(
					bookDisplayList));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}

}
