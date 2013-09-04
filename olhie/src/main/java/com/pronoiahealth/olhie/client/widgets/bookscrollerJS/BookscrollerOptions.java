package com.pronoiahealth.olhie.client.widgets.bookscrollerJS;

public enum BookscrollerOptions {

	SCROLL("scroll"), SLIDERSPEED("sliderSpeed"), SLIDEREASING("sliderEasing"), ITEMEASING(
			"itemEasing"), ITEMSPEED("itemSpeed");

	private String value;

	BookscrollerOptions(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
