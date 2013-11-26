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
package com.pronoiahealth.olhie.client.widgets.booklist3d.errai;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;

/**
 * TOCDescriptionItemWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 25, 2013
 *
 */
@Templated("#root")
public class TOCDescriptionItemWidget extends Composite {
	
	@DataField
	private Element root = DOM.createDiv(); 
	
	@DataField
	private Element itemNumber = DOM.createDiv();
	
	@DataField
	private Element itemDescription = DOM.createDiv();
	
	/**
	 * Constructor
	 *
	 */
	public TOCDescriptionItemWidget() {
	}
	
	/**
	 * Sets the data for the row
	 * 
	 * @param count
	 * @param itemDesc
	 * @param hoursOfWork
	 */
	public Element setData(int count, String itemDesc, String hoursOfWork) {
		root.setAttribute("item-ref", "" + count);
		itemNumber.setInnerText("" + count + ". ");
		itemDescription.setInnerText(itemDesc + " (" + hoursOfWork + ")");
		return root;
	}

}
