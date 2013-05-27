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

	public static String parseClassSimpleName(Class clazz) {
		String name = clazz.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf > 0) {
			return name.substring(lastIndexOf + 1);
		} else {
			return "";
		}
	}
}
