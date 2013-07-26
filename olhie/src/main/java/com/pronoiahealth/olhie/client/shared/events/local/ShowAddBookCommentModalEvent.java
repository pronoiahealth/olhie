package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;

@Local
public class ShowAddBookCommentModalEvent {

	private String bookId;

	/**
	 * Constructor
	 *
	 */
	public ShowAddBookCommentModalEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 */
	public ShowAddBookCommentModalEvent(String bookId) {
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
