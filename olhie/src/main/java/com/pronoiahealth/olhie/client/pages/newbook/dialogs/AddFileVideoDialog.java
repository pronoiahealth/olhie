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

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.pages.newbook.NewBookConstants;
import com.pronoiahealth.olhie.client.pages.newbook.NewBookMessages;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetActionType;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;
import com.pronoiahealth.olhie.client.shared.events.local.BookContentUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddFileModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddVideoModalEvent;
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
@Dependent
public class AddFileVideoDialog extends Composite {

	@Inject
	UiBinder<Widget, AddFileVideoDialog> binder;

	@UiField
	public Modal addFileModal;

	@UiField
	public TextBox description;

	@UiField
	public TextArea descriptionDetail;

	@UiField
	public TextBox hoursOfWork;

	@UiField
	public ControlLabel uploadFileLbl;

	@UiField
	public ControlGroup descriptionCG;

	@UiField
	public ControlGroup descriptionDetailCG;

	@UiField
	public ControlGroup hoursOfWorkCG;

	@UiField
	public Label noFileToUploadErr;

	@UiField
	public HorizontalPanel uploadContainer;

	@UiField
	public Button uploadButton;

	private Uploader uploader;

	private ProgressBar progressBar;

	private Image cancelButton;

	private String currentBookId;

	private static final String fileTitle = "What file do you want to add?";

	private static final String videoTitle = "What video do you want to add?";

	private static final String uploadingTitle = "Processing your request. Please wait.";

	private String dataType;

	@Inject
	private Event<BookContentUpdatedEvent> bookContentUpdatedEvent;

	/**
	 * Constructor
	 * 
	 */
	public AddFileVideoDialog() {
	}

