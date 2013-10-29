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

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * AbstractDialogBasedListenerClientFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 28, 2013
 * 
 */
public abstract class AbstractWidgetBasedListenerClientFeature extends
		AbstractClientFeature {

	protected Widget widget;

	/**
	 * Attach the dialog to the RootPanel if not already attached
	 * 
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#activate()
	 */
	@Override
	public boolean activate() {
		AsyncBeanManager bm = IOC.getAsyncBeanManager();

		// Remove it if its there
		if (widget != null) {
			RootPanel.get().remove(widget);
			bm.destroyBean(widget);
		}

		this.widget = getNewWidget();
		RootPanel.get().add(widget);

		return true;
	}

	/**
	 * Remove the dialog from the RootPanel if attached.
	 * 
	 * 
	 * @see com.pronoiahealth.olhie.client.features.ClientFeature#deactivate()
	 */
	@Override
	public boolean deactivate() {
		if (widget != null) {
			AsyncBeanManager bm = IOC.getAsyncBeanManager();
			RootPanel.get().remove(widget);
			bm.destroyBean(widget);
			return true;
		} else {
			return false;
		}
	}

	protected abstract Widget getNewWidget();
}
