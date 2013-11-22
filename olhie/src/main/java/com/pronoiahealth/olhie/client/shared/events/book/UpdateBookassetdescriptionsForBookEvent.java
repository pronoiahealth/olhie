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
 * 1. Updates the position for the Bookassetdescriptions in a Book.</br>
 * 2. Optionally inactivate a Bookassetdescrition.</br>
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
public class UpdateBookassetdescriptionsForBookEvent {

	private Map<String, Integer> posMap;
	private String badIdToRemove;

	/**
	 * Constructor
	 * 
	 */
	public UpdateBookassetdescriptionsForBookEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param posMap
	 */
	public UpdateBookassetdescriptionsForBookEvent(Map<String, Integer> posMap) {
		super();
		this.posMap = posMap;
	}

	/**
	 * Constructor
	 *
	 * @param posMap
	 * @param badIdToRemove
	 */
	public UpdateBookassetdescriptionsForBookEvent(Map<String, Integer> posMap,
			String badIdToRemove) {
		super();
		this.posMap = posMap;
		this.badIdToRemove = badIdToRemove;
	}

	public Map<String, Integer> getPosMap() {
		return posMap;
	}

	public void setPosMap(Map<String, Integer> posMap) {
		this.posMap = posMap;
	}

	public String getBadIdToRemove() {
		return badIdToRemove;
	}

	public void setBadIdToRemove(String badIdToRemove) {
		this.badIdToRemove = badIdToRemove;
	}
}
