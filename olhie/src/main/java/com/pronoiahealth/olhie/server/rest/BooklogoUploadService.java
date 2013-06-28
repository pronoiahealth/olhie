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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.pronoiahealth.olhie.client.shared.exceptions.FileUploadException;

/**
 * BooklogoUploadService.java<br/>
 * Responsibilities:<br/>
 * 1. Saves a book upload logo
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 28, 2013
 *
 */
public interface BooklogoUploadService {
	/**
	 * Save the book upload logo
	 * 
	 * @param req
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws FileUploadException
	 */
	public String process( HttpServletRequest req)
			throws ServletException, IOException, FileUploadException;

}
