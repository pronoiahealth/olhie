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

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * ChannelProgram.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jan 12, 2014
 *
 */
@Portable
public class ChannelProgram {
	private String programName;
	private String programKey;

	/**
	 * Constructor
	 *
	 */
	public ChannelProgram() {
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramKey() {
		return programKey;
	}

	public void setProgramKey(String programKey) {
		this.programKey = programKey;
	}
}
