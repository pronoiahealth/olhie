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
package com.pronoiahealth.olhie.client.pages.newbook;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * NewBookMessages.java<br/>
 * Responsibilities:<br/>
 * 1. Produce messages for New Book<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 10, 2013
 *
 */
@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en_US")
public interface NewBookMessages extends Messages {
	public static NewBookMessages INSTANCE = GWT.create(NewBookMessages.class);

	@DefaultMessage("Hours to create: {0} Created: {1}  Published: {2}  Category: {3}")
	SafeHtml setCreatedPublishedCategoryLbl(int hrsToCreate, String created, String published, String category);
	
	@DefaultMessage("{0}")
	SafeHtml setIntroductionText(String txt);
	
	@DefaultMessage("Created on {0}")
	String setCreatedDateText(String txt);
	
	@DefaultMessage("{0}.")
	String createTOCNumber(String txt);
	
	@DefaultMessage("Upload a File ({0}):")
	String uploadAFileWithName(String txt);
	
	@DefaultMessage("Upload the logo ({0}):")
	String uploadLogoWithName(String txt);
}
