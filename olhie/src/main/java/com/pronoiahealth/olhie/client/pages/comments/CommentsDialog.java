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
package com.pronoiahealth.olhie.client.pages.comments;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.databinding.client.api.DataBinder;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.constants.MiscConstants;
import com.pronoiahealth.olhie.client.shared.events.comments.CommentEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowCommentsModalEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.shared.vo.Comment;

/**
 * CommentsDialog.java<br/>
 * Responsibilities:<br/>
 * 1. Allows users to leave a comment<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public class CommentsDialog extends Composite {

	@Inject
	UiBinder<Widget, CommentsDialog> binder;

	@UiField
	public Modal commentModal;

	@UiField
	public Form commentForm;

	@UiField
	public TextArea comment;

	@UiField
	public ControlGroup commentGroup;

	@UiField
	public HelpInline commentErrors;

	@UiField
	public TextBox email;

	@UiField
	public ControlGroup emailGroup;

	@UiField
	public HelpInline emailErrors;

	@UiField
	public Button sendButton;

	@Inject
	private DataBinder<Comment> dataBinder;

	@Inject
	@New
	private Event<CommentEvent> newCommentEvent;

	@Inject
	private ClientUserToken clientUserToken;

	/**
	 * Constructor
	 * 
	 */
	public CommentsDialog() {
	}

	/**
	 * Create the gui via uiBinder. Initialize the DataBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Initialize the binder
		dataBinder.bind(email, "eMail");
		dataBinder.bind(comment, "comment");
		dataBinder.getModel();

		// Add custom style
		commentModal.setStyleName("ph-Comment-Modal", true);

		// Set focus when shown
		commentModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				comment.getElement().focus();
			}
		});
	}

	/**
	 * Shows the dialog
	 */
	public void show() {
		clearErrors();
		commentForm.reset();
		commentModal.show();
	}

	/**
	 * Handle the clicking of the send button
	 * 
	 * @param event
	 */
	@UiHandler("sendButton")
	public void onSendButtonClicked(ClickEvent event) {
		// Clear previous errors
		clearErrors();

		// Validate and submit
		if (hasSubmissionErrors() == false) {
			Comment com = dataBinder.getModel();
			if (clientUserToken.isLoggedIn() == true) {
				com.setUserId(clientUserToken.getUserId());
			}
			newCommentEvent.fire(new CommentEvent(com));
			commentModal.hide();
		}
	}

	/**
	 * Check for form errors
	 * 
	 * @return
	 */
	private boolean hasSubmissionErrors() {
		boolean ret = false;

		String com = comment.getValue();
		String mailAdd = email.getValue();

		if (com == null || "".equals(com.trim()) == true) {
			commentGroup.setType(ControlGroupType.ERROR);
			commentErrors.setText("Comment can't be blank");
			ret = true;
		}

		if (mailAdd != null && mailAdd.length() > 0) {
			if (mailAdd.trim().matches(MiscConstants.emailRegex) == false) {
				emailGroup.setType(ControlGroupType.ERROR);
				emailErrors.setText("Must enter a valid email address.");
				ret = true;
			}
		}

		// Return value after validation
		return ret;
	}

	/**
	 * Clear any previously displayed errors
	 */
	private void clearErrors() {
		commentGroup.setType(ControlGroupType.NONE);
		commentErrors.setText("");
		emailGroup.setType(ControlGroupType.NONE);
		emailErrors.setText("");
	}

	/**
	 * Show the modal dialog
	 * 
	 * @param showCommentsModalEvent
	 */
	protected void observesShowCommentsModalEvent(
			@Observes ShowCommentsModalEvent showCommentsModalEvent) {
		show();
	}

}
