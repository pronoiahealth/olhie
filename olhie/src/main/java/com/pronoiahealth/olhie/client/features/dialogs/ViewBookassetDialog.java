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
package com.pronoiahealth.olhie.client.features.dialogs;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.local.ShowViewBookassetDialogEvent;
import com.pronoiahealth.olhie.client.utils.Utils;

/**
 * ViewBookassetDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 23, 2013
 * 
 */
@Dependent
public class ViewBookassetDialog extends Composite {

	@Inject
	UiBinder<Widget, ViewBookassetDialog> binder;

	@UiField
	public Modal viewBookassetModal;

	@UiField
	public HTMLPanel viewBookassetPanel;

	@UiField
	public Button closeButton;

	private NamedFrame iFrame;

	/**
	 * Constructor
	 * 
	 */
	public ViewBookassetDialog() {
	}

	/**
	 * Create the gui via uiBinder. Initialize the DataBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		viewBookassetModal.setStyleName("ph-ViewBookassetDialog-Modal", true);
		viewBookassetModal.setStyleName("ph-ViewBookassetDialog-Modal-Size", true);;
	}

	/**
	 * Observes ShowViewBookassetDialogEvent
	 * 
	 * @param serviceErrorEvent
	 */
	protected void observesShowViewBookassetDialogEvent(
			@Observes ShowViewBookassetDialogEvent showViewBookassetDialogEvent) {
		String assetId = showViewBookassetDialogEvent.getBookassetId();
		String viewType = showViewBookassetDialogEvent.getViewType();
		String uri = Utils.buildRestServiceForAssetDownloadLink(assetId, viewType);
		downloadContent(uri);
		viewBookassetModal.show();
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
		iFrame = new NamedFrame("view");
		iFrame.getElement().setId("viewFrame");
		iFrame.setWidth("100%");
		iFrame.setHeight("590px");
		viewBookassetPanel.add(iFrame);
		iFrame.setUrl(uri);
	}

	/**
	 * Close the error dialog.
	 * 
	 * @param event
	 */
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event) {
		viewBookassetModal.hide();
	}

}
