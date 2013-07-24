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
 * UserLoggoutEvent.java<br/>
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
public class UserLogoutEvent {
	private String userId;
	private String erraiSessionId;

	/**
	 * Constructor
	 *
	 */
	public UserLogoutEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param userId
	 * @param erraiSessionId
	 */
	public UserLogoutEvent(String userId, String erraiSessionId) {
		super();
		this.userId = userId;
		this.erraiSessionId = erraiSessionId;
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

}
