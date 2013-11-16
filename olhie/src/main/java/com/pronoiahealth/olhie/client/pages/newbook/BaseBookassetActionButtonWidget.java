package com.pronoiahealth.olhie.client.pages.newbook;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;

@Templated("ButtonWidget.html#root")
public class BaseBookassetActionButtonWidget extends Composite {

	@Inject
	@DataField
	private Anchor button;

	@DataField
	private Element buttonIcon = DOM.createElement("i");

	protected String iconName;
	
	protected String buttonStyle;
	
	protected String title;

	public BaseBookassetActionButtonWidget() {
	}

	/**
	 * Sets icon. Depends on icon name string being set in constructor
	 */
	@PostConstruct
	protected final void postConstruct() {
		button.setStyleName(buttonStyle, true);
		button.getElement().setAttribute("data-original-title", title);
		button.getElement().setAttribute("title", title);
		buttonIcon.setClassName(iconName);
		setTooltip(title);
	}

	/**
	 * Call this method before adding this widget to the DOM to set its
	 * properties
	 * 
	 * @param bookassetid
	 * @return
	 */
	public Element setAndBind(String bookassetid) {
		button.getElement().setAttribute("bookassetid", bookassetid);

		// Return the element to be bound latter
		return button.getElement();
	}
	
	protected void setTooltip(String message) {
		Tooltip tip = new Tooltip();
		tip.setWidget(button);
		tip.setText(message);
		tip.setPlacement(Placement.TOP);
		tip.setContainer("body");
		tip.reconfigure();
	}

}
