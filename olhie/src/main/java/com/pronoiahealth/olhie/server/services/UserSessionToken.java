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
package com.pronoiahealth.olhie.server.services;


/**
 * UserSessionToken.java<br/>
 * Responsibilities:<br/>
 * 1. Holds a logged in user session info.<br/>
 * 2. Tracks the timeout of a user.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 1, 2013
 * 
 */
public class UserSessionToken {
	/**
	 * After 60 seconds with no LoggedInPingEvent from the user we assume the
	 * user has closed the browser.
	 */
	// TODO: Make this a configurable variable
	private final static long TIMEOUT = 1000 * 60;
	private String userId;
	private String erraiSessionId;
	private String userLastName;
	private String userFirstName;
	private long lastActivity = System.currentTimeMillis();

	/**
	 * Constructor
	 * 
	 */
	public UserSessionToken() {
	}

	/**
	 * Constructor
	 * 
	 * @param userId
	 * @param erraiSessionId
	 */
	public UserSessionToken(String userId, String userLastName,
			String userFirstName, String erraiSessionId) {
		super();
		this.userId = userId;
		this.erraiSessionId = erraiSessionId;
		this.userLastName = userLastName;
		this.userFirstName = userFirstName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getErraiSessionId() {
		return erraiSessionId;
	}

	public void setErraiSessionId(String erraiSessionId) {
		this.erraiSessionId = erraiSessionId;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public void activity() {
		lastActivity = System.currentTimeMillis();
	}

	public boolean isSessionTimedout() {
		return (System.currentTimeMillis() - lastActivity) > TIMEOUT;
	}
}
