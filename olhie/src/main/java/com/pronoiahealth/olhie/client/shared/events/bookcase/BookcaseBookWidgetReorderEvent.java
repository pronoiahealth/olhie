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
package com.pronoiahealth.olhie.client.shared.events.bookcase;

import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * BookcaseBookWidgetReorderEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Fired By: BookCaseContainerWidget<br/>
 * Observed By: BookcaseService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 2, 2013
 * 
 */
@Portable
@Conversational
public class BookcaseBookWidgetReorderEvent {
	private Map<String, Integer> widgetPositionMap;

	/**
	 * Constructor
	 * 
	 */
	public BookcaseBookWidgetReorderEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param widgetPositionMap
	 */
	public BookcaseBookWidgetReorderEvent(Map<String, Integer> widgetPositionMap) {
		super();
		this.widgetPositionMap = widgetPositionMap;
	}

	public Map<String, Integer> getWidgetPositionMap() {
		return widgetPositionMap;
	}

	public void setWidgetPositionMap(Map<String, Integer> widgetPositionMap) {
		this.widgetPositionMap = widgetPositionMap;
	}

}
