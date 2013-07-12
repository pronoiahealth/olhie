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
 * PeerNotLoggedInException.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 11, 2013
 *
 */
@Portable
public class PeerNotLoggedInException extends Exception {
	private static final long serialVersionUID = 1L;

	public PeerNotLoggedInException() {
	}

	public PeerNotLoggedInException(String arg0) {
		super(arg0);
	}

	public PeerNotLoggedInException(Throwable arg0) {
		super(arg0);
	}

	public PeerNotLoggedInException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
