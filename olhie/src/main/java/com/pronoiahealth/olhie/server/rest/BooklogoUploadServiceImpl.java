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
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.services.BookCoverImageService;
import com.pronoiahealth.olhie.server.services.TempThemeHolder;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

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

	private long FILE_SIZE_LIMIT = 50 * 1024; // 50k

	@Inject
	@DAO
	private BookDAO bookDAO;

	@Inject
	private TempThemeHolder holder;

	@Inject
	private BookCoverImageService imgService;

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
				// String data = null;
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
							bytes = bos.toByteArray(); // fileItem.get();
							size = bytes.length;
							// data = Base64.encodeBytes(bytes);
						}
					}
				}

				// Add the logo
				Book book = bookDAO.getBookById(bookId);

				// Update the front cover
				BookCategory cat = holder.getCategoryByName(book.getCategory());
				BookCover cover = holder.getCoverByName(book.getCoverName());
				String authorName = bookDAO.getAuthorName(book.getAuthorId());
				//String frontBookCoverEncoded = imgService
				//		.createDefaultFrontCoverEncoded(book, cat, cover,
				//				bytes, authorName);
				byte[] frontBookCoverBytes = imgService.createDefaultFrontCover(book, cat, cover,
										bytes, authorName);
				
				//String smallFrontBookCoverEncoded = imgService
				//		.createDefaultSmallFrontCoverEncoded(book, cat, cover,
				//				bytes, authorName);
				byte[] frontBookCoverSmallBytes = imgService.createDefaultSmallFrontCover(book, cat, cover,
						bytes, authorName);
				

				// Save it
				// Add the logo
				book = bookDAO.addLogoAndFrontCoverBytes(bookId, contentType, bytes,
						fileName, size, frontBookCoverBytes, frontBookCoverSmallBytes);

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
