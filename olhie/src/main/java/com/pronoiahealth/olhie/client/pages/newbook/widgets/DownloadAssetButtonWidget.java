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

import org.jboss.errai.ui.shared.api.annotations.Templated;

/**
 * DownloadAssetButtonWidget.java<br/>
 * Responsibilities:<br/>
 * 1. Create a Download button for a book asset
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 16, 2013
 *
 */
@Templated("ButtonWidget.html#button")
public class DownloadAssetButtonWidget extends BaseBookassetActionButtonWidget {

	/**
	 * Constructor<br/>
	 * 
	 * Sets the iconName, buttonStyle, and title <br/>
	 * 
	 * @see BaseBookassetActionButtonWidget
	 *
	 */
	public DownloadAssetButtonWidget() {
		this.iconName = "icon-cloud-download";
		this.buttonStyle = "btn-info";
		this.title = "Download";
		this.additionalClasses = "btn-mini";
	}
}
