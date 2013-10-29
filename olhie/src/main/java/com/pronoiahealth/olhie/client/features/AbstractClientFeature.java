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
 * AbstractClientFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 28, 2013
 * 
 */
public abstract class AbstractClientFeature implements ClientFeature {

	/**
	 * Optional
	 */
	protected FeatureCallbackHandler standUpFeaturecallbackHandler;

	/**
	 * Constructor
	 * 
	 */
	public AbstractClientFeature() {
	}

	@Override
	public boolean standUp(Map<String, Object> params) {
		return true;
	}

	/**
	 * Default implementation that calls standUp and activate
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#standUpAndStart(java.util.Map)
	 */
	@Override
	public boolean standUpAndActivate(Map<String, Object> params) {
		if (standUp(params) == true) {
			return activate();
		} else {
			return false;
		}
	}

	/**
	 * Default call deactivate only
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#tearDown()
	 */
	@Override
	public boolean tearDown() {
		return deactivate();
	}

	@Override
	public void addStandupCallbackHandler(
			FeatureCallbackHandler featureCallbackHandler) {
		this.standUpFeaturecallbackHandler = featureCallbackHandler;
	}
}
