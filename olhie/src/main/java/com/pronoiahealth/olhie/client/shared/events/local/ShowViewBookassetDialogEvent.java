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
 * ShowViewBookassetDialogEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Show the book asset view dialog<br/>
 * 
 * <p>
 * Fired By: NewBookPage<br/>
 * Observed By: ViewBookassetDialog<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 23, 2013
 *
 */
@Local
@NonPortable
public class ShowViewBookassetDialogEvent {
	private String bookassetId;
	private String viewType;
	

	/**
	 * Constructor
	 *
	 */
	public ShowViewBookassetDialogEvent() {
	}


	/**
	 * Constructor
	 *
	 * @param booassetId
	 */
	public ShowViewBookassetDialogEvent(String bookassetId, String viewType) {
		super();
		this.bookassetId = bookassetId;
		this.viewType = viewType;
	}

	public String getBookassetId() {
		return bookassetId;
	}

	public void setBookassetId(String bookassetId) {
		this.bookassetId = bookassetId;
	}


	public String getViewType() {
		return viewType;
	}


	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
}
