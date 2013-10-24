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
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
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
public class OrientNewsItemDAOImpl extends OrientBaseTxDAO implements NewsItemDAO {

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
}
