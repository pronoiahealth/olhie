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
import java.util.List;
import java.util.Set;

import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.vo.LoggedInSession;
import com.pronoiahealth.olhie.client.shared.vo.Offer;

/**
 * LoggedInSessionDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 13, 2013
 * 
 */
public interface LoggedInSessionDAO {

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
	public LoggedInSession addSession(String userId, String erraiSessionId) throws Exception ;

	/**
	 * End all the open sessions for a user
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @param handleTransaction
	 * @throws Exception
	 */
	public void endAllActiveSessionsByUserId(String userId) throws Exception;

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
	public void endActiveSessionsByErraiSessionId(String erraiSessionId) throws Exception;

	/**
	 * Returns only active sessions of all depending on value of active.
	 * 
	 * @param userId
	 * @param active
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public List<LoggedInSession> getSessionByUserId(String userId,
			boolean active) throws Exception;

	/**
	 * Active session id's for a given userId
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public Set<String> getSessionIdsForACtiveSessionsByUserId(String userId)
			throws Exception;

	/**
	 * Returns only active sessions of all depending on value of active.
	 * 
	 * @param userId
	 * @param active
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public List<LoggedInSession> getSessionByErraiSessionId(
			String erraiSessionId, boolean active) throws Exception;

	/**
	 * Get a list of logged in users. Exclude the user sending the request.
	 * 
	 * @param qry
	 * @param currentUserId
	 * @param ooDbTx
	 * @return
	 */
	public List<LoggedInSession> getActiveSessionsByLookupNameQry(String qry,
			String currentUserId)  throws Exception;
}
