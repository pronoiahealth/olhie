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
package com.pronoiahealth.olhie.client.shared.events.errors;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * ServiceError.java<br/>
 * Responsibilities:<br/>
 * 1. Returned by a service when an error is encountered<br/>
 * 
 * <p>
 * Fired From : LoginService, RegistrationDialog class<br/>
 * Observed By: ErrorDialogDisplay class, SearchResultsComponent (removes
 * spinner if its there)<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Portable
@Conversational
public class ServiceErrorEvent {
	String errorMsg;

	/**
	 * Constructor
	 * 
	 */
	public ServiceErrorEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param errorMsg
	 */
	public ServiceErrorEvent(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
