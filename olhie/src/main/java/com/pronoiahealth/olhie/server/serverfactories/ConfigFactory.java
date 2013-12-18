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
package com.pronoiahealth.olhie.server.serverfactories;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

/**
 * ConfigFactory.java<br/>
 * Responsibilities:<br/>
 * 1. Produces configuration parameters
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 28, 2013
 * 
 */
public class ConfigFactory {

	@Inject
	@ConfigProperty(name = "SEARCH_PAGE_SIZE")
	private String searchPageSize;
	
	@Inject
	@ConfigProperty(name = "SOLR_MAX_RESULTS")
	private String maxSolrResults;
	

	/**
	 * Default no-args Constructor
	 * 
	 */
	public ConfigFactory() {
	}

	/**
	 * Converts the string value from the properties file to an int
	 * 
	 * @return
	 */
	@Produces
	@SearchPageSize
	public int getSearchPageSize() {
		if (searchPageSize != null) {
			return Integer.parseInt(searchPageSize.trim());
		} else {
			return 0;
		}
	}
	
	@Produces
	@SolrMaxResultsReturned
	public int getMaxSolrResults() {
		if (maxSolrResults != null) {
			return Integer.parseInt(maxSolrResults.trim());
		} else {
			return -1;
		}
	}

}