	@PostConstruct
	public void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Add keyboard handler to hours field
		hoursOfWork.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				int keyCode = event.getNativeEvent().getKeyCode();
				char charCode = event.getCharCode();
				if (!Character.isDigit(charCode)
						&& (keyCode != KeyCodes.KEY_BACKSPACE)) {
					((TextBox) event.getSource()).cancelKey();
				}
			}
		});

		// set focus
		addFileModal.addShownHandler(new ShownHandler() {
			@Override
			public void onShown(ShownEvent shownEvent) {
				description.setFocus(true);
			}
		});
	}

	/**
	 * The uploader uses stats which can't be changes so it needs to be
	 * recreated when the dialog is shown.
	 */
	private void initUploader() {
		// Create the upload widget
		progressBar = new ProgressBar();
		progressBar.setHeight("18px");
		progressBar.setWidth("200px");
		uploadFileLbl.getElement().setInnerText(
				NewBookConstants.INSTANCE.uploadAFile());
		cancelButton = new Image(GWT.getModuleName() + "/images/cancel.png");

		uploader = new Uploader()
				.setUploadURL("/olhie/rest/book_upload/upload2")
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
						String fileName = fileQueuedEvent.getFile().getName();
						uploadFileLbl.getElement().setInnerText(
								NewBookMessages.INSTANCE
										.uploadAFileWithName(fileName));
						progressBar.setTitle(fileName);
						progressBar.setHeight("18px");
						progressBar.setWidth("200px");

						// Add Cancel Button Image
						cancelButton.setStyleName("cancelButton");
						cancelButton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								uploader.cancelUpload(fileQueuedEvent.getFile()
										.getId(), false);
								uploadFileLbl.getElement()
										.setInnerText(
												NewBookConstants.INSTANCE
														.uploadAFile());
								progressBar.setProgress(-1.0d);
								uploader.setButtonDisabled(false);
								cancelButton.removeFromParent();
							}
						});

						uploadContainer.add(progressBar);
						uploadContainer.add(cancelButton);

						// Only allow one file to be uploaded
						uploader.setButtonDisabled(true);

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
				// Close the dialog, clean up screen, tell the app that a book
				// has been updated
				.setUploadSuccessHandler(new UploadSuccessHandler() {
					public boolean onUploadSuccess(
							UploadSuccessEvent uploadSuccessEvent) {
						cancelButton.removeFromParent();
						addFileModal.hide();
						onSuccess();
						clearErrors();
						clearFields();
						return true;
					}
				})
				.setFileDialogCompleteHandler(new FileDialogCompleteHandler() {
					public boolean onFileDialogComplete(
							FileDialogCompleteEvent fileDialogCompleteEvent) {
						if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0
								&& uploader.getStats().getUploadsInProgress() <= 0) {
							progressBar.setProgress(0.0);
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
						uploadButton.setVisible(true);
						noFileToUploadErr.setText("");
						Window.alert("Upload of file "
								+ uploadErrorEvent.getFile().getName()
								+ " failed due to ["
								+ uploadErrorEvent.getErrorCode().toString()
								+ "]: " + uploadErrorEvent.getMessage());
						return true;
					}
				});

		if (dataType.equals(BookAssetDataType.VIDEO.toString())) {
			uploader.setFileTypes("*.mp4");
		}

		uploadContainer.add(uploader);
		uploadContainer.add(progressBar);
	}

	/**
	 * Removes the current uploader from the dom if its there
	 */
	private void removeUploader() {
		if (uploader != null) {
			uploadContainer.remove(uploader);
			uploader = null;
			uploadContainer.remove(progressBar);
			progressBar = null;
		}
	}

	/**
	 * Show the modal dialog
	 */
	private void showModal(String bookId) {
		// clear fields
		clearFields();

		// Remove the current one
		removeUploader();

		// Create a new one
		initUploader();

		// Clear any left over error messages
		clearErrors();

		// Set the book Id
		this.currentBookId = bookId;

		// Show the dialog
		addFileModal.show();

		// Make sure uploadButton showing
		uploadButton.setVisible(true);
	}

	/**
	 * Watches for an event to tell this component to show the contained dialog
	 * 
	 * @param showAddFileModalEvent
	 */
	protected void observesShowAddFileModalEvent(
			@Observes ShowAddFileModalEvent showAddFileModalEvent) {
		addFileModal.setTitle(fileTitle);
		dataType = BookAssetDataType.FILE.toString();
		showModal(showAddFileModalEvent.getBookId());
	}

	/**
	 * Watches for an event to tell this component to show the contained dialog
	 * 
	 * @param showAddFileModalEvent
	 */
	protected void observesShowAddVideoModalEvent(
			@Observes ShowAddVideoModalEvent showAddVideoModalEvent) {
		addFileModal.setTitle(videoTitle);
		dataType = BookAssetDataType.VIDEO.toString();
		showModal(showAddVideoModalEvent.getBookId());
	}

	/**
	 * If the upload was successful fire the book content updated event
	 */
	protected void onSuccess() {
		bookContentUpdatedEvent
				.fire(new BookContentUpdatedEvent(currentBookId));
	}

	/**
	 * The upload button has been clicked. First check that there is a
	 * description and a file queued for upload.
	 * 
	 * @param clickEvt
	 */
	@UiHandler("uploadButton")
	public void uploadButtonClicked(ClickEvent clickEvt) {
		if (uploader != null && uploader.getStats().getUploadsInProgress() <= 0) {
			boolean hasErrors = false;
			clearErrors();

			// Check description
			String desc = description.getText();
			if (desc != null) {
				int descLen = desc.length();
				if (descLen < 1 || descLen > 250) {
					hasErrors = true;
					descriptionCG.setType(ControlGroupType.ERROR);
				}
			} else {
				hasErrors = true;
				descriptionCG.setType(ControlGroupType.ERROR);
			}

			// Check descriptionDetail
			String descDetail = descriptionDetail.getText();
			if (descDetail != null) {
				int descLen = descDetail.length();
				if (descLen > 2048) {
					hasErrors = true;
					descriptionDetailCG.setType(ControlGroupType.ERROR);
				}
			}

			// Check hours of work
			String hours = hoursOfWork.getText();
			if (hours == null || hours.length() == 0) {
				hours = "0";
			} else {
				try {
					int hoursRet = Integer.parseInt(hours);
				} catch (Exception e) {
					hasErrors = true;
					hoursOfWorkCG.setType(ControlGroupType.ERROR);
				}
			}

			// Check upload
			int filesQueued = uploader.getStats().getFilesQueued();
			if (filesQueued != 1) {
				hasErrors = true;
				noFileToUploadErr.setText(NewBookConstants.INSTANCE
						.uploadFileError());
			}

			if (hasErrors == false) {
				JSONObject params = new JSONObject();
				params.put("description", new JSONString(desc));
				params.put("descriptionDetail", new JSONString(descDetail));
				params.put("hoursOfWork", new JSONString(hours));
				params.put("bookId", new JSONString(this.currentBookId));
				params.put("action",
						new JSONString(BookAssetActionType.NEW.name()));
				params.put("dataType", new JSONString(dataType));
				uploader.setPostParams(params);
				uploader.startUpload();
				uploadButton.setVisible(false);
				noFileToUploadErr.setText(this.uploadingTitle);
			}
		}
	}

	/**
	 * Clear the dialog errors
	 */
	private void clearErrors() {
		descriptionCG.setType(ControlGroupType.NONE);
		descriptionDetailCG.setType(ControlGroupType.NONE);
		hoursOfWorkCG.setType(ControlGroupType.NONE);
		noFileToUploadErr.setText("");
	}

	/**
	 * Clear form fields
	 */
	private void clearFields() {
		description.setText(null);
		descriptionDetail.setText(null);
		hoursOfWork.setText(null);
		this.currentBookId = null;
	}
}
