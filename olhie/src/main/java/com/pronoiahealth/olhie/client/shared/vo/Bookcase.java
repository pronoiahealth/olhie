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
 * Bookcase.java<br/>
 * Responsibilities:<br/>
 * 1. Represents a users bookcase<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Portable
public class Bookcase {
	private List<BookCollection> bookCollections;

	/**
	 * Constructor
	 *
	 */
	public Bookcase() {
	}

	public List<BookCollection> getBookCollections() {
		return bookCollections;
	}

	public void setBookCollections(List<BookCollection> bookCollections) {
		this.bookCollections = bookCollections;
	}

	public void addCollection(BookCollection bookCollection) {
		if (bookCollections == null) {
			bookCollections = new ArrayList<BookCollection>();
		}
		bookCollections.add(bookCollection);
	}

}
