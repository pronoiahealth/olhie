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

import java.util.List;

import org.jboss.errai.ioc.client.container.IOC;
import org.jboss.errai.ioc.client.container.async.AsyncBeanDef;
import org.jboss.errai.ioc.client.container.async.AsyncBeanManager;
import org.jboss.errai.ioc.client.container.async.CreationalCallback;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Random;
import com.pronoiahealth.olhie.client.shared.constants.BookImageSizeEnum;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3D_3;

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
	 * Gets a random integer
	 * 
	 * @return
	 */
	public static String getRandom() {
		return "" + Random.nextInt(100000);
	}

	/**
	 * Takes a book list and destroys it through the bean manager. Creates a new
	 * one and calls the callback method with the new bean instance.
	 * 
	 * @param bl
	 * @param books
	 */
	public static void cleanUpAndInitBookList3D(BookList3D_3 bl,
			final List<BookDisplay> books, final BookList3DCreationalHandler callback) {

		AsyncBeanManager bm = IOC.getAsyncBeanManager();

		// Get rid of the old one
		if (bl != null) {
			bm.destroyBean(bl);
		}

		// Create a new one
		AsyncBeanDef<BookList3D_3> itemBeanDef = bm
				.lookupBean(BookList3D_3.class);
		itemBeanDef.getInstance(new CreationalCallback<BookList3D_3>() {
			@Override
			public void callback(BookList3D_3 beanInstance) {
				beanInstance.build(books, false);
				callback.bookListCreationalCallback(beanInstance);
			}
		});
	}
}
