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

import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3D_3;

/**
 * BookList3DCreationalCallback.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 1, 2013
 * 
 */
public interface BookList3DCreationalHandler {

	/**
	 * @param currentBookList3D
	 */
	public void bookListCreationalCallback(BookList3D_3 currentBookList3D);
}
