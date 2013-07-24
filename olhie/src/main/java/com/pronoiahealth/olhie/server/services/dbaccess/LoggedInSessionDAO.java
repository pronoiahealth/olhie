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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.LoggedInSession;

/**
 * LoggedInSessionDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 23, 2013
 * 
 */
public class LoggedInSessionDAO {

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
	public static LoggedInSession addSession(String userId,
			String erraiSessionId, String userFirstName, String userLastName,
			Date startDateDT, OObjectDatabaseTx ooDbTx,
			boolean handleTransaction) {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		LoggedInSession com = new LoggedInSession();
		com.setErraiSessionId(erraiSessionId);
		com.setSessionStartDT(startDateDT);
		com.setUserId(userId);
		com.setLookupName(createLookupName(userId, userFirstName, userLastName));
		com.setActive(true);
		com = ooDbTx.save(com);

		if (handleTransaction == true) {
			ooDbTx.commit();
			com = ooDbTx.detach(com, true);
		}

		return com;
	}

	/**
	 * End all the open sessions for a user
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @param handleTransaction
	 * @throws Exception
	 */
	public static void endAllActiveSessionsByUserId(String userId,
			OObjectDatabaseTx ooDbTx, boolean handleTransaction)
			throws Exception {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		List<LoggedInSession> sesses = getSessionByUserId(userId, true, ooDbTx);
		if (sesses != null) {
			Date now = new Date();
			for (LoggedInSession sess : sesses) {
				sess.setSessionEndDT(now);
				sess.setActive(false);
				ooDbTx.save(sess);
			}
		}

		if (handleTransaction == true) {
			ooDbTx.commit();
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
	public static void endActiveSessionsByErraiSessionId(String erraiSessionId,
			OObjectDatabaseTx ooDbTx, boolean handleTransaction)
			throws Exception {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		List<LoggedInSession> sesses = getSessionByErraiSessionId(
				erraiSessionId, true, ooDbTx);
		// There should only be one as the erraiSessionId is unique
		if (sesses != null) {
			for (LoggedInSession sess : sesses) {
				sess.setSessionEndDT(new Date());
				sess.setActive(false);
				sess = ooDbTx.save(sess);
			}
		}

		if (handleTransaction == true) {
			ooDbTx.commit();
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
	public static List<LoggedInSession> getSessionByUserId(String userId,
			boolean active, OObjectDatabaseTx ooDbTx) throws Exception {
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
	}

	/**
	 * Active session id's for a given userId
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getSessionIdsForACtiveSessionsByUserId(
			String userId, OObjectDatabaseTx ooDbTx) throws Exception {
		Set<String> retLst = new TreeSet<String>();
		List<LoggedInSession> lst = getSessionByUserId(userId, true, ooDbTx);
		if (lst != null && lst.size() > 0) {
			for (LoggedInSession sess : lst) {
				retLst.add(sess.getErraiSessionId());
			}
		}
		return retLst;
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
	public static List<LoggedInSession> getSessionByErraiSessionId(
			String erraiSessionId, boolean active, OObjectDatabaseTx ooDbTx)
			throws Exception {
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
	}

	/**
	 * Get a list of logged in users. Exclude the user sending the request.
	 * 
	 * @param qry
	 * @param currentUserId
	 * @param ooDbTx
	 * @return
	 */
	public static List<LoggedInSession> getActiveSessionsByLookupNameQry(
			String qry, String currentUserId, OObjectDatabaseTx ooDbTx) {
		OSQLSynchQuery<LoggedInSession> baQuery = null;
		baQuery = new OSQLSynchQuery<LoggedInSession>(
				"select from LoggedInSession where lookupName.toLowerCase() like :qry and active = true and userId <> :uId");
		HashMap<String, String> baparams = new HashMap<String, String>();
		baparams.put("uId", currentUserId);
		baparams.put("qry", qry.toLowerCase() + "%");
		List<LoggedInSession> baResult = ooDbTx.command(baQuery).execute(
				baparams);
		return baResult;
	}

	/**
	 * Get all rows that are active
	 * 
	 * @param ooDbTx
	 * @return
	 */
	public static List<LoggedInSession> getAllActiveSessions(
			OObjectDatabaseTx ooDbTx) throws Exception {
		OSQLSynchQuery<LoggedInSession> baQuery = null;
		baQuery = new OSQLSynchQuery<LoggedInSession>(
				"select from LoggedInSession where active = true");
		HashMap<String, String> baparams = new HashMap<String, String>();
		List<LoggedInSession> baResult = ooDbTx.command(baQuery).execute(
				baparams);
		return baResult;
	}

	/**
	 * Called when loading application to clear any active records
	 * 
	 * @param ooDbTx
	 * @param handleTransaction
	 * @throws Exception
	 */
	public static void inactivateAllActive(OObjectDatabaseTx ooDbTx,
			boolean handleTransaction) throws Exception {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		List<LoggedInSession> sesses = getAllActiveSessions(ooDbTx);
		if (sesses != null && sesses.size() > 0) {
			for (LoggedInSession sess : sesses) {
				sess.setActive(false);
				ooDbTx.save(sess);
			}
		}

		if (handleTransaction == true) {
			ooDbTx.commit();
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
