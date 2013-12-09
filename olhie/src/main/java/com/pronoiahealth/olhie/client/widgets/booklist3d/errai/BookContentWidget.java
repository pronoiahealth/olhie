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

import javax.enterprise.context.Dependent;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;

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
@Dependent
@Templated("#bookContent")
public class BookContentWidget extends Composite {

	@DataField("bookContentDesc")
	private Element bookContentDesc = DOM.createElement("p");

	@DataField("bookItemDownloadBtn")
	private Element bookItemDownloadBtn = DOM.createAnchor();

	@DataField("bookItemLinkBtn")
	private Element bookItemLinkBtn = DOM.createAnchor();

	@DataField("bookItemViewBtn")
	private Element bookItemViewBtn = DOM.createAnchor();

	@DataField("addedDate")
	private Element addedDate = DOM.createDiv();

	@DataField("contentType")
	private Element contentType = DOM.createDiv();

	@DataField("buttonIcon")
	private Element buttonIcon = DOM.createElement("i");

	@DataField("creationHrs")
	private Element creationHrs = DOM.createDiv();

	/**
	 * Constructor
	 * 
	 */
	public BookContentWidget() {
	}

	/**
	 * Sets up the book asset display:<br/>
	 * 1. Set the description and data elements<br/>
	 * 2. If the content is not viewable then hide the view button<br/>
	 * 3. If the itemType is a link type set the attributes for a direct link,
	 * otherwise, set for download<br/>
	 * 
	 * @param desc
	 */
	public void setDescription(String desc, String addedDateStr, String hrs,
			boolean downLoadOnly, String assetId, String assetType,
			String itemType, String linkRef) {
		addedDate.setInnerText(addedDateStr);
		contentType.setInnerText(assetType);
		BookAssetDataType dataType = BookAssetDataType.valueOf(itemType);
		buttonIcon.setClassName(dataType.getBootstrapClass());
		bookContentDesc.setInnerText(desc);
		creationHrs.setInnerText(hrs);

		// Buttons
		// View button
		if (downLoadOnly == true) {
			bookItemViewBtn.setAttribute("disabled", "true");
			bookItemViewBtn.setAttribute("style", "display: none;");
		} else {
			bookItemViewBtn.setAttribute("bookassetid", assetId);
			bookItemViewBtn.setAttribute("viewable-content-key", assetType);
			bookItemViewBtn.removeAttribute("disabled");
			bookItemViewBtn.setAttribute("style", "display: inline-block;");
		}
		
		// Download
		if (dataType == BookAssetDataType.FILE
				|| dataType == BookAssetDataType.VIDEO) {
			bookItemDownloadBtn.setAttribute("style", "display: inline-block; border: 1px; padding-left: 6px; padding-right: 6px; height: 20px;");
			bookItemDownloadBtn.setAttribute("bookassetid", assetId);
		} else {
			bookItemDownloadBtn
					.setAttribute("style",
							"display: none;");
		}

		// Link
		if (dataType == BookAssetDataType.LINK
				|| dataType == BookAssetDataType.YOUTUBE) {
			bookItemLinkBtn.setAttribute("style", "display: inline-block; border: 1px; padding-left: 6px; padding-right: 6px; height: 20px;");
			bookItemLinkBtn.setAttribute("href", linkRef);
			bookItemLinkBtn.setAttribute("target", "_target");
		} else {
			bookItemLinkBtn
					.setAttribute("style",
							"display: none;");
		}

	}

}
