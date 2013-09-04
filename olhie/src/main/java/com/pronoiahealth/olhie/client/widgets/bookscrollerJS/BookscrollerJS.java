package com.pronoiahealth.olhie.client.widgets.bookscrollerJS;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

public class BookscrollerJS extends Widget {
	private JSONObject defaultOptions;
	private String id;

	public BookscrollerJS(String id, JSONObject defaultOptions) {
		this.id = id;
		this.defaultOptions = defaultOptions;

		if (this.defaultOptions == null) {
			this.defaultOptions = getOptions(1, 500, "easeOutExpo", 500, "easeOutExpo");
		}
	}

	private JSONObject getOptions(int scroll, int sliderSpeed,
			String sliderEasing, int itemSpeed, String itemEasing) {
		JSONObject options = new JSONObject();
		options.put(BookscrollerOptions.SCROLL.toString(), new JSONNumber(
				scroll));
		options.put(BookscrollerOptions.SLIDERSPEED.toString(), new JSONNumber(
				sliderSpeed));
		options.put(BookscrollerOptions.SLIDEREASING.toString(),
				new JSONString(sliderEasing));
		options.put(BookscrollerOptions.ITEMSPEED.toString(), new JSONNumber(
				itemSpeed));
		options.put(BookscrollerOptions.ITEMEASING.toString(), new JSONString(
				itemEasing));
		return options;
	}

	@Override
	protected void onLoad() {
		createBookSliderJS(id, defaultOptions.getJavaScriptObject());
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		destroyBookSliderJS(id);
		super.onUnload();
	}

	private native void createBookSliderJS(String id, JavaScriptObject options) /*-{
		$wnd.$("#" + id).contentcarousel(options);
	}-*/;

	private native void destroyBookSliderJS(String id) /*-{
		$wnd.$("#" + id).remove();
	}-*/;

}
