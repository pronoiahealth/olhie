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

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;

/**
 * NewBookButtonHolderWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 2, 2013
 * 
 */
@Templated("#buttonGroup")
public class NewBookButtonHolderWidget extends Composite {

	@DataField
	private Element buttonGroup = DOM.createDiv();

	/**
	 * Constructor
	 * 
	 */
	public NewBookButtonHolderWidget() {
	}

	public void addButton(BaseBookassetActionButtonWidget button) {
		buttonGroup.appendChild(button.getElement());
	}
}
