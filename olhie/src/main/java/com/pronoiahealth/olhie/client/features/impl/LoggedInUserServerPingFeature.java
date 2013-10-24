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

import java.util.Date;
import java.util.Map;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.Timer;
import com.pronoiahealth.olhie.client.clientfactories.PingFireTime;
import com.pronoiahealth.olhie.client.features.AbstractClientFeature;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoggedInPingEvent;

/**
 * LoggedInUserServerPingFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 24, 2013
 * 
 */
public class LoggedInUserServerPingFeature extends AbstractClientFeature {

	@Inject
	@PingFireTime
	private Integer pingFireTime;

	@Inject
	private Event<LoggedInPingEvent> loggedInPingEvent;

	private long lastPingTime;

	private Timer pingTimer;

	/**
	 * Constructor
	 * 
	 */
	public LoggedInUserServerPingFeature() {
	}

	/**
	 * After a user logs in start pinging the server. The ping functionality
	 * also checks the last time a ping was sent against the current time. If
	 * there is more than a 5 minute difference it is assumed the user put the
	 * system to sleep. This will cause an automatic log out.
	 */
	@Override
	public boolean standUp(Map<String, Object> params) {
		try {
			pingTimer = new Timer() {
				@Override
				public void run() {
					long currentTime = (new Date()).getTime();

					// If the difference is greater than 5 minutes then log the
					// user
					// out.
					if (currentTime > (lastPingTime + (1000 * 60 * 5))) {
						if (standUpFeaturecallbackHandler != null) {
							standUpFeaturecallbackHandler.executeCallBack();
						}
					} else {
						loggedInPingEvent.fire(new LoggedInPingEvent());
						lastPingTime = currentTime;
					}
				}
			};
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Set the initial last ping time and start the timer. This will cause a
	 * ping event to be fired every pingFireTime milliseconds
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#activate()
	 */
	@Override
	public boolean activate() {
		try {
			lastPingTime = new Date().getTime();
			pingTimer.cancel();
			pingTimer.scheduleRepeating(pingFireTime);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Cancell the ping timer
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#deactivate()
	 */
	@Override
	public boolean deactivate() {
		try {
			pingTimer.cancel();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
