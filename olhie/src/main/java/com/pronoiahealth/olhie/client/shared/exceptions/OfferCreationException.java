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
package com.pronoiahealth.olhie.client.shared.exceptions;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * OfferCreationException.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 10, 2013
 *
 */
@Portable
public class OfferCreationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param arg0
	 */
	public OfferCreationException(String arg0) {
		super(arg0);
	}
	
	/**
	 * Constructor
	 *
	 */
	public OfferCreationException() {
	}
}
