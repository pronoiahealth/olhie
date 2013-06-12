package com.pronoiahealth.olhie.client.widgets.progressbar;

import com.google.gwt.user.client.Element;

public interface ResizableWidget {
	Element getElement();
	boolean isAttached();
	void onResize(int width, int height);
}
