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
package com.pronoiahealth.olhie.client.pages.register;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.annotations.Load;
import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.annotations.Update;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowEditProfileModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowRegisterModalEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.LoadProfileResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RegistrationResponseEvent;
import com.pronoiahealth.olhie.client.shared.exceptions.DataValidationException;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;

/**
 * RegisterDialog.java<br/>
 * Responsibilities:<br/>
 * 1. Shows Registration dialog<br/>
 * 2. Submits registration for a new user<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public class RegisterDialog extends Composite {

	private boolean updateState = false;
	
	@Inject
	UiBinder<Widget, RegisterDialog> binder;

	@Inject
	private ClientUserToken clientUserToken;

	@UiField
	public Modal registerModal;

	@UiField
	public HTMLPanel regFormHolder;

	@UiField
	public Button submitButton;

	@Inject
	private RegisterForm regForm;

	@Inject @New
	private Event<RegistrationRequestEvent> registrationRequestEvent;

	@Inject @Update
	private Event<RegistrationRequestEvent> editRegistrationRequestEvent;

	@Inject @Load
	private Event<RegistrationRequestEvent> loadRegistrationRequestEvent;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;
	
	@Inject
	private Event<ClientUserUpdatedEvent> clientUserUpdatedEvent;

	/**
	 * Constructor
	 * 
	 */
	public RegisterDialog() {
	}

	/**
	 * Create the gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		registerModal.setStyleName("ph-Register-Modal", true);
		registerModal.setStyleName("ph-Register-Modal-Size", true);
		
		// Set focus
		registerModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				regForm.setFocusOnFirstName();
			}
		});

		// Add the RegisterForm to the form holder
		regFormHolder.add(regForm);
	}

	/**
	 * Shows the modal dialog
	 */
	public void show() {
		regForm.clearForm();
		registerModal.show();
	}

	/**
	 * Validates the form and fires a RegistrationRequestEvent with the
	 * validated data.
	 * 
	 * @param clickEvt
	 */
	@UiHandler("submitButton")
	public void handleSubmitButtonClick(ClickEvent clickEvt) {
		try {
			RegistrationForm populatedForm = regForm.validateForm();
			if (updateState) {
				editRegistrationRequestEvent.fire(new RegistrationRequestEvent(
					populatedForm));
				clientUserToken.setFullName(populatedForm.getFirstName() + " " + populatedForm.getLastName());
				clientUserUpdatedEvent.fire(new ClientUserUpdatedEvent());
			} else {
				registrationRequestEvent.fire(new RegistrationRequestEvent(
					populatedForm));
			}
		} catch (DataValidationException e) {
			// Intentionally blank. The RegistrationForm will post error
			// messages to the form during the validation process
		}
	}

	/**
	 * Signifies a good registration process resulting in the dialog closing and
	 * a message to the user to login.
	 * 
	 * @param registrationResponseEvent
	 */
	// TODO: Pop up window saying you can log in or something like that
	protected void observesRegistrationResponseEvent(
			@Observes RegistrationResponseEvent registrationResponseEvent) {
		registerModal.hide();
	}

	/**
	 * Something went wrong and we need to tell the user.
	 * 
	 * @param registrationErrorEvent
	 */
	protected void observesRegistrationErrorEvent(
			@Observes RegistrationErrorEvent registrationErrorEvent) {
		// Get the error type
		RegistrationErrorEvent.ErrorTypeEnum errorType = registrationErrorEvent
				.getErrorType();

		switch (errorType) {
		case OTHER:
			registerModal.hide();
			serviceErrorEvent.fire(new ServiceErrorEvent(registrationErrorEvent
					.getMsg()));
			break;

		case PASSWORDS_DONT_MATCH:
			regForm.setPasswordsDontMatchError();
			break;

		case USER_ID_ALREADY_EXISTS:
			regForm.setUserIdAlreadyInUse();
			break;
		}
	}

	/**
	 * Shows the dialog
	 * 
	 * @param showRegisterModalEvent
	 */
	protected void observesShowRegisterModalEvent(
			@Observes ShowRegisterModalEvent showRegisterModalEvent) {
		updateState = false;
		regForm.prepareRegister();
		show();
	}

	/**
	 * Shows the dialog
	 * 
	 * @param ShowEditProfileModalEvent
	 */
	protected void observesLoadProfileModalEvent(
			@Observes LoadProfileResponseEvent loadProfileResponseEvent) {
		RegistrationForm populatedForm = loadProfileResponseEvent.getRegistrationForm();
		RegistrationForm blankForm = regForm.getUnwrappedModelData();
		blankForm.setFirstName(populatedForm.getFirstName());
		blankForm.setLastName(populatedForm.getLastName());
		blankForm.setEmail(populatedForm.getEmail());
		blankForm.setPwd("Aaaaaaaa8"); // TODO: remove validation bypass -- not actually read
		blankForm.setPwdRepeat("Aaaaaaaa8"); // TODO: remove validation bypass -- not actually read
		updateState = true;
		regForm.populateForm();
		regForm.prepareEdit();
		regForm.setFocusOnFirstName();
		registerModal.show();
	}

	/**
	 * Shows the dialog
	 * 
	 * @param ShowEditProfileModalEvent
	 */
	protected void observesEditProfileModalEvent(
			@Observes ShowEditProfileModalEvent showEditProfileModalEvent) {
		regForm.clearForm();
		RegistrationForm blankForm = regForm.getUnwrappedModelData();
		blankForm.setUserId(clientUserToken.getUserId());
		loadRegistrationRequestEvent.fire(new RegistrationRequestEvent(
				blankForm));
	}

}
