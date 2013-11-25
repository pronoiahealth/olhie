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

/**
 * BookAssetDataType.java<br/>
 * Responsibilities:<br/>
 * 1. Various types of uploadable book assets<br/>
 * 2. Could be data driven in the future<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 12, 2013
 * 
 */
public enum BookAssetDataType {
	FILE("FILE_ALT", "icon-file-alt"), LINK("LINK", "icon-link"), YOUTUBE(
			"YOUTUBE", "icon-youtube"), VIDEO("FILM", "icon-film");

	private String gwtBootstrapName;
	private String bootstrapClass;

	BookAssetDataType(String gwtBootstrapName, String bootstrapClass) {
		this.gwtBootstrapName = gwtBootstrapName;
		this.bootstrapClass = bootstrapClass;
	}

	public String getGwtBootstrapName() {
		return gwtBootstrapName;
	}

	public String getBootstrapClass() {
		return bootstrapClass;
	}
}
