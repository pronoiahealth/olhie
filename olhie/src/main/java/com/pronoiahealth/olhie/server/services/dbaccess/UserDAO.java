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
package com.pronoiahealth.olhie.server.services.dbaccess;

import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.User;

/**
 * UserDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 11, 2013
 *
 */
public class UserDAO {

	/**
	 * Gets the user by there Id
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public static User getUserByUserId(String userId, OObjectDatabaseTx ooDbTx)
			throws Exception {
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
		return user;
	}

}
