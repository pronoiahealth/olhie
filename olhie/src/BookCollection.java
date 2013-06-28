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

import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * BookCollection.java<br/>
 * Responsibilities:<br/>
 * 1. A Collection category for the Bookcase<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Portable
public class BookCollection {
	private String collectionName;
	private List<BookForDisplay> books;

	/**
	 * Constructor
	 *
	 */
	public BookCollection() {
	}

	public BookCollection(String collectionName, List<BookForDisplay> books) {
		super();
		this.collectionName = collectionName;
		this.books = books;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public List<BookForDisplay> getBooks() {
		return books;
	}

	public void setBooks(List<BookForDisplay> books) {
		this.books = books;
	}
	
	public void addBook(BookForDisplay book) {
		if (getBooks() == null) {
			books = new ArrayList<BookForDisplay>();
		}
		books.add(book);
	}

}
