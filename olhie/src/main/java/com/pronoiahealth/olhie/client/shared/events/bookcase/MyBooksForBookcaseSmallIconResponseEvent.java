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
package com.pronoiahealth.olhie.client.shared.events.bookcase;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.constants.BookcaseEnum;
import com.pronoiahealth.olhie.client.shared.vo.BookcaseDisplay;

/**
 * MyBookcaseSmallIconResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired By: <br/>
 * Observed By: <br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 29, 2013
 * 
 */
@Portable
@Conversational
public class MyBooksForBookcaseSmallIconResponseEvent {
	private List<BookcaseDisplay> bookCaseDisplayLst;
	private BookcaseEnum requestedTab;

	/**
	 * Constructor
	 * 
	 */
	public MyBooksForBookcaseSmallIconResponseEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param bookCaseDisplayLst
	 */
	public MyBooksForBookcaseSmallIconResponseEvent(
			List<BookcaseDisplay> bookCaseDisplayLst, BookcaseEnum requestedTab) {
		super();
		this.bookCaseDisplayLst = bookCaseDisplayLst;
		this.requestedTab = requestedTab;
	}

	public List<BookcaseDisplay> getBookCaseDisplayLst() {
		return bookCaseDisplayLst;
	}

	public void setBookCaseDisplayLst(List<BookcaseDisplay> bookCaseDisplayLst) {
		this.bookCaseDisplayLst = bookCaseDisplayLst;
	}

	public BookcaseEnum getRequestedTab() {
		return requestedTab;
	}

	public void setRequestedTab(BookcaseEnum requestedTab) {
		this.requestedTab = requestedTab;
	}
	
}
