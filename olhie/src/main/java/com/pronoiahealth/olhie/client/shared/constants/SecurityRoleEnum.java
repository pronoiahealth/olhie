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
 * SecurityRoleEnum.java<br/>
 * Responsibilities:<br/>
 * 1. Used to handle security roles<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
@Portable
public enum SecurityRoleEnum {
	ADMIN("ADMIN", 4), AUTHOR("AUTHOR", 3), REGISTERED("REGISTERED", 2), ANONYMOUS("ANONYMOUS", 1);
	
	private String name;
	private int precedence;
	
	/**
	 * Constructor
	 *
	 * @param name
	 * @param precedence
	 */
	SecurityRoleEnum(String name, int precedence) {
		this.name = name;
		this.precedence = precedence;
	}

	public String getName() {
		return name;
	}

	public int getPrecedence() {
		return precedence;
	}
	
	
}
