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
package com.pronoiahealth.olhie.client.pages.newbook;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.AfterInitialization;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.SplitDropdownButton;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.BookCategoryListRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCategoryListResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCoverListRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCoverListResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewBookModalEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.client.widgets.bookcat.BookCategoryListWidget;
import com.pronoiahealth.olhie.client.widgets.bookcover.BookCoverListWidget;

/**
 * NewBookDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 5, 2013
 * 
 */
public class NewBookDialog extends Composite {

	@Inject
	UiBinder<Widget, NewBookDialog> binder;

	@UiField
	public Modal newBookModal;

	@UiField
	public SplitDropdownButton catagoryDropDown;

	@UiField
	public SplitDropdownButton bookCoverDropDown;

	@UiField
	public Button submitButton;

	private ClickHandler categoryClickedHandler;

	private ClickHandler coverClickedHandler;

	@Inject
	private Event<BookCategoryListRequestEvent> bookCategoryListRequestEvent;

	@Inject
	private Event<BookCoverListRequestEvent> bookCoverListRequestEvent;

	/**
	 * Constructor
	 * 
	 */
	public NewBookDialog() {
	}

	/**
	 * Init the display and set its style. Load the catagory list box
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		newBookModal.setStyleName("ph-NewBook-Modal", true);
		newBookModal.setStyleName("ph-NewBook-Modal-Size", true);

		// Set up click events
		// When the user clicks one of the categories then set the text of the
		// drop down to the category name
		categoryClickedHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object obj = event.getSource();
				if (obj instanceof IconAnchor) {
					IconAnchor a = (IconAnchor) obj;
					catagoryDropDown.setText(a.getName());
				}
			}
		};

		// Update the drop down after a cover is selected
		coverClickedHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object obj = event.getSource();
				if (obj instanceof IconAnchor) {
					IconAnchor a = (IconAnchor) obj;
					bookCoverDropDown.setText(a.getName());
				}
			}
		};
	}

	/**
	 * Call out for the list of books categories
	 */
	@AfterInitialization
	protected void afterInitialization() {
		bookCategoryListRequestEvent.fire(new BookCategoryListRequestEvent());
		bookCoverListRequestEvent.fire(new BookCoverListRequestEvent());
	}

	/**
	 * Watches for a list of categories to be returned and then loads the
	 * category list
	 * 
	 * @param bookCategoryListResponseEvent
	 */
	protected void observesBookCategoryListResponseEvent(
			@Observes BookCategoryListResponseEvent bookCategoryListResponseEvent) {
		List<BookCategory> bookCategories = bookCategoryListResponseEvent
				.getBokkCategories();
		if (bookCategories != null) {
			for (BookCategory cat : bookCategories) {
				BookCategoryListWidget nav = new BookCategoryListWidget(cat);
				nav.addClickHandler(categoryClickedHandler);
				catagoryDropDown.getMenuWiget().add(nav);
			}
		}
	}
	
	/**
	 * Watches for a list of covers to be returned and then loads the
	 * cover list
	 * 
	 * @param bookCoverListResponseEvent
	 */
	protected void observesBookCoverListResponseEvent(
			@Observes BookCoverListResponseEvent bookCoverListResponseEvent) {
		List<BookCover> bookCovers = bookCoverListResponseEvent.getBookCover();
		if (bookCovers != null) {
			for (BookCover cover : bookCovers) {
				BookCoverListWidget nav = new BookCoverListWidget(cover);
				nav.addClickHandler(coverClickedHandler);
				bookCoverDropDown.getMenuWiget().add(nav);
			}
		}
	}

	/**
	 * Shows the modal dialog
	 */
	public void show() {
		// Clear form

		// Show modal
		newBookModal.show();
	}

	/**
	 * Validates the form and fires a BookUpdateRequestEvent with the validated
	 * data.
	 * 
	 * @param clickEvt
	 */
	@UiHandler("submitButton")
	public void handleSubmitButtonClick(ClickEvent clickEvt) {
		try {
			// Validate form

			// Submit to server
		} catch (Exception e) {
			// Intentionally blank. The Form will post error
			// messages to the form during the validation process
		}
	}

	/**
	 * Shows the modal dialog
	 * 
	 * @param showNewBookModalEvent
	 */
	protected void observersShowNewBookModalEvent(
			@Observes ShowNewBookModalEvent showNewBookModalEvent) {
		show();
	}

}
