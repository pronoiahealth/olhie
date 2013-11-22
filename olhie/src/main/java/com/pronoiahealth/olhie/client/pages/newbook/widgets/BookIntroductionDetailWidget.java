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

import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

/**
 * BookIntroductionDetailWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 21, 2013
 *
 */
@Templated("#root")
public class BookIntroductionDetailWidget extends Composite {

	@Inject
	@DataField
	private Image logoImageDisplay;

	@DataField
	private Element created = DOM.createDiv();

	@DataField
	private Element published = DOM.createDiv();

	@DataField
	private Element category = DOM.createDiv();

	@DataField
	private Element hours = DOM.createDiv();

	/**
	 * Constructor
	 *
	 */
	public BookIntroductionDetailWidget() {
	}

	/**
	 * Sets the book cover image source
	 * 
	 * @param imgSrc
	 */
	public void setImageSrc(String imgSrc) {
		logoImageDisplay.setUrl(imgSrc);
	}

	/**
	 * Set data for widget
	 * 
	 * @param imgSrc
	 * @param createdStr
	 * @param publishedStr
	 * @param categoryStr
	 * @param hoursStr
	 */
	public void setData(String imgSrc, String createdStr, String publishedStr,
			String categoryStr, String hoursStr) {
		setImageSrc(imgSrc);
		created.setInnerText(createdStr);
		published.setInnerText(publishedStr);
		category.setInnerText(categoryStr);
		hours.setInnerText(hoursStr);
	}

}
