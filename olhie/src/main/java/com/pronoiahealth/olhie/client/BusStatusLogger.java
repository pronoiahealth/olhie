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

import org.jboss.errai.bus.client.api.BusLifecycleEvent;
import org.jboss.errai.bus.client.api.BusLifecycleListener;

import com.google.gwt.core.client.GWT;

/**
 * BusStatusLogger.java<br/>
 * Responsibilities:<br/>
 * 1. Used to log the health of the message bus. Its attached from the Ohlie
 * entry point class.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 12, 2013
 * 
 */
class BusStatusLogger implements BusLifecycleListener {

	@Override
	public void busAssociating(BusLifecycleEvent e) {
		GWT.log("Errai Bus trying to connect...");
	}

	@Override
	public void busOnline(BusLifecycleEvent e) {
		GWT.log("Errai Bus connected!");
	}

	@Override
	public void busOffline(BusLifecycleEvent e) {
		GWT.log("Errai Bus trying to connect...");
	}

	@Override
	public void busDisassociating(BusLifecycleEvent e) {
		GWT.log("Errai Bus going into local-only mode.");
	}
}