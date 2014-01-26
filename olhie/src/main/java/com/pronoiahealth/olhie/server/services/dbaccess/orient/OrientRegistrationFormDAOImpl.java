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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.services.dbaccess.RegistrationFormDAO;

/**
 * OrientRegistrationFormDAOImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 11, 2013
 * 
 */
public class OrientRegistrationFormDAOImpl extends OrientBaseTxDAO implements
		RegistrationFormDAO {

	public OrientRegistrationFormDAOImpl() {
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.RegistrationFormDAO#findFormsByUserLastName(java.lang.String)
	 */
	@Override
	public List<RegistrationForm> findFormsByUserLastName(String lastNameQry)
			throws Exception {
		OSQLSynchQuery<RegistrationForm> bQuery = null;
		HashMap<String, Object> bparams = new HashMap<String, Object>();
		bparams.put("lastNameQry", "%" + lastNameQry.toLowerCase() + "%");
		bQuery = new OSQLSynchQuery<RegistrationForm>(
				"select from RegistrationForm where lastName.toLowerCase() like :lastNameQry");
		List<RegistrationForm> bResult = ooDbTx.command(bQuery)
				.execute(bparams);
		return bResult;
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.RegistrationFormDAO#findFormsByUserId(java.lang.String)
	 */
	@Override
	public List<RegistrationForm> findFormsByUserId(String userId)
			throws Exception {
		OSQLSynchQuery<RegistrationForm> bQuery = null;
		HashMap<String, Object> bparams = new HashMap<String, Object>();
		bparams.put("userIdQry", userId);
		bQuery = new OSQLSynchQuery<RegistrationForm>(
				"select from RegistrationForm where userId = :userIdQry");
		List<RegistrationForm> bResult = ooDbTx.command(bQuery)
				.execute(bparams);
		return bResult;
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.RegistrationFormDAO#findAuthorPendingForms()
	 */
	@Override
	public List<RegistrationForm> findAuthorPendingForms(boolean detach)
			throws Exception {
		OSQLSynchQuery<RegistrationForm> bQuery = null;
		HashMap<String, Object> bparams = new HashMap<String, Object>();
		bQuery = new OSQLSynchQuery<RegistrationForm>(
				"select from RegistrationForm where author = true and authorStatus = 'PENDING'");
		List<RegistrationForm> bResult = ooDbTx.command(bQuery)
				.execute(bparams);
		if (detach = true) {
			if (bResult != null && bResult.size() > 0) {
				List<RegistrationForm> retLst = new ArrayList<RegistrationForm>();
				for (RegistrationForm r : bResult) {
					retLst.add((RegistrationForm) ooDbTx.detach(r, true));
				}
				return retLst;
			}
		}
		return bResult;
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.RegistrationFormDAO#addRegistrationForm(com.pronoiahealth.olhie.client.shared.vo.RegistrationForm)
	 */
	@Override
	public RegistrationForm addRegistrationForm(RegistrationForm form)
			throws Exception {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			form = ooDbTx.save(form);
			ooDbTx.commit();
			return ooDbTx.detach(form, true);
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.RegistrationFormDAO#acceptRejectAuthorApplication(java.lang.String,
	 *      java.lang.String, boolean)
	 */
	@Override
	public void acceptRejectAuthorApplication(String registrationFormId,
			String adminUserId, boolean acceptReject, String authorStatus,
			boolean updateUserRole) throws Exception {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);

			// Lookup form
			RegistrationForm form = findRegistrationFormById(registrationFormId);
			if (form == null) {
				throw new Exception("Can't find form with id "
						+ registrationFormId);
			}

			// Update form
			form.setAuthorDecision(acceptReject);
			form.setAuthorDecisionDate(new Date());
			form.setAdminUserId(adminUserId);
			form.setAuthorStatus(authorStatus);
			form = ooDbTx.save(form);

			if (updateUserRole == true) {
				User user = getUserByUserId(form.getUserId());
				if (acceptReject == true) {
					user.setRole(SecurityRoleEnum.AUTHOR.toString());
				} else {
					user.setRole(SecurityRoleEnum.REGISTERED.toString());
				}
				user = ooDbTx.save(user);
			}

			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.RegistrationFormDAO#findRegistrationFormById(java.lang.String)
	 */
	@Override
	public RegistrationForm findRegistrationFormById(String registrationFormId)
			throws Exception {
		OSQLSynchQuery<RegistrationForm> bQuery = null;
		HashMap<String, Object> bparams = new HashMap<String, Object>();
		bparams.put("idQry", registrationFormId);
		bQuery = new OSQLSynchQuery<RegistrationForm>(
				"select from RegistrationForm where @rId = :idQry");
		List<RegistrationForm> bResult = ooDbTx.command(bQuery)
				.execute(bparams);
		if (bResult != null && bResult.size() > 0) {
			return bResult.get(0);
		} else {
			return null;
		}
	}
}
