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
package com.pronoiahealth.olhie.client.widgets.booklist;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * BookImagePanelWidget.java<br/>
 * Responsibilities:<br/>
 * 1. Book image<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public class BookImagePanelWidget extends SimplePanel {
	private boolean visible;
	private String binderColor;
	private DivElement privateDiv;
	private DivElement binderDiv;

	/**
	 * Constructor
	 *
	 * @param backgroundColor
	 * @param backgroundPatternUrl
	 * @param binderColor
	 */
	public BookImagePanelWidget(String backgroundColor,
			String backgroundPatternUrl, String binderColor) {
		setStyleName("ph-SearchResults-BookListResult-BookImagePanel");
		getElement().setAttribute(
				"style",
				"background-color:" + backgroundColor
						+ "; background-image: url('" + backgroundPatternUrl
						+ "'); background-repeat: repeat;");
		privateDiv = Document.get().createDivElement();
		privateDiv
				.setClassName("ph-SearchResults-BookListResult-BookImagePanel-NoLock");
		binderDiv = Document.get().createDivElement();
		binderDiv
				.setClassName("ph-SearchResults-BookListResult-BookImagePanel-Binder");
		setBinderColor(binderColor);
		Element rootElem = this.getElement();
		rootElem.appendChild(binderDiv);
		rootElem.appendChild(privateDiv);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;

		if (visible == true) {
			privateDiv
					.setClassName("ph-SearchResults-BookListResult-BookImagePanel-NoLock");
		} else {
			privateDiv
					.setClassName("ph-SearchResults-BookListResult-BookImagePanel-Lock");
		}
	}

	public String getBinderColor() {
		return binderColor;
	}

	public void setBinderColor(String binderColor) {
		this.binderColor = binderColor;
		binderDiv
				.setAttribute("style", "background-color:" + binderColor + ";");
	}

}
