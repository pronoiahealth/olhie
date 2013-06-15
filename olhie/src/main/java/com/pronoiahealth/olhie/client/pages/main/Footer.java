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
package com.pronoiahealth.olhie.client.pages.main;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.ClientMessageBus;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.widgets.BusStatusWidget;

/**
 * Footer.java<br/>
 * Responsibilities:<br/>
 * 1. Shows the footer of the main layout using Errai pure html templating
 * system<br/>
 * 2. Registers the bus status widget with the client message bus.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public class Footer extends Composite {
	@Inject
	UiBinder<Widget, Footer> binder;

	@UiField
	public HTMLPanel statusWidgetContainer;

	@Inject
	private BusStatusWidget statusWidget;

	private ClientMessageBus cBus = (ClientMessageBus) ErraiBus.get();

	/**
	 * Default Constructor
	 * 
	 */
	public Footer() {
	}

	/**
	 * Register the bus lifecycle listener class
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Add the status widget
		cBus.addLifecycleListener(statusWidget);
		statusWidgetContainer.add(statusWidget);
	}

}
