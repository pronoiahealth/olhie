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
package com.pronoiahealth.olhie.client.features.impl;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.features.AbstractWidgetBasedListenerClientFeature;
import com.pronoiahealth.olhie.client.features.dialogs.ResetPwdDialog;

/**
 * ResetPwdDialogHandlerFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 * 
 */
public class ResetPwdDialogHandlerFeature extends
		AbstractWidgetBasedListenerClientFeature {

	@Inject
	private Instance<ResetPwdDialog> instFactory;

	/**
	 * Constructor
	 * 
	 */
	public ResetPwdDialogHandlerFeature() {
	}

	/**
	 * Overrides the parent class to return a ResetPwdDialog
	 * 
	 * @see com.pronoiahealth.olhie.client.features.AbstractWidgetBasedListenerClientFeature#getNewWidget()
	 */
	@Override
	protected Widget getNewWidget() {
		return instFactory.get();
	}

}
