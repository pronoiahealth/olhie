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

import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;

/**
 * RegistrationFormDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 11, 2013
 * 
 */
public interface RegistrationFormDAO extends BaseDAO {

	/**
	 * @param lastNameQry
	 * @return
	 * @throws Exception
	 */
	public List<RegistrationForm> findFormsByUserLastName(String lastNameQry)
			throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<RegistrationForm> findFormsByUserId(String userId)
			throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public List<RegistrationForm> findAuthorPendingForms(boolean detach)
			throws Exception;

	/**
	 * @param form
	 * @throws Exception
	 */
	public RegistrationForm addRegistrationForm(RegistrationForm form)
			throws Exception;

	/**
	 * @param registrationFormId
	 * @param adminUserId
	 * @param acceptReject
	 * @throws Exception
	 */
	public void acceptRejectAuthorApplication(String registrationFormId,
			String adminUserId, boolean acceptReject, String authorStatus,
			boolean updateUserRole) throws Exception;

	/**
	 * @param registrationFormId
	 * @return
	 * @throws Exception
	 */
	public RegistrationForm findRegistrationFormById(String registrationFormId)
			throws Exception;

}
