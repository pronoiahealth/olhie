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
 * Observed By: BookSearchService<br/>
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

	/**
	 * Constructor
	 * 
	 */
	public BookSearchEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param book
	 */
	public BookSearchEvent(String searchText) {
		super();
		this.searchText = searchText;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}


}
