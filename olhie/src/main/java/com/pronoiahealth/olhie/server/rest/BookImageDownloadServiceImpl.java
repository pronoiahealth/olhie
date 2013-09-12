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
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.exceptions.FileDownloadException;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * BookImageDownloadServiceImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 11, 2013
 * 
 */
@Path("/book_image_download")
public class BookImageDownloadServiceImpl implements BookImageDownloadService {
	@Inject
	private Logger log;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public BookImageDownloadServiceImpl() {
	}

	/**
	 * @see com.pronoiahealth.olhie.server.rest.BookImageDownloadService#getBookFrontCoverImage(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.String,
	 *      javax.servlet.ServletContext)
	 */
	@Override
	@GET
	@Path("/front/{uniqueNumb}/{bookId}")
	@SecureAccess({ SecurityRoleEnum.ANONYMOUS })
	public InputStream getBookFrontCoverImage(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("bookId") String bookId, @Context ServletContext context)
			throws ServletException, IOException, FileDownloadException {
		DataInputStream in = null;
		try {
			// Get the file contents
			Book book = BookDAO.getBookById(bookId, ooDbTx);
			if (book == null) {
				throw new FileDownloadException(String.format(
						"Could not find Book for id %s", bookId));
			}

			byte[] fileBytes = null;
			String fileContents = book.getBase64FrontCover();
			if (fileContents != null && fileContents.length() > 0) {
				String fileName = book.getBookTitle() + "_front_cover.png";
				String mimetype = context.getMimeType(fileName);
				fileBytes = Base64.decode(fileContents);
				response.setContentType((mimetype != null) ? mimetype
						: "application/octet-stream");
			} else {
				fileBytes = new byte[0];
			}

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

	/**
	 * @see com.pronoiahealth.olhie.server.rest.BookImageDownloadService#getBookBackCoverImage(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.String,
	 *      javax.servlet.ServletContext)
	 */
	@Override
	@GET
	@Path("/path/{uniqueNumb}/{bookId}")
	@Produces({ "application/octet-stream" })
	@SecureAccess({ SecurityRoleEnum.ANONYMOUS })
	public InputStream getBookBackCoverImage(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("bookId") String bookId, @Context ServletContext context)
			throws ServletException, IOException, FileDownloadException {
		DataInputStream in = null;
		try {
			// Get the file contents
			Book book = BookDAO.getBookById(bookId, ooDbTx);
			if (book == null) {
				throw new FileDownloadException(String.format(
						"Could not find Book for id %s", bookId));
			}

			byte[] fileBytes = null;
			String fileContents = book.getBase64BackCover();
			if (fileContents != null && fileContents.length() > 0) {
				String fileName = book.getBookTitle() + "_back_cover.png";
				String mimetype = context.getMimeType(fileName);
				fileBytes = Base64.decode(fileContents);
				response.setContentType((mimetype != null) ? mimetype
						: "application/octet-stream");
			} else {
				fileBytes = new byte[0];
			}

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
