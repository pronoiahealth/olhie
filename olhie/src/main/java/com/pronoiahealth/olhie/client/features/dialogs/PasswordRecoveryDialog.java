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

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.local.ShowPasswordRecoveryDialogEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RecoverPwdRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.registration.RecoverPwdResponseEvent;

/**
 * PasswordRecoveryDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 * 
 */
@Dependent
public class PasswordRecoveryDialog extends Composite {

	@Inject
	UiBinder<Widget, PasswordRecoveryDialog> binder;

	@UiField
	public Modal pwdRecoverModal;

	@UiField
	public Form pwdRecoverForm;

	@UiField
	public TextBox username;

	@UiField
	public ControlGroup usernameGroup;

	@UiField
	public HelpInline usernameErrors;

	@UiField
	public TextBox email;

	@UiField
	public ControlGroup emailGroup;

	@UiField
	public HelpInline emailErrors;

	@UiField
	public HTML errorMsg;

	@UiField
	public Button revoverPwdBtn;

	@Inject
	private Event<RecoverPwdRequestEvent> recoverPwdRequestEvent;

	/**
	 * Constructor
	 * 
	 */
	public PasswordRecoveryDialog() {
	}

	/**
	 * Create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		pwdRecoverModal.setStyleName("ph-Login-Modal", true);

		pwdRecoverModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				username.getElement().focus();
			}
		});
	}

	/**
	 * Watches for an event to show this dialog. Usually would come from the
	 * LoginDialog.
	 * 
	 * @param showPasswordRecoveryDialogEvent
	 */
	protected void observesShowPasswordRecoveryDialogEvent(
			@Observes ShowPasswordRecoveryDialogEvent showPasswordRecoveryDialogEvent) {
		// Set the user name if the data is there
		String userId = showPasswordRecoveryDialogEvent.getUserId();
		if (userId != null) {
			show(userId);
		} else {
			show(null);
		}
	}

	/**
	 * Password recovery response
	 * 
	 * @param recoverPwdResponseEvent
	 */
	protected void observesRecoverPwdResponseEvent(
			@Observes RecoverPwdResponseEvent recoverPwdResponseEvent) {
		if (recoverPwdResponseEvent.isSentNewPwdEmail() == false) {
			errorMsg.setHTML(recoverPwdResponseEvent.getErrMsg());
		} else {
			errorMsg.setHTML("A new password has been sent to the provided email address.");
		}
	}

	/**
	 * Shows the dialog
	 */
	public void show(String userId) {
		pwdRecoverForm.reset();
		if (userId != null) {
			username.setText(userId);
		}
		pwdRecoverModal.show();
	}

	/**
	 * Handle the clicking of the Sign-In button
	 * 
	 * @param event
	 */
	@UiHandler("revoverPwdBtn")
	public void onRecoverClick(ClickEvent event) {
		// Clear previous errors
		clearErrors();

		// Validate and submit
		if (hasSubmissionErrors() == false) {
			recoverPwdRequestEvent.fire(new RecoverPwdRequestEvent(username
					.getText(), email.getText()));
		}
	}

	/**
	 * Check for user name and password errors
	 * 
	 * @return
	 */
	private boolean hasSubmissionErrors() {
		boolean ret = false;

		String uName = username.getValue();
		String emailAdd = email.getValue();

		if (uName == null || "".equals(uName.trim()) == true) {
			usernameGroup.setType(ControlGroupType.ERROR);
			usernameErrors.setText("Must enter a user name.");
			ret = true;
		}

		if (emailAdd == null || "".equals(emailAdd.trim()) == true) {
			emailGroup.setType(ControlGroupType.ERROR);
			emailErrors
					.setText("Must enter the email that matches the user id. The one you used when you registered.");
			ret = true;
		}

		// Return value after validation
		return ret;
	}

	/**
	 * Clear any previously displayed errors
	 */
	private void clearErrors() {
		usernameGroup.setType(ControlGroupType.NONE);
		usernameErrors.setText("");
		emailGroup.setType(ControlGroupType.NONE);
		emailErrors.setText("");
		errorMsg.setText("");
	}

}
