package com.pronoiahealth.olhie.client.widgets.bootstrap;

import com.github.gwtbootstrap.client.ui.base.HoverBase;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.github.gwtbootstrap.client.ui.constants.VisibilityChange;
import com.google.gwt.dom.client.Element;

public class CustomPopover extends HoverBase {

	private String content;
	private String heading;
	private boolean html = false;

	/**
	 * Creates an empty Popover.
	 */
	public CustomPopover() {
		super();
		placement = Placement.RIGHT;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setText(String content) {
		this.content = content;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getText() {
		return this.content;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getHeading() {
		return this.heading;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reconfigure() {

		removeDataIfExists();

		setDataAttribute(getWidget().getElement(), "original-title", heading);

		setDataAttribute(getWidget().getElement(), "content", content);

		configure(getWidget().getElement(), getAnimation(), getPlacement()
				.get(), getTrigger().get(), getShowDelay(), getHideDelay(),
				isHtml(), getContainer());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void changeVisibility(VisibilityChange visibilityChange) {
		changeVisibility(getWidget().getElement(), visibilityChange.get());
	}

	private native void configure(Element element, boolean animated,
			String placement, String trigger, int showDelay, int hideDelay,
			boolean html, String container) /*-{
		$wnd.jQuery(element).popover({
			animation : animated,
			placement : placement,
			html : html,
			trigger : trigger,
			delay : {
				show : showDelay,
				hide : hideDelay
			},
			container : container
		});
	}-*/;

	/**
	 * Change Visibility
	 * 
	 * @param e
	 *            target
	 * @param visibility
	 *            please use VisibilityChange enum.
	 */
	public static native void changeVisibility(Element e, String visibility) /*-{
		$wnd.jQuery(e).popover(visibility);
	}-*/;

	@Override
	protected String getDataName() {
		return "popover";
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}
}