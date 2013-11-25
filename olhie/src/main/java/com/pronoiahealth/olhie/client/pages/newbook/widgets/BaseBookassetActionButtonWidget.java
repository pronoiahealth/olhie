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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;

/**
 * BaseBookassetActionButtonWidget.java<br/>
 * Responsibilities:<br/>
 * 1. Servers as the base class for book asset action buttons (download, view,
 * remove). <br/>
 * 2. Specific actions are handled by sub classes. <br/>
 * 3. Those classes should set the iconName, buttonStyle, and title in the
 * constructor. Injection could be used.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 16, 2013
 * 
 */
@Templated("ButtonWidget.html#root")
public class BaseBookassetActionButtonWidget extends Composite {

	@Inject
	@DataField
	private Anchor button;

	@DataField
	private Element buttonIcon = DOM.createElement("i");

	/**
	 * Bootstrap icon name
	 */
	protected String iconName;

	/**
	 * Warn (btn-warn), info (btn-info), success, etc.. as in Bootstrap buttons
	 */
	protected String buttonStyle;

	/**
	 * The tool tip text
	 */
	protected String title;

	protected String hRef;

	/**
	 * Constructor
	 * 
	 */
	public BaseBookassetActionButtonWidget() {
	}

	/**
	 * Sets icon, buttonStyle (info, warn, etc..), and tooltip title. Depends on
	 * icon name string being set in constructor
	 */
	@PostConstruct
	protected final void postConstruct() {
		button.setStyleName(buttonStyle, true);
		button.getElement().setAttribute("data-original-title", title);
		button.getElement().setAttribute("title", title);
		buttonIcon.setClassName(iconName);
		if (hRef != null) {
			button.setHref(hRef);
			button.setTarget("_blank");
		}
		setTooltip(title);
	}

	/**
	 * This will return the element (anchor) for the configure link. Useful when
	 * adding this widget to the DOM tree. properties
	 * 
	 * @param bookassetid
	 * @return
	 */
	public Element bindButton() {
		// Return the element to be bound latter
		return button.getElement();
	}

	/**
	 * Set the hRef for this link with a target 0f _blank.
	 * 
	 * @param hRefLink
	 */
	public void setLink(String hRefLink) {
		this.hRef = hRefLink;
		button.setHref(hRef);
		button.setTarget("_blank");
	}

	/**
	 * Add tooltip using GwtBootstrap
	 * 
	 * 
	 * @param message
	 */
	protected void setTooltip(String message) {
		Tooltip tip = new Tooltip();
		tip.setWidget(button);
		tip.setText(message);
		tip.setPlacement(Placement.TOP);
		// tip.setContainer("body");
		tip.reconfigure();
	}
}
