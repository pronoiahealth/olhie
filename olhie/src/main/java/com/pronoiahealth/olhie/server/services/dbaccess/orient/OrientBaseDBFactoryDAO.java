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
package com.pronoiahealth.olhie.server.services.dbaccess.orient;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.orient.OrientFactory;
import com.pronoiahealth.olhie.server.services.LoggedInHandleExpiredSessionsService;

/**
 * OrientBaseDBFactoryDAO.java<br/>
 * Responsibilities:<br/>
 * 1. Used with non-requestscopred beans
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 16, 2013
 * 
 */
public abstract class OrientBaseDBFactoryDAO {

	@Inject
	private Logger log;

	@Inject
	protected OrientFactory oFac;

	/**
	 * Constructor
	 * 
	 */
	public OrientBaseDBFactoryDAO() {
	}

	/**
	 * Gets user
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public User getUserByUserId(String userId) throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
					"select from User where userId = :uId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("uId", userId);
			List<User> uResult = ooDbTx.command(uQuery).execute(uparams);
			User user = null;
			if (uResult != null && uResult.size() == 1) {
				user = uResult.get(0);
			} else {
				throw new Exception("Can't find user.");
			}
			return ooDbTx.detach(user, true);
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Gets a connection
	 * 
	 * @return
	 * @throws Exception
	 */
	protected OObjectDatabaseTx getConnection() throws Exception {
		OObjectDatabaseTx ooDbTx = oFac.getUninjectedConnection();
		if (log.isLoggable(Level.INFO)) {
			log.log(Level.INFO,
					"Aquired connection "
							+ ooDbTx.hashCode()
							+ " for bean "
							+ LoggedInHandleExpiredSessionsService.class
									.getName());
		}

		return ooDbTx;
	}

	/**
	 * Closes the given connection
	 * 
	 * @param ooDbTx
	 */
	protected void closeConnection(OObjectDatabaseTx ooDbTx) {
		if (ooDbTx != null && ooDbTx.isClosed() == false) {
			if (log.isLoggable(Level.INFO)) {
				log.log(Level.INFO, "Released connection " + ooDbTx.hashCode());
			}
			ooDbTx.close();
		}
	}
}
