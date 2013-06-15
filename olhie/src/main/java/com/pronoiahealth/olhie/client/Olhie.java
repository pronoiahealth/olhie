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

import org.jboss.errai.bus.client.api.ClientMessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pronoiahealth.olhie.client.pages.main.MainPage;
import com.pronoiahealth.olhie.client.shared.events.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.NewsItemsRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
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

	@Inject
	private Event<ClientErrorEvent> clientErrorEvent;

	@Inject
	private Event<ClientLogoutRequestEvent> clientLogoutRequestEvent;

	@Inject
	private MessageBus bus;

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

	/**
	 * 1. Set up transport error <br/>
	 * 2. Set up bus Life Cycle listener
	 */
	@AfterInitialization
	protected void afterInitialization() {
		final ClientMessageBus cBus = (ClientMessageBus) bus;
		
		/*

		// 1. Default bus error handler
		bus.subscribe(DefaultErrorCallback.CLIENT_ERROR_SUBJECT,
				new MessageCallback() {

					@Override
					public void callback(Message message) {
						try {
							Throwable caught = message.get(Throwable.class,
									MessageParts.Throwable);
							throw caught;
						} catch (TransportIOException e) {
							// thrown in case the server can't be reached or an
							// unexpected status code was returned
							if (Log.isWarnEnabled()) {
								String errMsg = "ErraiBus Transport error occured - "
										+ e.getErrorMessage()
										+ " with code "
										+ e.errorCode();
								Log.warn(errMsg);
							}
							clientErrorEvent
									.fire(new ClientErrorEvent(
											"Client side error "
													+ e.getErrorMessage()));

						} catch (Throwable t) {
							// handle system errors (e.g response marshaling
							// errors) - that of course should never happen :)
							if (Log.isWarnEnabled()) {
								Log.warn("Sytem error occured "
										+ t.getMessage());
							}
							clientErrorEvent.fire(new ClientErrorEvent(
									"Client side error " + t.getMessage()));
						}
					}
				});

		// 2. Transport error handler
		// - If its a 404, can't find the server then log the client out and
		// restart the bus
		cBus.addTransportErrorHandler(new TransportErrorHandler() {
			@Override
			public void onError(TransportError error) {
				if (Log.isWarnEnabled()) {
					Log.warn("Errai transport error:", error.getException()
							+ " with code " + error.getStatusCode());
				}
				// if (error.isHTTP() == true) {
				// if (error.getStatusCode() == 404) {
				// clientLogoutRequestEvent
				// .fire(new ClientLogoutRequestEvent());
				// cBus.stop(true);
				// cBus.init();
				// }
				// }
			}
		});
		
		*/
	}
}
