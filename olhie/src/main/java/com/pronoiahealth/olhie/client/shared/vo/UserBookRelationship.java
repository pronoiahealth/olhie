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

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * UserBookRelationship.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 18, 2013
 *
 */
@Portable
public class UserBookRelationship {
	@OId
	private String id;
	
	@OVersion
	private String version;
	
	@NotNull
	private String userId;
	
	@NotNull
	private String bookId;
	
	@NotNull
	@Size(min=5, max=25)
	private String userRelationship;
	
	@NotNull
	private boolean activeRelationship;

	/**
	 * Constructor
	 *
	 */
	public UserBookRelationship() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getUserRelationship() {
		return userRelationship;
	}

	public void setUserRelationship(String userRelationship) {
		this.userRelationship = userRelationship;
	}

	public boolean isActiveRelationship() {
		return activeRelationship;
	}

	public void setActiveRelationship(boolean activeRelationship) {
		this.activeRelationship = activeRelationship;
	}

	public String getId() {
		return id;
	}
}
