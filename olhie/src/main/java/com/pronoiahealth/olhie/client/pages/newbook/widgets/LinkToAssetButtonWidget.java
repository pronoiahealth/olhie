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
 * LinkToAssetButtonWidget.java<br/>
 * Responsibilities:<br/>
 * 1. Creates a widget to use as a link. When using call the base classes
 * setLink method to supply the url for the link. Also, don't attach any click
 * handlers to this class.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 24, 2013
 * 
 */
@Templated("ButtonWidget.html#button")
public class LinkToAssetButtonWidget extends BaseBookassetActionButtonWidget {

	public LinkToAssetButtonWidget() {
		this.iconName = "icon-external-link";
		this.buttonStyle = "btn-primary";
		this.title = "Link";
	}

}
