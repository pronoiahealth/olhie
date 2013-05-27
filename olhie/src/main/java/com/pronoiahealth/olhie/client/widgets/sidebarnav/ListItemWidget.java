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
package com.pronoiahealth.olhie.client.widgets.sidebarnav;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ListItemWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public class ListItemWidget extends SimplePanel {
	private boolean current;

	/**
	 * Constructor
	 *
	 */
	public ListItemWidget() {
		super((Element) Document.get().createLIElement().cast());
	}

	public ListItemWidget(String s) {
		this();
		getElement().setInnerText(s);
	}

	public ListItemWidget(Widget w) {
		this();
		this.add(w);
	}

	public boolean isCurrent() {
		return current;
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	public void setCurrent(boolean current) {
		this.current = current;
		if (current == true) {
			getElement().setAttribute("class", "current");
		} else {
			getElement().removeAttribute("class");
		}
	}
}
