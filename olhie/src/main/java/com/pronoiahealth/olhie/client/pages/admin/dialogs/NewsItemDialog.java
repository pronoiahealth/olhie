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
package com.pronoiahealth.olhie.client.pages.admin.dialogs;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
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
import com.pronoiahealth.olhie.client.shared.events.admin.AddNewsItemEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewNewsItemModalEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.shared.vo.NewsItem;

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
@Dependent
public class NewsItemDialog extends Composite {

	@Inject
	UiBinder<Widget, NewsItemDialog> binder;

	@UiField
	public Modal newsItemModal;

	@UiField
	public Form newsItemForm;

	@UiField
	public TextArea title;

	@UiField
	public ControlGroup titleGroup;

	@UiField
	public HelpInline titleErrors;

	@UiField
	public TextArea href;

	@UiField
	public ControlGroup hrefGroup;

	@UiField
	public HelpInline hrefErrors;

	@UiField
	public TextArea story;

	@UiField
	public ControlGroup storyGroup;

	@UiField
	public HelpInline storyErrors;

	@UiField
	public Button saveButton;

	@Inject
	private DataBinder<NewsItem> dataBinder;

	@Inject
	@New
	private Event<AddNewsItemEvent> newEvent;

	@Inject
	private ClientUserToken clientUserToken;

	/**
	 * Constructor
	 * 
	 */
	public NewsItemDialog() {
	}

	/**
	 * Create the gui via uiBinder. Initialize the DataBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Initialize the binder
		dataBinder.bind(href, "href");
		dataBinder.bind(title, "title");
		dataBinder.bind(story, "story");
		dataBinder.getModel();

		// Add custom style
		newsItemModal.setStyleName("ph-Comment-Modal", true);

		// Set focus when shown
		newsItemModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				title.getElement().focus();
			}
		});
	}

	/**
	 * Shows the dialog
	 */
	public void show() {
		clearErrors();
		newsItemForm.reset();
		newsItemModal.show();
	}

	/**
	 * Handle the clicking of the send button
	 * 
	 * @param event
	 */
	@UiHandler("saveButton")
	public void onSendButtonClicked(ClickEvent event) {
		// Clear previous errors
		clearErrors();

		// Validate and submit
		if (hasSubmissionErrors() == false) {
			NewsItem com = dataBinder.getModel();
			com.setAuthorId(clientUserToken.getUserId());
			com.setActive(true);
			com.setDatePublished(new Date());
			newEvent.fire(new AddNewsItemEvent(com));
			newsItemModal.hide();
		}
	}

	/**
	 * Check for form errors
	 * 
	 * @return
	 */
	private boolean hasSubmissionErrors() {
		boolean ret = false;

		String titleStr = title.getValue();
		String hrefStr = href.getValue();
		String storyStr = story.getValue();

		if (titleStr == null || "".equals(titleStr.trim()) == true) {
			titleGroup.setType(ControlGroupType.ERROR);
			titleErrors.setText("Title can't be blank");
			ret = true;
		}

		if (hrefStr == null || "".equals(hrefStr.trim()) == true) {
			hrefGroup.setType(ControlGroupType.ERROR);
			hrefErrors.setText("HREF can't be blank");
			ret = true;
		}
		
		if (storyStr == null || "".equals(storyStr.trim()) == true) {
			storyGroup.setType(ControlGroupType.ERROR);
			storyErrors.setText("Story can't be blank");
			ret = true;
		}

		// Return value after validation
		return ret;
	}

	/**
	 * Clear any previously displayed errors
	 */
	private void clearErrors() {
		titleGroup.setType(ControlGroupType.NONE);
		titleErrors.setText("");
		hrefGroup.setType(ControlGroupType.NONE);
		hrefErrors.setText("");
		storyGroup.setType(ControlGroupType.NONE);
		storyErrors.setText("");
	}

	/**
	 * Show the modal dialog
	 * 
	 * @param showCommentsModalEvent
	 */
	protected void observesShowNewNewsItemModalEvent(
			@Observes ShowNewNewsItemModalEvent showNewNewsItemModalEvent) {
		show();
	}

}
