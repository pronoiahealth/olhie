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
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * UserOffer.java<br/>
 * Responsibilities:<br/>
 * 1. Information to be displayed when finding a logged in user<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 8, 2013
 * 
 */
@Portable
public class ConnectedUser {
	private String userId;
	private String userName;

	/**
	 * Constructor
	 *
	 * @param userId
	 * @param userName
	 */
	public ConnectedUser(@MapsTo("userId") String userId,
			@MapsTo("userName") String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public String toString() {
		return userName + " (" + userId + ")";
	}
}
