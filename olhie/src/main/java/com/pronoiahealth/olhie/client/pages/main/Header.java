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
import com.github.gwtbootstrap.client.ui.NavWidget;
import com.github.gwtbootstrap.client.ui.VerticalDivider;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.shared.events.LogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageHidingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageShowingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowCommentsModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowLoginModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewBookModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowRegisterModalEvent;
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
	private PageNavigator nav;

	@UiField
	public NavLink commentsLink;

	@UiField
	public NavLink registerLink;

	@UiField
	public NavLink loginLink;

	@UiField
	public NavWidget addBookLink;

	@UiField
	public Dropdown personDropDown;

	@UiField
	public NavLink logoutMenuItemLink;

	@UiField
	public NavLink editProfileMenuLink;

	@UiField
	public VerticalDivider regBookAddDivider;

	@UiField
	public VerticalDivider addBookPersonDivider;

	@Inject
	private ClientUserToken clientUserToken;

	@Inject
	private Event<LogoutRequestEvent> logoutRequestEvent;

	@Inject
	private Event<ShowNewBookModalEvent> showNewBookModalEvent;

	@Inject
	private Event<ShowLoginModalEvent> showLoginEvent;

	@Inject
	private Event<ShowRegisterModalEvent> showRegisterEvent;

	@Inject
	private Event<ShowCommentsModalEvent> showCommentsEvent;

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

		// Add formatting to Add link
		addBookLink.getAnchor().getElement()
				.setAttribute("style", "padding: 0px");

		// When built hide the dropdown
		setNotLoggedIn();
	}

	/**
	 * Show the Login dialog
	 * 
	 * @param event
	 */
	@UiHandler("loginLink")
	public void loginLinkClicked(ClickEvent event) {
		showLoginEvent.fire(new ShowLoginModalEvent());
	}

	/**
	 * Show the Comments dialog
	 * 
	 * @param event
	 */
	@UiHandler("commentsLink")
	public void commentsLinkClicked(ClickEvent event) {
		showCommentsEvent.fire(new ShowCommentsModalEvent());
	}

	/**
	 * Show the Register dialog
	 * 
	 * @param event
	 */
	@UiHandler("registerLink")
	public void registerLinkClicked(ClickEvent event) {
		showRegisterEvent.fire(new ShowRegisterModalEvent());
	}

	/**
	 * Show the Add Book dialog
	 * 
	 * @param event
	 */
	@UiHandler("addBookLink")
	public void addBookLinkClicked(ClickEvent event) {
		showNewBookModalEvent.fire(new ShowNewBookModalEvent());
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
			setLoggedIn();
		} else {
			setNotLoggedIn();
		}
	}

	/**
	 * Observes for the new book page hiding event and then shows the add book
	 * selection.
	 * 
	 * @param newBookPageHidingEvent
	 */
	protected void observesNewBookPageHidingEvent(
			@Observes NewBookPageHidingEvent newBookPageHidingEvent) {
		showAddBook();
		showAddBookPersonDivider();
	}

	/**
	 * Observes for the new book page showing event and then hides the add book
	 * selection.
	 * 
	 * @param newBookPageShowingEvent
	 */
	protected void observesNewBookPageShowingEvent(
			@Observes NewBookPageShowingEvent newBookPageShowingEvent) {
		hideAddBook();
		hideAddBookPersonDivider();
	}

	/**
	 * Set the options up for a user not being logged in
	 */
	private void setNotLoggedIn() {
		hidePersonDropDown();
		showRegisterLink();
		showRegBookAddDivider();
		hideAddBookPersonDivider();
		showLoginLink();
		hideAddBook();
	}

	/**
	 * When a user is logged in set up their menu
	 */
	private void setLoggedIn() {
		hideRegisterLink();
		hideRegBookAddDivider();
		showAddBookPersonDivider();
		hideLoginLink();
		showAddBook();
		showPersonDropDown(clientUserToken.getFullName());
	}

	/**
	 * Shows the add book selection only if the user is at least an author
	 */
	private void showAddBook() {
		if (clientUserToken.isRoleAtLeastAuthor() == true) {
			addBookLink.removeStyleName("header-North-AddBookLink-Hide");
			addBookLink.setStyleName("header-North-AddBookLink-Show", true);
		}
	}

	/**
	 * Hides the add book selection
	 */
	private void hideAddBook() {
		addBookLink.removeStyleName("header-North-AddBookLink-Show");
		addBookLink.setStyleName("header-North-AddBookLink-Hide", true);
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
	private void showPersonDropDown(String userName) {
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
		registerLink.setStyleName("header-North-RegisterLink-Show", true);
	}

	/**
	 * Hide the divider between the Book Add and Log in link
	 */
	private void hideRegBookAddDivider() {
		regBookAddDivider
				.removeStyleName("header-North-RegBookAddDivider-Show");
		regBookAddDivider.setStyleName("header-North-RegBookAddDivider-Hide",
				true);
	}

	/**
	 * Shows the divider between the Book Add and Log in link
	 */
	private void showRegBookAddDivider() {
		regBookAddDivider
				.removeStyleName("header-North-RegBookAddDivider-Hide");
		regBookAddDivider.setStyleName("header-North-RegBookAddDivider-Show",
				true);
	}

	/**
	 * Hide the divider between the Book Add and person link
	 */
	private void hideAddBookPersonDivider() {
		addBookPersonDivider
				.removeStyleName("header-North-AddBookPersonDivider-Show");
		addBookPersonDivider.setStyleName(
				"header-North-AddBookPersonDivider-Hide", true);
	}

	/**
	 * Shows the divider between the Book Add and person link
	 */
	private void showAddBookPersonDivider() {
		addBookPersonDivider
				.removeStyleName("header-North-AddBookPersonDivider-Hide");
		addBookPersonDivider.setStyleName(
				"header-North-AddBookPersonDivider-Show", true);
	}

}
