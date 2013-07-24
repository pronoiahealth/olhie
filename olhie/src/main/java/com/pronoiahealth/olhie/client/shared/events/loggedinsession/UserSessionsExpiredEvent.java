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
package com.pronoiahealth.olhie.client.shared.events.loggedinsession;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * UserSessionsExpiredEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired By: <br/>
 * Observed By: <br/>
 * </p>
 * @author John DeStefano
 * @version 1.0
 * @since Jul 23, 2013
 *
 */
@Portable
@Conversational
public class UserSessionsExpiredEvent {
	
	private List<String> erraiSessionIds;

	/**
	 * Constructor
	 *
	 */
	public UserSessionsExpiredEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param erraiSessionIds
	 */
	public UserSessionsExpiredEvent(List<String> erraiSessionIds) {
		super();
		this.erraiSessionIds = erraiSessionIds;
	}

	public List<String> getErraiSessionIds() {
		return erraiSessionIds;
	}

	public void setErraiSessionIds(List<String> erraiSessionIds) {
		this.erraiSessionIds = erraiSessionIds;
	}
}
