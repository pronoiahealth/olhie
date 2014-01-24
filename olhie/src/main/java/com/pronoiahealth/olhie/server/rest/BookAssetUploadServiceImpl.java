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

import javax.enterprise.event.Event;
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
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.QueueBookEvent;
import com.pronoiahealth.olhie.client.shared.exceptions.FileUploadException;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
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

	@Inject
	private ServerUserToken userToken;

	private long FILE_SIZE_LIMIT = 50 * 1024 * 1024; // 50 MiB

	@Inject
	@DAO
	private BookDAO bookDAO;

	@Inject
	private Event<QueueBookEvent> queueBookEvent;

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

				// FileItemFactory fileItemFactory = new FileItemFactory();
				String description = null;
				String hoursOfWorkStr = null;
				String bookId = null;
				String action = null;
				String dataType = null;
				String contentType = null;
				//String data = null;
				byte[] bytes = null;
				String fileName = null;
				long size = 0;
				ServletFileUpload fileUpload = new ServletFileUpload();
				fileUpload.setSizeMax(FILE_SIZE_LIMIT);
				FileItemIterator iter = fileUpload.getItemIterator(req);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					InputStream stream = item.openStream();
					if (item.isFormField()) {
						// description
						if (item.getFieldName().equals("description")) {
							description = Streams.asString(stream);
						}

						if (item.getFieldName().equals("hoursOfWork")) {
							hoursOfWorkStr = Streams.asString(stream);
						}

						// BookId
						if (item.getFieldName().equals("bookId")) {
							bookId = Streams.asString(stream);
						}

						// action
						if (item.getFieldName().equals("action")) {
							action = Streams.asString(stream);
						}

						// datatype
						if (item.getFieldName().equals("dataType")) {
							dataType = Streams.asString(stream);
						}

					} else {
						if (item != null) {
							contentType = item.getContentType();
							fileName = item.getName();
							item.openStream();
							InputStream in = item.openStream();
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							IOUtils.copy(in, bos);
							bytes = bos.toByteArray(); // fileItem.get();
							size = bytes.length;
							//data = Base64.encodeBytes(bytes);
						}
					}
				}

				// convert the hoursOfWork
				int hoursOfWork = 0;
				if (hoursOfWorkStr != null) {
					try {
						hoursOfWork = Integer.parseInt(hoursOfWorkStr);
					} catch (Exception e) {
						log.log(Level.WARNING,
								"Could not conver "
										+ hoursOfWorkStr
										+ " to an int in BookAssetUploadServiceImpl. Converting to 0.");
						hoursOfWork = 0;
					}
				}

				// Verify that the session user is the author or co-author of
				// the book. They would be the only ones who could add to the
				// book.
				String userId = userToken.getUserId();
				boolean isAuthor = bookDAO.isUserAuthorOrCoauthorOfBook(userId,
						bookId);
				if (isAuthor == false) {
					throw new Exception("The user " + userId
							+ " is not the author or co-author of the book.");
				}

				// Add to the database
				bookDAO.addUpdateBookassetBytes(description, bookId, contentType,
						BookAssetDataType.valueOf(dataType).toString(), bytes,
						action, fileName, null, null, size, hoursOfWork, userId);

				// Tell Solr about the update
				queueBookEvent.fire(new QueueBookEvent(bookId, userId));
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