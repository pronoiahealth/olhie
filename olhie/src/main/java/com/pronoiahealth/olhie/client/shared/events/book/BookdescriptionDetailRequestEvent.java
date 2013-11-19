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
 * BookdescriptionDetailRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired form: NewBookPage<br/>
 * Observed by: BookdescriptionDetailService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 18, 2013
 * 
 */
@Portable
@Conversational
public class BookdescriptionDetailRequestEvent {
	private String bookDescriptionId;
	private String bookAssetId;

	/**
	 * Constructor
	 * 
	 */
	public BookdescriptionDetailRequestEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookDescriptionId
	 * @param bookAssetId
	 */
	public BookdescriptionDetailRequestEvent(String bookDescriptionId,
			String bookAssetId) {
		super();
		this.bookDescriptionId = bookDescriptionId;
		this.bookAssetId = bookAssetId;
	}

	public String getBookDescriptionId() {
		return bookDescriptionId;
	}

	public void setBookDescriptionId(String bookDescriptionId) {
		this.bookDescriptionId = bookDescriptionId;
	}

	public String getBookAssetId() {
		return bookAssetId;
	}

	public void setBookAssetId(String bookAssetId) {
		this.bookAssetId = bookAssetId;
	}
}
