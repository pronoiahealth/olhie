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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * Images used by components. Saves some space and download bandwidth.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since 5/24/2013
 * 
 */
public interface ImageResourceBundle extends ClientBundle {
	public static final ImageResourceBundle INSTANCE = GWT
			.create(ImageResourceBundle.class);
}
