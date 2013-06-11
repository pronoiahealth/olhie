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
package com.pronoiahealth.olhie.client.pages.main;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Footer.java<br/>
 * Responsibilities:<br/>
 * 1. Shows the footer of the main layout using Errai pure html templating
 * system<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public class Footer extends Composite {
	@Inject
	UiBinder<Widget, Footer> binder;

	/**
	 * Default Constructor
	 * 
	 */
	public Footer() {
	}

	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
	}

}
