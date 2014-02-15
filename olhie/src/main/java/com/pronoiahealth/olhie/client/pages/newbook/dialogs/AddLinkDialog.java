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
package com.pronoiahealth.olhie.client.pages.newbook.dialogs;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.book.AddBookLinkAssetEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddLinkModalEvent;

/**
 * AddLinkDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 25, 2013
 * 
 */
@Dependent
public class AddLinkDialog extends Composite {

	@Inject
	UiBinder<Widget, AddLinkDialog> binder;

	@UiField
	public Modal linkModal;

	@UiField
	public Form linkForm;

	@UiField
	public TextBox description;

	@UiField
	public TextArea descriptionDetail;

	@UiField
	public ControlGroup descriptionCG;

	@UiField
	public ControlGroup descriptionDetailCG;

	@UiField
	public TextBox hoursOfWork;

	@UiField
	public ControlGroup hoursOfWorkCG;

	@UiField
	public TextArea link;

	@UiField
	public ControlGroup linkCG;

	@UiField
	public Button saveButton;

	@Inject
	private Event<AddBookLinkAssetEvent> addBookLinkAssetEvent;

	private String currentBookId;

	/**
	 * Constructor
	 * 
	 */
	public AddLinkDialog() {
	}

	/**
	 * Adds a key handler to the hoursOfWork input to only accept numbers. Sets
	 * the focus on the description field.
	 */
	@PostConstruct
	public void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Add keyboard handler to hours field
		hoursOfWork.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				int keyCode = event.getNativeEvent().getKeyCode();
				char charCode = event.getCharCode();
				if (!Character.isDigit(charCode)
						&& (keyCode != KeyCodes.KEY_BACKSPACE)) {
					((TextBox) event.getSource()).cancelKey();
				}
			}
		});

		// set focus
		linkModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				description.setFocus(true);
			}
		});
	}

	/**
	 * Watches for the event that will cause the dialog to be shown
	 * 
	 * @param showAddLinkModalEvent
	 */
	protected void observesShowAddLinkModalEvent(
			@Observes ShowAddLinkModalEvent showAddLinkModalEvent) {
		showModal(showAddLinkModalEvent.getBookId());
	}

	/**
	 * Checks the data, if it's ok then it sends a message to the server by way
	 * of the AddBookLinkAssetEvent event.
	 * 
	 * @param clickEvt
	 */
	@UiHandler("saveButton")
	public void saveButtonClicked(ClickEvent clickEvt) {
		boolean hasErrors = false;
		clearErrors();

		// Check description
		String desc = description.getText();
		if (desc != null) {
			int descLen = desc.length();
			if (descLen < 1 || descLen > 250) {
				hasErrors = true;
				descriptionCG.setType(ControlGroupType.ERROR);
			}
		} else {
			hasErrors = true;
			descriptionCG.setType(ControlGroupType.ERROR);
		}

		// Check descriptionDetail
		String descDetail = descriptionDetail.getText();
		if (descDetail != null) {
			int descLen = descDetail.length();
			if (descLen > 2048) {
				hasErrors = true;
				descriptionDetailCG.setType(ControlGroupType.ERROR);
			}
		}

		// Check link
		String linkStr = link.getText();
		if (linkStr != null) {
			int linklen = linkStr.length();
			if (linklen < 1 || linklen > 400) {
				hasErrors = true;
				linkCG.setType(ControlGroupType.ERROR);
			}
		} else {
			hasErrors = true;
			linkCG.setType(ControlGroupType.ERROR);
		}

		// Check hours of work
		String hours = hoursOfWork.getText();
		int hoursRet = 0;
		if (hours == null || hours.length() == 0) {
			hours = "0";
		} else {
			try {
				hoursRet = Integer.parseInt(hours);
			} catch (Exception e) {
				hasErrors = true;
				hoursOfWorkCG.setType(ControlGroupType.ERROR);
			}
		}

		if (hasErrors == false) {
			// Hide the modal
			linkModal.hide();

			// Fire away
			addBookLinkAssetEvent.fire(new AddBookLinkAssetEvent(currentBookId,
					desc, descDetail, linkStr, hoursRet));
		}
	}

	/**
	 * Show the modal dialog
	 */
	private void showModal(String bookId) {
		// clear fields
		linkForm.reset();

		// Clear any left over error messages
		clearErrors();

		// Set the book Id
		this.currentBookId = bookId;

		// Show the dialog
		linkModal.show();
	}

	/**
	 * Clear the dialog errors
	 */
	private void clearErrors() {
		descriptionCG.setType(ControlGroupType.NONE);
		descriptionDetailCG.setType(ControlGroupType.NONE);
		hoursOfWorkCG.setType(ControlGroupType.NONE);
		linkCG.setType(ControlGroupType.NONE);
	}

}
