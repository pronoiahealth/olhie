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
package com.pronoiahealth.olhie.client.shared.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * Bookasset.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 12, 2013
 * 
 */
public class Bookasset {
	@OId
	private String id;

	@OVersion
	private Long version;

	@NotNull
	private Date createdDate;
	
	@NotNull
	@Size(min=6, max=20, message="Must be between 6 and 20 characters.")
	private String authorId;

	@NotNull
	@Size(min=1, max=50, message="Must be between 1 and 50 characters.")
	private String itemType;

	@NotNull
	@Size(min=1, max=250, message="Must be between 1 and 250 characters.")
	private String itemName;
	
	@NotNull
	@Size(min=1, max=250, message="Must be between 1 and 250 characters.")
	private String contentType;
	
	private Long size;
	
	@NotNull
	private String base64Data;
	
	@NotNull
	private String bookassetdescriptionId;
	
	/**
	 * Constructor
	 * 
	 */
	public Bookasset() {
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
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

	public String getBookassetdescriptionId() {
		return bookassetdescriptionId;
	}

	public void setBookassetdescriptionId(String bookassetdescriptionId) {
		this.bookassetdescriptionId = bookassetdescriptionId;
	}

	public String getId() {
		return id;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getBase64Data() {
		return base64Data;
	}

	public void setBase64Data(String base64Data) {
		this.base64Data = base64Data;
	}
}
