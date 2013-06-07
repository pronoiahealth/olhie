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
package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;

/**
 * ShowRegisterModalEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Fired to show the Register modal dialog<br/>
 * 
 * <p>
 * Fired From: Header class <br/>
 * Observed By: RegisterDialog class <br/> 
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Local
public class ShowRegisterModalEvent {

	/**
	 * Constructor
	 *
	 */
	public ShowRegisterModalEvent() {
	}

}
