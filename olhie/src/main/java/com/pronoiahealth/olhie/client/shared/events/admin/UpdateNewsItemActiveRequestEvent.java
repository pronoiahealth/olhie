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
package com.pronoiahealth.olhie.client.shared.events.admin;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * UpdateNewsItemActiveRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 
 * <p>
 * Fired From: NewsItemWidget <br/>
 * Observed By: AdminService <br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jan 31, 2014
 *
 */
@Portable
@Conversational
public class UpdateNewsItemActiveRequestEvent {
	private boolean active;
	private String newsItemId;

	/**
	 * Constructor
	 *
	 */
	public UpdateNewsItemActiveRequestEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param active
	 */
	public UpdateNewsItemActiveRequestEvent(String newsItemId, boolean active) {
		super();
		this.newsItemId = newsItemId;
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getNewsItemId() {
		return newsItemId;
	}

	public void setNewsItemId(String newsItemId) {
		this.newsItemId = newsItemId;
	}
}
