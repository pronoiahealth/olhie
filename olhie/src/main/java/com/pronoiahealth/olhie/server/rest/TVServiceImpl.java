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
import java.io.File;
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

import org.apache.commons.io.FileUtils;
import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.exceptions.FileDownloadException;
import com.pronoiahealth.olhie.server.security.SecureAccess;

/**
 * TVServiceImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jan 14, 2014
 * 
 */
@Path("/tv_download")
public class TVServiceImpl implements TVService {
	@Inject
	private Logger log;

	@Inject
	@ConfigProperty(name = "OLHIE_TV_BASE_DIR")
	private String tvChannelBaseDir;

	/**
	 * Constructor
	 * 
	 */
	public TVServiceImpl() {
	}

	/**
	 * @see com.pronoiahealth.olhie.server.rest.TVService#getVideo(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.String,
	 *      javax.servlet.ServletContext)
	 */
	@Override
	@GET
	@Path("/tv/{uniqueNumb}/{programRef}")
	// @Produces({"application/pdf", "application/octet-stream", "text/html"})
	@Produces({ "application/octet-stream" })
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
		SecurityRoleEnum.REGISTERED, SecurityRoleEnum.ANONYMOUS })
	public InputStream getVideo(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("programRef") String programRef,
			@Context ServletContext context) throws ServletException,
			IOException, FileDownloadException {
		DataInputStream in = null;
		try {
			// Get the file contents
			File programFile = findFile(programRef);
			if (programFile == null) {
				throw new FileDownloadException(String.format(
						"Could not find file for id %s", programRef));
			}

			String fileName = programRef.substring(programRef.lastIndexOf("|"));
			String mimetype = context.getMimeType(fileName);
			// Base64 unencode
			byte[] fileBytes = FileUtils.readFileToByteArray(programFile);

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

	/**
	 * Find file
	 * 
	 * @param programKey
	 * @return
	 */
	private File findFile(String programKey) {
		String fStr = programKey.replace('_', ' ').replace('|', '/');
		String channelDir = fStr.substring(0, fStr.indexOf('/'));
		String fName = fStr.substring(fStr.lastIndexOf('/'));
		String fDir = fName.substring(0, fName.lastIndexOf('.'));
		return new File(tvChannelBaseDir + "/" + channelDir + "/" + fDir + "/"
				+ fName);
	}

}
