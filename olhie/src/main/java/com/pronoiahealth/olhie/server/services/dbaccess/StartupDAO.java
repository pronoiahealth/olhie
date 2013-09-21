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

import java.util.List;

import com.pronoiahealth.olhie.client.shared.vo.LoggedInSession;

/**
 * StartupDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Sep 15, 2013
 *
 */
public interface StartupDAO {
	
	/**
	 * Called when loading application to clear any active records
	 * 
	 * @param ooDbTx
	 * @param handleTransaction
	 * @throws Exception
	 */
	public void inactivateAllActive() throws Exception;
	
	
	/**
	 * Get all rows that are active
	 * 
	 * @param ooDbTx
	 * @return
	 */
	public List<LoggedInSession> getAllActiveSessions() throws Exception;
	
	

}
