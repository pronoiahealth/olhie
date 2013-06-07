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
package com.pronoiahealth.olhie.client.widgets.bookcat;

import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.NavWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;

/**
 * BookCategoryListWidget.java<br/>
 * Responsibilities:<br/>
 * 1. Holds BookCategories for diplay<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 6, 2013
 *
 */
public class BookCategoryListWidget extends NavWidget {

	/**
	 * Constructor
	 *
	 */
	public BookCategoryListWidget() {
	}

	/**
	 * Constructor
	 *
	 * @param category
	 */
	public BookCategoryListWidget(BookCategory category) {
		super();
		String name = category.getCatagory();
		FlowPanel fp = new FlowPanel();
		SimplePanel colorPanel = new SimplePanel();
		colorPanel
				.getElement()
				.setAttribute(
						"style",
						"background-color: "
								+ category.getColor()
								+ "; margin-right: 5px; display: inline; border: 1px solid black;");
		Image i = new Image(GWT.getModuleName() + "/images/transparent_15x1.png");
		colorPanel.add(i);
		fp.add(colorPanel);
		HTML h = new HTML();
		h.getElement().setAttribute("style", "display: inline;");
		h.setText(name);
		fp.add(h);
		setName(name);
		add(fp);
	}
}
