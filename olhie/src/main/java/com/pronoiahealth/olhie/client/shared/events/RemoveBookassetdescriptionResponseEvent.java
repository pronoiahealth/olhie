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
package com.pronoiahealth.olhie.client.shared.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * RemoveBookassetdescriptionResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Response from the RemoveBookassetdescriptionEvent<br/>
 * 
 * <p>
 * Fired By: RemoveBookassetdescriptionService<br/>
 * Observed By: NewBookPage<br/>
 * </P>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 24, 2013
 * 
 */
@Portable
@Conversational
public class RemoveBookassetdescriptionResponseEvent {
	private String bookAssetdescriptionId;
	private String bookId;

	public RemoveBookassetdescriptionResponseEvent() {
	}

	public String getBookAssetdescriptionId() {
		return bookAssetdescriptionId;
	}

	public RemoveBookassetdescriptionResponseEvent(
			String bookAssetdescriptionId, String bookId) {
		super();
		this.bookAssetdescriptionId = bookAssetdescriptionId;
		this.bookId = bookId;
	}

	public void setBookAssetdescriptionId(String bookAssetdescriptionId) {
		this.bookAssetdescriptionId = bookAssetdescriptionId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
}