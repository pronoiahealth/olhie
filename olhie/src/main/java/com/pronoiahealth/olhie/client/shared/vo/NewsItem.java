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

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * NewsItem.java<br/>
 * Responsibilities:<br/>
 * 1. This is a new item for display on the main screen.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 3, 2013
 * 
 */
@Portable
@Bindable
public class NewsItem {
	@OId
	private String id;

	@OVersion
	private Long version;

	@NotNull
	@Size(min = 1, max = 250, message = "Between 1 and 250 characters.")
	private String title;

	@NotNull
	@Size(min = 1, max = 250, message = "Between 1 and 250 characters.")
	private String href;

	@NotNull
	@Size(min = 1, max = 500, message = "Between 1 and 500 characters.")
	private String story;
	
	@NotNull
	@Size(min = 6, max = 2, message = "Between 6 and 20 characters.")
	private String authorId;

	@NotNull
	private Boolean active;

	@NotNull
	private Date datePublished;

	/**
	 * Constructor
	 * 
	 */
	public NewsItem() {
	}

	/**
	 * Constructor
	 *
	 * @param title
	 * @param href
	 * @param story
	 * @param active
	 * @param datePublished
	 */
	public NewsItem(String title, String href, String story, boolean active,
			Date datePublished, String authorId) {
		super();
		this.title = title;
		this.href = href;
		this.story = story;
		this.active = Boolean.valueOf(active);
		this.datePublished = datePublished;
		this.authorId = authorId;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
}
