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
import java.util.Set;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;

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
	private BookCover bookCover;
	private BookCategory bookCategory;
	private String authorFullName;
	private boolean bookLogo;
	private boolean hasComments;
	private Set<UserBookRelationshipEnum> relEnums;
	private int bookRating;
	private int userBookRating;
	private List<Bookassetdescription> bookAssetDescriptions;
	private int bookHoursOfWork;

	/**
	 * 
	 */
	public BookDisplay() {
	}

	/**
	 * @param book
	 * @param bookCategory
	 * @param bookCover
	 * @param authorFullName
	 * @param bookAssetDescriptions
	 */
	public BookDisplay(Book book, BookCover bookCover, BookCategory bookCategory, String authorFullName,
			List<Bookassetdescription> bookAssetDescriptions, int bookRating,
			int userBookRating, int bookHoursOfWork) {
		this(book, bookCover, bookCategory, authorFullName, bookAssetDescriptions, false,
				false, bookRating, userBookRating, bookHoursOfWork);
	}

	/**
	 * Constructor
	 * 
	 * @param book
	 * @param bookCategory
	 * @param bookCover
	 * @param authorFullName
	 * @param bookAssetDescriptions
	 * @param bookLogo
	 * @param hasComments
	 * @param bookRating
	 * @param userBookRating
	 */
	public BookDisplay(Book book, BookCover bookCover, BookCategory bookCategory, String authorFullName,
			List<Bookassetdescription> bookAssetDescriptions, boolean bookLogo,
			boolean hasComments, int bookRating, int userBookRating, int bookHoursOfWork) {
		super();
		this.book = book;
		this.bookCover = bookCover;
		this.bookCategory = bookCategory;
		this.authorFullName = authorFullName;
		this.bookLogo = bookLogo;
		this.hasComments = hasComments;
		this.bookAssetDescriptions = bookAssetDescriptions;
		this.bookLogo = bookLogo;
		this.bookRating = bookRating;
		this.userBookRating = userBookRating;
		this.bookHoursOfWork = bookHoursOfWork;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
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

	public boolean isBookLogo() {
		return bookLogo;
	}

	public void setBookLogo(boolean bookLogo) {
		this.bookLogo = bookLogo;
	}

	public Set<UserBookRelationshipEnum> getRelEnums() {
		return relEnums;
	}

	public void setRelEnums(Set<UserBookRelationshipEnum> relEnums) {
		this.relEnums = relEnums;
	}

	public int getBookRating() {
		return bookRating;
	}

	public void setBookRating(int bookRating) {
		this.bookRating = bookRating;
	}

	public int getUserBookRating() {
		return userBookRating;
	}

	public void setUserBookRating(int userBookRating) {
		this.userBookRating = userBookRating;
	}

	public boolean isHasComments() {
		return hasComments;
	}

	public void setHasComments(boolean hasComments) {
		this.hasComments = hasComments;
	}

	public BookCover getBookCover() {
		return bookCover;
	}

	public void setBookCover(BookCover bookCover) {
		this.bookCover = bookCover;
	}

	public BookCategory getBookCategory() {
		return bookCategory;
	}

	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}

	public int getBookHoursOfWork() {
		return bookHoursOfWork;
	}

	public void setBookHoursOfWork(int bookHoursOfWork) {
		this.bookHoursOfWork = bookHoursOfWork;
	}
}
