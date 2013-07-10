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
 * GenericRPCException.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 9, 2013
 *
 */
public class GenericRPCException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GenericRPCException() {
	}

	public GenericRPCException(String arg0) {
		super(arg0);
	}

	public GenericRPCException(Throwable arg0) {
		super(arg0);
	}

	public GenericRPCException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
