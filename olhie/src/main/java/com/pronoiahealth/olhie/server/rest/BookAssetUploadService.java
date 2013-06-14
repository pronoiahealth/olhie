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
 * Responsibilities:<br/>
 * 1. Receive an uploaded file<br>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 25, 2013
 *
 */
public interface BookAssetUploadService {
	
	public String process2( HttpServletRequest req)
			throws ServletException, IOException, FileUploadException;
}
