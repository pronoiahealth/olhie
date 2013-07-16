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
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.Typeahead;
import com.github.gwtbootstrap.client.ui.Typeahead.UpdaterCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.events.local.ShowFindLoggedInUserEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.CreateOfferEvent;
import com.pronoiahealth.olhie.client.shared.vo.ConnectedUser;
import com.pronoiahealth.olhie.client.widgets.suggestoracle.GenericMultiWordSuggestion;

public class LookupUserDialog extends Composite {

	@Inject
	UiBinder<Widget, LookupUserDialog> binder;

	@UiField
	public Modal lookupUserModal;

	@UiField
	public Button submitButton;

	@UiField(provided = true)
	public Typeahead userNameTypeAhead;

	@UiField
	public TextBox userNameTxtBox;

	@Inject
	private LoggedInUsersSuggestOracle sOracle;

	@Inject
	private Event<CreateOfferEvent> createOfferEvent;

	private ConnectedUser currentSelection;

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
		initWidget(binder.createAndBindUi(this));
		sOracle.setSuggestWidget(userNameTxtBox);
		userNameTypeAhead.setUpdaterCallback(new UpdaterCallback() {
			@Override
			public String onSelection(Suggestion selectedSuggestion) {
				GenericMultiWordSuggestion<ConnectedUser> sug = (GenericMultiWordSuggestion<ConnectedUser>) selectedSuggestion;
				currentSelection = (ConnectedUser) sug.getPojo();
				return sug.getDisplayString();
			}
		});
		lookupUserModal.setStyleName("ph-LookupUser-Modal", true);
		lookupUserModal.setStyleName("ph-LookupUser-Modal-Size", true);
	}

	/**
	 * Shows the modal dialog
	 */
	public void show() {
		userNameTxtBox.setText("");
		lookupUserModal.show();
	}

	/**
	 * Sends an offer to the selected user and fires the OpenChatDialogEvent.
	 * 
	 * @param clickEvt
	 */
	@UiHandler("submitButton")
	public void handleSubmitButtonClick(ClickEvent clickEvt) {
		String txtBxSel = userNameTxtBox.getText();
		if (txtBxSel != null && txtBxSel.length() > 0
				&& currentSelection != null
				&& currentSelection.getUserName().length() > 0) {
			lookupUserModal.hide();
			createOfferEvent.fire(new CreateOfferEvent(currentSelection
					.getUserId(), currentSelection.getUserName(),
					OfferTypeEnum.CHAT));
		}
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
