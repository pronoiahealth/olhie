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

import org.jboss.errai.ioc.client.container.IOC;
import org.jboss.errai.ioc.client.container.async.AsyncBeanManager;

/**
 * AbstractListenerClientFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Dec 14, 2013
 *
 */
public abstract class AbstractListenerClientFeature extends
		AbstractClientFeature {

	protected Object beanInstance;

	/**
	 * Constructor
	 *
	 */
	public AbstractListenerClientFeature() {
	}

	@Override
	public boolean activate() {
		AsyncBeanManager bm = IOC.getAsyncBeanManager();

		// Remove it if its there
		if (beanInstance != null) {
			bm.destroyBean(beanInstance);
		}
		beanInstance = getNewBeanInstance();
		return true;
	}

	@Override
	public boolean deactivate() {
		if (beanInstance != null) {
			AsyncBeanManager bm = IOC.getAsyncBeanManager();
			bm.destroyBean(beanInstance);
			return true;
		} else {
			return false;
		}
	}

	protected abstract Object getNewBeanInstance();

}
