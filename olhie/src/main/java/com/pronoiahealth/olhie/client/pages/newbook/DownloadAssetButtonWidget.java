package com.pronoiahealth.olhie.client.pages.newbook;

import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated("ButtonWidget.html#button")
public class DownloadAssetButtonWidget extends BaseBookassetActionButtonWidget {

	public DownloadAssetButtonWidget() {
		this.iconName = "icon-cloud-download";
		this.buttonStyle = "btn-info";
		this.title = "Download";
	}
}
