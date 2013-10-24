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

import java.util.ArrayList;
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

	/**
	 * Because of how Orient proxies returned POJO's they must be "detached" and
	 * a non proxy instance returned. The JSON transport is not capable of
	 * marshalling the proxied instances.
	 * 
	 * 
	 * @param proxyLst
	 * @return - Will return the unproxyed list of an empty list
	 */
	public <T> List<T> createDetachedRetLst(List<T> proxyLst) {
		List<T> uLst = new ArrayList<T>();
		if (proxyLst != null) {
			for (T u : proxyLst) {
				T dU = detachObject(u);
				uLst.add(dU);
			}
		}
		return uLst;
	}

	/**
	 * Detachs User object from Orient. Returns a nonProxyed instance
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T detachObject(T obj) {
		return (T) ooDbTx.detachAll(obj, true);
	}

}
