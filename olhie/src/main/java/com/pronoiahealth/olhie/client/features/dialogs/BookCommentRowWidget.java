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
package com.pronoiahealth.olhie.client.features.dialogs;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.pronoiahealth.olhie.client.widgets.rating.StarRating;

/**
 * BookCommentRowWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 27, 2013
 *
 */
@Templated("#row")
public class BookCommentRowWidget extends Composite {

	@DataField
	private Element row = DOM.createDiv();

	@DataField
	private Element comment = DOM.createDiv();

	@DataField
	private Element rating = DOM.createDiv();

	/**
	 * Constructor
	 *
	 */
	public BookCommentRowWidget() {
	}

	/**
	 * Add the data for the row
	 * 
	 * @param commentStr
	 * @param ratingVal
	 */
	public Element setData(String commentStr, int ratingVal) {
		comment.setInnerText(commentStr);

		// Star rating
		StarRating sr = new StarRating(5, true);
		sr.setRating(ratingVal);
		Element srElem = sr.getElement();
		srElem.setAttribute("style", "display: inline-block;");
		rating.appendChild(srElem);
		
		// return row;
		return row;
	}

}
