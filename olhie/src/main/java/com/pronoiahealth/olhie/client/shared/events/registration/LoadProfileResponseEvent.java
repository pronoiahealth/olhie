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
 * LoadProfileModalEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Fired to show the Register modal dialog<br/>
 * 
 * <p>
 * Fired From: Header class <br/>
 * Observed By: RegisterDialog class <br/> 
 * </p>
 *
 * @author Alex Roman
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Portable
@Conversational
public class LoadProfileResponseEvent {

	private RegistrationForm registrationForm;

	/**
	 * Constructor
	 *
	 */
	public LoadProfileResponseEvent() {
		
	}
	
	/**
	 * Constructor
	 *
	 * @param registrationForm
	 */

	public LoadProfileResponseEvent(RegistrationForm registrationForm) {
		this.registrationForm = registrationForm;
	}

	public RegistrationForm getRegistrationForm() {
		return registrationForm;
	}

	public void setRegistrationForm(RegistrationForm registrationForm) {
		this.registrationForm = registrationForm;
	}

}
