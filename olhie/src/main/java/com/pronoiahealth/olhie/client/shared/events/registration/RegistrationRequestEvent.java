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

import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;

/**
 * RegistrationRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Request to register a new user<br/>
 * 
 * <p>
 * Fired From : RgisterDialog class<br/>
 * Observed By: RegistrationService class<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 29, 2013
 *
 */
@Portable
@Conversational
public class RegistrationRequestEvent {
	private RegistrationForm registrationForm;

	/**
	 * Constructor
	 *
	 */
	public RegistrationRequestEvent() {
		
	}
	
	/**
	 * Constructor
	 *
	 * @param registrationForm
	 */
	public RegistrationRequestEvent(RegistrationForm registrationForm) {
		this.registrationForm = registrationForm;
	}

	public RegistrationForm getRegistrationForm() {
		return registrationForm;
	}

	public void setRegistrationForm(RegistrationForm registrationForm) {
		this.registrationForm = registrationForm;
	}
}
