/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pronoiahealth.olhie.client.widgets;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * RemoveBookassetdescriptionButton.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 23, 2013
 *
 */
public class RemoveBookassetdescriptionButton extends Button {
	
	/**
	 * Constructor
	 * 
	 */
	public RemoveBookassetdescriptionButton(ClickHandler handler, String bookassetId) {
		this.setSize(ButtonSize.MINI);
		this.setIcon(IconType.REMOVE_CIRCLE);
		this.setType(ButtonType.WARNING);
		this.addClickHandler(handler);
		this.setBookassetId(bookassetId);
		Tooltip tip = new Tooltip();
		tip.setWidget(this);
		tip.setText("Remove this item.");
		tip.setPlacement(Placement.TOP);
		tip.setContainer("body");
		tip.reconfigure();
	}

	public void setBookassetId(String id) {
		this.getElement().setAttribute("bookassetdescriptionId", id);
	}

	public String getBookassetId() {
		return this.getElement().getAttribute("bookassetdescriptionId");
	}
}
