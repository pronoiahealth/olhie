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
package com.pronoiahealth.olhie.client.shared.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * NewStartRatingResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Response from the NewStartRatingEvent<br/>
 * 
 * <p>
 * Fired By: StartRatingService<br/>
 * Observed By: NewBookPage<br/>
 * </P>
 * 
 * @author Alex Roman
 * @version 1.0
 * @since Jul 07, 2013
 * 
 */
@Portable
@Conversational
public class NewStarRatingResponseEvent {
	private int stars;
	private String userId;
	private String bookId;

	public NewStarRatingResponseEvent() {
	}

	public String getUserId() {
		return userId;
	}

	public NewStarRatingResponseEvent(int stars,
			String userId, String bookId) {
		super();
		this.stars = stars;
		this.userId = userId;
		this.bookId = bookId;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public void setUserIdId(String userId) {
		this.userId = userId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
}