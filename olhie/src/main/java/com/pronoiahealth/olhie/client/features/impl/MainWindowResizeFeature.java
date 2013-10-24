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

import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.pronoiahealth.olhie.client.features.AbstractClientFeature;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;

/**
 * MainWindowResizeFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 24, 2013
 * 
 */
public class MainWindowResizeFeature extends AbstractClientFeature {

	@Inject
	private Event<WindowResizeEvent> windowResizeEvent;
	
	private HandlerRegistration windowResizeHandlerRegistration;

	/**
	 * Constructor
	 * 
	 */
	public MainWindowResizeFeature() {
	}

	/**
	 * Add resize handler and fire windowResizeEvent
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#activate()
	 */
	@Override
	public boolean activate() {
		windowResizeHandlerRegistration = Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				windowResizeEvent.fire(new WindowResizeEvent());
			}
		});
		return true;
	}

	/**
	 * Remove handler
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#deactivate()
	 */
	@Override
	public boolean deactivate() {
		if (windowResizeHandlerRegistration != null) {
			windowResizeHandlerRegistration.removeHandler();
			windowResizeHandlerRegistration = null;
		}
		return true;
	}

}
