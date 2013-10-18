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

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.vo.Password;

/**
 * UserDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 12, 2013
 * 
 */
public interface UserDAO extends BaseDAO {

	/**
	 * Gets the user by there Id. Returns a fully populated (detached) instance.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public User getUserByUserId(String userId) throws Exception;

	/**
	 * Checks the user by there Id. Returns true if id in use or false
	 * otherwise.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean checkUserByUserId(String userId);

	/**
	 * Returns a fully populated (detached) instance.
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Password getPwdByUserId(String userId) throws Exception;

	/**
	 * Add a user
	 * 
	 * @param userId
	 * @param lastName
	 * @param firstName
	 * @param role
	 * @param email
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public void addUser(String userId, String lastName, String firstName,
			SecurityRoleEnum role, String email, String pwd) throws Exception;

	/**
	 * Update the user
	 * 
	 * @param userId
	 * @param lastName
	 * @param firstName
	 * @param role
	 * @param email
	 * @throws Exception
	 */
	public void updateUser(String userId, String lastName, String firstName,
			SecurityRoleEnum role, String email) throws Exception;
}
