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
import javax.validation.constraints.Size;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * LoggedInSession.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 23, 2013
 * 
 */
@Portable
public class LoggedInSession {
	@OId
	private String id;

	@OVersion
	private Long version;

	@NotNull
	@Size(min = 6, max = 20, message = "Must be between 6 and 20 characters")
	private String userId;

	@NotNull
	@Size(min = 1, max = 255, message = "Must be between 1 and 255 characters")
	private String lookupName;

	@NotNull
	@Size(min = 1, max = 255, message = "Must be between 1 and 255 characters")
	private String erraiSessionId;

	@NotNull
	private Date sessionStartDT;

	private Date sessionEndDT;
	
	@NotNull
	private boolean active;

	/**
	 * Constructor
	 * 
	 */
	public LoggedInSession() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLookupName() {
		return lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getErraiSessionId() {
		return erraiSessionId;
	}

	public void setErraiSessionId(String erraiSessionId) {
		this.erraiSessionId = erraiSessionId;
	}

	public Date getSessionStartDT() {
		return sessionStartDT;
	}

	public void setSessionStartDT(Date sessionStartDT) {
		this.sessionStartDT = sessionStartDT;
	}

	public Date getSessionEndDT() {
		return sessionEndDT;
	}

	public void setSessionEndDT(Date sessionEndDT) {
		this.sessionEndDT = sessionEndDT;
	}

	public String getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
