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
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * Bookassetdescription.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 12, 2013
 *
 */
@Portable
public class Bookassetdescription {
	@OId
	private String id;

	@OVersion
	private Long version;

	@NotNull
	@Size(min = 1, max = 250, message = "Must be between 1 and 250 characters.")
	private String description;
	
	@Size(max = 2048, message = "Must be between 0 and 2048 characters.")
	private String descriptionDetail;

	@NotNull
	private Date createdDate;

	@NotNull
	private Boolean removed;

	private Date removedDate;
	
	private String removedBy;

	@NotNull
	private String bookId;
	
	// @NotNull
	private int position;

	private List<Bookasset> bookAssets;

	/**
	 * Constructor
	 *
	 */
	public Bookassetdescription() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getRemoved() {
		return removed;
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	public Date getRemovedDate() {
		return removedDate;
	}

	public void setRemovedDate(Date removedDate) {
		this.removedDate = removedDate;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public List<Bookasset> getBookAssets() {
		return bookAssets;
	}

	public void setBookAssets(List<Bookasset> bookAssets) {
		this.bookAssets = bookAssets;
	}

	public String getId() {
		return id;
	}

	public String getRemovedBy() {
		return removedBy;
	}

	public void setRemovedBy(String removedBy) {
		this.removedBy = removedBy;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getDescriptionDetail() {
		return descriptionDetail;
	}

	public void setDescriptionDetail(String descriptionDetail) {
		this.descriptionDetail = descriptionDetail;
	}
	
}
