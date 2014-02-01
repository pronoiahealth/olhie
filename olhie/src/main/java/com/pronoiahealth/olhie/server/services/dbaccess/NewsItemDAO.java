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
package com.pronoiahealth.olhie.server.services.dbaccess;

import java.util.List;

import com.pronoiahealth.olhie.client.shared.vo.NewsItem;

/**
 * NewsItemDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 15, 2013
 * 
 */
public interface NewsItemDAO extends BaseDAO {

	/**
	 * Gets a list of active news items.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<NewsItem> getActiveNewsItems() throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public List<NewsItem> getAllNewsItems() throws Exception;

	/**
	 * @param newsItemId
	 * @param adminId
	 * @throws Exception
	 */
	public void removeNewsItems(String newsItemId) throws Exception;

	/**
	 * @param newsItem
	 * @throws Exception
	 */
	public void addNewsItems(NewsItem newsItem) throws Exception;

	/**
	 * @param newsItem
	 * @throws Exception
	 */
	public void updateNewsItemsActiveStatus(String itemId, boolean activeStatus)
			throws Exception;
	
	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public NewsItem getNewsItemById(String id) throws Exception;
}
