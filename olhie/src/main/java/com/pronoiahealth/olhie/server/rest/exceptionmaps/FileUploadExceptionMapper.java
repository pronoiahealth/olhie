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
package com.pronoiahealth.olhie.server.rest.exceptionmaps;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.pronoiahealth.olhie.client.shared.exceptions.FileUploadException;

/**
 * FileUploadExceptionMapper.java<br/>
 * Responsibilities:<br/>
 * 1. Maps the exception to a HTTP response (500)<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 22, 2013
 *
 */
@Provider
public class FileUploadExceptionMapper implements
		ExceptionMapper<FileUploadException> {

	/**
	 * Constructor
	 *
	 */
	public FileUploadExceptionMapper() {
	}

	@Override
	public Response toResponse(FileUploadException arg0) {
		return Response.status(500).build();
	}

}
