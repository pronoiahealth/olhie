package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;

@Local
public class ShowAddBookCommentModalEvent {

	private String bookId;
	private String bookTitle;
	private boolean provideMostRecentComment;

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
	public ShowAddBookCommentModalEvent(String bookId, String bookTitle,
			boolean provideMostRecentComment) {
		super();
		this.bookId = bookId;
		this.bookTitle = bookTitle;
		this.provideMostRecentComment = provideMostRecentComment;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public boolean isProvideMostRecentComment() {
		return provideMostRecentComment;
	}

	public void setProvideMostRecentComment(boolean provideMostRecentComment) {
		this.provideMostRecentComment = provideMostRecentComment;
	}
}
