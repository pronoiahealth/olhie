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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.pronoiahealth.olhie.client.features.AbstractClientFeature;

/**
 * UnhandledExceptionHandlerFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 24, 2013
 * 
 */
public class UnhandledExceptionHandlerFeature extends AbstractClientFeature {

	/**
	 * Constructor
	 * 
	 */
	public UnhandledExceptionHandlerFeature() {
	}

	@Override
	public boolean activate() {
		try {
			GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void onUncaughtException(Throwable e) {
					// TODO
				}
			});

			// Handler initialized
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Doesn't do anything xcept return true
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#deactivate()
	 */
	@Override
	public boolean deactivate() {
		return true;
	}
}
