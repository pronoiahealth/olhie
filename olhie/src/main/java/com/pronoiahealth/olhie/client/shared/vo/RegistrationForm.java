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

import java.util.Date;

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
	@OId
	private String id;

	@OVersion
	private Long opVer;
	
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
	
	@Size(max = 75, message="75 chracters allowed.")
	private String organization;
	
	@NotNull
	private boolean author;
	
	@NotNull
	private Date regDate;
	
	private boolean authorDecision;
	
	private Date authorDecisionDate;
	
	private String adminUserId;
	
	@NotNull
	private boolean acceptedPolicyStatement;
	
	@NotNull
	@Size(min = 1, max = 25)
	private String type;
	
	private String authorStatus;

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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public boolean isAuthor() {
		return author;
	}

	public void setAuthor(boolean author) {
		this.author = author;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public boolean isAuthorDecision() {
		return authorDecision;
	}

	public void setAuthorDecision(boolean authorDecision) {
		this.authorDecision = authorDecision;
	}

	public Date getAuthorDecisionDate() {
		return authorDecisionDate;
	}

	public void setAuthorDecisionDate(Date authorDecisionDate) {
		this.authorDecisionDate = authorDecisionDate;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAcceptedPolicyStatement() {
		return acceptedPolicyStatement;
	}

	public void setAcceptedPolicyStatement(boolean acceptedPolicyStatement) {
		this.acceptedPolicyStatement = acceptedPolicyStatement;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthorStatus() {
		return authorStatus;
	}

	public void setAuthorStatus(String authorStatus) {
		this.authorStatus = authorStatus;
	}
	
}
