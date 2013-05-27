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
package com.pronoiahealth.olhie.client.pages.register;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * RegisterDialog.java<br/>
 * Responsibilities:<br/>
 * 1. Shows Registration dialog<br/>
 * 2. Submits registration for a new user<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public class RegisterDialog extends Composite {
	
	@Inject
	UiBinder<Widget, RegisterDialog> binder;
	
	@UiField
	public Modal registerModal;

	/**
	 * Constructor
	 *
	 */
	public RegisterDialog() {
	}
	
	/**
	 * Create the gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		registerModal.setStyleName("ph-Register-Modal", true);
		registerModal.setStyleName("ph-Register-Modal-Size", true);
	}
	
	/**
	 * Shows the modal dialog
	 */
	public void show() {
		registerModal.show();
	}

}
