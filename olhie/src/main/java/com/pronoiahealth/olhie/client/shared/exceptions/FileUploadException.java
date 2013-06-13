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
 * FileUploadException.java<br/>
 * Responsibilities:<br/>
 * 1. Returned from a file uplaod if an unexpected exception occurs<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 12, 2013
 *
 */
public class FileUploadException extends Exception {
	private static final long serialVersionUID = 1L;

	public FileUploadException() {
		super();
	}

	public FileUploadException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FileUploadException(String arg0) {
		super(arg0);
	}

	public FileUploadException(Throwable arg0) {
		super(arg0);
	}
}
