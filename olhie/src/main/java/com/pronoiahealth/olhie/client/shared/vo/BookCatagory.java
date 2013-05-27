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
public enum BookCatagory {

	INTERFACE("black"), LEGAL("yellow");
	
	private String color;
	
	/**
	 * Constructor
	 *
	 * @param color
	 */
	BookCatagory(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
}
