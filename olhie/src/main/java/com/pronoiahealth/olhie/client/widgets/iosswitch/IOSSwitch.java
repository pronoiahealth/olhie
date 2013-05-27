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
package com.pronoiahealth.olhie.client.widgets.iosswitch;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.ui.Widget;

/**
 * IOSSwitch.java<br/>
 * Responsibilities:<br/>
 * 1. Fancy on off switch that is primarily css driven<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public class IOSSwitch extends Widget {
	private InputElement input;
	private LabelElement label;

	/**
	 * Constructor
	 *
	 */
	public IOSSwitch() {
		super();
		this.label = Document.get().createLabelElement();
		this.input = Document.get().createCheckInputElement();
		input.setClassName("ios-switch");
		DivElement div = Document.get().createDivElement();
		div.setClassName("switch");
		label.appendChild(input);
		label.appendChild(div);
		setElement(label);
	}

	public boolean isChecked() {
		return input.isChecked();
	}

	public void setChecked(boolean checked) {
		input.setChecked(checked);
	}

}
