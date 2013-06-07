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
package com.pronoiahealth.olhie.client.shared.vo;

import javax.inject.Singleton;

import org.jboss.errai.bus.client.api.Local;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;

/**
 * ClientUserToken.java<br/>
 * Responsibilities:<br/>
 * 1. Singleton class which holds user information on client side<br>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Local
@Singleton
public class ClientUserToken {
	private String fullName;
	private boolean loggedIn;
	private String userId;
	private String role;

	/**
	 * Constructor
	 * 
	 */
	public ClientUserToken() {
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void clear() {
		fullName = null;
		userId = null;
		loggedIn = false;
		role = null;
	}

	/**
	 * Check the current users role to see if he/she has at least author rights
	 * 
	 * @return
	 */
	public boolean isRoleAtLeastAuthor() {
		if (role != null) {
			int prec = SecurityRoleEnum.valueOf(role).getPrecedence();
			if (prec >= 3) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
