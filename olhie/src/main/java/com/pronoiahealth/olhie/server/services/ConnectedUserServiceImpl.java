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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.exceptions.GenericRPCException;
import com.pronoiahealth.olhie.client.shared.services.ConnectedUserService;
import com.pronoiahealth.olhie.client.shared.vo.ConnectedUser;
import com.pronoiahealth.olhie.client.shared.vo.LoggedInSession;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.LoggedInSessionDAO;

/**
 * ConnectedUserService.java<br/>
 * Responsibilities:<br/>
 * 1. Return a list of connected users based on the query sent<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 8, 2013
 * 
 */
@Service
@RequestScoped
public class ConnectedUserServiceImpl implements ConnectedUserService {
	@Inject
	private SessionTracker sessionTracker;

	@Inject
	private ServerUserToken userToken;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public ConnectedUserServiceImpl() {
	}

	/**
	 * Returns a list of connected users who's name starts with the query string
	 * 
	 * @param qry
	 * @return
	 */
	@Override
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED })
	public List<ConnectedUser> getConnectedUsers(String qry) {
		try {
			List<ConnectedUser> retLst = new ArrayList<ConnectedUser>();
			List<LoggedInSession> sesses = LoggedInSessionDAO
					.getActiveSessionsByLookupNameQry(qry,
							userToken.getUserId(), ooDbTx);

			if (sesses != null && sesses.size() > 0) {
				for (LoggedInSession sess : sesses) {
					ConnectedUser user = new ConnectedUser(sess.getUserId(),
							sess.getLookupName());
					retLst.add(user);
				}
			}

			return retLst;
		} catch (Exception e) {
			throw new GenericRPCException(e.getMessage());
		}
	}
}
