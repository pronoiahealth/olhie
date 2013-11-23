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

import com.pronoiahealth.olhie.client.shared.Email;

/**
 * RegistrationForm.java<br/>
 * Responsibilities:<br/>
 * 1. Represents a registration request by a user<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 29, 2013
 * 
 */
@Portable
@Bindable
public class RegistrationForm {
	@Size(min = 0, max = 25)
	@Pattern(regexp = "[A-Za-z ]*", message = "First name must contain only letters and spaces")
	private String firstName;

	@NotNull
	@Size(min = 1, max = 50)
	@Pattern(regexp = "[A-Za-z ]*", message = "Last name must contain only letters and spaces")
	private String lastName;

	@NotNull
	@NotEmpty
	@Email
	private String email;

	@NotNull
	@Size(min = 6, max = 20)
	private String userId;

	@NotNull
	@Size(min = 1, max = 32)
	@Pattern(regexp = "^((?=.*[^a-zA-Z])(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).{8,})$", message = "Password must contain one upercase, one lowercase, and one non-alpha, and be at least 8 characters long")
	private String pwd;
	
	@NotNull
	@Size(min = 1, max = 32)
	@Pattern(regexp = "^((?=.*[^a-zA-Z])(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).{8,})$", message = "Password must contain one upercase, one lowercase, and one non-alpha, and be at least 8 characters long")
	private String pwdRepeat;

	/**
	 * Constructor
	 *
	 */
	public RegistrationForm() {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPwdRepeat() {
		return pwdRepeat;
	}

	public void setPwdRepeat(String pwdRepeat) {
		this.pwdRepeat = pwdRepeat;
	}
}
