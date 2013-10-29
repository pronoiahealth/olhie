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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import com.lowagie.text.pdf.codec.Base64;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.exceptions.FileUploadException;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * FileUploadServiceImpl.java<br/>
 * Responsibilities:<br/>
 * 1. Restful file upload service implementation<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Path("/book_upload")
public class BookAssetUploadServiceImpl implements BookAssetUploadService {
	@Inject
	private Logger log;

	private long FILE_SIZE_LIMIT = 50 * 1024 * 1024; // 50 MiB

	@Inject
	@DAO
	private BookDAO bookDAO;;

	@Inject
	public BookAssetUploadServiceImpl() {
	}
	
	/**
	 * Receive an upload book assest
	 * 
	 * @see com.pronoiahealth.olhie.server.rest.BookAssetUploadService#process2(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@POST
	@Path("/upload2")
	@Produces("text/html")
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	public String process2(@Context HttpServletRequest req)
			throws ServletException, IOException, FileUploadException {
		try {
			// Check that we have a file upload request
			boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			if (isMultipart == true) {

				//FileItemFactory fileItemFactory = new FileItemFactory();
				String description = null;
				String hoursOfWork = null;
				String bookId = null;
				String action = null;
				String contentType = null;
				String data = null;
				String fileName = null;
				long size = 0;
				ServletFileUpload fileUpload = new ServletFileUpload();
				fileUpload.setSizeMax(FILE_SIZE_LIMIT);
				FileItemIterator iter = fileUpload.getItemIterator(req);
				while(iter.hasNext()) {
					FileItemStream item = iter.next();
				    InputStream stream = item.openStream();
				    if (item.isFormField()) {
						// description
						if (item.getFieldName().equals("description")) {
							description = Streams.asString(stream);
						}
						
						if (item.getFieldName().equals("hoursOfWork")) {
							hoursOfWork = Streams.asString(stream);
						}

						// BookId
						if (item.getFieldName().equals("bookId")) {
							bookId = Streams.asString(stream);
						}

						// action
						if (item.getFieldName().equals("action")) {
							action = Streams.asString(stream);
						}

				    } else {
						if (item != null) {
							contentType = item.getContentType();
							fileName = item.getName();
							item.openStream();
							InputStream in = item.openStream();
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							IOUtils.copy(in, bos);
							byte[] bytes = bos.toByteArray(); // fileItem.get();
							size = bytes.length;
							data = Base64.encodeBytes(bytes);
						}
				    }
				}
				bookDAO.addUpdateBookasset(description, bookId, contentType, data, action,
						fileName, size);
			}
			return "OK";
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"Throwing servlet exception for unhandled exception", e);
			// return "ERROR:\n" + e.getMessage();

			if (e instanceof FileUploadException) {
				throw (FileUploadException) e;
			} else {
				throw new FileUploadException(e.getMessage());
			}
		}
	}
}

/*
 * DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
 * fileItemFactory.setRepository(new File(
 * "/Users/johndestefano/tempFileUpload")); ServletFileUpload fileUpload = new
 * ServletFileUpload( fileItemFactory); fileUpload.setSizeMax(FILE_SIZE_LIMIT);
 * 
 * List<FileItem> items = fileUpload.parseRequest(req);
 * 
 * FileItem fileItem = null; String description = null; String bookId = null;
 * String action = null; for (FileItem item : items) { if (item.isFormField()) {
 * if (log.isLoggable(Level.FINEST)) { log.log(Level.INFO,
 * "Received form field:"); log.log(Level.INFO, "Name: " + item.getFieldName());
 * log.log(Level.INFO, "Value: " + item.getString()); }
 * 
 * // description if (item.getFieldName().equals("description")) { description =
 * item.getString(); }
 * 
 * // BookId if (item.getFieldName().equals("bookId")) { bookId =
 * item.getString(); }
 * 
 * // action if (item.getFieldName().equals("action")) { action =
 * item.getString(); }
 * 
 * } else { if (log.isLoggable(Level.FINEST)) { log.log(Level.INFO,
 * "Received file:"); log.log(Level.INFO, "Name: " + item.getName());
 * log.log(Level.INFO, "Size: " + item.getSize()); } fileItem = item; }
 * 
 * if (!item.isFormField()) { if (item.getSize() > FILE_SIZE_LIMIT) { throw new
 * FileUploadException( "File size exceeds limit"); }
 * 
 * if (!item.isInMemory()) { item.delete(); } } }
 * 
 * // Now process file long size = 0; String contentType = null; String data =
 * null; String fileName = null; if (fileItem != null) { contentType =
 * fileItem.getContentType(); size = fileItem.getSize(); fileName =
 * fileItem.getName(); InputStream in = fileItem.getInputStream();
 * ByteArrayOutputStream bos = new ByteArrayOutputStream(); IOUtils.copy(in,
 * bos); byte[] bytes = bos.toByteArray(); // fileItem.get(); data =
 * Base64.encodeBytes(bytes); }
 * 
 * this.addToDB(description, bookId, contentType, data, action, fileName, size);
 */
