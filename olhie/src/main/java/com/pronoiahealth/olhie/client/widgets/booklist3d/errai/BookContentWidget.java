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
 * BookContent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Sep 30, 2013
 *
 */
@Templated("#bookContent")
public class BookContentWidget extends Composite {

	@DataField("bookContentDesc")
	private Element bookContentDesc = DOM.createElement("p");
	
	@DataField("bookItemDownloadBtn")
	private Element bookItemDownloadBtn = DOM.createAnchor();
	
	@DataField("bookItemViewBtn")
	private Element bookItemViewBtn = DOM.createAnchor();

	/**
	 * Constructor
	 *
	 */
	public BookContentWidget() {
	}
	
	/**
	 * @param desc
	 */
	public void setDescription(String desc, boolean downLoadOnly, String assetId, String assetType) {
		bookContentDesc.setInnerText(desc);
		if (downLoadOnly == true) {
			bookItemViewBtn.setAttribute("disabled", "true");
		}
		bookItemViewBtn.setAttribute("bookassetid", assetId);
		bookItemViewBtn.setAttribute("viewable-content-key", assetType);
		bookItemDownloadBtn.setAttribute("bookassetid", assetId);
	}

}