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
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;

/**
 * OrientBaseDAO.java<br/>
 * Responsibilities:<br/>
 * 1. Used with RequestScoped beans
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 12, 2013
 * 
 */
@RequestScoped
public abstract class OrientBaseTxDAO {
	@Inject
	protected Logger log;

	@Inject
	@OODbTx
	protected OObjectDatabaseTx ooDbTx;

	public OrientBaseTxDAO() {
	}

	/**
	 * Gets user
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public User getUserByUserId(String userId) throws Exception {
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
	}

	/**
	 * Close the connection when the dispose method won't work
	 */
	protected void closeDbConnection() {
		if (ooDbTx != null) {
			ooDbTx.close();
		}
	}

}
