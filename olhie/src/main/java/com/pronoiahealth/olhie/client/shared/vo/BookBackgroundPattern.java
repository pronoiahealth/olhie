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
package com.pronoiahealth.olhie.client.shared.vo;

/**
 * BookBackgroundPattern.java<br/>
 * Responsibilities:<br/>
 * 1. Holds book patterns<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public enum BookBackgroundPattern {
	GREY_WASH("Olhie/images/grey_wash_wall.png"), PAPER(
			"Olhie/images/paper.png");

	private String pattern;

	BookBackgroundPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}
}
