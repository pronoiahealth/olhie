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
 * BookCover.java<br/>
 * Responsibilities:<br/>
 * 1. Represents a BookCover<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 6, 2013
 * 
 */
@Portable
@Bindable
public class BookCover {

	@OId
	private String id;

	@OVersion
	private Long version;

	@NotNull
	@Size(min = 4, max = 100, message = "Image URL between 4 and 100 characters.")
	private String imgUrl;
	
	@NotNull
	@Size(min = 4, max = 100, message = "Image URL between 4 and 100 characters.")
	private String customIcon;

	@NotNull
	@Size(min = 1, max = 50, message = "Cover name between 1 and 50 characters.")
	private String coverName;
	
	@NotNull
	@Size(min = 1, max = 50, message = "Cover name between 1 and 50 characters.")
	private String authorTextColor = "#FFFFFF";
	
	@NotNull
	@Size(min = 1, max = 50, message = "Cover name between 1 and 50 characters.")
	private String coverTitleTextColor = "#FFFFFF";

	/**
	 * Constructor
	 * 
	 */
	public BookCover() {
	}

	/**
	 * Constructor
	 *
	 * @param imgUrl
	 * @param customIcon
	 * @param coverName
	 * @param authorTextColor
	 * @param coverTitleTextColor
	 */
	public BookCover(String imgUrl, String customIcon, String coverName,
			String authorTextColor, String coverTitleTextColor) {
		super();
		this.imgUrl = imgUrl;
		this.customIcon = customIcon;
		this.coverName = coverName;
		this.authorTextColor = authorTextColor;
		this.coverTitleTextColor = coverTitleTextColor;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCoverName() {
		return coverName;
	}

	public void setCoverName(String coverName) {
		this.coverName = coverName;
	}

	public String getCustomIcon() {
		return customIcon;
	}

	public void setCustomIcon(String customIcon) {
		this.customIcon = customIcon;
	}

	public String getAuthorTextColor() {
		return authorTextColor;
	}

	public void setAuthorTextColor(String authorTextColor) {
		this.authorTextColor = authorTextColor;
	}

	public String getCoverTitleTextColor() {
		return coverTitleTextColor;
	}

	public void setCoverTitleTextColor(String coverTitleTextColor) {
		this.coverTitleTextColor = coverTitleTextColor;
	}
}
