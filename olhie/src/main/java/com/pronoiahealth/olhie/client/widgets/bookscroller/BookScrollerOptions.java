package com.pronoiahealth.olhie.client.widgets.bookscroller;

import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;

public class BookScrollerOptions {
	private int scroll = 1;
	private int sliderSpeed = 500;
	private Easing sliderEasing = Easing.SWING;
	private Easing itemEasing = Easing.SWING;
	private int itemSpeed = 500;

	public BookScrollerOptions() {
	}

	public int getScroll() {
		return scroll;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}

	public int getSliderSpeed() {
		return sliderSpeed;
	}

	public void setSliderSpeed(int sliderSpeed) {
		this.sliderSpeed = sliderSpeed;
	}

	public Easing getSliderEasing() {
		return sliderEasing;
	}

	public void setSliderEasing(Easing sliderEasing) {
		this.sliderEasing = sliderEasing;
	}

	public int getItemSpeed() {
		return itemSpeed;
	}

	public void setItemSpeed(int itemSpeed) {
		this.itemSpeed = itemSpeed;
	}

	public Easing getItemEasing() {
		return itemEasing;
	}

	public void setItemEasing(Easing itemEasing) {
		this.itemEasing = itemEasing;
	}

}
