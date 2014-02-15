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
package com.pronoiahealth.olhie.client.shared.events.book;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * AddBookYouTubeAssetEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired By: AddYoutubeDialog<br/>
 * Observed By: BookYouTubeAssetService<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 23, 2013
 *
 */
@Portable
@Conversational
public class AddBookYouTubeAssetEvent {
	private String bookId;
	private String assetDescription;
	private String assetDescriptionDetail;
	private String youTubeLink;
	private int hoursOfWork;

	/**
	 * Constructor
	 *
	 */
	public AddBookYouTubeAssetEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 * @param assetDescription
	 * @param youTubeLink
	 * @param hoursOfWork
	 */
	public AddBookYouTubeAssetEvent(String bookId, String assetDescription, String assetDescriptionDetail,
			String youTubeLink, int hoursOfWork) {
		super();
		this.bookId = bookId;
		this.assetDescription = assetDescription;
		this.assetDescriptionDetail = assetDescriptionDetail;
		this.youTubeLink = youTubeLink;
		this.hoursOfWork = hoursOfWork;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getAssetDescription() {
		return assetDescription;
	}

	public void setAssetDescription(String assetDescription) {
		this.assetDescription = assetDescription;
	}

	public String getYouTubeLink() {
		return youTubeLink;
	}

	public void setYouTubeLink(String youTubeLink) {
		this.youTubeLink = youTubeLink;
	}

	public int getHoursOfWork() {
		return hoursOfWork;
	}

	public void setHoursOfWork(int hoursOfWork) {
		this.hoursOfWork = hoursOfWork;
	}

	public String getAssetDescriptionDetail() {
		return assetDescriptionDetail;
	}

	public void setAssetDescriptionDetail(String assetDescriptionDetail) {
		this.assetDescriptionDetail = assetDescriptionDetail;
	}
}
