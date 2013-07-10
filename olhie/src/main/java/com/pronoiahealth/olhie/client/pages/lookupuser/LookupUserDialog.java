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
package com.pronoiahealth.olhie.client.pages.lookupuser;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.Typeahead;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.local.ShowFindLoggedInUserEvent;

public class LookupUserDialog extends Composite {
	
	@Inject
	UiBinder<Widget, LookupUserDialog> binder;
	
	@UiField
	public Modal lookupUserModal;
	
	@UiField
	public Button submitButton;
	
	@UiField(provided=true)
	public Typeahead userNameTypeAhead;
	
	@UiField
	public TextBox userNameTxtBox;
	
	@Inject
	private LoggedInUsersSuggestOracle sOracle;

	public LookupUserDialog() {
	}
	
	/**
	 * Create the gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		sOracle.setSuggestWidget(userNameTxtBox);
		userNameTypeAhead = new Typeahead(sOracle);
		userNameTypeAhead.setDisplayItemCount(5);
		userNameTypeAhead.setMinLength(2);
		//userNameTxtBox = new TextBox();
		//userNameTypeAhead.add(userNameTxtBox);
		initWidget(binder.createAndBindUi(this));
		sOracle.setSuggestWidget(userNameTxtBox);
		lookupUserModal.setStyleName("ph-LookupUser-Modal", true);
		lookupUserModal.setStyleName("ph-LookupUser-Modal-Size", true);
	}
	
	/**
	 * Shows the modal dialog
	 */
	public void show() {
		lookupUserModal.show();
	}
	
	/**
	 * Sends an offer to the selected user and fires the OpenChatDialogEvent.
	 * 
	 * @param clickEvt
	 */
	@UiHandler("submitButton")
	public void handleSubmitButtonClick(ClickEvent clickEvt) {
		lookupUserModal.hide();
	}

	/**
	 * Shows the dialog
	 * 
	 * @param showRegisterModalEvent
	 */
	protected void observesShowFindLoggedInUserEvent(
			@Observes ShowFindLoggedInUserEvent showFindLoggedInUserEvent) {
		show();
	}
}
