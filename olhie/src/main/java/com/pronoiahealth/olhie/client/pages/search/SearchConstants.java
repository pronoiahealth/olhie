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
package com.pronoiahealth.olhie.client.pages.search;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * SearchConstants.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 31, 2013
 *
 */
public interface SearchConstants extends Constants {
	SearchConstants INSTANCE = GWT.create(SearchConstants.class);
	
	@DefaultStringValue("Search Results")
	String searchResults();
	
	@DefaultStringValue("Sorry, nothing matches your query.")
	String noResults();

}
