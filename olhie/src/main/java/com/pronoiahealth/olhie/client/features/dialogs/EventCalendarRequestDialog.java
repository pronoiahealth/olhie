package com.pronoiahealth.olhie.client.features.dialogs;

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

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.timepicker.client.ui.TimeBoxAppended;
import com.github.gwtbootstrap.timepicker.client.ui.base.HasTemplate.Template;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.local.ShowEventCalendarRequestDialogEvent;

/**
 * EventCalendarRequestDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 30, 2013
 * 
 */
@Dependent
public class EventCalendarRequestDialog extends Composite {

	@Inject
	UiBinder<Widget, EventCalendarRequestDialog> binder;

	@UiField
	public Modal eventRequestModal;

	/*
	@UiField
	public WellForm eventRequestForm;
	
	@UiField
	public ControlGroup titleGroup;
	
	@UiField
	public HelpInline titleErrors;
	
	@UiField
	public TextBox title;
	
	@UiField
	public ControlGroup eventStartDateTimeGroup;
	
	@UiField
	public HelpInline eventStartDateTimeErrors;
	
	@UiField
	public DateTimeBoxAppended eventStartDateTime;
	
	*/
	
	@UiField
	public TimeBoxAppended eventStartTime;
	
	/*
	@UiField
	public ControlGroup eventEndDateTimeGroup;
	
	@UiField
	public HelpInline eventEndDateTimeErrors;
	
	@UiField
	public DateTimeBoxAppended eventEndDateTime;
	
	@UiField
	public ControlGroup eventDescriptionGroup;
	
	@UiField
	public HelpInline eventDescriptionErrors;
	
	@UiField
	public TextArea eventDescription;

	@Inject
	private ClientUserToken userToken;
	
	*/

	/**
	 * Constructor
	 * 
	 */
	public EventCalendarRequestDialog() {
	}

	/**
	 * Init the display and set its style. Load the category list box
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		this.eventRequestModal.setStyleName("ph-BookComment-Modal", true);
		
		// Start time
		eventStartTime.setShowInputs(true);
		eventStartTime.setMeridian(true);
		eventStartTime.setTemplate(Template.DROPDOWN);
	}

	/**
	 * Show the modal dialog
	 * 
	 * @param showCommentsModalEvent
	 */
	protected void observesShowEventCalendarRequestDialogEvent(
			@Observes ShowEventCalendarRequestDialogEvent showEventCalendarRequestDialogEvent) {
		show();
	}

	/**
	 * Shows the dialog
	 */
	private void show() {
		/*
		clearErrors();
		eventRequestForm.reset();
		*/
		eventRequestModal.show();
	}

	/**
	 * Clear form errors
	 */
	private void clearErrors() {
		/*
		titleGroup.setType(ControlGroupType.NONE);
		titleErrors.setText("");
		eventStartDateTimeGroup.setType(ControlGroupType.NONE);
		eventStartDateTimeErrors.setText("");
		eventEndDateTimeGroup.setType(ControlGroupType.NONE);
		eventEndDateTimeErrors.setText("");
		eventDescriptionGroup.setType(ControlGroupType.NONE);
		eventDescriptionErrors.setText("");
		*/
	}

}
