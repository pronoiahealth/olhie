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
package com.pronoiahealth.olhie.server.security;

import java.io.Serializable;

/**
 * Salted Password
 * 
 * @author John DeStefano
 * @version 1.0
 * @since 7/17/2011
 *
 */
public class SaltedPassword implements Serializable {
	private static final long serialVersionUID = 1604659001872648544L;
	private String pwdDigest;
	private String salt;
	
	/**
	 * Constructor
	 *
	 * @param pwdDigest
	 * @param salt
	 */
	public SaltedPassword(String pwdDigest, String salt) {
		super();
		this.pwdDigest = pwdDigest;
		this.salt = salt;
	}

	/**
	 * Constructor
	 *
	 */
	public SaltedPassword() {
	}

	public String getPwdDigest() {
		return pwdDigest;
	}

	public void setPwdDigest(String pwdDigest) {
		this.pwdDigest = pwdDigest;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
