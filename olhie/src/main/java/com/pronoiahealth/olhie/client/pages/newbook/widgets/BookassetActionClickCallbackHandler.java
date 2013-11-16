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
package com.pronoiahealth.olhie.client.pages.newbook.widgets;

import com.google.gwt.user.client.Event;

/**
 * BookassetActionClickCallbackHandler.java<br/>
 * Responsibilities:<br/>
 * 1. Callback interface for book asset actions. See BookItemDisplay for sample
 * usage.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 16, 2013
 * 
 */
public interface BookassetActionClickCallbackHandler {
	
	/**
	 * Callback
	 * 
	 * @param e
	 * @param baDescId
	 * @param baId
	 * @param viewType
	 * @return
	 */
	public boolean handleButtonClick(Event e, String baDescId, String baId,
			String viewType);
}
