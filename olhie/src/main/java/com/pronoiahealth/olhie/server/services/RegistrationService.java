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

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.annotations.Load;
import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.annotations.Update;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.registration.LoadProfileResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.dataaccess.vo.Password;
import com.pronoiahealth.olhie.server.security.SaltedPassword;
import com.pronoiahealth.olhie.server.security.SecurityUtils;

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
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

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
			// Do in a single transaction
			ooDbTx.begin(TXTYPE.OPTIMISTIC);

			OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
					"select from User where userId = :uId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("uId", userId);
			List<User> uResult = ooDbTx.command(uQuery).execute(uparams);

			//
			if (uResult != null && uResult.size() == 1) {
				// Already used
				log.log(Level.SEVERE, "UserId " + userId
						+ " is already in use.");
				RegistrationErrorEvent errEvt = new RegistrationErrorEvent(
						"UserId already in use.",
						RegistrationErrorEvent.ErrorTypeEnum.USER_ID_ALREADY_EXISTS);
				registrationErrorEvent.fire(errEvt);
			} else {

				// Create a User object and add it to the database
				User user = new User();
				user.setFirstName(form.getFirstName());
				user.setLastName(form.getLastName());
				user.setRole(SecurityRoleEnum.AUTHOR.getName());
				user.setEmail(form.getEmail());
				user.setUserId(userId);
				ooDbTx.save(user);

				// Create a password object and add it to the database
				// One last time to make sure the passwords match
				String pwd = form.getPwd();
				if (!pwd.equals(form.getPwdRepeat())) {
					handleError(
							Level.SEVERE,
							"Passwords don't match",
							RegistrationErrorEvent.ErrorTypeEnum.PASSWORDS_DONT_MATCH,
							null);
				}

				Password password = new Password();
				SaltedPassword passSalt = SecurityUtils
						.genSaltedPasswordAndSalt(form.getPwd());
				password.setPwdDigest(passSalt.getPwdDigest());
				password.setPwdSalt(passSalt.getSalt());
				password.setUserId(userId);
				ooDbTx.save(password);

				// Saved the user and password now commit
				ooDbTx.commit();

				// Fire the good response event
				registrationResponseEvent.fire(new RegistrationResponseEvent());
			}
		} catch (Exception e) {
			ooDbTx.rollback();
			handleError(Level.SEVERE, e.getMessage(),
					RegistrationErrorEvent.ErrorTypeEnum.OTHER, e);
		}
	}

	/**
	 * When a new comment is received add it to the database
	 * 
	 * @param commentEvent
	 */
	protected void observesLoadRegistrationRequestEvent(
			@Observes @Load RegistrationRequestEvent registrationRequestEvent) {

		// Check the id to make sure it is unique
		RegistrationForm form = registrationRequestEvent.getRegistrationForm();
		String userId = form.getUserId();
		//log.log(Level.SEVERE, userId);

		try {
			// Do in a single transaction
			ooDbTx.begin(TXTYPE.OPTIMISTIC);

			OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
					"select from User where userId = :uId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("uId", userId);
			List<User> uResult = ooDbTx.command(uQuery).execute(uparams);

			//
			if (uResult != null && uResult.size() == 1) {
				
				User user = uResult.get(0);
				form.setLastName(user.getLastName());
				form.setFirstName(user.getFirstName());
				form.setEmail(user.getEmail());

				// Fire the good response event
				loadProfileResponseEvent.fire(new LoadProfileResponseEvent(form));
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
			ooDbTx.rollback();
			handleError(Level.SEVERE, e.getMessage(),
					RegistrationErrorEvent.ErrorTypeEnum.OTHER, e);
		}
	}
	
	/**
	 * When a new comment is received add it to the database
	 * 
	 * @param commentEvent
	 */
	protected void observesUpdateRegistrationRequestEvent(
			@Observes @Update RegistrationRequestEvent registrationRequestEvent) {

		// Check the id to make sure it is unique
		RegistrationForm form = registrationRequestEvent.getRegistrationForm();
		String userId = form.getUserId();

		try {
			// Do in a single transaction
			ooDbTx.begin(TXTYPE.OPTIMISTIC);

			OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
					"select from User where userId = :uId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("uId", userId);
			List<User> uResult = ooDbTx.command(uQuery).execute(uparams);

			//
			if (uResult != null && uResult.size() == 1) {
				
				User user = uResult.get(0);
				user.setFirstName(form.getFirstName());
				user.setLastName(form.getLastName());
				user.setRole(SecurityRoleEnum.AUTHOR.getName());
				user.setEmail(form.getEmail());

				ooDbTx.save(user);

				// Saved the user and password now commit
				ooDbTx.commit();

				// Fire the good response event
				registrationResponseEvent.fire(new RegistrationResponseEvent());
			}
		} catch (Exception e) {
			ooDbTx.rollback();
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