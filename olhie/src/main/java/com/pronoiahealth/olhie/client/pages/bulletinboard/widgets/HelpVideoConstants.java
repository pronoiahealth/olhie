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
package com.pronoiahealth.olhie.client.pages.bulletinboard.widgets;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * HelpVideoConstants.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 1, 2013
 *
 */
public interface HelpVideoConstants extends Constants {
	public static final HelpVideoConstants INSTANCE = GWT
			.create(HelpVideoConstants.class);
	
	Map<String, String> urlMap();
	
	String[] displayList();
}
