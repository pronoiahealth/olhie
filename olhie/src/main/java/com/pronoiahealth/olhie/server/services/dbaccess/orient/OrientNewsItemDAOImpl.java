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
package com.pronoiahealth.olhie.server.services.dbaccess.orient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.pronoiahealth.olhie.client.shared.vo.NewsItem;
import com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO;

/**
 * OrientNewsItemDAOImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 15, 2013
 * 
 */
public class OrientNewsItemDAOImpl extends OrientBaseTxDAO implements
		NewsItemDAO {

	/**
	 * Constructor
	 * 
	 */
	public OrientNewsItemDAOImpl() {
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO#getActiveNewsItems()
	 */
	@Override
	public List<NewsItem> getActiveNewsItems() throws Exception {
		// Run query
		OSQLSynchQuery<NewsItem> uQuery = new OSQLSynchQuery<NewsItem>(
				"select from NewsItem where active = true");
		List<NewsItem> newsItems = ooDbTx.command(uQuery).execute();

		// Convert return
		newsItems = createDetachedRetLst(newsItems);
		if (newsItems == null) {
			newsItems = new ArrayList<NewsItem>();
		}

		// Return the news items
		return newsItems;
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO#getAllNewsItems()
	 */
	@Override
	public List<NewsItem> getAllNewsItems() throws Exception {
		// Run query
		OSQLSynchQuery<NewsItem> uQuery = new OSQLSynchQuery<NewsItem>(
				"select from NewsItem");
		List<NewsItem> newsItems = ooDbTx.command(uQuery).execute();

		// Convert return
		newsItems = createDetachedRetLst(newsItems);
		if (newsItems == null) {
			newsItems = new ArrayList<NewsItem>();
		}

		// Return the news items
		return newsItems;
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO#removeNewsItems(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void removeNewsItems(String newsItemId) throws Exception {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			NewsItem item = this.getNewsItemById(newsItemId);
			ooDbTx.delete(item);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO#addNewsItems(com.pronoiahealth.olhie.client.shared.vo.NewsItem)
	 */
	@Override
	public void addNewsItems(NewsItem newsItem) throws Exception {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			newsItem = ooDbTx.save(newsItem);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO#updateNewsItemsActiveStatus(java.lang.String,
	 *      boolean)
	 */
	@Override
	public void updateNewsItemsActiveStatus(String itemId, boolean activeStatus)
			throws Exception {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			NewsItem nItem = getNewsItemById(itemId);
			nItem.setActive(activeStatus);
			NewsItem newsItem = ooDbTx.save(nItem);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO#getNewsItemById(java.lang.String)
	 */
	@Override
	public NewsItem getNewsItemById(String id) throws Exception {
		OSQLSynchQuery<NewsItem> bQuery = new OSQLSynchQuery<NewsItem>(
				"select from NewsItem where @rid = :bId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", id);
		List<NewsItem> bResult = ooDbTx.command(bQuery).execute(bparams);
		if (bResult != null && bResult.size() == 1) {
			return bResult.get(0);
		} else {
			throw new Exception("Could not find News Item.");
		}
	}

}
