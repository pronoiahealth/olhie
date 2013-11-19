package com.pronoiahealth.olhie.client.pages.newbook.features;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.features.AbstractWidgetBasedListenerClientFeature;
import com.pronoiahealth.olhie.client.pages.newbook.dialogs.BookDescriptionDetailDialog;

public class BookDescriptionDetailDialogHandlerFeature extends
		AbstractWidgetBasedListenerClientFeature {

	@Inject
	private Instance<BookDescriptionDetailDialog> instFactory;

	public BookDescriptionDetailDialogHandlerFeature() {
	}

	@Override
	protected Widget getNewWidget() {
		return instFactory.get();
	}

}
