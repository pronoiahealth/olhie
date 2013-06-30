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
package com.pronoiahealth.olhie.client.shared.constants;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * UserBookRelationshipEnum.java<br/>
 * Responsibilities:<br/>
 * 1. The relationship between books and the collector.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 25, 2013
 * 
 */
@Portable
public enum UserBookRelationshipEnum {
	CREATOR("My Books"), COAUTHOR("My Books"), MYCOLLECTION("My Collection"), NONE(
			"No Relationship"), NONLOGGEDINUSER ("User not logged in.");

	private String description;

	UserBookRelationshipEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
