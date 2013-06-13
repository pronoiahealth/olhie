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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.lowagie.text.pdf.codec.Base64;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetActionType;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.exceptions.FileUploadException;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;

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
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	@Inject
	public BookAssetUploadServiceImpl() {
	}

	/**
	 * Process the request
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@POST
	@Path("/upload")
	@Produces("text/html")
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	public String process() throws ServletException, IOException {
		StringBuilder stringBuilder = new StringBuilder();
		Writer w = null;
		ByteArrayInputStream bais = null;
		BufferedInputStream bin = null;
		try {
			HttpServletRequest req = ResteasyProviderFactory
					.getContextData(HttpServletRequest.class);

			// Set up output file
			String fileName = "/Users/johndestefano/user/"
					+ req.getHeader("filename");
			File f = new File(fileName);

			// Read input stream
			bin = new BufferedInputStream(req.getInputStream());
			int c = 0;
			while ((c = bin.read()) != -1) {
				stringBuilder.append((char) c);
			}

			if (c == 0) {
				stringBuilder.append("");
			}

			// Decode
			String b64Str = stringBuilder.toString();
			log.log(Level.INFO, "Before:\n" + b64Str);
			byte[] decoded = Base64.decode(b64Str);

			// Save as file
			w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					f), Charset.forName("ISO-8859-1")));
			bais = new ByteArrayInputStream(decoded);

			c = 0;
			while ((c = bais.read()) != -1) {
				w.write((byte) c);
			}
			w.flush();

			return "OK";
		} catch (IOException ex) {
			log.log(Level.SEVERE, "Error saving file", ex);
			return "ERROR" + ex.getMessage();
		} finally {
			if (bais != null) {
				bais.close();
			}

			if (w != null) {
				w.close();
			}

			if (bin != null) {
				bin.close();
			}
		}
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
			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			ServletFileUpload fileUpload = new ServletFileUpload(
					fileItemFactory);
			fileUpload.setSizeMax(FILE_SIZE_LIMIT);

			List<FileItem> items = fileUpload.parseRequest(req);

			FileItem fileItem = null;
			String description = null;
			String bookId = null;
			String action = null;
			for (FileItem item : items) {
				if (item.isFormField()) {
					log.log(Level.INFO, "Received form field:");
					log.log(Level.INFO, "Name: " + item.getFieldName());
					log.log(Level.INFO, "Value: " + item.getString());

					// description
					if (item.getFieldName().equals("description")) {
						description = item.getString();
					}

					// BookId
					if (item.getFieldName().equals("bookId")) {
						bookId = item.getString();
					}

					// action
					if (item.getFieldName().equals("action")) {
						action = item.getString();
					}

				} else {
					log.log(Level.INFO, "Received file:");
					log.log(Level.INFO, "Name: " + item.getName());
					log.log(Level.INFO, "Size: " + item.getSize());
					fileItem = item;
				}

				if (!item.isFormField()) {
					if (item.getSize() > FILE_SIZE_LIMIT) {
						return "File size exceeds limit";
					}

					if (!item.isInMemory()) {
						item.delete();
					}
				}
			}

			// Now process file
			long size = 0;
			String contentType = null;
			String data = null;
			String fileName = null;
			if (fileItem != null) {
				contentType = fileItem.getContentType();
				size = fileItem.getSize();
				fileName = fileItem.getName();
				data = Base64.encodeBytes(fileItem.get());
			}

			this.addToDB(description, bookId, contentType, data, action,
					fileName, size);

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
	 * If its new then add a Bookassetdescription and a Bookasset. If the action
	 * is revise then add a Bookasset and link it to the Bookassetdescription
	 * 
	 * @param description
	 * @param bookId
	 * @param contentType
	 * @param data
	 * @param action
	 * @param fileName
	 * @param size
	 * @throws Exception
	 */
	// TODO: fix this up so it does leave an inconsistent state in the db
	private void addToDB(String description, String bookId, String contentType,
			String data, String action, String fileName, long size)
			throws Exception {

		if (BookAssetActionType.valueOf(action).equals(BookAssetActionType.NEW)) {
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

			// Create Bookassetdescription
			Date now = new Date();
			Bookassetdescription bad = new Bookassetdescription();
			try {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				bad.setBookId(bookId);
				bad.setCreatedDate(now);
				bad.setDescription(description);
				bad.setRemoved(Boolean.FALSE);
				bad = ooDbTx.save(bad);
				ooDbTx.commit();
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				ooDbTx.rollback();
				throw new FileUploadException(e);
			}

			// Bookasset and add to bad list
			Bookasset ba = new Bookasset();
			try {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				ba.setAuthorId(book.getAuthorId());
				ba.setBookassetdescriptionId(bad.getId());
				ba.setContentType(contentType);
				ba.setCreatedDate(now);
				ba.setItemName(fileName);
				ba.setItemType(BookAssetDataType.FILE.name());
				ba.setSize(size);
				ba.setBase64Data(data);
				ba = ooDbTx.save(ba);
				ooDbTx.commit();
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				ooDbTx.rollback();
				throw new FileUploadException(e);
			}

			// Add Bookasset to BookasssetDescription list
			try {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				List<Bookasset> baLst = new LinkedList<Bookasset>();
				baLst.add(ba);
				bad.setBookAssets(baLst);
				bad = ooDbTx.save(bad);
				ooDbTx.commit();
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				ooDbTx.rollback();
				throw new FileUploadException(e);
			}
		}
	}
}
