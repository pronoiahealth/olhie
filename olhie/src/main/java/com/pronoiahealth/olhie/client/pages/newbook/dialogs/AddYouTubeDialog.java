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
import com.pronoiahealth.olhie.client.shared.events.book.AddBookYouTubeAssetEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddYouTubeModalEvent;

/**
 * AddYouTubeDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 23, 2013
 * 
 */
@Dependent
public class AddYouTubeDialog extends Composite {

	@Inject
	UiBinder<Widget, AddYouTubeDialog> binder;

	@UiField
	public Modal addYouTubeModal;

	@UiField
	public Form addYouTubeForm;

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
	public TextArea youTubeLink;

	@UiField
	public ControlGroup youTubeLinkCG;

	@UiField
	public Button saveButton;

	@Inject
	private Event<AddBookYouTubeAssetEvent> addBookYouTubeAssetEvent;

	private String currentBookId;

	/**
	 * Constructor
	 * 
	 */
	public AddYouTubeDialog() {
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
		addYouTubeModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				description.setFocus(true);
			}
		});
	}

	/**
	 * Watches for the event that will cause the dialog to be shown
	 * 
	 * @param showAddYouTubeModalEvent
	 */
	protected void observesShowAddYouTubeModalEvent(
			@Observes ShowAddYouTubeModalEvent showAddYouTubeModalEvent) {
		showModal(showAddYouTubeModalEvent.getBookId());
	}

	/**
	 * Checks the data, if it's ok then it sends a message to the server by way
	 * of the AddBookYouTubeAssetEvent event.
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

		// Check youtube link
		String youTube = youTubeLink.getText();
		if (youTube != null) {
			int youTubeLen = youTube.length();
			if (youTubeLen < 1 || youTubeLen > 400) {
				hasErrors = true;
				youTubeLinkCG.setType(ControlGroupType.ERROR);
			}
		} else {
			hasErrors = true;
			youTubeLinkCG.setType(ControlGroupType.ERROR);
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
			addYouTubeModal.hide();

			// Fire away
			addBookYouTubeAssetEvent.fire(new AddBookYouTubeAssetEvent(
					currentBookId, desc, descDetail, youTube,
					hoursRet));
		}
	}

	/**
	 * Show the modal dialog
	 */
	private void showModal(String bookId) {
		// clear fields
		addYouTubeForm.reset();

		// Clear any left over error messages
		clearErrors();

		// Set the book Id
		this.currentBookId = bookId;

		// Show the dialog
		addYouTubeModal.show();
	}

	/**
	 * Clear the dialog errors
	 */
	private void clearErrors() {
		descriptionCG.setType(ControlGroupType.NONE);
		descriptionDetailCG.setType(ControlGroupType.NONE);
		hoursOfWorkCG.setType(ControlGroupType.NONE);
		youTubeLinkCG.setType(ControlGroupType.NONE);
	}

}
