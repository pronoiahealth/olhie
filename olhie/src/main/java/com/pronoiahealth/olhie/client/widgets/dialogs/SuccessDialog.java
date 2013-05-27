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
package com.pronoiahealth.olhie.client.widgets.dialogs;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;

/**
 * SuccessDialog.java<br/>
 * Responsibilities:<br/>
 * 1. Success dialog<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Singleton
public class SuccessDialog extends BaseModalDialog {
	
	@Inject
	public SuccessDialog() {
		super("Success!", false, true, BackdropType.STATIC, true,
				"Action was successful:", "Close", ButtonType.INFO,
				IconType.STAR);
	}
}
