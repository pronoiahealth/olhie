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

import javax.enterprise.event.Observes;

import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.SimplePanel;
import com.pronoiahealth.olhie.client.shared.events.local.DownloadBookAssetEvent;
import com.pronoiahealth.olhie.client.utils.Utils;

/**
 * DownloadFrame.java<br/>
 * Responsibilities:<br/>
 * 1. Used as a target to download. It's part of the main page.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 21, 2013
 * 
 */
public class DownloadFrame extends SimplePanel {
	private NamedFrame iFrame;

	/**
	 * Constructor
	 * 
	 */
	public DownloadFrame() {
	}

	/**
	 * Sets up hidden iFrame for download
	 * 
	 * @param uri
	 */
	private void downloadContent(String uri) {
		if (iFrame != null) {
			iFrame.removeFromParent();
		}
		iFrame = new NamedFrame("download");
		iFrame.setVisible(false);
		iFrame.setUrl(uri);
		add(iFrame);
	}

	protected void observesDownloadBookAssetEvent(
			@Observes DownloadBookAssetEvent downloadBookAssetEvent) {
		String assetId = downloadBookAssetEvent.getBookAssetId();
		String uri = Utils.buildRestServiceForAssetDownloadLink(assetId, true);
		downloadContent(uri);
	}
}
