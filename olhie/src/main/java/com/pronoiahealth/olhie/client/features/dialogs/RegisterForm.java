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

import static com.google.gwt.query.client.GQuery.$;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.errai.databinding.client.BindableProxy;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.validation.client.impl.Validation;
import com.pronoiahealth.olhie.client.shared.constants.RegistrationEventEnum;
import com.pronoiahealth.olhie.client.shared.exceptions.DataValidationException;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;

@Templated("#form")
public class RegisterForm extends Composite {

	/**
	 * Tracks form state
	 */
	private RegistrationEventEnum mode;

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
	private Label pwdLbl;

	@Inject
	@DataField
	private Label pwdErr;

	@Inject
	@Bound
	@DataField
	private TextBox pwdRepeat;

	@Inject
	@DataField
	private Label pwdRepeatLbl;

	@Inject
	@DataField
	private Label pwdRepeatErr;

	@Inject
	@DataField
	private Label organizationErr;

	@Inject
	@Bound
	@DataField
	private TextBox organization;

	@Inject
	@DataField
	private Label authorErr;

	@Inject
	@DataField
	private Label authorLbl;

	@Inject
	@Bound
	@DataField
	private CheckBox author;

	@Inject
	@DataField
	private Label acceptedPolicyStatementErr;

	@Inject
	@DataField
	private Label acceptedPolicyStatementLbl;

	@Inject
	@Bound
	@DataField
	private CheckBox acceptedPolicyStatement;

	@DataField
	private Element policyStatement = DOM.createDiv();

	@DataField
	private Element policyHeader = DOM.createDiv();

	private DataBinder<RegistrationForm> formBinder;

	private GQuery scrollPanelQry;

