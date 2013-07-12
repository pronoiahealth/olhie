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
package com.pronoiahealth.olhie.client.shared.events.book;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * RemoveBookassetdescriptionEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Remove a book asset description from a book. This only deactivates it and
 * does not delete it from the database.<br/>
 * 
 * <p>
 * Fired By: NewBookPage<br/>
 * Observed By: RemoveBookassetdescriptionService<br/>
 * </P>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 24, 2013
 * 
 */
@Portable
@Conversational
public class RemoveBookassetdescriptionEvent {
	private String bookAssetdescriptionId;

	public RemoveBookassetdescriptionEvent() {
	}

	public RemoveBookassetdescriptionEvent(String bookAssetdescriptionId) {
		super();
		this.bookAssetdescriptionId = bookAssetdescriptionId;
	}

	public String getBookAssetdescriptionId() {
		return bookAssetdescriptionId;
	}

	public void setBookAssetdescriptionId(String bookAssetdescriptionId) {
		this.bookAssetdescriptionId = bookAssetdescriptionId;
	}
}
