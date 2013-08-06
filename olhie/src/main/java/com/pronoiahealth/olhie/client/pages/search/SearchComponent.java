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
package com.pronoiahealth.olhie.client.pages.search;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.pages.AbstractComposite;
import com.pronoiahealth.olhie.client.shared.events.book.BookSearchEvent;

/**
 * SearchComponent.java<br/>
 * Responsibilities:<br/>
 * 1. Displays the search form on the Search screen<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public class SearchComponent extends AbstractComposite {

	@Inject
	UiBinder<Widget, SearchComponent> binder;

	@UiField
	public WellForm searchForm;

	@UiField
	public TextBox searchQryBox;

	@UiField
	public ListBox returnVal;

	@UiField
	public Button findButton;

	@Inject
	private Event<BookSearchEvent> newBookSearchEvent;

	/**
	 * Constructor
	 * 
	 */
	public SearchComponent() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		searchForm.setStyleName("ph-SearchComponent-Form", true);

		// Some style stuff
		findButton.getElement()
				.setAttribute("style", "border-radius: 0 0 0 0;");
		searchQryBox.getElement().setAttribute("style",
				"border-radius: 0 0 0 0;");
		returnVal.getElement().setAttribute("style", "width: 75px; border-radius: 0 0 0 0;");

		// Add an enter key handler
		searchQryBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				String qry = searchQryBox.getText();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
						&& qry != null && !qry.trim().equals("")) {
					handleFindButtonClick(null);
				}
			}
		});
	}

	/**
	 * Validates the form and fires a BookSearchEvent with the validated data.
	 * 
	 * @param clickEvt
	 */
	@UiHandler("findButton")
	public void handleFindButtonClick(ClickEvent clickEvt) {
		try {
			// Grab the search text
			String searchText = searchQryBox.getText();
			String rowSelection = returnVal.getValue(returnVal.getSelectedIndex());
			int rows = -1; // -1 indicates return all rows
			try {
				rows = Integer.parseInt(rowSelection);
			} catch (Exception e) {
				e.printStackTrace();
			}

			boolean isValid = validateSearchText(searchText);

			if (isValid) {
				// Fire event and send the results to the
				// SearchResultsComponent.
				newBookSearchEvent.fire(new BookSearchEvent(searchText, rows));
			}

		} catch (Exception e) {
			// Intentionally blank. The Form will post error
			// messages to the form during the validation process
		}
	}

	/**
	 * @param _searchText
	 * @return
	 */
	public boolean validateSearchText(String searchText) {
		if (searchText != null && searchText.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setFocusOnSearchQryBox() {
		searchQryBox.setFocus(true);
	}

}
