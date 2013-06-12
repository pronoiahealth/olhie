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
public class NewAssetDialog extends Composite {

	@Inject
	UiBinder<Widget, NewAssetDialog> binder;

	@UiField
	public Modal newAssetModal;

	@UiField
	public FocusPanel addFile;

	@UiField
	public FocusPanel addLink;

	@Inject
	private Event<ShowAddFileModalEvent> showAddFileModalEvent;

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
		show();
	}

	@UiHandler("addFile")
	public void handleAddFileClick(ClickEvent click) {
		newAssetModal.hide();
		showAddFileModalEvent.fire(new ShowAddFileModalEvent());
	}

	@UiHandler("addLink")
	public void handleAddLinkClick(ClickEvent click) {

	}

}
