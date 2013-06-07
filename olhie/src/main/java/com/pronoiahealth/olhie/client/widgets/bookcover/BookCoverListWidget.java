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
package com.pronoiahealth.olhie.client.widgets.bookcover;

import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.NavWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;

/**
 * BookCoverListWidget.java<br/>
 * Responsibilities:<br/>
 * 1. Display a book cover.<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 6, 2013
 *
 */
public class BookCoverListWidget extends NavWidget {
	public static final String IMG_URL_HOLDER = "img-url";

	/**
	 * Constructor
	 *
	 */
	public BookCoverListWidget() {
	}

	/**
	 * Constructor
	 *
	 * @param bookCover
	 */
	public BookCoverListWidget(BookCover bookCover) {
		super();
		String name = bookCover.getCoverName();
		String imgUrl = bookCover.getImgUrl();
		FlowPanel fp = new FlowPanel();
		SimplePanel colorPanel = new SimplePanel();
		colorPanel.setStyleName("ph-NewBook-BookImagePanel", true);
		colorPanel.getElement().setAttribute(
				"style",
				"background-image: url('" + imgUrl
						+ "'); background-repeat: repeat; display: inline;");
		Image i = new Image(GWT.getModuleName() + "/images/transparent_15x1.png");
		i.getElement().setAttribute("style", "width: 40px;");
		colorPanel.add(i);
		fp.add(colorPanel);
		HTML h = new HTML();
		h.getElement().setAttribute("style", "display: inline;");
		h.setText(name);
		fp.add(h);
		setName(name);
		getAnchor().getElement().setAttribute(IMG_URL_HOLDER, imgUrl);
		add(fp);
	}
}
