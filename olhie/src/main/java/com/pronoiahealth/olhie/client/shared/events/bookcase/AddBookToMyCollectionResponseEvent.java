package com.pronoiahealth.olhie.client.shared.events.bookcase;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * AddBookToMyCollectionResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 *<p>
 * Fired From: RemoveBookFromMyCollectionService<br/>
 * Observed By: Book3d_3<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 17, 2013
 *
 */
@Portable
@Conversational
public class AddBookToMyCollectionResponseEvent {
	private String bookId;

	/**
	 * Constructor
	 *
	 */
	public AddBookToMyCollectionResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 */
	public AddBookToMyCollectionResponseEvent(String bookId) {
		super();
		this.bookId = bookId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

}
