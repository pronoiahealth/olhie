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
package com.pronoiahealth.olhie.client.pages.newbook.dialogs;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddFileModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddYouTubeModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewAssetModalEvent;

/**
 * NewAssetDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 11, 2013
 * 
 */
@Dependent
public class NewAssetDialog extends Composite {

	@Inject
	UiBinder<Widget, NewAssetDialog> binder;

	@UiField
	public Modal newAssetModal;

	@UiField
	public FocusPanel addFile;

	@UiField
	public FocusPanel addLink;

	@UiField
	public FocusPanel addYouTube;

	@UiField
	public FocusPanel addVideo;

	@UiField
	public FocusPanel addOlhieDoc;

	@Inject
	private Event<ShowAddFileModalEvent> showAddFileModalEvent;

	@Inject
	private Event<ShowAddYouTubeModalEvent> showAddYouTubeModalEvent;

	private String currentBookId;

	/**
	 * Constructor
	 * 
	 */
	public NewAssetDialog() {
	}

	@PostConstruct
	public void postConstruct() {
		initWidget(binder.createAndBindUi(this));
	}

	public void show() {
		newAssetModal.show();
	}

	/**
	 * Shows the modal dialog
	 * 
	 * @param showNewBookModalEvent
	 */
	protected void observersShowNewAssetModalEvent(
			@Observes ShowNewAssetModalEvent showNewAssetModalEvent) {
		this.currentBookId = showNewAssetModalEvent.getBookId();
		show();
	}

	@UiHandler("addFile")
	public void handleAddFileClick(ClickEvent click) {
		newAssetModal.hide();
		showAddFileModalEvent.fire(new ShowAddFileModalEvent(currentBookId));
	}

	@UiHandler("addLink")
	public void handleAddLinkClick(ClickEvent click) {
	}

	@UiHandler("addYouTube")
	public void handleAddYouTubeClick(ClickEvent click) {
		newAssetModal.hide();
		showAddYouTubeModalEvent.fire(new ShowAddYouTubeModalEvent(
				currentBookId));
	}

	@UiHandler("addVideo")
	public void handleAddVideoClick(ClickEvent click) {
	}

	@UiHandler("addOlhieDoc")
	public void handleAddOlhieDocClick(ClickEvent click) {
	}

}
