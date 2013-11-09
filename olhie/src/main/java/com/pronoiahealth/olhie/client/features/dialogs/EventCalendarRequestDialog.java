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

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.errai.databinding.client.BindableProxy;
import org.jboss.errai.databinding.client.api.DataBinder;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.impl.Validation;
import com.pronoiahealth.olhie.client.shared.events.eventcalendar.CalendarRequestSaveEvent;
import com.pronoiahealth.olhie.client.shared.events.eventcalendar.UserEmailRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.eventcalendar.UserEmailResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowEventCalendarRequestDialogEvent;
import com.pronoiahealth.olhie.client.shared.vo.CalendarRequest;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;

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

	@UiField
	public WellForm eventRequestForm;

	@UiField
	public ControlGroup titleGroup;

	@UiField
	public HelpInline titleErrors;

	@UiField
	public TextBox title;

	@UiField
	public ControlGroup eventDescriptionGroup;

	@UiField
	public HelpInline eventDescriptionErrors;

	@UiField
	public TextArea eventDescription;

	@UiField
	public ControlGroup contactEmailGroup;

	@UiField
	public HelpInline contactEmailErrors;

	@UiField
	public TextBox contactEmail;

	@UiField
	public Button submitButton;

	@Inject
	private DataBinder<CalendarRequest> dataBinder;

	@Inject
	private ClientUserToken clientUserToken;

	@Inject
	private Event<UserEmailRequestEvent> userEmailRequestEvent;
	
	@Inject
	private Event<CalendarRequestSaveEvent> calendarRequestSaveEvent;
	

	private String defaultEmailAddress = "";

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

		// Set up databinder
		// Initialize the binder
		dataBinder.bind(title, "title");
		dataBinder.bind(eventDescription, "description");
		dataBinder.bind(contactEmail, "contactEmail");
		dataBinder.getModel();

		// Set the maximum length for the description
		// This only works for browsers that support HTML5
		eventDescription.getElement().setAttribute("maxlength", "500");
		eventRequestModal.setStyleName("ph-BookComment-Modal", true);

		// Set focus when shown
		eventRequestModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				title.getElement().focus();
			}
		});
	}

	@UiHandler("submitButton")
	public void onSubmitButtonClick(ClickEvent event) {
		// Clear previous errors
		clearErrors();

		// Set the userId
		// This dialog only visible to logged in users
		CalendarRequest cr = dataBinder.getModel();
		cr.setRequestorUserId(clientUserToken.getUserId());

		// Validate and submit
		if (hasSubmissionErrors() == false) {
			calendarRequestSaveEvent.fire(new CalendarRequestSaveEvent(cr));
			eventRequestModal.hide();
		}
	}

	/**
	 * Check for form errors
	 * 
	 * @return
	 */
	private boolean hasSubmissionErrors() {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();

		// proxy needs to be unwrapped to work with the validator
		CalendarRequest cr = getUnwrappedModelData();
		Set<ConstraintViolation<CalendarRequest>> violations = validator
				.validate(cr);

		for (ConstraintViolation<CalendarRequest> cv : violations) {
			String prop = cv.getPropertyPath().toString();
			if (prop.equals("title")) {
				titleErrors.setText(cv.getMessage());
				titleGroup.setType(ControlGroupType.ERROR);
			} else if (prop.equals("description")) {
				eventDescriptionErrors.setText(cv.getMessage());
				eventDescriptionGroup.setType(ControlGroupType.ERROR);
			} else if (prop.equals("contactEmail")) {
				contactEmailErrors.setText(cv.getMessage());
				contactEmailGroup.setType(ControlGroupType.ERROR);
			}
		}

		// Return value
		if (violations.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Get the current data in the form. This requires "unwrapping" the data
	 * model from the formBinder.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public CalendarRequest getUnwrappedModelData() {
		return (CalendarRequest) ((BindableProxy<CalendarRequest>) dataBinder
				.getModel()).unwrap();
	}

	/**
	 * Ask the the current users email address then show the modal dialog. See
	 * observesUserEmailResponseEvent for processing the request.
	 * 
	 * @param showCommentsModalEvent
	 */
	protected void observesShowEventCalendarRequestDialogEvent(
			@Observes ShowEventCalendarRequestDialogEvent showEventCalendarRequestDialogEvent) {
		userEmailRequestEvent.fire(new UserEmailRequestEvent());
		show();
	}

	/**
	 * Get the current users id and display it
	 * 
	 * @param userEmailResponseEvent
	 */
	protected void observesUserEmailResponseEvent(
			@Observes UserEmailResponseEvent userEmailResponseEvent) {
		String userEmail = userEmailResponseEvent.getUserEmail();
		defaultEmailAddress = userEmail;
		dataBinder.getModel().setContactEmail(userEmail);
	}

	/**
	 * Shows the dialog
	 */
	private void show() {
		clearErrors();
		eventRequestForm.reset();
		eventRequestModal.show();
	}

	/**
	 * Clear form errors
	 */
	private void clearErrors() {
		titleGroup.setType(ControlGroupType.NONE);
		titleErrors.setText("");
		eventDescriptionGroup.setType(ControlGroupType.NONE);
		eventDescriptionErrors.setText("");
		contactEmailGroup.setType(ControlGroupType.NONE);
		contactEmail.setText(defaultEmailAddress);
	}
}
