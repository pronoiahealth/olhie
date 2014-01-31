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
import java.util.logging.Level;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.vo.Password;
import com.pronoiahealth.olhie.server.security.SaltedPassword;
import com.pronoiahealth.olhie.server.security.SecurityUtils;
import com.pronoiahealth.olhie.server.services.dbaccess.UserDAO;

/**
 * OrientUserDAOImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 12, 2013
 * 
 */
public class OrientUserDAOImpl extends OrientBaseTxDAO implements UserDAO {

	/**
	 * Constructor
	 * 
	 */
	public OrientUserDAOImpl() {
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#getPwdByUserId(java.lang.String)
	 */
	@Override
	public Password getPwdByUserId(String userId) throws Exception {
		OSQLSynchQuery<Password> query = new OSQLSynchQuery<Password>(
				"select from Password where userId = :uId");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("uId", userId);
		List<Password> pResult = ooDbTx.command(query).execute(params);
		if (pResult != null && pResult.size() == 1) {
			return ooDbTx.detach(pResult.get(0), true);
		} else {
			throw new Exception("Can't find password for userId");
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#checkUserByUserId(java.lang.String)
	 */
	@Override
	public boolean checkUserByUserId(String userId) {
		try {
			getUserByUserId(userId);
			return true;
		} catch (Exception e) {
			if (log.isLoggable(Level.FINEST) == true) {
				log.log(Level.FINEST, "Exception in getting user.", e);
			}
			return false;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#addUser(java.lang.String,
	 *      java.lang.String, java.lang.String,
	 *      com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void addUser(String userId, String lastName, String firstName,
			SecurityRoleEnum role, String email, String pwd,
			String organization, boolean requestAuthor) throws Exception {
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			// Create a User object and add it to the database
			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setRole(role.getName());
			user.setEmail(email);
			user.setUserId(userId);
			user.setOrganization(organization);
			user.setRequestedAuthor(requestAuthor);
			ooDbTx.save(user);

			// Password
			Password password = new Password();
			SaltedPassword passSalt = SecurityUtils
					.genSaltedPasswordAndSalt(pwd);
			password.setPwdDigest(passSalt.getPwdDigest());
			password.setPwdSalt(passSalt.getSalt());
			password.setUserId(userId);
			ooDbTx.save(password);

			// Now commit
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#updateUser(java.lang.String,
	 *      java.lang.String, java.lang.String,
	 *      com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum,
	 *      java.lang.String)
	 */
	@Override
	public void updateUser(String userId, String lastName, String firstName,
			SecurityRoleEnum role, String email, String organization,
			boolean requestAuthor) throws Exception {
		// Look up user
		User user = getUserByUserId(userId);
		if (user == null) {
			throw new Exception("Unknown user with id " + userId);
		}

		// Save the new data
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setRole(role.getName());
			user.setEmail(email);
			user.setOrganization(organization);

			// if the user has already requested to be an author then don't
			// change
			if (user.isRequestedAuthor() == false) {
				user.setRequestedAuthor(requestAuthor);
			}
			ooDbTx.save(user);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * 
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#resetUserPwd(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void resetUserPwd(String userId, String newPwd) throws Exception {

		// Get the detached password from the databse
		Password pwd = this.getPwdByUserId(userId);

		// Save the new data
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			SaltedPassword passSalt = SecurityUtils
					.genSaltedPasswordAndSalt(newPwd);
			pwd.setPwdDigest(passSalt.getPwdDigest());
			pwd.setPwdSalt(passSalt.getSalt());
			ooDbTx.save(pwd);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#updateUserRole(java.lang.String,
	 *      com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum)
	 */
	@Override
	public void updateUserRole(String userId, SecurityRoleEnum role)
			throws Exception {
		// Look up user
		User user = getUserByUserId(userId);
		if (user == null) {
			throw new Exception("Unknown user with id " + userId);
		}

		// Save the new data
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			user.setRole(role.toString());
			ooDbTx.save(user);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	@Override
	public void updateUserResetPw(String userId, boolean reset)
			throws Exception {
		// Look up user
		User user = getUserByUserId(userId);
		if (user == null) {
			throw new Exception("Unknown user with id " + userId);
		}

		// Save the new data
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			user.setResetPwd(reset);
			ooDbTx.save(user);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#updateUserEmail(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void updateUserEmail(String userId, String eMail) throws Exception {
		// Look up user
		User user = getUserByUserId(userId);
		if (user == null) {
			throw new Exception("Unknown user with id " + userId);
		}

		// Save the new data
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			user.setEmail(eMail);
			ooDbTx.save(user);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}

	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#updateOrganization(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void updateOrganization(String userId, String organization)
			throws Exception {
		// Look up user
		User user = getUserByUserId(userId);
		if (user == null) {
			throw new Exception("Unknown user with id " + userId);
		}

		// Save the new data
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			user.setOrganization(organization);
			ooDbTx.save(user);
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * 
	 * 
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserDAO#findUserByLastName(java.lang.String)
	 */
	@Override
	public List<User> findUserByLastName(String lastName, boolean detach)
			throws Exception {
		if (lastName != null) {
			String srchStr = lastName.toLowerCase();
			OSQLSynchQuery<User> query = new OSQLSynchQuery<User>(
					"select from User where lastName.toLowerCase() like :ln");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ln", srchStr + "%");
			List<User> pResult = ooDbTx.command(query).execute(params);

			if (pResult == null) {
				pResult = new ArrayList<User>();
			}

			if (detach == true && pResult != null && pResult.size() > 0) {
				List<User> retLst = new ArrayList<User>();
				for (User u : pResult) {
					retLst.add((User) ooDbTx.detach(u, true));
				}
				return retLst;
			}
			return pResult;
		} else {
			return new ArrayList<User>();
		}
	}
}
