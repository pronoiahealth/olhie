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
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import com.pronoiahealth.olhie.client.shared.exceptions.FileDownloadException;

/**
 * BookassetDownloadService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 20, 2013
 * 
 */
public interface BookassetDownloadService {
	public InputStream downloadAsset(HttpServletRequest request,
			HttpServletResponse response, String assetId, ServletContext context)
			throws ServletException, IOException, FileDownloadException;

	public InputStream viewPDFAsset(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("assetId") String assetId,
			@Context ServletContext context) throws ServletException,
			IOException, FileDownloadException;

	public InputStream viewTestAsset(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("assetId") String assetId,
			@Context ServletContext context) throws ServletException,
			IOException, FileDownloadException;

	public InputStream viewHTMLAsset(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("assetId") String assetId,
			@Context ServletContext context) throws ServletException,
			IOException, FileDownloadException;
}
