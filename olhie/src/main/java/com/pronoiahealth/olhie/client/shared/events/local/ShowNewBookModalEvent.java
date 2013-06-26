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
package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;

import com.pronoiahealth.olhie.client.shared.constants.ModeEnum;
import com.pronoiahealth.olhie.client.shared.vo.Book;

/**
 * ShowNewBookModalEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Fired to show the New Book Dialog.<br/>
 * 
 * <p>
 * Fired Form: Header class<br/>
 * Observed By: NewBookDialog class<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 5, 2013
 *
 */
@Local
public class ShowNewBookModalEvent {
	private ModeEnum mode;
	private Book editBook;

	//public ShowNewBookModalEvent() {
	//}

	public ShowNewBookModalEvent(ModeEnum mode, Book editBook) {
		super();
		this.mode = mode;
		this.editBook = editBook;
	}
	
	public ModeEnum getMode() {
		return mode;
	}

	public void setMode(ModeEnum mode) {
		this.mode = mode;
	}

	public Book getEditBook() {
		return editBook;
	}

	public void setEditBook(Book editBook) {
		this.editBook = editBook;
	}
}
