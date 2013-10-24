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
package com.pronoiahealth.olhie.client.features;

import java.util.Map;

/**
 * ClientFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 24, 2013
 * 
 */
public interface ClientFeature {
	/**
	 * Initialize the feature
	 * 
	 * @param params
	 * @throws Exception
	 */
	public boolean standUp(Map<String, Object> params);

	/**
	 * @param params
	 * @throws Exception
	 */
	public boolean standUpAndActivate(Map<String, Object> params);


	/**
	 * Start the feature
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean activate();

	/**
	 * stop the feature
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean deactivate();

	/**
	 * Tear the feature down
	 * 
	 * @throws Exception
	 */
	public boolean tearDown();
	
	/**
	 * @param featureCallbackHandler
	 */
	public void addStandupCallbackHandler(FeatureCallbackHandler featureCallbackHandler);
}
