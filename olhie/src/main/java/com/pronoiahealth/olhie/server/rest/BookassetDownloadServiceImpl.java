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
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;

/**
 * BookassetDownloadServiceImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 20, 2013
 * 
 */
@Path("/book_download")
public class BookassetDownloadServiceImpl implements BookassetDownloadService {
	@Inject
	private Logger log;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	/**
	 * Constructor
	 * 
	 */
	public BookassetDownloadServiceImpl() {
	}

	@Override
	@GET
	@Path("/book/{uniqueNumb}/{assetId}/DOWNLOAD")
	// @Produces({"application/pdf", "application/octet-stream", "text/html"})
	@Produces({ "application/octet-stream" })
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	public InputStream downloadAsset(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("assetId") String assetId,
			@Context ServletContext context) throws ServletException,
			IOException, FileDownloadException {
		
		DataInputStream in = null;
		try {
			// Get the file contents
			OSQLSynchQuery<Bookasset> bQuery = new OSQLSynchQuery<Bookasset>(
					"select from Bookasset where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", assetId);
			List<Bookasset> bResult = ooDbTx.command(bQuery).execute(bparams);
			Bookasset bookasset = null;
			if (bResult != null && bResult.size() == 1) {
				bookasset = bResult.get(0);
			} else {
				throw new FileDownloadException(String.format(
						"Could not find Book for id %s", assetId));
			}

			String fileContents = bookasset.getBase64Data();
			String fileName = bookasset.getItemName();
			String mimetype = context.getMimeType(fileName);
			// Base64 unencode
			byte[] fileBytes = Base64.decode(fileContents);

			response.setContentType((mimetype != null) ? mimetype
					: "application/octet-stream");

			// No image caching
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);

			// response.setContentLength(fileBytes.length);
			// response.setHeader("Content-Disposition", "inline; filename="
			// + fileName);

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

	@Override
	@GET
	@Path("/book/{uniqueNumb}/{assetId}/PDF")
	@Produces({ "application/pdf" })
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	public InputStream viewPDFAsset(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("assetId") String assetId,
			@Context ServletContext context) throws ServletException,
			IOException, FileDownloadException {

		DataInputStream in = null;
		try {
			// Get the file contents
			OSQLSynchQuery<Bookasset> bQuery = new OSQLSynchQuery<Bookasset>(
					"select from Bookasset where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", assetId);
			List<Bookasset> bResult = ooDbTx.command(bQuery).execute(bparams);
			Bookasset bookasset = null;
			if (bResult != null && bResult.size() == 1) {
				bookasset = bResult.get(0);
			} else {
				throw new FileDownloadException(String.format(
						"Could not find Book for id %s", assetId));
			}

			String fileContents = bookasset.getBase64Data();
			String fileName = bookasset.getItemName();
			// Base64 unencode
			byte[] fileBytes = Base64.decode(fileContents);

			response.setContentType("application/pdf");
			// No image caching
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			response.setContentLength(fileBytes.length);
			response.setHeader("Content-Disposition", "inline; filename="
					+ fileName);

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
	
	@Override
	@GET
	@Path("/book/{uniqueNumb}/{assetId}/TEXT")
	@Produces({ "text/plain" })
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	public InputStream viewTestAsset(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("assetId") String assetId,
			@Context ServletContext context) throws ServletException,
			IOException, FileDownloadException {

		DataInputStream in = null;
		try {
			// Get the file contents
			OSQLSynchQuery<Bookasset> bQuery = new OSQLSynchQuery<Bookasset>(
					"select from Bookasset where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", assetId);
			List<Bookasset> bResult = ooDbTx.command(bQuery).execute(bparams);
			Bookasset bookasset = null;
			if (bResult != null && bResult.size() == 1) {
				bookasset = bResult.get(0);
			} else {
				throw new FileDownloadException(String.format(
						"Could not find Book for id %s", assetId));
			}

			String fileContents = bookasset.getBase64Data();
			String fileName = bookasset.getItemName();
			// Base64 unencode
			byte[] fileBytes = Base64.decode(fileContents);

			response.setContentType("text/plain");
			// No image caching
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			response.setContentLength(fileBytes.length);
			response.setHeader("Content-Disposition", "inline; filename="
					+ fileName);

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
	
	@Override
	@GET
	@Path("/book/{uniqueNumb}/{assetId}/HTML")
	@Produces({ "text/html" })
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	public InputStream viewHTMLAsset(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("assetId") String assetId,
			@Context ServletContext context) throws ServletException,
			IOException, FileDownloadException {

		DataInputStream in = null;
		try {
			// Get the file contents
			OSQLSynchQuery<Bookasset> bQuery = new OSQLSynchQuery<Bookasset>(
					"select from Bookasset where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", assetId);
			List<Bookasset> bResult = ooDbTx.command(bQuery).execute(bparams);
			Bookasset bookasset = null;
			if (bResult != null && bResult.size() == 1) {
				bookasset = bResult.get(0);
			} else {
				throw new FileDownloadException(String.format(
						"Could not find Book for id %s", assetId));
			}

			String fileContents = bookasset.getBase64Data();
			String fileName = bookasset.getItemName();
			// Base64 unencode
			byte[] fileBytes = Base64.decode(fileContents);

			response.setContentType("text/html");
			// No image caching
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			response.setContentLength(fileBytes.length);
			response.setHeader("Content-Disposition", "inline; filename="
					+ fileName);

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
