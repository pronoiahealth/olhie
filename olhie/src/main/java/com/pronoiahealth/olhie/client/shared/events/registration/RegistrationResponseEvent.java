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
 * RegistrationResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Response from a registration event<br/>
 * 
 * <p>
 * Fired From : RegistrationService class<br/>
 * Observed By: RegisterDialog class<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 29, 2013
 * 
 */
@Portable
@Conversational
public class RegistrationResponseEvent {
	private String msg;

	public RegistrationResponseEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param msg
	 */
	public RegistrationResponseEvent(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
