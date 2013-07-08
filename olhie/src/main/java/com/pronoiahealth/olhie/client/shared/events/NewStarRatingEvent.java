/**
 * 
 */
package com.pronoiahealth.olhie.client.shared.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * NewStarRatingEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Modify the star rating for a given book.<br/>
 * 
 * <p>
 * Fired By: NewBookPage<br/>
 * Observed By: StarRatingService<br/>
 * </P>
 * 
 * @author Alex Roman
 * @version 1.0
 * @since Jul 07, 2013
 */
@Portable
@Conversational
public class NewStarRatingEvent {

	int stars = 0;
	String bookId = null;
	
	/**
	 * 
	 */
	public NewStarRatingEvent()
	{
	}
	
	/**
	 * @param _stars
	 * @param _bookId
	 */
	public NewStarRatingEvent(int _stars, String bookId)
	{
		this.stars = _stars;
		this.bookId = bookId;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
}
