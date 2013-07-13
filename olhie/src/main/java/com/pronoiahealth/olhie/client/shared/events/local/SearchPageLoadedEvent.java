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
package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;
import org.jboss.errai.common.client.api.annotations.NonPortable;

/**
 * Fired from the Search Page when it is onLoaded
 * in the document. This can be used to give children
 * a chance to adjust their size.<br/>
 * 
 *
 * @author johndestefano
 * @version
 * @since
 *
 */
@Local
@NonPortable
public class SearchPageLoadedEvent {

	/**
	 * Constructor
	 *
	 */
	public SearchPageLoadedEvent() {
	}

}
