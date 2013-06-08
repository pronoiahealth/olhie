package com.pronoiahealth.olhie.client.shared.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.vo.Book;

/**
 * BookUpdateEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Updates a book.<br/>
 * 
 * <p>
 * Fired By: NewBookDialog class<br/>
 * Observed By: BookUpdateService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 7, 2013
 * 
 */
@Portable
@Conversational
public class BookUpdateEvent {
	
	private Book book;

	/**
	 * Constructor
	 * 
	 */
	public BookUpdateEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param book
	 */
	public BookUpdateEvent(Book book) {
		super();
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	

}
