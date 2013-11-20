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
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.book.BookdescriptionDetailResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;

/**
 * BookDescriptionDetailDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 18, 2013
 * 
 */
@Dependent
public class BookDescriptionDetailDialog extends Composite {

	@Inject
	UiBinder<Widget, BookDescriptionDetailDialog> binder;

	@UiField
	public Modal bookDescriptionModal;

	@UiField
	public WellForm bookDetailForm;

	@UiField
	public Legend legendTxt;

	@UiField
	public TextBox addedDate;

	@UiField
	public TextBox hoursToCreate;

	@UiField
	public TextBox assetType;

	@UiField
	public Button closeButton;

	/**
	 * Constructor
	 * 
	 */
	public BookDescriptionDetailDialog() {
	}

	@PostConstruct
	public void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		bookDescriptionModal.setStyleName("ph-NewBook-Modal", true);
		bookDescriptionModal.setStyleName(
				"ph-NewBook-BookdescriptionDetail-Modal-Size", true);

		// Set focus
		bookDescriptionModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
			}
		});

		// Set legend style
		legendTxt.getElement().setAttribute("style",
				"font-style: italic; font-weight: bold;");
	}

	/**
	 * Load the data
	 * 
	 * @param bookdescriptionDetailResponseEvent
	 */
	protected void observesBookdescriptionDetailResponseEvent(
			@Observes BookdescriptionDetailResponseEvent bookdescriptionDetailResponseEvent) {
		// Clear data
		bookDetailForm.reset();

		// Load data
		// Description
		String bookDescp = bookdescriptionDetailResponseEvent
				.getBookDescription();
		if (bookDescp != null) {
			legendTxt.getElement().setInnerText(bookDescp);
		}

		// Added date
		String addedDateStr = bookdescriptionDetailResponseEvent
				.getCreationDate();
		if (addedDateStr != null) {
			addedDate.setText(addedDateStr);
		}

		// Hrs
		String hrsStr = bookdescriptionDetailResponseEvent.getCreationHrs();
		if (hrsStr != null) {
			hoursToCreate
					.setText(hrsStr.equals("0") == true ? "Hours not available"
							: hrsStr);
		}

		// Type
		String itemType = bookdescriptionDetailResponseEvent.getItemType();
		if (itemType != null) {
			assetType.setText(itemType);
		}

		// Show dialog
		bookDescriptionModal.show();
	}

	/**
	 * If a service error occurs (could in response to the
	 * MostRecentUserBookCommentEvent event) then hide the dialog as the error
	 * dialog will pop-up.
	 * 
	 * @param serviceErrorEvent
	 */
	protected void observesServiceErrorEvent(
			@Observes ServiceErrorEvent serviceErrorEvent) {
		bookDescriptionModal.hide();
	}

	/**
	 * Close the error dialog.
	 * 
	 * @param event
	 */
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event) {
		bookDescriptionModal.hide();
	}
}