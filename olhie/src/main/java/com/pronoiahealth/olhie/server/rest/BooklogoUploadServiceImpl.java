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
import java.util.HashMap;
import java.util.List;
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
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.exceptions.FileUploadException;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;

/**
 * BooklogoUploadServiceImpl.java<br/>
 * Responsibilities:<br/>
 * 1. Book logo upload service implementation<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 28, 2013
 * 
 */
@Path("/logo_upload")
public class BooklogoUploadServiceImpl implements BooklogoUploadService {
	@Inject
	private Logger log;

	private long FILE_SIZE_LIMIT = 5 * 1024 * 1024; // 5 MiB

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	@Inject
	public BooklogoUploadServiceImpl() {
	}

	@Override
	@POST
	@Path("/upload")
	@Produces("text/html")
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	public String process(@Context HttpServletRequest req)
			throws ServletException, IOException, FileUploadException {
		try {
			// Check that we have a file upload request
			boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			if (isMultipart == true) {

				// FileItemFactory fileItemFactory = new FileItemFactory();
				String bookId = null;
				String contentType = null;
				String data = null;
				String fileName = null;
				long size = 0;
				ServletFileUpload fileUpload = new ServletFileUpload();
				fileUpload.setSizeMax(FILE_SIZE_LIMIT);
				FileItemIterator iter = fileUpload.getItemIterator(req);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					InputStream stream = item.openStream();
					if (item.isFormField()) {
						// BookId
						if (item.getFieldName().equals("bookId")) {
							bookId = Streams.asString(stream);
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
				addToDB(bookId, contentType, data, fileName, size);
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

	
	/**
	 * Save the logo
	 * 
	 * @param bookId
	 * @param contentType
	 * @param data
	 * @param fileName
	 * @param size
	 * @throws Exception
	 */
	private void addToDB(String bookId, String contentType, String data,
			String fileName, long size) throws Exception {

		// Find Book
		OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
				"select from Book where @rid = :bId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);
		Book book = null;
		if (bResult != null && bResult.size() == 1) {
			book = bResult.get(0);
		} else {
			throw new FileUploadException(String.format(
					"Could not find Book for id %s", bookId));
		}
		
		// Update the book
		book.setLogoFileName(fileName);
		book.setBase64LogoData(data);
		ooDbTx.save(book);
	}

}
