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
package com.pronoiahealth.olhie.client.widgets;

import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * GlassPanelSpinner.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 18, 2013
 * 
 */
public class GlassPanelSpinner extends SimplePanel implements
		NativePreviewHandler {

	public GlassPanelSpinner() {
		setStyleName("glass-panel");
	}

	@Override
	public void onPreviewNativeEvent(NativePreviewEvent event) {
		event.consume();
		event.cancel();
	}

}
