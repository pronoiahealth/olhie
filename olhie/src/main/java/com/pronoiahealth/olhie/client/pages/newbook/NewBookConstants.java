package com.pronoiahealth.olhie.client.pages.newbook;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Constants;

public interface NewBookConstants extends Constants {
	NewBookConstants INSTANCE = GWT.create(NewBookConstants.class);
	
	@DefaultStringValue("Please select a File to upload.")
	String uploadFileError();
}
