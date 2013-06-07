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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * Book.java<br/>
 * Responsibilities:<br/>
 * 1. Book<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 7, 2013
 * 
 */
@Portable
@Bindable
public class Book {
	@OId
	private String id;

	@OVersion
	private Long version;

	@NotNull
	@Size(min = 1, max = 250, message = "Must be between 1 and 250 characters long.")
	private String bookTitle;

	@NotNull
	@Size(min = 10, message = "Must be at least 10 characters long.")
	private String introduction;

	@Size(max = 250)
	private String keywords;

	@NotNull
	@Size(min = 1, max = 50)
	private String category;

	@NotNull
	@Size(min = 1, max = 50)
	private String coverName;

	/**
	 * Constructor
	 * 
	 */
	public Book() {
	}

	/**
	 * Constructor
	 *
	 * @param bookTitle
	 * @param introduction
	 * @param keywords
	 * @param category
	 * @param coverName
	 */
	public Book(String bookTitle, String introduction, String keywords,
			String category, String coverName) {
		super();
		this.bookTitle = bookTitle;
		this.introduction = introduction;
		this.keywords = keywords;
		this.category = category;
		this.coverName = coverName;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCoverName() {
		return coverName;
	}

	public void setCoverName(String coverName) {
		this.coverName = coverName;
	}

	public String getId() {
		return id;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
