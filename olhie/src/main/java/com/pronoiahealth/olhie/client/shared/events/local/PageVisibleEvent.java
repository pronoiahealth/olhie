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

/**
 * Fired when a page is being shown<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 17, 2013
 *
 */
public class PageVisibleEvent {

	private String pageName;

	/**
	 * Constructor
	 *
	 * @param pageName
	 */
	public PageVisibleEvent(String pageName) {
		this.pageName = pageName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

}
