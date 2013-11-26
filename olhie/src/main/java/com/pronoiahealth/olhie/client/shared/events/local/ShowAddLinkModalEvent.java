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
import org.jboss.errai.common.client.api.annotations.NonPortable;

/**
 * ShowAddLinkModalEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 *
 * <p>
 * Fired By: NewAssetDialog<br/>
 * Observed By: AddLinkDialog<br/>
 * </p>
 * @author John DeStefano
 * @version 1.0
 * @since Nov 23, 2013
 *
 */
@Local
@NonPortable
public class ShowAddLinkModalEvent {
	private String bookId;

	/**
	 * Constructor
	 *
	 */
	public ShowAddLinkModalEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookId
	 */
	public ShowAddLinkModalEvent(String bookId) {
		super();
		this.bookId = bookId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
}