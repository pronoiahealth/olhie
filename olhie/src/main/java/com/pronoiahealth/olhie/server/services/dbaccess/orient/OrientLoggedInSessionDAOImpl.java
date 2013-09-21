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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.LoggedInSession;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.orient.OrientFactory;
import com.pronoiahealth.olhie.server.services.dbaccess.LoggedInSessionDAO;

/**
 * OrientLoggedInSessionDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 13, 2013
 * 
 */
public class OrientLoggedInSessionDAOImpl extends OrientBaseDBFactoryDAO
		implements LoggedInSessionDAO {

	@Inject
	private Logger log;

	@Inject
	private OrientFactory oFac;

	/**
	 * Constructor
	 * 
	 */
	public OrientLoggedInSessionDAOImpl() {
	}

	/**
	 * Adds a new session to the database. This would happen on login.
	 * 
	 * @param userId
	 * @param erraiSessionId
	 * @param userFirstName
	 * @param userLastName
	 * @param startDateDT
	 * @param ooDbTx
	 * @param handleTransaction
	 * @return
	 */
	@Override
	public LoggedInSession addSession(String userId, String erraiSessionId)
			throws Exception {
		// Set the date to now
		Date now = new Date();

		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();

			// Find the user
			User user = getUserByUserId(userId);

			// Update the persistence store
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			LoggedInSession com = new LoggedInSession();
			com.setErraiSessionId(erraiSessionId);
			com.setSessionStartDT(now);
			com.setUserId(userId);
			com.setLookupName(createLookupName(userId, user.getFirstName(),
					user.getLastName()));
			com.setActive(true);
			com = ooDbTx.save(com);
			ooDbTx.commit();
			com = ooDbTx.detach(com, true);

			// Return the loggedInSession object
			return com;
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * End all the open sessions for a user
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @param handleTransaction
	 * @throws Exception
	 */
	@Override
	public void endAllActiveSessionsByUserId(String userId) throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			List<LoggedInSession> sesses = getSessionByUserId(userId, true);
			if (sesses != null) {
				Date now = new Date();
				for (LoggedInSession sess : sesses) {
					sess.setSessionEndDT(now);
					sess.setActive(false);
					ooDbTx.save(sess);
				}
			}
			ooDbTx.commit();
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Returns null if session was not found or the updated LoggedInSession if
	 * it was found.
	 * 
	 * @param erraiSessionId
	 * @param ooDbTx
	 * @param handleTransaction
	 * @return
	 * @throws Exception
	 */
	@Override
	public void endActiveSessionsByErraiSessionId(String erraiSessionId)
			throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();

			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			List<LoggedInSession> sesses = getSessionByErraiSessionId(
					erraiSessionId, true);
			// There should only be one as the erraiSessionId is unique
			if (sesses != null) {
				for (LoggedInSession sess : sesses) {
					sess.setSessionEndDT(new Date());
					sess.setActive(false);
					sess = ooDbTx.save(sess);
				}
			}
			ooDbTx.commit();
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Returns only active sessions of all depending on value of active.
	 * 
	 * @param userId
	 * @param active
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<LoggedInSession> getSessionByUserId(String userId,
			boolean active) throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();

			OSQLSynchQuery<LoggedInSession> baQuery = null;
			if (active == true) {
				baQuery = new OSQLSynchQuery<LoggedInSession>(
						"select from LoggedInSession where userId = :uId and active = true");
			} else {
				baQuery = new OSQLSynchQuery<LoggedInSession>(
						"select from LoggedInSession where userId = :uId");
			}
			HashMap<String, String> baparams = new HashMap<String, String>();
			baparams.put("uId", userId);
			List<LoggedInSession> baResult = ooDbTx.command(baQuery).execute(
					baparams);
			return baResult;
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Active session id's for a given userId
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public Set<String> getSessionIdsForACtiveSessionsByUserId(String userId)
			throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();

			Set<String> retLst = new TreeSet<String>();
			List<LoggedInSession> lst = getSessionByUserId(userId, true);
			if (lst != null && lst.size() > 0) {
				for (LoggedInSession sess : lst) {
					retLst.add(sess.getErraiSessionId());
				}
			}
			return retLst;
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Returns only active sessions of all depending on value of active.
	 * 
	 * @param userId
	 * @param active
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<LoggedInSession> getSessionByErraiSessionId(
			String erraiSessionId, boolean active) throws Exception {

		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			OSQLSynchQuery<LoggedInSession> baQuery = null;
			if (active == true) {
				baQuery = new OSQLSynchQuery<LoggedInSession>(
						"select from LoggedInSession where erraiSessionId = :sId and active = true");
			} else {
				baQuery = new OSQLSynchQuery<LoggedInSession>(
						"select from LoggedInSession where erraiSessionId = :sId");
			}
			HashMap<String, String> baparams = new HashMap<String, String>();
			baparams.put("sId", erraiSessionId);
			List<LoggedInSession> baResult = ooDbTx.command(baQuery).execute(
					baparams);
			return baResult;
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Get a list of logged in users. Exclude the user sending the request.
	 * 
	 * @param qry
	 * @param currentUserId
	 * @param ooDbTx
	 * @return
	 */
	@Override
	public List<LoggedInSession> getActiveSessionsByLookupNameQry(String qry,
			String currentUserId) throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			OSQLSynchQuery<LoggedInSession> baQuery = null;
			baQuery = new OSQLSynchQuery<LoggedInSession>(
					"select from LoggedInSession where lookupName.toLowerCase() like :qry and active = true and userId <> :uId");
			HashMap<String, String> baparams = new HashMap<String, String>();
			baparams.put("uId", currentUserId);
			baparams.put("qry", qry.toLowerCase() + "%");
			List<LoggedInSession> baResult = ooDbTx.command(baQuery).execute(
					baparams);
			return baResult;
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Create a name to look up. Handy for creating a list of logged in users
	 * from a name search
	 * 
	 * @param userId
	 * @param userFirstName
	 * @param userLastName
	 * @return
	 */
	private static String createLookupName(String userId, String userFirstName,
			String userLastName) {
		StringBuilder sb = new StringBuilder();
		return sb.append(userFirstName).append(" ").append(userLastName)
				.append(" (").append(userId).append(")").toString();
	}

}
