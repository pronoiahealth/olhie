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
 * BookdescriptionDetailButtonWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Nov 18, 2013
 *
 */
@Templated("ButtonWidget.html#button")
public class BookdescriptionDetailButtonWidget extends BaseBookassetActionButtonWidget {

	/**
	 * Constructor
	 *
	 */
	public BookdescriptionDetailButtonWidget() {
		this.iconName = "icon-rotate-right";
		this.buttonStyle = "btn-danger";
		this.title = "Detail";
		this.additionalClasses = "btn-mini";
	}

}
