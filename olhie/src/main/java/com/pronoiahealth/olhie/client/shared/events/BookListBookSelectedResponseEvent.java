package com.pronoiahealth.olhie.client.shared.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

@Portable
@Conversational
public class BookListBookSelectedResponseEvent {
	private String bookId;
	private boolean authorSelected;

	public BookListBookSelectedResponseEvent() {
	}

	public BookListBookSelectedResponseEvent(String bookId,
			boolean authorSelected) {
		super();
		this.bookId = bookId;
		this.authorSelected = authorSelected;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public boolean isAuthorSelected() {
		return authorSelected;
	}

	public void setAuthorSelected(boolean authorSelected) {
		this.authorSelected = authorSelected;
	}

}
