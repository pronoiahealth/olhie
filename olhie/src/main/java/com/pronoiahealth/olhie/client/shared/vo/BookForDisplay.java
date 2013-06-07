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

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * Book.java<br/>
 * Responsibilities:<br/>
 * 1. Represents the Book<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Portable
public class BookForDisplay {
	private String id;

	private String title;

	private BookState bookState;

	private String authorName;

	private Integer rating;

	private String introduction;

	private String toc;

	private String publishedDate;

	private String numberDownloads;

	private String otherPubsByAuthor;

	private BookCover bookCover;

	private BookCategory catagory;

	/**
	 * Constructor
	 */
	public BookForDisplay() {
	}

	/**
	 * Constructor
	 * 
	 * @param bookId
	 * @param title
	 * @param authorName
	 * @param rating
	 * @param introduction
	 * @param toc
	 * @param publishedDate
	 * @param numberDownloads
	 * @param otherPubsByAuthor
	 * @param bookImg
	 * @param bookImgHeight
	 * @param bookWidthHeight
	 */
	public BookForDisplay(String id, String title, String authorName, int rating,
			String introduction, String toc, String publishedDate,
			String numberDownloads, String otherPubsByAuthor,
			BookState bookState, BookCategory catagory, BookCover bookCover) {
		super();
		this.id = id;
		this.title = title;
		this.authorName = authorName;
		this.rating = rating;
		this.introduction = introduction;
		this.toc = toc;
		this.publishedDate = publishedDate;
		this.numberDownloads = numberDownloads;
		this.otherPubsByAuthor = otherPubsByAuthor;
		this.bookState = bookState;
		this.catagory = catagory;
		this.bookCover = bookCover;

	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getToc() {
		return toc;
	}

	public void setToc(String toc) {
		this.toc = toc;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getNumberDownloads() {
		return numberDownloads;
	}

	public void setNumberDownloads(String numberDownloads) {
		this.numberDownloads = numberDownloads;
	}

	public String getOtherPubsByAuthor() {
		return otherPubsByAuthor;
	}

	public void setOtherPubsByAuthor(String otherPubsByAuthor) {
		this.otherPubsByAuthor = otherPubsByAuthor;
	}

	public BookState getBookState() {
		return bookState;
	}

	public void setBookState(BookState bookState) {
		this.bookState = bookState;
	}

	public BookCategory getCatagory() {
		return catagory;
	}

	public void setCatagory(BookCategory catagory) {
		this.catagory = catagory;
	}

	public BookCover getBookCover() {
		return bookCover;
	}

	public void setBookCover(BookCover bookCover) {
		this.bookCover = bookCover;
	}

}
