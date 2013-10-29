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

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.features.AbstractWidgetBasedListenerClientFeature;
import com.pronoiahealth.olhie.client.features.dialogs.ViewBookCommentsDialog;

/**
 * ViewBookCommentsHandlerFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 28, 2013
 * 
 */
@Dependent
public class ViewBookCommentsHandlerFeature extends
		AbstractWidgetBasedListenerClientFeature {

	@Inject
	private Instance<ViewBookCommentsDialog> instFactory;

	/**
	 * Constructor
	 * 
	 * @param dialog
	 */
	@Inject
	public ViewBookCommentsHandlerFeature() {
	}

	@Override
	protected Widget getNewWidget() {
		return instFactory.get();
	}

}
