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
import java.util.ArrayList;
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
	private int pageSize;
	private int calculateCurrentPage;

	/**
	 * Constructor
	 * 
	 */
	public SearchResultHolder() {
	}

	/**
	 * Used to set the initial state. It will set the internal
	 * currentPagePosition to the first page.
	 * 
	 * @param searchResults
	 * @param searchStr
	 * @param pageSize
	 * @param currentPage
	 */
	public void setSearchResultHolder(List<String> searchResults,
			String searchStr, int pageSize) {
		this.searchResults = searchResults;
		this.searchStr = searchStr;
		this.pageSize = pageSize;

		// Initialize the first page position
		this.setCurrentPageToFirst();
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * True if the the results returned are part of the first page of results.
	 * 
	 * 
	 * @return
	 */
	public boolean isFirstPage() {
		if (calculateCurrentPage == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Indicates that the starting index for the return of BookDisplays is part
	 * of the last page
	 * 
	 * 
	 * @return
	 */
	public boolean isLastPage() {
		if (calculateCurrentPage == getTotalPages()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the starting index of the last page
	 * 
	 * @return
	 */
	public int getStartIdxLastPage() {
		double pages = getPages();
		int wholePages = (int) pages;
		if (pages > wholePages) {
			return (wholePages * pageSize) + 1;
		} else {
			return ((wholePages - 1) * pageSize) + 1;
		}
	}

	/**
	 * Will return a double value. A whole number indicates a number of pages
	 * evenly divisible by the pageSize.
	 * 
	 * @return
	 */
	private double getPages() {
		if (getResultCnt() != 0) {
			// Performs casts to coerce final value to double
			return (double) getResultCnt() / (int) pageSize;
		} else {
			return 0;
		}
	}

	/**
	 * Returns the total number of whole pages. There may be a last page that is
	 * not full.
	 * 
	 * @return
	 */
	public int getWholePages() {
		double pages = getPages();
		int wholePages = (int) pages;
		return wholePages;
	}

	/**
	 * Returns the total number of pages
	 * 
	 * @return
	 */
	public int getTotalPages() {
		double pages = getPages();
		int wholePages = (int) pages;
		int totalPages = 0;
		if (pages > wholePages) {
			totalPages = wholePages + 1;
		} else {
			totalPages = wholePages;
		}
		return totalPages;
	}

	public int getCurrentPageStartIdx() {
		int results = (calculateCurrentPage * pageSize);
		if (results > 0) {
			return results - (pageSize - 1);
		} else {
			return 0;
		}
	}

	public int getCurrentPageEndIdx() {
		int maxRet = (calculateCurrentPage * pageSize);
		int lstCnt = this.getResultCnt();
		if (maxRet > lstCnt) {
			return lstCnt;
		} else {
			return maxRet;
		}
	}

	/**
	 * Moves the page indicator back 1
	 */
	public void setCurrentPageToPrevious() {
		if (this.getResultCnt() > 0) {
			if ((calculateCurrentPage - 1) > 0) {
				calculateCurrentPage--;
			}
		}
	}

	/**
	 * Sets the page indicator to the first page
	 */
	public void setCurrentPageToFirst() {
		if (this.getResultCnt() > 0) {
			calculateCurrentPage = 1;
		} else {
			calculateCurrentPage = 1;
		}
	}

	/**
	 * Sets the page indicator to the next page
	 */
	public void setCurrentPageToNext() {
		if (this.getResultCnt() > 0) {
			int totalPages = getTotalPages();
			if (calculateCurrentPage < totalPages) {
				calculateCurrentPage++;
			}
		}
	}

	/**
	 * Sets the page indiactor to the last page
	 */
	public void setCurrentPageToLast() {
		if (this.getResultCnt() > 0) {
			calculateCurrentPage = getTotalPages();
		} else {
			calculateCurrentPage = 0;
		}
	}

	/**
	 * Will the entire result set fit on one page
	 * 
	 * @return
	 */
	public boolean isFitsOnOnePage() {
		if (this.getResultCnt() <= pageSize) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a portion of the list. If the current results list is empty then
	 * an empty list will be returned.
	 * 
	 * @return
	 */
	public List<String> getResultsLstForCurrentPage() {
		if (this.getResultCnt() > 0) {
			int startIdx = this.getCurrentPageStartIdx();
			int endIdx = this.getCurrentPageEndIdx();
			return searchResults.subList(startIdx - 1, endIdx);
		} else {
			return new ArrayList<String>();
		}
	}
}