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
 * BookdescriptionDetailResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * 
 * <p>
 * Fired form: BookdescriptionDetailService class<br/>
 * Observed by: BookDescriptionDetailDialog<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 18, 2013
 * 
 */
@Portable
@Conversational
public class BookdescriptionDetailResponseEvent {
	private String bookDescription;
	private String creationDate;
	private String creationHrs;
	private String itemType;
	private String itemName;

	/**
	 * Constructor
	 * 
	 */
	public BookdescriptionDetailResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookDescription
	 * @param creationDate
	 * @param creationHrs
	 * @param itemType
	 */
	public BookdescriptionDetailResponseEvent(String bookDescription,
			String creationDate, String creationHrs, String itemType, String itemName) {
		super();
		this.bookDescription = bookDescription;
		this.creationDate = creationDate;
		this.creationHrs = creationHrs;
		this.itemType = itemType;
		this.itemName = itemName;
	}

	public String getBookDescription() {
		return bookDescription;
	}

	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationHrs() {
		return creationHrs;
	}

	public void setCreationHrs(String creationHrs) {
		this.creationHrs = creationHrs;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
