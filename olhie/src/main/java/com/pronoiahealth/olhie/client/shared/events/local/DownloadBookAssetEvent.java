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

/**
 * DownloadBookAssetEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 21, 2013
 * 
 */
@Local
public class DownloadBookAssetEvent {
	private String bookAssetId;

	/**
	 * Constructor
	 * 
	 */
	public DownloadBookAssetEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param bookAssetId
	 */
	public DownloadBookAssetEvent(String bookAssetId) {
		super();
		this.bookAssetId = bookAssetId;
	}

	public String getBookAssetId() {
		return bookAssetId;
	}

	public void setBookAssetId(String bookAssetId) {
		this.bookAssetId = bookAssetId;
	}
}
