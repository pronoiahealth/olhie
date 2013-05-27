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
package com.pronoiahealth.olhie.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.Messages;

/**
 * MessageFormatResources.java<br/>
 * Responsibilities:<br/>
 * 1. Used for safe message display throughout application<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en_US")
public interface MessageFormatResources extends Messages {
	public static MessageFormatResources INSTANCE = GWT
			.create(MessageFormatResources.class);

	@DefaultMessage("{0} by {1}")
	String setBookTitleAndAuthor(String title, String author);

	@DefaultMessage("Title: {0}")
	String setBookTitle(String title);

	@DefaultMessage("Author: {0}")
	String setBookAuthor(String title);

	@DefaultMessage("Publication Date: {0}")
	String setBookPubDate(String title);

	@DefaultMessage("Rating: {0}")
	String setBookRating(String title);

}
