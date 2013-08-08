package com.pronoiahealth.olhie.client.shared.events.book;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.vo.Book;

/**
 * BookSearchEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Updates a book.<br/>
 * 
 * <p>
 * Fired By: SearchComponent class<br/>
 * Observed By: BookSearchService, SeachResultsComponnet (starts the spinner)<br/>
 * </p>
 * 
 * @author Alex Roman
 * @version 1.0
 * @since Jun 25, 2013
 * 
 */
@Portable
@Conversational
public class BookSearchEvent {
	
	private String searchText;
	private int rows;

	/**
	 * Constructor
	 * 
	 */
	public BookSearchEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param searchText
	 * @param _rows
	 */
	public BookSearchEvent(String searchText, int _rows) {
		super();
		this.searchText = searchText;
		this.rows = _rows;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	

}
