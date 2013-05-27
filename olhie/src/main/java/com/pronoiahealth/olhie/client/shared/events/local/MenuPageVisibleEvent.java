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

/**
 * Used for event where a page is being shown and the 
 * sidebar menu should be updates
 * 
 * @author John DeStefano
 * @version 1.0
 * @since 5/13/2013
 *
 */
/**
 * MenuPageVisibleEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Fired by pages that must sync with the AppNavMenu<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Local
public class MenuPageVisibleEvent {

	private String pageName;

	/**
	 * Constructor
	 *
	 * @param pageName
	 */
	public MenuPageVisibleEvent(String pageName) {
		this.pageName = pageName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}
