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
package com.pronoiahealth.olhie.client.widgets.newbook;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * TOCElement.java<br/>
 * Responsibilities:<br/>
 * 1. A table of contents element<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 10, 2013
 * 
 */
public class TOCElement extends Composite {

	@Inject
	UiBinder<Widget, TOCElement> binder;
	
	@UiField
	public Tooltip toolTip;
	
	@UiField
	public Label description;

	/**
	 * Constructor
	 * 
	 */
	public TOCElement() {
	}

	/**
	 * Construct elements
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
	}
	
	/**
	 * Set the tool tip text
	 * 
	 * @param toolTipTxt
	 */
	public void setToolTip(String toolTipTxt) {
		toolTip.setText(toolTipTxt);
		
	}
	
	/**
	 * Sets the description of the element
	 * 
	 * @param descriptionTxt
	 */
	public void setDescription(String descriptionTxt) {
		description.setText(descriptionTxt);
	}
}
