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
package com.pronoiahealth.olhie.resources;

import com.github.gwtbootstrap.client.ui.config.Configurator;
import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.core.client.GWT;

/**
 * OlhieBootstrapConfigurator.java<br/>
 * Responsibilities:<br/>
 * 1. Custom configurator for gwtBootstrap components<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public class OlhieBootstrapConfigurator implements Configurator {
	public Resources getResources() {
		return GWT.create(OlhieBootstrapResources.class);
	}

	public boolean hasResponsiveDesign() {
		return true;
	}
}