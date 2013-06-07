package com.pronoiahealth.olhie.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.events.BookCoverListRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCoverListResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;

@RequestScoped
public class BookCoverService {

	@Inject
	private Logger log;

	@Inject
	private Event<BookCoverListResponseEvent> bookCoverListResponseEvent;

	/**
	 * Constructor
	 * 
	 */
	public BookCoverService() {
	}

	/**
	 * Observes for a book cover request
	 * 
	 * @param bookCoverListRequestEvent
	 */
	// TODO: Store in database
	protected void observesBookCoverListRequestEvent(
			@Observes BookCoverListRequestEvent bookCoverListRequestEvent) {
		List<BookCover> bookCovers = new ArrayList<BookCover>();
		bookCovers.add(new BookCover("Olhie/images/p1.png", "Paper"));
		bookCovers.add(new BookCover("Olhie/images/paper.png", "Paper 1"));
		bookCovers.add(new BookCover("Olhie/images/paper1.png", "Paper 2"));

		bookCoverListResponseEvent.fire(new BookCoverListResponseEvent(
				bookCovers));
	}
}
