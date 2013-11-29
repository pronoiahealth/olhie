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
package com.pronoiahealth.olhie.client.shared.events.book;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.constants.SearchPageActionEnum;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;

/**
 * SearchPageNavigationEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired By: BookSearchService<br/>
 * Observed By: SearchPagerWidget<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 29, 2013
 * 
 */
@Portable
@Conversational
public class SearchPageNavigationResponseEvent {
	List<BookDisplay> bookDisplays;
	int startIndex;
	int endIndex;
	int totalInResultsSet;
	boolean isFirstPage;
	boolean isLastPage;
	boolean isFitsOnOnePage;
	private SearchPageActionEnum requestedAction;

	/**
	 * Constructor
	 * 
	 */
	public SearchPageNavigationResponseEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param bookDisplays
	 * @param startIndex
	 * @param endIndex
	 * @param totalInResultsSet
	 * @param isFirstPage
	 * @param isLastPage
	 * @param requestedAction
	 */
	public SearchPageNavigationResponseEvent(List<BookDisplay> bookDisplays,
			int startIndex, int endIndex, int totalInResultsSet,
			boolean isFirstPage, boolean isLastPage, boolean isFitsOnOnePage,
			SearchPageActionEnum requestedAction) {
		this.bookDisplays = bookDisplays;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.totalInResultsSet = totalInResultsSet;
		this.isFirstPage = isFirstPage;
		this.isLastPage = isLastPage;
		this.requestedAction = requestedAction;
		this.isFitsOnOnePage = isFitsOnOnePage;
	}

	public List<BookDisplay> getBookDisplays() {
		return bookDisplays;
	}

	public void setBookDisplays(List<BookDisplay> bookDisplays) {
		this.bookDisplays = bookDisplays;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getTotalInResultsSet() {
		return totalInResultsSet;
	}

	public void setTotalInResultsSet(int totalInResultsSet) {
		this.totalInResultsSet = totalInResultsSet;
	}

	public boolean isFirstPage() {
		return isFirstPage;
	}

	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public SearchPageActionEnum getRequestedAction() {
		return requestedAction;
	}

	public void setRequestedAction(SearchPageActionEnum requestedAction) {
		this.requestedAction = requestedAction;
	}

	public boolean isFitsOnOnePage() {
		return isFitsOnOnePage;
	}

	public void setFitsOnOnePage(boolean isFitsOnOnePage) {
		this.isFitsOnOnePage = isFitsOnOnePage;
	}
}
