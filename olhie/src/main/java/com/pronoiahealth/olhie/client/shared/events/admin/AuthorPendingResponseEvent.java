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
package com.pronoiahealth.olhie.client.shared.events.admin;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;

/**
 * AuthorPendingResponse.java<br/>
 * Responsibilities:<br/>
 * 
 * <p>
 * Fired From: AdminService <br/>
 * Observed By: AuthorRequestWidget <br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jan 25, 2014
 * 
 */
@Portable
@Conversational
public class AuthorPendingResponseEvent {

	private List<RegistrationForm> forms;

	/**
	 * Constructor
	 * 
	 */
	public AuthorPendingResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param forms
	 */
	public AuthorPendingResponseEvent(List<RegistrationForm> forms) {
		super();
		this.forms = forms;
	}

	public List<RegistrationForm> getForms() {
		return forms;
	}

	public void setForms(List<RegistrationForm> forms) {
		this.forms = forms;
	}
	
}
