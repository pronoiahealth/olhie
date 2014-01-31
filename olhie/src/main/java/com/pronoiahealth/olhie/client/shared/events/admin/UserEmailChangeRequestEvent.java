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
 * UserEmailChangeRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 
 * <p>
 * Fired From: UserManagemntWidget <br/>
 * Observed By: AdminService <br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jan 30, 2014
 *
 */
@Portable
@Conversational
public class UserEmailChangeRequestEvent {
	private String email;
	private String userId;

	/**
	 * Constructor
	 *
	 */
	public UserEmailChangeRequestEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param eMail
	 */
	public UserEmailChangeRequestEvent(String email, String userId) {
		super();
		this.email = email;
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
