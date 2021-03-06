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
package com.pronoiahealth.olhie.client.shared.events.loginout;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * LogoutRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Portable
@Conversational
public class LogoutRequestEvent {
	private boolean sendResponse;

	/**
	 * Constructor
	 * 
	 * By default set the sendResponse to true
	 *
	 */
	public LogoutRequestEvent() {
		sendResponse = true;
	}

	/**
	 * Constructor
	 *
	 * @param sendResponse
	 */
	public LogoutRequestEvent(boolean sendResponse) {
		super();
		this.sendResponse = sendResponse;
	}

	public boolean isSendResponse() {
		return sendResponse;
	}

	public void setSendResponse(boolean sendResponse) {
		this.sendResponse = sendResponse;
	}

}
