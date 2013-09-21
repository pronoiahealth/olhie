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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.news.NewsItemsRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.news.NewsItemsResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.NewsItem;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO;

/**
 * NewsItemsService.java<br/>
 * Responsibilities:<br/>
 * 1. Retreive news items.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 4, 2013
 * 
 */
@RequestScoped
public class NewsItemsService {

	@Inject
	private Logger log;

	@Inject
	private Event<NewsItemsResponseEvent> newsItemsResponseEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@DAO
	private NewsItemDAO newsItemDAO;

	/**
	 * Constructor
	 * 
	 */
	public NewsItemsService() {
	}

	/**
	 * Return news items for display
	 * 
	 * @param newsItemsRequestEvent
	 */
	public void observesNewsItemsRequestEvent(
			@Observes NewsItemsRequestEvent newsItemsRequestEvent) {
		try {
			// Get the list
			List<NewsItem> newsItems = newsItemDAO.getActiveNewsItems();

			// Send the data back
			newsItemsResponseEvent.fire(new NewsItemsResponseEvent(newsItems));
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);

			// Tell the user what the exception was
			serviceErrorEvent.fire(new ServiceErrorEvent(e.getMessage()));
		}
	}

}
