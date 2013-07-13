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
 * ClientLogoutRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Signals the client side of the application should be configured for a new login.<br/>
 * 
 * <p>
 * Fired By: Header class<br/>
 * Observed By: MainPage class<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 12, 2013
 *
 */
@Local
@NonPortable
public class ClientLogoutRequestEvent {

	/**
	 * Constructor
	 *
	 */
	public ClientLogoutRequestEvent() {
	}

}
