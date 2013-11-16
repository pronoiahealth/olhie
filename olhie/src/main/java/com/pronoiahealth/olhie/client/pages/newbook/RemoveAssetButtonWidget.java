package com.pronoiahealth.olhie.client.pages.newbook;

import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated("ButtonWidget.html#button")
public class RemoveAssetButtonWidget extends BaseBookassetActionButtonWidget {

	public RemoveAssetButtonWidget() {
		this.iconName = "icon-remove-circle";
		this.buttonStyle = "btn-warning";
		this.title = "Remove";
	}

}
