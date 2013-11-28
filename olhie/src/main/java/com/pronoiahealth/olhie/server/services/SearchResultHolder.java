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

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * SearchResultHolder.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 27, 2013
 * 
 */
@Named
@SessionScoped
public class SearchResultHolder implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> searchResults;
	private String searchStr;
	private int resultCnt;

	/**
	 * Constructor
	 * 
	 */
	public SearchResultHolder() {
	}

	/**
	 * Sets the holders values
	 * 
	 * @param searchResults
	 * @param searchStr
	 */
	public void setParams(List<String> searchResults, String searchStr) {
		this.searchResults = searchResults;
		this.searchStr = searchStr;
	}

	/**
	 * Clears the holder
	 */
	public void clear() {
		this.searchResults = null;
		this.searchStr = null;
	}

	public List<String> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<String> searchResults) {
		this.searchResults = searchResults;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public int getResultCnt() {
		if (searchResults != null) {
			return searchResults.size();
		} else {
			return 0;
		}
	}
}
