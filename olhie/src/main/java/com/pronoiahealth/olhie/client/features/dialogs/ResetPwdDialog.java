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
package com.pronoiahealth.olhie.client.features.dialogs;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.impl.Validation;
import com.pronoiahealth.olhie.client.shared.events.local.ShowResetPwdModalEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.UpdatePwdRequestEvent;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;

/**
 * ResetPwdDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 * 
 */
@Dependent
public class ResetPwdDialog extends Composite {
	@Inject
	UiBinder<Widget, ResetPwdDialog> binder;

	@UiField
	public Modal resetPwdModal;

	@UiField
	public Form resetPwdForm;

	@UiField
	public PasswordTextBox password;

	@UiField
	public ControlGroup passwordGroup;

	@UiField
	public HelpInline passwordErrors;

	@UiField
	public PasswordTextBox reEnterPassword;

	@UiField
	public ControlGroup reEnterPasswordGroup;

	@UiField
	public HelpInline reEnterPasswordErrors;

	@UiField
	public Button resetPwdButton;

	@Inject
	private Event<UpdatePwdRequestEvent> updatePwdRequestEvent;

	/**
	 * Constructor
	 * 
	 */
	public ResetPwdDialog() {
	}

	/**
	 * Initial set up for the dialog
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		resetPwdModal.setStyleName("ph-Login-Modal", true);

		resetPwdModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				password.getElement().focus();
			}
		});
	}

	/**
	 * Send the new password to the server
	 * 
	 * @param event
	 */
	@UiHandler("resetPwdButton")
	public void onResetPwdButtonClick(ClickEvent event) {
		// Clear previous errors
		clearErrors();

		// Validate and submit
		if (hasSubmissionErrors() == false) {
			// Hide dialog
			resetPwdModal.hide();

			// Tell the server
			updatePwdRequestEvent.fire(new UpdatePwdRequestEvent(password
					.getText()));
		}
	}

	/**
	 * Shows the dialog
	 */
	public void show() {
		clearErrors();
		resetPwdForm.reset();
		resetPwdModal.show();
	}

	/**
	 * Clear any previously displayed errors
	 */
	private void clearErrors() {
		passwordGroup.setType(ControlGroupType.NONE);
		passwordErrors.setText("");
		reEnterPasswordGroup.setType(ControlGroupType.NONE);
		reEnterPasswordErrors.setText("");
	}

	/**
	 * Check that the password matches the regEx and that the re-entered
	 * password is the same as the passowrd
	 * 
	 * @return true if there are errors
	 */
	private boolean hasSubmissionErrors() {
		boolean ret = false;

		// This may be over kill but at least its consistent with the
		// registration form process
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<RegistrationForm>> violations = validator
				.validateValue(RegistrationForm.class, "pwd", password.getText(), Default.class);
		// Check the password for match against the password regEx (defined on
		// the RegistrationForm value object)
		for (ConstraintViolation<RegistrationForm> cv : violations) {
			String prop = cv.getPropertyPath().toString();
			if (prop.equals("pwd")) {
				passwordErrors.setText(cv.getMessage());
				passwordGroup.setType(ControlGroupType.ERROR);
				ret = true;
			}
		}

		// The password and the re typed password must match
		if (!password.getValue().equals(reEnterPassword.getValue())) {
			reEnterPasswordErrors
					.setText("Passwords must match. Please retype.");
			reEnterPasswordGroup.setType(ControlGroupType.ERROR);
			ret = true;
		}

		// Return value after validation
		return ret;
	}

	/**
	 * Listens for the event that tells this component to show the dialog box.
	 * 
	 * @param showResetPwdModalEvent
	 */
	protected void observerShowResetPwdModalEvent(
			@Observes ShowResetPwdModalEvent showResetPwdModalEvent) {
		show();
	}

}
