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
package com.pronoiahealth.olhie.client.features.dialogs;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindCommentsEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindCommentsResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowBookCommentsModalEvent;
import com.pronoiahealth.olhie.client.shared.vo.Bookcomment;

/**
 * ViewBookCommentsDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 20, 2013
 * 
 */
@Dependent
public class ViewBookCommentsDialog extends Composite {

	@Inject
	UiBinder<Widget, ViewBookCommentsDialog> binder;

	@UiField
	public Modal viewCommentModal;

	@UiField
	public HTMLPanel commentsContainer;

	private Element commentsTable;

	@UiField
	public Button closeButton;

	@Inject
	private Event<BookFindCommentsEvent> bookFindCommentsEvent;

	private String currentBookId;

	@Inject
	private Instance<BookCommentRowWidget> bookCommentRowWidgetFac;

	/**
	 * Constructor
	 * 
	 */
	public ViewBookCommentsDialog() {
	}

	/**
	 * Build the commentTable element
	 */
	@PostConstruct
	public void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		commentsTable = DOM.createDiv();
		commentsTable.setAttribute("style", "display: table;");
		commentsTable.setClassName("ph-NewBook-ViewComments-BoxShade");
		commentsContainer.getElement().appendChild(commentsTable);
	}

	/**
	 * Show the dialog
	 * 
	 * @param showBookCommentModalEvent
	 */
	protected void observesShowBookCommentsModalEvent(
			@Observes ShowBookCommentsModalEvent showShowBookCommentsModalEvent) {
		currentBookId = showShowBookCommentsModalEvent.getBookId();
		viewCommentModal.show();

		// Send request for comments
		bookFindCommentsEvent.fire(new BookFindCommentsEvent(currentBookId));
	}

	/**
	 * Load the list with the returned values
	 * 
	 * @param bookFindCommentsResponseEvent
	 */
	protected void observesBookFindCommentsResponseEvent(
			@Observes BookFindCommentsResponseEvent bookFindCommentsResponseEvent) {
		// Clear the table
		Node node = null;
		while ((node = commentsTable.getFirstChild()) != null) {
			commentsTable.removeChild(node);
		}

		List<Bookcomment> retLst = bookFindCommentsResponseEvent.getComments();
		if (retLst != null && retLst.size() > 0) {
			for (Bookcomment comment : retLst) {
				BookCommentRowWidget row = bookCommentRowWidgetFac.get();
				commentsTable.appendChild(row.setData(comment.getComment(),
						comment.getRating()));
			}
		}
	}

	/**
	 * Close the dialog
	 * 
	 * @param event
	 */
	@UiHandler("closeButton")
	public void closeDialog(ClickEvent event) {
		viewCommentModal.hide();
	}
}
