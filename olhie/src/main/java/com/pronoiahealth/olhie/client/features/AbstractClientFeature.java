package com.pronoiahealth.olhie.client.features;

import java.util.Map;

public abstract class AbstractClientFeature implements ClientFeature {
	
	/**
	 * Optional
	 */
	protected FeatureCallbackHandler standUpFeaturecallbackHandler;

	public AbstractClientFeature() {
	}

	@Override
	public boolean standUp(Map<String, Object> params) {
		return true;
	}

	/** 
	 * Default implementation that calls standUp and start
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
