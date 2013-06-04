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
public class NewsItem {
	private String id;
	private String title;
	private String href;
	private String story;

	/**
	 * Constructor
	 *
	 */
	public NewsItem() {
	}

	public NewsItem(String id, String title, String href, String story) {
		super();
		this.id = id;
		this.title = title;
		this.href = href;
		this.story = story;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
