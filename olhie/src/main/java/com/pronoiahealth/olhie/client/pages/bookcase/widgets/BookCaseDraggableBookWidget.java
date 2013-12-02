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
package com.pronoiahealth.olhie.client.pages.bookcase.widgets;

import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

/**
 * BookCaseBookWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 29, 2013
 * 
 */
@Templated("#root")
public class BookCaseDraggableBookWidget extends Composite {

	@DataField
	private Element root = DOM.createElement("li");

	@Inject
	@DataField
	private Anchor button;

	@Inject
	@DataField
	private Image smallBookImageDisplay;

	/**
	 * Constructor
	 * 
	 */
	public BookCaseDraggableBookWidget() {
	}

	public void setData(String imgUrl, String bookId) {
		smallBookImageDisplay.setUrl(imgUrl);
		button.getElement().setAttribute("bookId", bookId);
	}

	@Override
	public Element getElement() {
		return root;
	}

}
