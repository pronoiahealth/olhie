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
package com.pronoiahealth.olhie.server.security;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

/**
 * AppUserProducer.java<br/>
 * Responsibilities:<br/>
 * 1. Produces the session scoped AppServerUser object<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public class AppUserProducer {

	public AppUserProducer() {
	}
	
	@Produces
	@SessionScoped
	@AppServerUser
	public ServerUserToken getCurrentUser(ServerUserToken user) {
		return user;
	}
}
