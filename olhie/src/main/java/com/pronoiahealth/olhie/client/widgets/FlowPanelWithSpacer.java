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
package com.pronoiahealth.olhie.client.widgets;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * FlowPanelWithSpacer.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Sep 18, 2013
 *
 */
public class FlowPanelWithSpacer extends FlowPanel {

	/**
	 * Constructor
	 *
	 */
	public FlowPanelWithSpacer() {
		HTML spacerLabel = new HTML("");
		spacerLabel.setHeight("50px");
		super.add(spacerLabel);
	}

	@Override
	public void add(Widget w) {
		super.insert(w, getWidgetCount() - 1);
	}

	@Override
	public void insert(Widget w, int beforeIndex) {
		if (beforeIndex == getWidgetCount()) {
			beforeIndex--;
		}
		super.insert(w, beforeIndex);
	}

}