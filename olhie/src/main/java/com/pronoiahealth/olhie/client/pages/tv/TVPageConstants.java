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
package com.pronoiahealth.olhie.client.pages.tv;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * TVPageConstants.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jan 12, 2014
 *
 */
public interface TVPageConstants extends Constants {
	public static final TVPageConstants INSTANCE = GWT
			.create(TVPageConstants.class);

	Map<String, String> helpUrlMap();

	String[] helpDisplayList();

	Map<String, String> overviewUrlMap();

	String[] overviewDisplayList();

	Map<String, String> introductionUrlMap();

	String[] introductionDisplayList();
}