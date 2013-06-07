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
package com.pronoiahealth.olhie.client.shared.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * BookCoverListRequestEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired form: BookCCoverService class<br/>
 * Observed by: NewDialog class<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 6, 2013
 *
 */
@Portable
@Conversational
public class BookCoverListRequestEvent {

	/**
	 * Constructor
	 *
	 */
	public BookCoverListRequestEvent() {
	}

}
