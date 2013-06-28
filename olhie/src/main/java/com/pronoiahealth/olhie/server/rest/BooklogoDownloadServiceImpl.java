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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.lowagie.text.pdf.codec.Base64;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.exceptions.FileDownloadException;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;

/**
 * BooklogoDownloadServiceImpl.java<br/>
 * Responsibilities:<br/>
 * 1. Down load a book logo<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 28, 2013
 * 
 */
@Path("/logo_download")
public class BooklogoDownloadServiceImpl implements BooklogoDownloadService {
	@Inject
	private Logger log;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public BooklogoDownloadServiceImpl() {
	}

	@Override
	@GET
	@Path("/logo/{uniqueNumb}/{bookId}")
	// @Produces({"application/pdf", "application/octet-stream", "text/html"})
	@Produces({ "application/octet-stream" })
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.ANONYMOUS })
	public InputStream getBooklogo(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("bookId") String bookId, @Context ServletContext context)
			throws ServletException, IOException, FileDownloadException {
		DataInputStream in = null;
		try {
			// Get the file contents
			OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
					"select from Book where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", bookId);
			List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);
			Book book = null;
			if (bResult != null && bResult.size() == 1) {
				book = bResult.get(0);
			} else {
				throw new FileDownloadException(String.format(
						"Could not find Book for id %s", bookId));
			}

			// If this service gets called its assumed the logo exists
			String fileContents = book.getBase64LogoData();
			String fileName = book.getLogoFileName();
			String mimetype = context.getMimeType(fileName);
			byte[] fileBytes = Base64.decode(fileContents);
			response.setContentType((mimetype != null) ? mimetype
					: "application/octet-stream");

			// No image caching
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			in = new DataInputStream(new ByteArrayInputStream(fileBytes));
			return in;
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"Throwing servlet exception for unhandled exception", e);

			if (e instanceof FileDownloadException) {
				throw (FileDownloadException) e;
			} else {
				throw new FileDownloadException(e);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

}