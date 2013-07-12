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

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
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
	private LoadHandler loadHandler;

	@Inject
	private Event<ClientErrorEvent> clientErrorEvent;

	/**
	 * Constructor
	 * 
	 */
	public DownloadFrame() {
	}

	/**
	 * Create the LoadHandler to observe the Frame. Errors from the server will
	 * get sent back to the frame.
	 */
	@PostConstruct
	private void postConstruct() {

		// Create a load handler to watch for errors.
		loadHandler = new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				IFrameElement elem = ((IFrameElement) iFrame.getElement()
						.cast());
				Document doc = elem.getContentDocument();
				if (doc != null) {
					if (doc.getBody() != null) {
						String str = doc.getBody().getInnerHTML();
						if (str != null && str.length() > 0) {
							clientErrorEvent.fire(new ClientErrorEvent(str));
						}
					}
				}
			}
		};
	}

	/**
	 * Sets up hidden iFrame for download
	 * 
	 * @param uri
	 */
	private void downloadContent(String uri) {
		if (iFrame != null) {
			iFrame.removeFromParent();
			iFrame = null;
		}
		iFrame = new NamedFrame("download");
		iFrame.getElement().setId("downloadFrame");
		iFrame.setVisible(false);
		iFrame.addLoadHandler(loadHandler);
		add(iFrame);
		iFrame.setUrl(uri);
	}

	protected void observesDownloadBookAssetEvent(
			@Observes DownloadBookAssetEvent downloadBookAssetEvent) {
		String assetId = downloadBookAssetEvent.getBookAssetId();
		String uri = Utils.buildRestServiceForAssetDownloadLink(assetId, "DOWNLOAD");
		downloadContent(uri);
	}
}
