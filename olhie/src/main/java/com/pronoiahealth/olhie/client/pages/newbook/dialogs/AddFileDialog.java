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
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.moxieapps.gwt.uploader.client.Uploader;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileDialogStartEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogStartHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadProgressEvent;
import org.moxieapps.gwt.uploader.client.events.UploadProgressHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddFileModalEvent;
import com.pronoiahealth.olhie.client.widgets.progressbar.ProgressBar;

/**
 * AddFileDialog.java<br/>
 * Responsibilities:<br/>
 * 1. Dialog to add a new file<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 11, 2013
 * 
 */
public class AddFileDialog extends Composite {

	@Inject
	UiBinder<Widget, AddFileDialog> binder;

	@UiField
	public Modal addFileModal;

	@UiField
	public TextBox description;

	@UiField
	public HorizontalPanel uploadContainer;

	private Uploader uploader;

	private ProgressBar progressBar;
	
	private Image cancelButton;

	/**
	 * Constructor
	 * 
	 */
	public AddFileDialog() {
	}

	@PostConstruct
	public void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Create the upload widget
		progressBar = new ProgressBar();
		cancelButton = new Image(GWT.getModuleName() + "/images/cancel.png"); 
		
		uploader = new Uploader()
				.setUploadURL("/olhie/rest/file_upload/upload")
				.setButtonImageURL(
						GWT.getModuleName() + "/images/upload_button.png")
				.setButtonWidth(133)
				.setButtonHeight(22)
				.setFileSizeLimit("50 MB")
				.setButtonCursor(Uploader.Cursor.HAND)
				.setButtonAction(Uploader.ButtonAction.SELECT_FILE)
				.setFileQueueLimit(1)
				.setFileQueuedHandler(new FileQueuedHandler() {
					public boolean onFileQueued(
							final FileQueuedEvent fileQueuedEvent) {
						// Create a Progress Bar for this file
						progressBar.setTitle(fileQueuedEvent.getFile()
								.getName());
						progressBar.setHeight("18px");
						progressBar.setWidth("200px");

						// Add Cancel Button Image
						cancelButton.setStyleName("cancelButton");
						cancelButton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								uploader.cancelUpload(fileQueuedEvent.getFile()
										.getId(), false);
								progressBar.setProgress(-1.0d);
								cancelButton.removeFromParent();
							}
						});

						uploadContainer.add(progressBar);
						uploadContainer.add(cancelButton);

						return true;
					}
				})
				.setFileDialogStartHandler(new FileDialogStartHandler() {
					public boolean onFileDialogStartEvent(
							FileDialogStartEvent fileDialogStartEvent) {
						if (uploader.getStats().getUploadsInProgress() <= 0) {
							progressBar.removeFromParent();
							cancelButton.removeFromParent();
						}
						return true;
					}
				})
				.setUploadProgressHandler(new UploadProgressHandler() {
					public boolean onUploadProgress(
							UploadProgressEvent uploadProgressEvent) {
						progressBar.setProgress((double) uploadProgressEvent
								.getBytesComplete()
								/ uploadProgressEvent.getBytesTotal());
						return true;
					}
				})
				.setUploadSuccessHandler(new UploadSuccessHandler() {
					public boolean onUploadSuccess(
							UploadSuccessEvent uploadSuccessEvent) {
						cancelButton.removeFromParent();
						return true;
					}
				})
				.setFileDialogCompleteHandler(new FileDialogCompleteHandler() {
					public boolean onFileDialogComplete(
							FileDialogCompleteEvent fileDialogCompleteEvent) {
						if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0
								&& uploader.getStats().getUploadsInProgress() <= 0) {
							progressBar.setProgress(0.0);
							uploader.startUpload();
						}
						return true;
					}
				}).setFileQueueErrorHandler(new FileQueueErrorHandler() {
					public boolean onFileQueueError(
							FileQueueErrorEvent fileQueueErrorEvent) {
						progressBar.setProgress(0.0);
						cancelButton.removeFromParent();
						Window.alert("Upload of file "
								+ fileQueueErrorEvent.getFile().getName()
								+ " failed due to ["
								+ fileQueueErrorEvent.getErrorCode().toString()
								+ "]: " + fileQueueErrorEvent.getMessage());
						return true;
					}
				}).setUploadErrorHandler(new UploadErrorHandler() {
					public boolean onUploadError(
							UploadErrorEvent uploadErrorEvent) {
						progressBar.setProgress(0.0);
						cancelButton.removeFromParent();
						Window.alert("Upload of file "
								+ uploadErrorEvent.getFile().getName()
								+ " failed due to ["
								+ uploadErrorEvent.getErrorCode().toString()
								+ "]: " + uploadErrorEvent.getMessage());
						return true;
					}
				});

		uploadContainer.add(uploader);
		uploadContainer.add(progressBar);
	}

	/**
	 * Show the modal dialog
	 */
	private void showModal() {
		addFileModal.show();
	}

	/**
	 * Watches for an event to tell this component to show the contained dialog
	 * 
	 * @param showAddFileModalEvent
	 */
	protected void observesShowAddFileModalEvent(
			@Observes ShowAddFileModalEvent showAddFileModalEvent) {
		showModal();
	}
}
