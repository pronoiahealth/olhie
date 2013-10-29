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

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.CommunicationErrorEvent;

/**
 * ErrorDisplayDialog.java<br/>
 * Responsibilities:<br/>
 * 1. Displays errors in response to the ServiceErrorEvent<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Dependent
public class ErrorDisplayDialog extends Composite {

	@Inject
	UiBinder<Widget, ErrorDisplayDialog> binder;

	@UiField
	public Modal errorDisplayModal;

	@UiField
	public ScrollPanel errorMsgPanel;

	@UiField
	public HTML errorMsg;

	@UiField
	public Button closeButton;

	@Inject
	private MessageBus bus;

	private boolean reloadWindow;

	/**
	 * Constructor
	 * 
	 */
	public ErrorDisplayDialog() {
	}

	/**
	 * Create the gui via uiBinder. Initialize the DataBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		errorDisplayModal.setStyleName("ph-Error-Modal", true);

		// Set up message listener for JAX-RS errors
		bus.subscribe("ClientErrorDialog", new MessageCallback() {
			@Override
			public void callback(Message message) {
				String msg = message.get(String.class, "Message");
				displayError(msg, false);
			}
		});
	}

	/**
	 * Observes ServiceErrorEvent
	 * 
	 * @param serviceErrorEvent
	 */
	protected void observesServiceErrorEvent(
			@Observes ServiceErrorEvent serviceErrorEvent) {
		displayError(serviceErrorEvent.getErrorMsg(), false);
	}

	/**
	 * Observes ClientErrorEvent
	 * 
	 * @param clientErrorEvent
	 */
	protected void observesClientErrorEvent(
			@Observes ClientErrorEvent clientErrorEvent) {
		displayError(clientErrorEvent.getMessage(), false);
	}

	/**
	 * Observes CommunicationErrorEvent
	 * 
	 * @param communicationErrorEvent
	 */
	protected void observesCommunicationErrorEvent(
			@Observes CommunicationErrorEvent communicationErrorEvent) {
		boolean reloadWindow = communicationErrorEvent.isReloadWindow();
		displayError(communicationErrorEvent.getMsg(), reloadWindow);
	}

	private void displayError(String msg, boolean reloadWindow) {
		this.reloadWindow = reloadWindow;
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		if (msg == null) {
			msg = "No error message was reported.";
		}
		errorMsg.setHTML(builder.appendEscaped(msg).toSafeHtml());
		errorDisplayModal.show();
	}

	/**
	 * Close the error dialog.
	 * 
	 * @param event
	 */
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event) {
		errorDisplayModal.hide();

		// Should the window be reloaded as a result of this error
		if (reloadWindow == true) {
			Window.Location.reload();
		}
	}

}