	private GQuery policyHeaderQry;

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
	 * Grab a GQuery handle to the scrollPanel and the policyHeader. Add a value
	 * change handler to the author checkbox.
	 */
	@PostConstruct
	protected void postConstruct() {
		scrollPanelQry = $(policyStatement);
		policyHeaderQry = $(policyHeader);

		author.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue() == true) {
					authorErr
							.setText("The administrator will contact you when your author status has been approved.");
				} else {
					authorErr.setText("");
				}
			}
		});
	}

	/**
	 * Attach scroll listener. When the user scrolls to the bottom of the policy
	 * statement div the enable the acceptedPolicyStatement checkbox.
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#onLoad()
	 */
	@Override
	protected void onLoad() {
		super.onLoad();

		scrollPanelQry.bind(Event.ONSCROLL, new Function() {
			@Override
			public boolean f(Event e) {
				double scrollHeight = scrollPanelQry.prop("scrollHeight",
						Double.class);
				boolean visible = scrollPanelQry.visible();
				if ((scrollHeight - scrollPanelQry.scrollTop() <= scrollPanelQry
						.outerHeight()) && visible) {
					acceptedPolicyStatementLbl.setVisible(true);
					acceptedPolicyStatement.setVisible(true);
				}
				return false;
			}
		});
	}

	/**
	 * Unload scroll listener
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#onUnload()
	 */
	@Override
	protected void onUnload() {
		super.onUnload();

		if (scrollPanelQry != null) {
			scrollPanelQry.unbind(Event.ONSCROLL);
			scrollPanelQry = null;
		}
	}

	/**
	 * Prepare for editing. If the user has already requested to be an author
	 * then hide the check box.
	 */
	public void prepareEdit(boolean hideAuthor) {
		mode = RegistrationEventEnum.UPDATE;
		userId.setReadOnly(true);
		pwd.setVisible(false);
		pwdLbl.setVisible(false);
		pwdRepeat.setVisible(false);
		pwdRepeatLbl.setVisible(false);
		acceptedPolicyStatementLbl.setVisible(false);
		acceptedPolicyStatement.setVisible(false);
		scrollPanelQry.hide();
		policyHeaderQry.hide();

		if (hideAuthor == true) {
			author.setVisible(false);
			authorLbl.setVisible(false);
		} else {
			author.setVisible(true);
			authorLbl.setVisible(true);
		}
	}

	/**
	 * Prepare for new registration.
	 */
	public void prepareRegister() {
		mode = RegistrationEventEnum.NEW;
		userId.setReadOnly(false);
		pwd.setVisible(true);
		pwdLbl.setVisible(true);
		pwdRepeat.setVisible(true);
		pwdRepeatLbl.setVisible(true);
		pwdLbl.setText("Password:");
		pwdRepeatLbl.setText("Retype Password:");
		author.setVisible(true);
		authorLbl.setVisible(true);
		acceptedPolicyStatementLbl.setVisible(false);
		acceptedPolicyStatement.setVisible(false);
		scrollPanelQry.show();
		policyHeaderQry.show();
		scrollPanelQry.prop("scrollTop", 0);
	}

	/**
	 * Populate the form
	 */
	public void populateForm() {
		RegistrationForm form = getUnwrappedModelData();
		lastName.setText(form.getLastName());
		firstName.setText(form.getFirstName());
		email.setText(form.getEmail());
		userId.setText(form.getUserId());
		pwd.setText(form.getPwd());
		pwdRepeat.setText(form.getPwdRepeat());
		organization.setText(form.getOrganization());
		author.setValue(form.isAuthor());
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
		organization.setText("");
		author.setValue(Boolean.FALSE);
		acceptedPolicyStatement.setValue(Boolean.FALSE);
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
		organizationErr.setText("");
		acceptedPolicyStatementErr.setText("");
		authorErr.setText("");
	}

	/**
	 * Get the current data in the form. This requires "unwrapping" the data
	 * model from the formBinder.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RegistrationForm getUnwrappedModelData() {
		return (RegistrationForm) ((BindableProxy<RegistrationForm>) formBinder
				.getModel()).unwrap();
	}

	/**
	 * Validate the form and set errors if there are any
	 * 
	 * @return
	 */
	/**
	 * @return
	 * @throws DataValidationException
	 */
	public RegistrationForm validateForm() throws DataValidationException {
		clearErrors();
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		// proxy needs to be unwrapped to work with the validator
		RegistrationForm rf = getUnwrappedModelData();
		Set<ConstraintViolation<RegistrationForm>> violations = validator
				.validate(rf);

		boolean foundErr = false;
		for (ConstraintViolation<RegistrationForm> cv : violations) {
			String prop = cv.getPropertyPath().toString();
			if (prop.equals("lastName")) {
				lastNameErr.setText(cv.getMessage());
				foundErr = true;
			} else if (prop.equals("firstName")) {
				firstNameErr.setText(cv.getMessage());
				foundErr = true;
			} else if (prop.equals("email")) {
				emailErr.setText(cv.getMessage());
				foundErr = true;
			} else if (prop.equals("userId")) {
				userIdErr.setText(cv.getMessage());
				foundErr = true;
			} else if (prop.equals("pwd")) {
				pwdErr.setText(cv.getMessage());
				foundErr = true;
			} else if (prop.equals("organization")) {
				organizationErr.setText(cv.getMessage());
				foundErr = true;
			}
		}

		Boolean authorBVal = author.getValue();
		if (authorBVal == null) {
			author.setValue(Boolean.FALSE);
		}

		if (!pwd.getValue().equals(pwdRepeat.getValue())) {
			pwdRepeatErr.setText("Passwords must match. Please retype.");
			foundErr = true;
		}

		// User must accept the policy statement if this is a new registration
		if ((acceptedPolicyStatement.getValue() == null || acceptedPolicyStatement
				.getValue().booleanValue() == false)
				&& mode == RegistrationEventEnum.NEW) {
			acceptedPolicyStatementErr
					.setText("You must accept the policy statement.");
			foundErr = true;
		}

		// If any errors throw and exception
		// else return the form data
		if (foundErr == true) {
			throw new DataValidationException("Found error(s).");
		} else {
			return rf;
		}
	}

	/**
	 * Set the error message for passwords not matching. This might be returned
	 * from server side validation.
	 */
	public void setPasswordsDontMatchError() {
		pwdRepeatErr.setText("Passwords must match. Please retype.");
	}

	/**
	 * Set the error message for userId already in use. This will be returned
	 * from server side validation.
	 */
	public void setUserIdAlreadyInUse() {
		userIdErr.setText("This userid is already in use.");
	}

	public void setFocusOnFirstName() {
		firstName.setFocus(true);
	}
}
