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
package com.pronoiahealth.olhie.client.shared.events.book;

import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * UpdateBookassetdescriptionOrderEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired By: NewBookDroppablePanel<br/>
 * Observed By: BookdescriptionDetailService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 19, 2013
 * 
 */
@Portable
@Conversational
public class UpdateBookassetdescriptionOrderEvent {

	private Map<String, Integer> posMap;

	/**
	 * Constructor
	 * 
	 */
	public UpdateBookassetdescriptionOrderEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param posMap
	 */
	public UpdateBookassetdescriptionOrderEvent(Map<String, Integer> posMap) {
		super();
		this.posMap = posMap;
	}

	public Map<String, Integer> getPosMap() {
		return posMap;
	}

	public void setPosMap(Map<String, Integer> posMap) {
		this.posMap = posMap;
	}
}
