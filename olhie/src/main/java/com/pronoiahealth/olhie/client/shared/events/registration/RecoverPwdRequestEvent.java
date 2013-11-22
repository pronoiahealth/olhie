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
package com.pronoiahealth.olhie.client.shared.events.registration;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * RecoverPwdEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired By:<br/>
 * Observed By:<br/>
 * </p>
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 *
 */
@Portable
@Conversational
public class RecoverPwdRequestEvent {
	
	private String userId;
	private String userEmail;

	/**
	 * Constructor
	 *
	 */
	public RecoverPwdRequestEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param userId
	 * @param userEmail
	 */
	public RecoverPwdRequestEvent(String userId, String userEmail) {
		super();
		this.userId = userId;
		this.userEmail = userEmail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
