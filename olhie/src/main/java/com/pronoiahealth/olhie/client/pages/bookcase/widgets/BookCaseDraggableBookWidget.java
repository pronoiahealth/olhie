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

import static com.arcbees.gquery.tooltip.client.Tooltip.Tooltip;
import static com.google.gwt.query.client.GQuery.$;
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

	public void setData(String imgUrl, String bookId,
			String userBookRelationshipId, String bookTitle) {
		smallBookImageDisplay.setUrl(imgUrl);
		Element buttonElement = button.getElement();
		buttonElement.setAttribute("bookId", bookId);
		buttonElement.setAttribute("userBookRelationshipId",
				userBookRelationshipId);
		buttonElement.setAttribute("title", bookTitle);
		buttonElement.setAttribute("data-original-title", bookTitle);
		$(buttonElement).as(Tooltip).tooltip();
	}

	@Override
	public Element getElement() {
		return root;
	}

}
