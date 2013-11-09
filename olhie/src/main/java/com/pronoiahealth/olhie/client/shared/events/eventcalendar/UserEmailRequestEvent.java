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
package com.pronoiahealth.olhie.client.shared.events.eventcalendar;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * UserEmailRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired From : EventCalendarRequestDialog<br/>
 * Observed By: EventCalendarService<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 8, 2013
 *
 */
@Portable
@Conversational
public class UserEmailRequestEvent {

	/**
	 * Constructor
	 *
	 */
	public UserEmailRequestEvent() {
	}

}
