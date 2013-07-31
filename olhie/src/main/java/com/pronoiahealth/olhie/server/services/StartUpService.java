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
package com.pronoiahealth.olhie.server.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.dataaccess.orient.OrientFactory;
import com.pronoiahealth.olhie.server.services.dbaccess.LoggedInSessionDAO;

/**
 * StartUpService.java<br/>
 * Responsibilities:<br/>
 * 1. Startup clean-up in case server crashes or on redeploy or restart
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 23, 2013
 * 
 */
@Singleton
@Startup
public class StartUpService {

	@Inject
	private Logger log;

	@Inject
	private OrientFactory oFac;

	/**
	 * Constructor
	 * 
	 */
	public StartUpService() {
	}

	/**
	 * In-activate all active rows in the LoggedInSession entity
	 */
	@PostConstruct
	protected void postConstruct() {
		OObjectDatabaseTx ooDbTx = oFac.getUninjectedConnection();
		// if (log.isLoggable(Level.FINEST)) {
		log.log(Level.INFO, "Aquired connection " + ooDbTx.hashCode()
				+ " for bean " + StartUpService.class.getName());
		// }
		try {
			LoggedInSessionDAO.inactivateAllActive(ooDbTx, true);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
		} finally {
			// Explicitly close this connection as it will not be released
			// through CDI
			// if (log.isLoggable(Level.FINEST)) {
			log.log(Level.INFO, "Released connection " + ooDbTx.hashCode());
			// }

			ooDbTx.close();
		}
	}

}
