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

import java.util.List;
import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;

/**
 * GetMyBookcaseResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Responds to a GetMyBookcaseEvent<br/>
 * 
 * <p>
 * Fired By: BookcaseService<br/>
 * Observed By: BookcasePage<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 25, 2013
 * 
 */
@Portable
@Conversational
public class GetMyBookcaseResponseEvent {
	/**
	 * A map containing the display category and a list of book for the category
	 */
	Map<UserBookRelationshipEnum, List<BookDisplay>> displayMap;

	/**
	 * Constructor
	 * 
	 */
	public GetMyBookcaseResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param displayMap
	 */
	public GetMyBookcaseResponseEvent(
			Map<UserBookRelationshipEnum, List<BookDisplay>> displayMap) {
		super();
		this.displayMap = displayMap;
	}

	public Map<UserBookRelationshipEnum, List<BookDisplay>> getDisplayMap() {
		return displayMap;
	}

	public void setDisplayMap(
			Map<UserBookRelationshipEnum, List<BookDisplay>> displayMap) {
		this.displayMap = displayMap;
	}
}
