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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RecoverPwdRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RecoverPwdResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.UpdatePwdRequestEvent;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.UserDAO;

/**
 * PasswordRecoveryService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 * 
 */
@RequestScoped
public class PasswordService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	@DAO
	private UserDAO userDAO;

	@Inject
	private RandomPasswordGeneratorService pwdSrv;

	@Inject
	private MailSendingService mailSrv;

	@Inject
	@ConfigProperty(name = "OLHIE_SUPPORT_EMAIL", defaultValue = "jjdestef3@gmail.com")
	private String supportEmail;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private Event<RecoverPwdResponseEvent> recoverPwdResponseEvt;

	/**
	 * Constructor
	 * 
	 */
	public PasswordService() {
	}

	/**
	 * Observes the user asking for a new pwd
	 * 
	 * @param recoverPwdEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ANONYMOUS })
	protected void observesRecoverPwdEvent(
			@Observes RecoverPwdRequestEvent recoverPwdEvent) {
		try {
			String userId = recoverPwdEvent.getUserId();
			String email = recoverPwdEvent.getUserEmail();

			// User check
			// Look up user
			User user = null;
			try {
				user = userDAO.getUserByUserId(userId);
			} catch (Exception e) {
				recoverPwdResponseEvt.fire(new RecoverPwdResponseEvent(false,
						"User ID is not valid"));
				return;
			}

			// Check the email
			if (!user.getEmail().equals(email)) {
				recoverPwdResponseEvt
						.fire(new RecoverPwdResponseEvent(false,
								"The email you provided does not match the one for the user id entered."));
				return;
			}

			// OK generate a new password
			String newPwd = pwdSrv.generateApplicationDefaultPwd();

			// Rest in database
			userDAO.resetUserPwd(userId, newPwd);

			// Send the email
			try {
				mailSrv.sendPwdResetMailFromApp(email, newPwd);
			} catch (Exception e) {
				recoverPwdResponseEvt
						.fire(new RecoverPwdResponseEvent(
								false,
								"We can't connect to your email server. You can try again or contact the administrator at "
										+ supportEmail + "."));
			}

			// Tell the user that the password has been reset
			recoverPwdResponseEvt.fire(new RecoverPwdResponseEvent(true, null));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * Updates a users password. Assumes some kind of check for validity happens
	 * on the front end
	 * 
	 * @param updatePwdRequestEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED })
	protected void observesUpdatePwdRequestEvent(
			@Observes UpdatePwdRequestEvent updatePwdRequestEvent) {

		try {
			// Get the users id out of the session
			String userId = userToken.getUserId();

			// Get the new password
			String newPwd = updatePwdRequestEvent.getNewPwd();

			// Reset the password
			userDAO.resetUserPwd(userId, newPwd);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}
}
