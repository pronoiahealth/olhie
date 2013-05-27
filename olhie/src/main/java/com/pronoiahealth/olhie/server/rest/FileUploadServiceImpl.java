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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.lowagie.text.pdf.codec.Base64;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
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
@Named
@Path("/file_upload")
public class FileUploadServiceImpl implements FileUploadService {
	@Inject
	private Logger log;

	private long FILE_SIZE_LIMIT = 20 * 1024 * 1024; // 20 MiB

	@Inject
	public FileUploadServiceImpl() {
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
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED })
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

	@Override
	@POST
	@Path("/upload2")
	@Produces("text/html")
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED })
	public String process2(@Context HttpServletRequest req)
			throws ServletException, IOException {
		try {
			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			ServletFileUpload fileUpload = new ServletFileUpload(
					fileItemFactory);
			fileUpload.setSizeMax(FILE_SIZE_LIMIT);

			List<FileItem> items = fileUpload.parseRequest(req);

			for (FileItem item : items) {
				if (item.isFormField()) {
					log.log(Level.INFO, "Received form field:");
					log.log(Level.INFO, "Name: " + item.getFieldName());
					log.log(Level.INFO, "Value: " + item.getString());
				} else {
					log.log(Level.INFO, "Received file:");
					log.log(Level.INFO, "Name: " + item.getName());
					log.log(Level.INFO, "Size: " + item.getSize());
					IOUtils.copy(item.getInputStream(), new FileOutputStream(
							"/Users/johndestefano/user/" + item.getName()));
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

			return "OK";
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"Throwing servlet exception for unhandled exception", e);
			return "ERROR:\n" + e.getMessage();
		}
	}
}
