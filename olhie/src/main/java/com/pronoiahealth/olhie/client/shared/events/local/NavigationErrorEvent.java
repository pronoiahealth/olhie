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
 * NavigationErrorEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Thrown when a navigation error occurs (maybe invalid security)<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 27, 2013
 *
 */
@Local
public class NavigationErrorEvent {
	String reason;

	/**
	 * Constructor
	 *
	 */
	public NavigationErrorEvent() {
	}
	
	/**
	 * Constructor
	 *
	 * @param reason
	 */
	public NavigationErrorEvent(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
