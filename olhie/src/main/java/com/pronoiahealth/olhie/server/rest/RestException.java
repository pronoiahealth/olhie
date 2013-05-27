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
package com.pronoiahealth.olhie.server.rest;

/**
 * Rest Exception
 * 
 * Responsibilities:<br/>
 * 1. Contains an exception for a restful service<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 25, 2013
 *
 */
public class RestException extends Exception {
	private static final long serialVersionUID = 1234183830754679011L;

	public RestException() {
		super();
	}

	public RestException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RestException(String arg0) {
		super(arg0);
	}

	public RestException(Throwable arg0) {
		super(arg0);
	}

}
