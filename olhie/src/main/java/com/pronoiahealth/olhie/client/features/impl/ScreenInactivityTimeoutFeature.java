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
package com.pronoiahealth.olhie.client.features.impl;

import java.util.Map;

import javax.inject.Inject;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.pronoiahealth.olhie.client.clientfactories.ScreenTimeout;
import com.pronoiahealth.olhie.client.features.AbstractClientFeature;

/**
 * ScreenInactivityTimeoutFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 24, 2013
 * 
 */
public class ScreenInactivityTimeoutFeature extends AbstractClientFeature {

	/*
	 * Used to time things on screen such as when a key is pressed.
	 */
	private Timer screenTimer;

	@Inject
	@ScreenTimeout
	private Integer screenTimeout;

	private HandlerRegistration screenTimeOutHandlerRegistration;

	/**
	 * Constructor
	 * 
	 */
	public ScreenInactivityTimeoutFeature() {
	}

	/**
	 * Create timer that will call the observesClientLogoutRequestEvent if a
	 * logged in user does not access the mouse or keyboard within the provides
	 * screen timeout period.
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#standUp(java.util.Map)
	 */
	@Override
	public boolean standUp(Map<String, Object> params) {
		try {
			screenTimer = new Timer() {
				@Override
				public void run() {
					if (standUpFeaturecallbackHandler != null) {
						standUpFeaturecallbackHandler.executeCallBack();
					}
				}
			};
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Start the screen timeout handler. If the user does not move the mouse or
	 * input anything into the app for the screen timeout period then the system
	 * will automatically log the user out.
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#start()
	 */
	@Override
	public boolean activate() {
		try {
			// Cancel any current handler
			deactivate();

			// Re-init
			screenTimeOutHandlerRegistration = com.google.gwt.user.client.Event
					.addNativePreviewHandler(new NativePreviewHandler() {
						@Override
						public void onPreviewNativeEvent(
								NativePreviewEvent event) {
							screenTimer.cancel();
							screenTimer.schedule(screenTimeout);
						}
					});
			screenTimer.schedule(this.screenTimeout);

			// Started
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Stop the timer and remove the handler
	 * 
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#stop()
	 */
	@Override
	public boolean deactivate() {
		try {
			if (screenTimeOutHandlerRegistration != null) {
				screenTimeOutHandlerRegistration.removeHandler();
				screenTimeOutHandlerRegistration = null;
			}
			screenTimer.cancel();

			// Stopped
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
