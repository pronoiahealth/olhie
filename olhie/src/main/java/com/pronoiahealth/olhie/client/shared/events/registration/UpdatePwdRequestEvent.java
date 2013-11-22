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
 * UpdatePwdRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 *  <p>
 *  Fired By: UpdatePwdDialog<br/>
 *  Observed By: PasswordService<br/>
 *  </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 *
 */
@Portable
@Conversational
public class UpdatePwdRequestEvent {
	private String newPwd;

	/**
	 * Constructor
	 *
	 */
	public UpdatePwdRequestEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param newPwd
	 */
	public UpdatePwdRequestEvent(String newPwd) {
		super();
		this.newPwd = newPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

}
