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
import org.jboss.errai.common.client.api.annotations.NonPortable;

/**
 * ShowFindLoggedInUserEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Event to show the LoggedInUserDialog<br/>
 * 
 * <p>
 * Fired By: Header.class<br/>
 * Observed By: EventCalendarRequestDialog<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 9, 2013
 *
 */
@Local
@NonPortable
public class ShowEventCalendarRequestDialogEvent {

	/**
	 * Constructor
	 *
	 */
	public ShowEventCalendarRequestDialogEvent() {
	}
}
