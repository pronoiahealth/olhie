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
package com.pronoiahealth.olhie.client.pages.login;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.local.ShowLoginModalEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginResponseEvent;

/**
 * Modal Login Dialog<br/>
 * Responsibilities:<br/>
 * 1. Provide place for user to submit credentials<br/>
 * 2. Respond to LoginErrorEvent by displaying error to user<br/>
 * 3. Respond to LoginResponseEvent by closing modal dialog<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 25, 2013
 * 
 */
public class LoginDialog extends Composite {

	@Inject
	UiBinder<Widget, LoginDialog> binder;

	@UiField
	public Modal loginModal;

	@UiField
	public TextBox username;

	@UiField
	public Form loginForm;

	@UiField
	public ControlGroup usernameGroup;

	@UiField
	public HelpInline usernameErrors;

	@UiField
	public PasswordTextBox password;

	@UiField
	public ControlGroup passwordGroup;

	@UiField
	public HelpInline passwordErrors;

	@UiField
	public Button loginButton;

	@UiField
	public HTML errorMsg;

	@Inject
	private Event<LoginRequestEvent> loginRequestEvent;

	public LoginDialog() {
	}

	/**
	 * Create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		loginModal.setStyleName("ph-Login-Modal", true);

		loginModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				username.getElement().focus();
			}
		});

		// When user presses enter in userName or Password try to submit form
		KeyDownHandler keyDownHandler = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				String unTxt = username.getText();
				String pwdTxt = password.getText();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
						&& unTxt != null && !unTxt.trim().equals("")
						&& pwdTxt != null && !pwdTxt.trim().equals("")) {
					onLoginClick(null);
				}
			}
		};
		username.addKeyDownHandler(keyDownHandler);
		password.addKeyDownHandler(keyDownHandler);
	}

	/**
	 * Shows the dialog
	 */
	public void show() {
		clearErrors();
		loginForm.reset();
		loginModal.show();
	}

	/**
	 * Handle the clicking of the Sign-In button
	 * 
	 * @param event
	 */
	@UiHandler("loginButton")
	public void onLoginClick(ClickEvent event) {
		// Clear previous errors
		clearErrors();

		// Validate and submit
		if (hasSubmissionErrors() == false) {
			loginRequestEvent.fire(new LoginRequestEvent(username.getValue(),
					password.getValue()));
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
		String pwd = password.getValue();

		if (uName == null || "".equals(uName.trim()) == true) {
			usernameGroup.setType(ControlGroupType.ERROR);
			usernameErrors.setText("Must enter a user name.");
			ret = true;
		}

		if (pwd == null || "".equals(pwd.trim()) == true) {
			passwordGroup.setType(ControlGroupType.ERROR);
			passwordErrors.setText("Must enter a password.");
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
		passwordGroup.setType(ControlGroupType.NONE);
		passwordErrors.setText("");
		errorMsg.setText("");
	}

	/**
	 * Responds to a successful login buy closing the modal dialog
	 * 
	 * @param loginResponseEvent
	 */
	protected void observesLoginResponseEvent(
			@Observes LoginResponseEvent loginResponseEvent) {
		loginModal.hide();
	}

	/**
	 * Responds to an invalid login by displaying an error
	 * 
	 * @param loginErrorEvent
	 */
	protected void observesLoginErrorEvent(
			@Observes LoginErrorEvent loginErrorEvent) {
		errorMsg.setText(loginErrorEvent.getErrorString());
	}

	/**
	 * Shows the dialog
	 * 
	 * @param showLoginModalEvent
	 */
	protected void observesShowLoginModalEvent(
			@Observes ShowLoginModalEvent showLoginModalEvent) {
		show();
	}
}
