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
package com.pronoiahealth.olhie.client.pages.error;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.ServiceErrorEvent;

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
	}

	/**
	 * Observes ServiceErrorEvent
	 * 
	 * @param serviceErrorEvent
	 */
	protected void observesServiceErrorEvent(
			@Observes ServiceErrorEvent serviceErrorEvent) {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		errorMsg.setHTML(builder.appendEscaped(serviceErrorEvent.getErrorMsg())
				.toSafeHtml());
		errorDisplayModal.show();
	}

	/**
	 * Observes ClientErrorEvent
	 * 
	 * @param clientErrorEvent
	 */
	protected void observesClientErrorEvent(
			@Observes ClientErrorEvent clientErrorEvent) {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		errorMsg.setHTML(builder.appendEscaped(clientErrorEvent.getMessage())
				.toSafeHtml());
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
	}

}
