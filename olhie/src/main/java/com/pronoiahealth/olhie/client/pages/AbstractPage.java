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

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.PageShown;

import com.google.gwt.query.client.GQuery;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.shared.events.local.PageVisibleEvent;
import com.pronoiahealth.olhie.client.utils.Utils;

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
	protected Event<PageVisibleEvent> pageVisibleEvent;

	@Inject
	protected PageNavigator nav;

	public AbstractPage() {
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

	/**
	 * What to do when the @PageShown method is called. Subclasses can do
	 * security checks and menu synchronization, for example by implementing
	 * this method.
	 */
	protected abstract boolean whenPageShownCalled();

	/**
	 * Called by classes that are annotated with the @Page annotation when the
	 * page is shown. Subclasses can add logic to do security checks and menu
	 * synchronizations.
	 */
	@PageShown
	private void pageShown() {
		whenPageShownCalled();
	}

	/**
	 * Fire a PageVisibleEvent if required when the @PageShown method is called.
	 */
	protected void pageVisibleEvent() {
		pageVisibleEvent.fire(new PageVisibleEvent(Utils
				.parseClassSimpleName(this.getClass())));
	}
}
