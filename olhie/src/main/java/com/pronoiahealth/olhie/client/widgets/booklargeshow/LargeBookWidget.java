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
package com.pronoiahealth.olhie.client.widgets.booklargeshow;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * ShowBookWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 7, 2013
 *
 */
public class LargeBookWidget extends SimplePanel {
	private DivElement binderDiv;

	/**
	 * Constructor
	 *
	 */
	public LargeBookWidget() {
		setStyleName("ph-NewBook-BookImagePanel-Large");
		binderDiv = Document.get().createDivElement();
		binderDiv
				.setClassName("ph-NewBook-BookImagePanel-Large-Binder");
		getElement().appendChild(binderDiv);
	}
	
	/**
	 * Set the background
	 * 
	 * @param backgroundUrl
	 */
	public void setBackground(String backgroundUrl) {
		getElement().setAttribute(
				"style",
				"background-image: url('" + backgroundUrl
						+ "'); background-size: contain;");
	}
	
	/**
	 * Set the binder color
	 * 
	 * @param binderColor
	 */
	public void setBinderColor(String binderColor) {
		binderDiv
				.setAttribute("style", "background-color:" + binderColor + ";");
	}

}
