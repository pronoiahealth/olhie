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
package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;
import org.jboss.errai.common.client.api.annotations.NonPortable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pronoiahealth.olhie.client.widgets.sidebarnav.SideBarNavWidget;

/**
 * SyncMenuWithViewEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Fired to tell the AppNavMenu to sync with the currently displayed page
 * view<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Local
@NonPortable
public class SyncMenuWithViewEvent {

	private SideBarNavWidget widget;
	private Multimap<String, Object> state;

	/**
	 * Constructor
	 *
	 * @param widget
	 * @param state
	 */
	public SyncMenuWithViewEvent(SideBarNavWidget widget,
			Multimap<String, Object> state) {
		this.widget = widget;
		this.state = state;

	}

	public SideBarNavWidget getWidget() {
		return widget;
	}

	public void setWidget(SideBarNavWidget widget) {
		this.widget = widget;
	}

	public Multimap<String, Object> getState() {
		return state;
	}

	public void setState(Multimap<String, Object> state) {
		this.state = state;
	}

	public void addToMap(String key, Object value) {
		if (state == null) {
			state = ArrayListMultimap.create();
		}
		state.put(key, value);
	}
}
