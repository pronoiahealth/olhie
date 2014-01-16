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
package com.pronoiahealth.olhie.client.utils;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Random;
import com.pronoiahealth.olhie.client.shared.constants.BookImageSizeEnum;

/**
 * Utils.java<br/>
 * Responsibilities:<br/>
 * 1. Various client utilities<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public class Utils {

	/**
	 * Get simple class name. Not supported directly in gwt.
	 * 
	 * @param clazz
	 * @return
	 */
	public static String parseClassSimpleName(Class clazz) {
		String name = clazz.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf > 0) {
			return name.substring(lastIndexOf + 1);
		} else {
			return "";
		}
	}

	/**
	 * Build a URI for file download
	 * 
	 * @param assetId
	 * @param forDownload
	 * @return
	 */
	public static String buildRestServiceForAssetDownloadLink(String assetId,
			String viewType) {
		StringBuilder urlStr = new StringBuilder();
		urlStr.append("rest/book_download/book/");
		urlStr.append(getRandom());
		urlStr.append("/");
		urlStr.append(URL.encodeQueryString(assetId));
		urlStr.append("/");
		urlStr.append(viewType);
		return urlStr.toString();
	}

	/**
	 * Build a URI for logo download
	 * 
	 * @param assetId
	 * @param forDownload
	 * @return
	 */
	public static String buildRestServiceForLogoDownloadLink(String bookId) {
		StringBuilder urlStr = new StringBuilder();
		urlStr.append("rest/logo_download/logo/");
		urlStr.append(getRandom());
		urlStr.append("/");
		urlStr.append(URL.encodeQueryString(bookId));
		return urlStr.toString();
	}

	/**
	 * Build a URI for book front cover download
	 * 
	 * @param assetId
	 * @param forDownload
	 * @return
	 */
	public static String buildRestServiceForBookFrontCoverDownloadLink(
			String bookId, BookImageSizeEnum size) {
		StringBuilder urlStr = new StringBuilder();
		urlStr.append("rest/book_image_download/front/");
		urlStr.append(size.toString());
		urlStr.append("/");
		urlStr.append(getRandom());
		urlStr.append("/");
		urlStr.append(URL.encodeQueryString(bookId));
		return urlStr.toString();
	}

	/**
	 * Build a URI for book back cover download
	 * 
	 * @param assetId
	 * @param forDownload
	 * @return
	 */
	public static String buildRestServiceForBookBackCoverDownloadLink(
			String bookId) {
		StringBuilder urlStr = new StringBuilder();
		urlStr.append("rest/book_image_download/back/");
		urlStr.append(getRandom());
		urlStr.append("/");
		urlStr.append(URL.encodeQueryString(bookId));
		return urlStr.toString();
	}

	/**
	 * @param programRef
	 * @return
	 */
	public static String buildRestServiceForTVDownload(String programRef) {
		StringBuilder urlStr = new StringBuilder();
		urlStr.append("rest/tv_download/tv/");
		urlStr.append(getRandom());
		urlStr.append("/");
		urlStr.append(URL.encodeQueryString(programRef));
		return urlStr.toString();
	}

	/**
	 * Gets a random integer
	 * 
	 * @return
	 */
	public static String getRandom() {
		return "" + Random.nextInt(100000);
	}
}
