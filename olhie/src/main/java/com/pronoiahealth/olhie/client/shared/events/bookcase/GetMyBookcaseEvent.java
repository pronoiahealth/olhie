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
package com.pronoiahealth.olhie.client.shared.events.bookcase;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * ShowMyBookcaseEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Event to signal the server to send back the contents of my bookcase.<br/>
 * 
 * <p>
 * Fired By: BookcasePage<br/>
 * Observed By: BookcaseService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 25, 2013
 * 
 */
@Portable
@Conversational
public class GetMyBookcaseEvent {
	private String userId;

	/**
	 * Constructor
	 *
	 */
	public GetMyBookcaseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param userId
	 */
	public GetMyBookcaseEvent(String userId) {
		super();
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
