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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.pronoiahealth.olhie.client.shared.exceptions.FileDownloadException;

/**
 * FileDownloadExceptionMapper.java<br/>
 * Responsibilities:<br/>
 * 1. Maps the exception to an HTTP response (500)<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 22, 2013
 * 
 */
@Provider
public class FileDownloadExceptionMapper implements
		ExceptionMapper<FileDownloadException> {

	/**
	 * Constructor
	 * 
	 */
	public FileDownloadExceptionMapper() {
	}

	@Override
	public Response toResponse(FileDownloadException arg0) {
		StringBuilder response = new StringBuilder();
		response.append("There was an error on the server downloading the asset.");
		return Response.serverError().entity(response.toString())
				.type(MediaType.TEXT_PLAIN_TYPE).build();
	}

}
