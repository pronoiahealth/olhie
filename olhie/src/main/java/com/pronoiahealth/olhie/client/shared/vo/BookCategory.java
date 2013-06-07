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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * BookCatagory.java<br/>
 * Responsibilities:<br/>
 * 1. The categories correspond to the color of a books binder<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Portable
@Bindable
public class BookCategory {
	
	@OId
	private String id;
	
	@OVersion
	private Long version;
	
	@NotNull
	@Size(min=4, max=10, message="Color string must be between 4 and 10 characters.")
	private String color;
	
	@NotNull
	@Size(min=1, max=25, message="Catagory must be between 1 and 25 characters.")
	private String catagory;
	
	/**
	 * Constructor
	 *
	 */
	public BookCategory() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param color
	 */
	public BookCategory(String color, String catagory) {
		this.color = color;
		this.catagory = catagory;
	}

	public String getColor() {
		return color;
	}

	public String getCatagory() {
		return catagory;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}
}
