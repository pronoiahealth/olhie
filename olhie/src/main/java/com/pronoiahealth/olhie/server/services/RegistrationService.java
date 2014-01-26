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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.pronoiahealth.olhie.client.shared.annotations.Load;
import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.annotations.Update;
import com.pronoiahealth.olhie.client.shared.constants.RegistrationEventEnum;
import com.pronoiahealth.olhie.client.shared.constants.RequestFormAuthorApproval;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.registration.LoadProfileResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.services.dbaccess.RegistrationFormDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.UserDAO;

/**
 * RegistrationService.java<br/>
 * Responsibilities:<br/>
 * 1. Observes the RegistrationRequestEvent and if appropriate, will add a new
 * user.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 2, 2013
 * 
 */
@RequestScoped
public class RegistrationService {

	@Inject
	private Logger log;

	@Inject
	@DAO
	private UserDAO userDAO;

	@Inject
	@DAO
	private RegistrationFormDAO registrationFormDAO;

	@Inject
	@ConfigProperty(name = "OLHIE_SUPPORT_EMAIL")
	private String supportEmail;

	@Inject
	private MailSendingService mailService;

	@Inject
	private Event<RegistrationErrorEvent> registrationErrorEvent;

	@Inject
	private Event<RegistrationResponseEvent> registrationResponseEvent;

	@Inject
	private Event<LoadProfileResponseEvent> loadProfileResponseEvent;

	/**
	 * Constructor
	 * 
	 */
	public RegistrationService() {
	}

	/**
	 * When a new comment is received add it to the database
	 * 
	 * @param commentEvent
	 */
	protected void observesRegistrationRequestEvent(
			@Observes @New RegistrationRequestEvent registrationRequestEvent) {

		// Check the id to make sure it is unique
		RegistrationForm form = registrationRequestEvent.getRegistrationForm();
		String userId = form.getUserId();

		try {
			// See if user id is already in use
			boolean idInUser = userDAO.checkUserByUserId(userId);
			if (idInUser == true) {
				// Already used
				log.log(Level.SEVERE, "UserId " + userId
						+ " is already in use.");
				RegistrationErrorEvent errEvt = new RegistrationErrorEvent(
						"UserId already in use.",
						RegistrationErrorEvent.ErrorTypeEnum.USER_ID_ALREADY_EXISTS);
				registrationErrorEvent.fire(errEvt);
				return;
			}

			// Check the password
			// Create a password object and add it to the database
			// One last time to make sure the passwords match
			String pwd = form.getPwd();
			if (!pwd.equals(form.getPwdRepeat())) {
				handleError(
						Level.SEVERE,
						"Passwords don't match",
						RegistrationErrorEvent.ErrorTypeEnum.PASSWORDS_DONT_MATCH,
						null);
				return;
			}

			// Do they want to be an author
			boolean authYes = form.isAuthor();

			// Add user
			userDAO.addUser(userId, form.getLastName(), form.getFirstName(),
					SecurityRoleEnum.REGISTERED, form.getEmail(),
					form.getPwd(), form.getOrganization(), authYes);

			// Add the registration
			form.setPwd("X");
			form.setPwdRepeat("X");
			form.setRegDate(new Date());
			form.setUserId(userId);
			form.setType(RegistrationEventEnum.NEW.toString());
			if (authYes == true) {
				form.setAuthorStatus(RequestFormAuthorApproval.PENDING
						.toString());
			} else {
				form.setAuthorStatus(RequestFormAuthorApproval.NOT_REQUESTED
						.toString());
			}
			RegistrationForm savedForm = registrationFormDAO
					.addRegistrationForm(form);

			// Fire the good response event
			registrationResponseEvent.fire(new RegistrationResponseEvent());

			// Fire off an email to the administrator
			if (form.isAuthor() == true) {
				mailService.sendRequestAuthorMailFromApp(supportEmail, userId,
						form.getFirstName(), form.getLastName(),
						savedForm.getId());
			}
		} catch (Exception e) {
			handleError(Level.SEVERE, e.getMessage(),
					RegistrationErrorEvent.ErrorTypeEnum.OTHER, e);
		}
	}

