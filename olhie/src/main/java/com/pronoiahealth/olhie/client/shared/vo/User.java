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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;
import com.pronoiahealth.olhie.client.shared.Email;

/**
 * User class
 * 
 * Responsibilities:<br/>
 * 1. Represents a user of the system <br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 25, 2013
 * 
 */
@Bindable
@Portable
public class User {
	@OId
	private String id;

	@OVersion
	private Long opVer;

	@Size(min = 0, max = 25, message = "First name can't be more than 25 characters long")
	@Pattern(regexp = "[A-Za-z ]*", message = "First name must contain only letters and spaces")
	private String firstName;

	@NotNull
	@Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
	@Pattern(regexp = "[A-Za-z ]*", message = "Last Name must contain only letters and spaces")
	private String lastName;

	@NotNull
	@Size(min = 6, max = 20, message = "Must be between 6 and 20 characters")
	private String userId;

	@NotNull
	private boolean resetPwd;

	@NotNull
	@Size(min = 1, max = 20, message = "Must be between 1 and 20 characters")
	private String role;

	@NotNull
	@NotEmpty
	@Email
	private String email;
	
	@Size(max = 75, message="75 chracters allowed.")
	private String organization;
	
	@NotNull
	private boolean requestedAuthor;

	/**
	 * Default Constructor
	 * 
	 */
	public User() {
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isResetPwd() {
		return resetPwd;
	}

	public void setResetPwd(boolean resetPwd) {
		this.resetPwd = resetPwd;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public boolean isRequestedAuthor() {
		return requestedAuthor;
	}

	public void setRequestedAuthor(boolean requestedAuthor) {
		this.requestedAuthor = requestedAuthor;
	}
}
