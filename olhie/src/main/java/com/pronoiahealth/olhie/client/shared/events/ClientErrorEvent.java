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
package com.pronoiahealth.olhie.client.shared.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * ClientErrorEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Signifies an error on the client side.<br/>
 * 
 * <p>
 * Fired From : MainPage class - see bus error handler.<br/>
 * Observed By: ErrorDisplayDialog class<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 5, 2013
 *
 */
@Portable
@Conversational
public class ClientErrorEvent {
	private String message;

	/**
	 * Constructor
	 *
	 */
	public ClientErrorEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param message
	 */
	public ClientErrorEvent(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
