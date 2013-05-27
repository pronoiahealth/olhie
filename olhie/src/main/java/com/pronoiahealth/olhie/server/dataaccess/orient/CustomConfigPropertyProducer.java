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
package com.pronoiahealth.olhie.server.dataaccess.orient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.deltaspike.core.spi.config.BaseConfigPropertyProducer;

/**
 * Custom property for use with deltaspike CDI extensions. This property is a
 * comma delimited list of full class names. It is used in the OrientFactory
 * class to submit to the Orient entity manager. The list can be obtained as a
 * default in the appropriate annotation or, in the common case, from the
 * apache-deltaspike.properties file found in the application META-INF
 * directory. By default, DeltaSpike uses this file for global properties that
 * can be used for injection by the ConfigProperty annotation.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 25, 2013
 * 
 */
@ApplicationScoped
public class CustomConfigPropertyProducer extends BaseConfigPropertyProducer {

	/**
	 * Default Constructor
	 */
	public CustomConfigPropertyProducer() {
	}

	/**
	 * Takes a comma delimited string from configuration file and turns this
	 * into a String[]
	 * 
	 * @param injectionPoint
	 * @return
	 */
	@Produces
	@Dependent
	@DBValueObjects
	public String[] produceDBValueObject(InjectionPoint injectionPoint) {
		String configuredValue = getStringPropertyValue(injectionPoint);

		if (configuredValue == null || configuredValue.length() == 0) {
			return null;
		}

		String[] retVal = configuredValue.split(",", -2);
		return retVal;
	}
}
