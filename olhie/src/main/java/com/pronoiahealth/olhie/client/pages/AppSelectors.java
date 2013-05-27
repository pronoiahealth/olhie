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
package com.pronoiahealth.olhie.client.pages;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;

/**
 * AppSelectors<br/>
 * Responsibilities:<br/>
 * 1. Used with GQuery to pre-compile selectors<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public interface AppSelectors extends Selectors {
	public static AppSelectors INSTANCE = GWT.create(AppSelectors.class);

	@Selector(".center-background")
	GQuery getCenterBackground();
}
