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
package com.pronoiahealth.olhie.client;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pronoiahealth.olhie.client.pages.main.MainPage;
import com.pronoiahealth.olhie.client.shared.events.NewsItemsRequestEvent;
import com.pronoiahealth.olhie.resources.OlhieResourceInjector;

/**
 * Responsible for:<br />
 * 1. Entry point <br />
 * 2. Custom resource injection<br />
 * 
 * @author John DeStefano
 * @Version 1.0
 * @since 1/4/2013
 */
@EntryPoint
public class Olhie {

	@Inject
	private MainPage mainPage;

	@Inject
	private Event<NewsItemsRequestEvent> newsItemsRequestEvent;

	/**
	 * Constructor
	 * 
	 */
	public Olhie() {
		RestClient.setApplicationRoot("/olhie/rest");
	}

	/**
	 * Inject all css and js resources and attach the main page to the root
	 * panel
	 */
	@PostConstruct
	public void postConstruct() {
		// Inject required resources
		OlhieResourceInjector.configure();

		// Layout main panel
		RootLayoutPanel.get().add(mainPage);
	}
}
