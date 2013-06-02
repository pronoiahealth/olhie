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
package com.pronoiahealth.olhie.client.shared.exceptions;

/**
 * DataValidationException.java<br/>
 * Responsibilities:<br/>
 * 1. Thrown during data validation to signify the data did not validate
 * according to the rules for the data.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 2, 2013
 * 
 */
public class DataValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public DataValidationException() {
	}

	public DataValidationException(String arg0) {
		super(arg0);
	}

	public DataValidationException(Throwable arg0) {
		super(arg0);
	}

	public DataValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
