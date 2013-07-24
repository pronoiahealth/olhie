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
package com.pronoiahealth.olhie.client.shared.events.loggedinsession;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * UserLogginEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired By: <br/>
 * Observed By: <br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 23, 2013
 *
 */
@Portable
@Conversational
public class UserLogginEvent {
	private String erraiSessionId;
	private String userId;
	private String userFirstName;
	private String userLastName;

	/**
	 * Constructor
	 *
	 */
	public UserLogginEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param erraiSessionId
	 * @param userId
	 * @param userFirstName
	 * @param userLastName
	 */
	public UserLogginEvent(String erraiSessionId, String userId,
			String userFirstName, String userLastName) {
		super();
		this.erraiSessionId = erraiSessionId;
		this.userId = userId;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
	}

	public String getErraiSessionId() {
		return erraiSessionId;
	}

	public void setErraiSessionId(String erraiSessionId) {
		this.erraiSessionId = erraiSessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
}
