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

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Random;

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
			boolean forDownload) {
		StringBuilder urlStr = new StringBuilder();
		urlStr.append("rest/book_download/book/");
		urlStr.append(Random.nextInt(100000));
		urlStr.append("/");
		urlStr.append(URL.encodeQueryString(assetId));
		urlStr.append("/");
		if (forDownload == true) {
			urlStr.append("download");
		} else {
			urlStr.append("view");
		}
		return urlStr.toString();
	}

}
