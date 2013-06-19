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

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * Book.java<br/>
 * Responsibilities:<br/>
 * 1. Represents the Book data<br/>
 * 
 * @author Alex Roman
 * @version 1.0
 * @since Jun 19, 2013
 * 
 */
@Portable
public class BookDisplay {
	
	private Book book;
	private BookCategory bookCategory;
	private BookCover bookCover;
	private String authorFullName;
	private List<Bookassetdescription> bookAssetDescriptions;
	
	/**
	 * 
	 */
	public BookDisplay(){
	}
	
	/**
	 * @param book
	 * @param bookCategory
	 * @param bookCover
	 * @param authorFullName
	 * @param bookAssetDescriptions
	 */
	public BookDisplay(Book book, BookCategory bookCategory,
			BookCover bookCover, String authorFullName,
			List<Bookassetdescription> bookAssetDescriptions) {
		super();
		this.book = book;
		this.bookCategory = bookCategory;
		this.bookCover = bookCover;
		this.authorFullName = authorFullName;
		this.bookAssetDescriptions = bookAssetDescriptions;
	}

	public Book getBook() {
		return book;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public BookCategory getBookCategory() {
		return bookCategory;
	}
	
	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}
	
	public BookCover getBookCover() {
		return bookCover;
	}
	
	public void setBookCover(BookCover bookCover) {
		this.bookCover = bookCover;
	}
	
	public String getAuthorFullName() {
		return authorFullName;
	}
	
	public void setAuthorFullName(String authorFullName) {
		this.authorFullName = authorFullName;
	}
	
	public List<Bookassetdescription> getBookAssetDescriptions() {
		return bookAssetDescriptions;
	}
	
	public void setBookAssetDescriptions(
			List<Bookassetdescription> bookAssetDescriptions) {
		this.bookAssetDescriptions = bookAssetDescriptions;
	}

	
}
