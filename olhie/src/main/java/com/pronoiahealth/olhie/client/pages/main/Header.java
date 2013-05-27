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
package com.pronoiahealth.olhie.client.pages.main;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.VerticalDivider;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.Navigator;
import com.pronoiahealth.olhie.client.shared.events.LogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;

/**
 * Header.java<br/>
 * Responsibilities:<br/>
 * 1. Show top menu bar (Comments, Register, Login)<br/>
 * 2. Observes for the ClientUserUpdatedEvent to change the button display<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public class Header extends Composite {

	@Inject
	UiBinder<Widget, Header> binder;

	/**
	 * The application navigator which consolidates navigation
	 */
	@Inject
	private Navigator nav;

	@UiField
	public NavLink commentsLink;

	@UiField
	public NavLink registerLink;

	@UiField
	public NavLink loginLink;

	@UiField
	public Dropdown personDropDown;
	
	@UiField
	public NavLink logoutMenuItemLink;
	
	@UiField
	public NavLink editProfileMenuLink;

	@UiField
	public VerticalDivider regLogDivider;

	@Inject
	private ClientUserToken clientUserToken;
	
	@Inject
	private Event<LogoutRequestEvent> logoutRequestEvent;

	/**
	 * Default Constructor
	 * 
	 */
	public Header() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// When built hide the dropdown
		hidePersonDropDown();
	}

	/**
	 * Show the Login dialog
	 * 
	 * @param event
	 */
	@UiHandler("loginLink")
	public void loginLinkClicked(ClickEvent event) {
		nav.showLoginModalEvent();
	}

	/**
	 * Show the Comments dialog
	 * 
	 * @param event
	 */
	@UiHandler("commentsLink")
	public void commentsLinkClicked(ClickEvent event) {
		nav.showCommentsModalEvent();
	}

	/**
	 * Show the Register dialog
	 * 
	 * @param event
	 */
	@UiHandler("registerLink")
	public void registerLinkClicked(ClickEvent event) {
		nav.showRegisterModalEvent();
	}
	
	@UiHandler("logoutMenuItemLink") 
	public void logoutMenuItemLinkClicked(ClickEvent event) {
		logoutRequestEvent.fire(new LogoutRequestEvent());
	}

	/**
	 * Observes for the given event signaling that this component should hide
	 * the Login link and display the person
	 * 
	 * @param clientUserUpdatedEvent
	 */
	protected void observesClientUserUpdatedEvent(
			@Observes ClientUserUpdatedEvent clientUserUpdatedEvent) {
		if (clientUserToken.isLoggedIn() == true) {
			hideRegisterLink();
			hideRegLogDivider();
			hideLoginLink();
			ShowPersonDropDown(clientUserToken.getFullName());
		} else {
			hidePersonDropDown();
			showRegisterLink();
			showRegLogDivider();
			showLoginLink();
		}
	}

	/**
	 * Hides the person dropdown
	 */
	private void hidePersonDropDown() {
		personDropDown.removeStyleName("header-North-PersonDropDown-Show");
		personDropDown.setStyleName("header-North-PersonDropDown-Hide", true);
	}

	/**
	 * Shows persondropdown
	 * 
	 * @param userName
	 */
	private void ShowPersonDropDown(String userName) {
		personDropDown.removeStyleName("header-North-PersonDropDown-Hide");
		personDropDown.setStyleName("header-North-PersonDropDown-Show", true);
		personDropDown.setText(userName);
	}

	/**
	 * Hide the Login Link
	 */
	private void hideLoginLink() {
		loginLink.removeStyleName("header-North-LoginLink-Show");
		loginLink.setStyleName("header-North-LoginLink-Hide", true);
	}

	/**
	 * Shows the Login Link
	 */
	private void showLoginLink() {
		loginLink.removeStyleName("header-North-LoginLink-Hide");
		loginLink.setStyleName("header-North-LoginLink-Show", true);
	}

	/**
	 * Hide the Register Link
	 */
	private void hideRegisterLink() {
		registerLink.removeStyleName("header-North-RegisterLink-Show");
		registerLink.setStyleName("header-North-RegisterLink-Hide", true);
	}

	/**
	 * Shows the Register Link
	 */
	private void showRegisterLink() {
		registerLink.removeStyleName("header-North-RegisterLink-Hide");
		registerLink.setStyleName("header-North-registerLink-Show", true);
	}
	
	/**
	 * Hide the Register Link
	 */
	private void hideRegLogDivider() {
		regLogDivider.removeStyleName("header-North-RegLogDivider-Show");
		regLogDivider.setStyleName("header-North-RegLogDivider-Hide", true);
	}

	/**
	 * Shows the Register Link
	 */
	private void showRegLogDivider() {
		regLogDivider.removeStyleName("header-North-RegLogDivider-Hide");
		regLogDivider.setStyleName("header-North-RegLogDivider-Show", true);
	}

}
