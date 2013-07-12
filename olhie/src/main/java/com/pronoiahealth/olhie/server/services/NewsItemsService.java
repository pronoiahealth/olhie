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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.news.NewsItemsRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.news.NewsItemsResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.NewsItem;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;

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
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

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
			// Run query
			OSQLSynchQuery<NewsItem> uQuery = new OSQLSynchQuery<NewsItem>(
					"select from NewsItem where active = true");
			List<NewsItem> newsItems = ooDbTx.command(uQuery).execute();
			
			// Convert return
			newsItems = createRetLst(newsItems);
			if (newsItems == null) {
				newsItems = new ArrayList<NewsItem>();
			}
			
			// Send the data back
			newsItemsResponseEvent.fire(new NewsItemsResponseEvent(newsItems));
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);

			// Tell the user what the exception was
			serviceErrorEvent.fire(new ServiceErrorEvent(e.getMessage()));
		}
	}
	
	/**
	 * Because of how Orient proxies returned POJO's they must be "detached" and
	 * a non proxy instance returned. The JSON transport is not capable of
	 * marshalling the proxied instances.
	 * 
	 * 
	 * @param proxyLst
	 * @return - Will return the unproxyed list of an empty list
	 */
	private <T> List<T> createRetLst(List<T> proxyLst) {
		List<T> uLst = new ArrayList<T>();
		if (proxyLst != null) {
			for (T u : proxyLst) {
				T dU = detachUser(u);
				uLst.add(dU);
			}
		}
		return uLst;
	}
	
	/**
	 * Detachs User object from Orient. Returns a nonProxyed instance
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T detachUser(T obj) {
		return (T) ooDbTx.detachAll(obj, true);
	}

}
