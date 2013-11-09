/*******************************************************************************
 * Copyright (c) 2013 Pronoia Health LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pronoia Health LLC - initial API and implementation
 *******************************************************************************/
package com.pronoiahealth.olhie.client.pages;

import static com.google.gwt.query.client.GQuery.$;

import javax.inject.Inject;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;

/**
 * AbstractPage - Extends AbstractComposite<br/>
 * Responsibilities:<br/>
 * 1. Set page backgrounds<br/>
 * 2. Coordinate with AppNavMenu to sync menu with view<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
public abstract class AbstractPage extends AbstractComposite {

	@Inject
	protected PageNavigator nav;

	private boolean fullPageScrollingActive;

	private Timer scrollStopTimer;

	private GQuery scrollLink;

	public AbstractPage() {
	}

	/**
	 * Add full page scrolling. This can only be done before the page is loaded.
	 * 
	 * @param fullPageScrollingActive
	 */
	protected void addFullPageScrolling() {
		// Activate full page scrolling
		// Get the root div
		final GQuery gObj = AppSelectors.INSTANCE.getCenterBackground();

		// Called when scrolling stops
		// Timer set in scroll event
		// Address weird Chrome/Safari (Webkit) issue
		// Just asking for some data from the element emitting
		// scroll events seems to refresh the display and
		// help fix the issue
		scrollStopTimer = new Timer() {
			@Override
			public void run() {
				int scrollTop = gObj.scrollTop();
				// gObj.scrollTop(scrollTop + 3);
				// gObj.scrollTop(scrollTop);
			}
		};

		// Make sure its overflow is set to auto
		gObj.css(CSS.OVERFLOW.with(Overflow.AUTO));

		// State of scroll link
		gObj.data("scrollLinkActive", Boolean.FALSE);

		// Create the scroll link
		scrollLink = $("<a href=\"#\" class=\"ph-BulletinBoard-Scrollup\">Scroll</a>");

		final Function fadeIn = new Function() {
			@Override
			public void f(Element e) {
				scrollLink.css(Properties.create("opacity: 1.0;"));
				// super.f(e);
			}
		};

		final Function fadeOut = new Function() {
			@Override
			public void f(Element e) {
				scrollLink.css(Properties.create("opacity: 0.0;"));
				// super.f(e);
			}
		};

		// Append the link to the root div
		gObj.append(scrollLink);

		// Bind the scroll event
		gObj.bind(Event.ONSCROLL, new Function() {
			@Override
			public boolean f(Event e) {
				// GQuery rootDiv = $(e);
				// int scrollTop = rootDiv.scrollTop();
				// Set timer
				scrollStopTimer.cancel();
				scrollStopTimer.schedule(500);

				// Test scroll top
				int scrollTop = gObj.scrollTop();
				boolean scrollLinkActive = (Boolean) gObj
						.data("scrollLinkActive");
				if (scrollTop >= 100 && scrollLinkActive == false) {
					gObj.data("scrollLinkActive", Boolean.TRUE);
					scrollLink.fadeIn(500, fadeIn);
				} else if (scrollTop < 100 && scrollLinkActive == true) {
					gObj.data("scrollLinkActive", Boolean.FALSE);
					scrollLink.fadeOut(500, fadeOut);
				}

				// return super.f(e);
				return false;
			}
		});

		// Bind to the scrollup link
		scrollLink.bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				gObj.animate("scrollTop: 0", 500, (Function) null);
				scrollLink.hide();
				return false;
			}
		});

		this.fullPageScrollingActive = true;
	}

	/**
	 * Allows for page background to be set by adding a class to the div with
	 * the class center-background. Adjust the style attribute to be empty
	 * 
	 * @param backgroundClass
	 */
	protected void setPageBackgroundClass(String backgroundClass) {
		GQuery gObj = AppSelectors.INSTANCE.getCenterBackground();
		if (backgroundClass == null || backgroundClass.length() == 0) {
			gObj.attr("class", "center-background");
			gObj.attr("style", "");
		} else {
			gObj.attr("class", "center-background " + backgroundClass);
			gObj.attr("style", "");
		}
	}

	/**
	 * Sets a background image by setting the style attribute of the div with
	 * the class center-background. Calling the setPageBackgroundClass will
	 * remove the style setting.
	 * 
	 * @param backgroundImage
	 */
	protected void setPageBackgroundStyle(String backgroundImage) {
		GQuery gObj = AppSelectors.INSTANCE.getCenterBackground();
		gObj.attr("style", "background: white; " + "background-image: url(\""
				+ backgroundImage + "\"); " + "background-repeat: repeat;");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		super.onUnload();

		// If full page scrolling active unwind it
		if (fullPageScrollingActive == true) {
			AppSelectors.INSTANCE.getCenterBackground().unbind(Event.ONSCROLL);
			scrollLink.unbind(Event.ONCLICK);
			scrollLink.remove();
		}
	}
}
