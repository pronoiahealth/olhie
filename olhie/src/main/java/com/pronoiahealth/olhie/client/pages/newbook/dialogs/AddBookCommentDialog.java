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
import com.github.gwtbootstrap.client.ui.Legend;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.book.AddBookCommentRatingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddBookCommentModalEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.widgets.rating.StarRating;
import com.pronoiahealth.olhie.client.widgets.rating.StarRatingStarClickedHandler;

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
	public Legend legendTxt;

	@UiField
	public TextArea comment;

	@UiField
	public ControlGroup commentCG;

	@UiField
	public Button saveButton;

	@UiField
	public HTMLPanel starRatingPanel;

	@Inject
	private Event<AddBookCommentRatingEvent> addBookCommentEvent;

	@Inject
	private ClientUserToken userToken;

	private StarRating starRating;

	private int currentStarRating;

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
		commentModal.setStyleName("ph-BookComment-Modal", true);

		// Rating widget
		// Create star rating
		// When a star is created update the
		// current star rating
		starRating = new StarRating(5, false,
				new StarRatingStarClickedHandler() {
					@Override
					public void startClicked(int star) {
						currentStarRating = star;
					}
				});

		// Irritating Chrome issue fix
		starRating.getElement().setAttribute("style", "display: inline-block;");

		// Add it to the display
		starRatingPanel.add(starRating);

		// Set focus
		commentModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				comment.setFocus(true);
			}
		});

		// Set legend style
		legendTxt.getElement().setAttribute("style",
				"font-style: italic; font-weight: bold;");
	}

	/**
	 * Show the dialog
	 * 
	 * @param showBookCommentModalEvent
	 */
	protected void observesShowAddBookCommentModalEvent(
			@Observes ShowAddBookCommentModalEvent showAddBookCommentModalEvent) {
		comment.setText("");
		currentStarRating = 0;
		starRating.setRating(0);
		currentBookId = showAddBookCommentModalEvent.getBookId();
		String bookTitle = showAddBookCommentModalEvent.getBookTitle();
		legendTxt.getElement().setInnerText(
				bookTitle.length() > 30 ? bookTitle.substring(0, 29) + "..."
						: bookTitle);

		// Should we first check to see if this user has made a comment about
		// this book?
		// Call new BookFindUserCommentAndRatingForBookEvent
		// ..........

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
			addBookCommentEvent.fire(new AddBookCommentRatingEvent(
					currentBookId, commentTxt, currentStarRating));
		} else {
			commentCG.setType(ControlGroupType.ERROR);
		}
	}
}