	/**
	 * When a new comment is received add it to the database
	 * 
	 * @param commentEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED })
	protected void observesLoadRegistrationRequestEvent(
			@Observes @Load RegistrationRequestEvent registrationRequestEvent) {

		RegistrationForm form = registrationRequestEvent.getRegistrationForm();
		String userId = form.getUserId();

		try {
			// find the user
			User user = userDAO.getUserByUserId(userId);
			//
			if (user != null) {
				form.setLastName(user.getLastName());
				form.setFirstName(user.getFirstName());
				form.setEmail(user.getEmail());
				form.setOrganization(user.getOrganization());
				form.setAuthor(user.isRequestedAuthor());

				// Fire the good response event
				loadProfileResponseEvent
						.fire(new LoadProfileResponseEvent(form));
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
			handleError(Level.SEVERE, e.getMessage(),
					RegistrationErrorEvent.ErrorTypeEnum.OTHER, e);
		}
	}

	/**
	 * When a new comment is received add it to the database
	 * 
	 * @param commentEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED })
	protected void observesUpdateRegistrationRequestEvent(
			@Observes @Update RegistrationRequestEvent registrationRequestEvent) {

		// Check the id to make sure it is unique
		RegistrationForm form = registrationRequestEvent.getRegistrationForm();
		String userId = form.getUserId();

		try {
			// Get the current User
			User user = userDAO.getUserByUserId(userId);
			SecurityRoleEnum currentUserRole = SecurityRoleEnum.valueOf(user
					.getRole());

			// Do they want to be an author
			boolean authYes = form.isAuthor();
			boolean alreadyAuthor = false;
			if (user.getRole().equals(SecurityRoleEnum.AUTHOR.toString()) == true) {
				alreadyAuthor = true;
			}

			// Update User
			userDAO.updateUser(userId, form.getLastName(), form.getFirstName(),
					currentUserRole, form.getEmail(), form.getOrganization(),
					form.isAuthor());

			// Save registration changes
			// Add the registration
			form.setPwd("X");
			form.setPwdRepeat("X");
			form.setRegDate(new Date());
			form.setUserId(userId);
			form.setType(RegistrationEventEnum.UPDATE.toString());
			if (!alreadyAuthor && authYes) {
				form.setAuthorStatus(RequestFormAuthorApproval.PENDING
						.toString());
			}
			RegistrationForm savedForm = registrationFormDAO
					.addRegistrationForm(form);

			// Fire the good response event
			registrationResponseEvent.fire(new RegistrationResponseEvent());

			// Send author request email if form.isAuthor is true and the users
			// role is not AUTHOR
			if (form.isAuthor() == true
					&& !(currentUserRole == SecurityRoleEnum.ADMIN || currentUserRole == SecurityRoleEnum.AUTHOR)) {
				mailService.sendRequestAuthorMailFromApp(supportEmail, userId,
						form.getFirstName(), form.getLastName(),
						savedForm.getId());
			}
		} catch (Exception e) {
			handleError(Level.SEVERE, e.getMessage(),
					RegistrationErrorEvent.ErrorTypeEnum.OTHER, e);
		}
	}

	/**
	 * 
	 * Logs the error and fires a RegistrationErrorEvent
	 * 
	 * @param level
	 * @param message
	 * @param errorType
	 * @param e
	 */
	private void handleError(Level level, String message,
			RegistrationErrorEvent.ErrorTypeEnum errorType, Exception e) {
		// Log error
		if (e == null) {
			log.log(level, message);
		} else {
			log.log(level, message, e);
		}

		// Fire error event
		registrationErrorEvent.fire(new RegistrationErrorEvent(message,
				errorType));
	}

}