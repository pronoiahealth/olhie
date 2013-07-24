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
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.book.AddBookCommentEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowBookCommentModalEvent;

/**
 * CommentDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 20, 2013
 * 
 */
public class AddBookCommentDialog extends Composite {

	@Inject
	UiBinder<Widget, AddBookCommentDialog> binder;

	@UiField
	public Modal commentModal;

	@UiField
	public TextArea comment;

	@UiField
	public ControlGroup commentCG;

	@UiField
	public Button saveButton;

	@Inject
	private Event<AddBookCommentEvent> addBookCommentEvent;

	/**
	 * The current book ID
	 */
	private String currentBookId;

	/**
	 * Constructor
	 * 
	 */
	public AddBookCommentDialog() {
	}

	@PostConstruct
	public void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		
		// Set focus
		commentModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				comment.setFocus(true);
			}
		});
	}

	/**
	 * Show the dialog
	 * 
	 * @param showBookCommentModalEvent
	 */
	protected void observesShowBookCommentModalEvent(
			@Observes ShowBookCommentModalEvent showBookCommentModalEvent) {
		comment.setText("");
		currentBookId = showBookCommentModalEvent.getBookId();
		commentModal.show();
	}

	/**
	 * Save the comment if there is a comment.
	 * 
	 * @param event
	 */
	@UiHandler("saveButton")
	public void saveButtonClicked(ClickEvent event) {
		String commentTxt = comment.getText();
		if (commentTxt != null && commentTxt.length() > 0) {
			commentModal.hide();
			addBookCommentEvent.fire(new AddBookCommentEvent(currentBookId,
					commentTxt));
		} else {
			commentCG.setType(ControlGroupType.ERROR);
		}
	}
}
