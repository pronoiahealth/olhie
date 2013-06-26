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

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.ClientMessageBus;
import org.jboss.errai.bus.client.api.TransportError;
import org.jboss.errai.bus.client.api.TransportErrorHandler;
import org.jboss.errai.bus.client.api.base.DefaultErrorCallback;
import org.jboss.errai.bus.client.api.base.TransportIOException;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.common.client.protocols.MessageParts;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.pronoiahealth.olhie.client.pages.main.MainPage;
import com.pronoiahealth.olhie.client.shared.events.ClientErrorEvent;
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
	private Event<ClientErrorEvent> clientErrorEvent;

	private ClientMessageBus cBus = (ClientMessageBus) ErraiBus.get();

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
	 */
	@AfterInitialization
	protected void afterInitialization() {
		// 1. Transport error handler
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

		// General catch for errors
		cBus.subscribe(DefaultErrorCallback.CLIENT_ERROR_SUBJECT,
				new MessageCallback() {
					@Override
					public void callback(Message message) {
						try {
							Throwable caught = message.get(Throwable.class,
									MessageParts.Throwable);
							throw caught;
						} catch (TransportIOException e) {
							String errStr = e.getErrorMessage();
							if (Log.isWarnEnabled()) {
								Log.warn(
										"Errai transport error:",
										errStr + " with code "
												+ e.errorCode());
							}
							clientErrorEvent.fire(new ClientErrorEvent(errStr));
							
						} catch (Throwable throwable) {
							String errStr = throwable.getMessage();
							if (Log.isWarnEnabled()) {
								Log.warn("Errai transport error:", errStr);
							}
							clientErrorEvent.fire(new ClientErrorEvent(errStr));
						}
					}
				});
	}
}
