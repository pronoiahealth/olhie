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
 * RegistrationErrorEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Fire during the registration process.<br/>
 * 
 * <p>
 * Fired From : RegistrationService class<br/>
 * Observed By: RegisterDialog class<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 2, 2013
 *
 */
@Portable
@Conversational
public class RegistrationErrorEvent {
	public enum ErrorTypeEnum {
		USER_ID_ALREADY_EXISTS, PASSWORDS_DONT_MATCH, OTHER
	};

	private ErrorTypeEnum errorType;
	private String msg;

	/**
	 * Constructor
	 *
	 */
	public RegistrationErrorEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param msg
	 * @param errorType
	 */
	public RegistrationErrorEvent(String msg, ErrorTypeEnum errorType) {
		super();
		this.msg = msg;
		this.errorType = errorType;
	}

	public ErrorTypeEnum getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorTypeEnum errorType) {
		this.errorType = errorType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
