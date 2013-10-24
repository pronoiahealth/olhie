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

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.pronoiahealth.olhie.client.features.AbstractClientFeature;

/**
 * SleepDetectionFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 24, 2013
 * 
 */
public class SleepDetectionFeature extends AbstractClientFeature {

	private long lastSystemTime;

	private Timer sleepTimer;

	/**
	 * Constructor
	 * 
	 */
	public SleepDetectionFeature() {
	}

	/**
	 * This method tries to detect when the system has been put to sleep for a
	 * period of time that exceeds the server side timeout. If this happens the
	 * page is reloaded. The time is set to 35 minutes (35*60*1000 milliseconds)
	 * 
	 * @see com.pronoiahealth.olhie.client.features.AbstractClientFeature#standUp(java.util.Map)
	 */
	@Override
	public boolean standUp(Map<String, Object> params) {
		try {
			sleepTimer = new Timer() {
				@Override
				public void run() {
					long now = System.currentTimeMillis();
					if (now - lastSystemTime > 2100000) {
						Window.Location.reload();
					} else {
						lastSystemTime = now;
					}
				}
			};
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Sets initial system time and starts timer
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#activate()
	 */
	@Override
	public boolean activate() {
		deactivate();
		// CurrentTime
		this.lastSystemTime = System.currentTimeMillis();
		sleepTimer.scheduleRepeating(30000);
		return true;
	}

	/**
	 * Once started this can not be stopped
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#deactivate()
	 */
	@Override
	public boolean deactivate() {
		sleepTimer.cancel();
		return true;
	}
}
