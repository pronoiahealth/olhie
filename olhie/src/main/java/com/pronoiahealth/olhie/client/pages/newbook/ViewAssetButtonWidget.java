package com.pronoiahealth.olhie.client.pages.newbook;

import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated("ButtonWidget.html#button")
public class ViewAssetButtonWidget extends BaseBookassetActionButtonWidget {

	public ViewAssetButtonWidget() {
		this.iconName = "icon-eye-open";
		this.buttonStyle = "btn-success";
		this.title = "View";
	}

}
