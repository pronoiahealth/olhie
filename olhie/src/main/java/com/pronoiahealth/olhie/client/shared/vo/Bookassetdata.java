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

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * Bookassetdata.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jan 23, 2014
 * 
 */
@Portable
@Bindable
public class Bookassetdata {

	@OId
	private String id;

	@OVersion
	private Long version;

	private String bookassetId;

	private byte[] assetData;

	/**
	 * Constructor
	 * 
	 */
	public Bookassetdata() {
	}

	/**
	 * Constructor
	 *
	 * @param bookassetId
	 * @param assetData
	 */
	public Bookassetdata(String bookassetId, byte[] assetData) {
		super();
		this.bookassetId = bookassetId;
		this.assetData = assetData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getAssetData() {
		return assetData;
	}

	public void setAssetData(byte[] assetData) {
		this.assetData = assetData;
	}

	public String getBookassetId() {
		return bookassetId;
	}

	public void setBookassetId(String bookassetId) {
		this.bookassetId = bookassetId;
	}
}
