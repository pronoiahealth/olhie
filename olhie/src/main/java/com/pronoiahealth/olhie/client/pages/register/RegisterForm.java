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

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.errai.databinding.client.BindableProxy;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.validation.client.impl.Validation;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;

@Templated("#form")
public class RegisterForm extends Composite {

	@Inject
	@Bound
	@DataField
	private TextBox firstName;

	@Inject
	@DataField
	private Label firstNameErr;

	@Inject
	@Bound
	@DataField
	private TextBox lastName;

	@Inject
	@DataField
	private Label lastNameErr;

	@Inject
	@Bound
	@DataField
	private TextBox email;

	@Inject
	@DataField
	private Label emailErr;

	@Inject
	@Bound
	@DataField
	private TextBox userId;

	@Inject
	@DataField
	private Label userIdErr;

	@Inject
	@Bound
	@DataField
	private TextBox pwd;

	@Inject
	@DataField
	private Label pwdErr;

	@Inject
	@Bound
	@DataField
	private TextBox pwdRepeat;

	@Inject
	@DataField
	private Label pwdRepeatErr;

	private DataBinder<RegistrationForm> formBinder;

	/**
	 * Constructor
	 * 
	 * @param formBinder
	 */
	@Inject
	public RegisterForm(@AutoBound DataBinder<RegistrationForm> formBinder) {
		this.formBinder = formBinder;
		formBinder.getModel();
	}

	/**
	 * Clear form
	 */
	public void clearForm() {
		clearDataInputs();
		clearErrors();
	}

	/**
	 * Clear the data inputs
	 */
	public void clearDataInputs() {
		lastName.setText("");
		firstName.setText("");
		email.setText("");
		userId.setText("");
		pwd.setText("");
		pwdRepeat.setText("");
	}

	/**
	 * Clear the errors
	 */
	public void clearErrors() {
		lastNameErr.setText("");
		firstNameErr.setText("");
		emailErr.setText("");
		userIdErr.setText("");
		pwdErr.setText("");
		pwdRepeatErr.setText("");
	}

	/**
	 * Get the current data in the form
	 * 
	 * @return
	 */
	public RegistrationForm getModelData() {
		return formBinder.getModel();
	}

	/**
	 * Validate the form and set errors if there are any
	 * 
	 * @return
	 */
	public boolean validateForm() {
		clearErrors();
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		// proxy needs to be unwrapped to work with the validator
		RegistrationForm rf = (RegistrationForm) ((BindableProxy<RegistrationForm>) getModelData())
				.unwrap();
		Set<ConstraintViolation<RegistrationForm>> violations = validator
				.validate(rf);

		for (ConstraintViolation<RegistrationForm> cv : violations) {
			String prop = cv.getPropertyPath().toString();
			if (prop.equals("lastName")) {
				lastNameErr.setText(cv.getMessage());
			} else if (prop.equals("firstName")) {
				firstNameErr.setText(cv.getMessage());
			} else if (prop.equals("email")) {
				emailErr.setText(cv.getMessage());
			} else if (prop.equals("userId")) {
				userIdErr.setText(cv.getMessage());
			} else if (prop.equals("pwd")) {
				pwdErr.setText(cv.getMessage());
			}
		}

		if (!pwd.getValue().equals(pwdRepeat.getValue())) {
			pwdRepeatErr.setText("Passwords must match. Please retype.");
			return false;
		}

		if (!violations.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}
