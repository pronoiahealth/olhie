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
package com.pronoiahealth.olhie.client.pages.tv;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.github.gwtbootstrap.client.ui.SplitDropdownButton;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.pronoiahealth.olhie.client.navigation.AnonymousRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;

/**
 * TVPage.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 23, 2013
 * 
 */
@Dependent
@Templated("#root")
@Page(role = { AnonymousRole.class })
public class TVPage extends AbstractPage {

	@Inject
	private TVPromotion tvPromotion;

	@Inject
	private TVVideo tvVideo;
	
	@Inject
	@DataField
	private SplitDropdownButton splitButton;
	
	@DataField
	private Element tvVideoContainer = DOM.createDiv();

	/**
	 * Constructor
	 * 
	 */
	public TVPage() {
	}

	@PostConstruct
	protected void postConstruct() {
		// Configure the splitbutton
		splitButton.setIcon(IconType.MAGIC);
		splitButton.setText("Pick a Channel");
		splitButton.setDropup(true);
	}

	@PageShowing
	protected void onPageShowing() {
		// Set the background which will make the containing div overflow
		// setPageBackgroundClass("ph-BulletinBoard-Background");
		
		// Clear the container
		if (tvVideoContainer.getChildCount() > 0) {
			tvVideoContainer.removeChild(tvVideoContainer.getChild(0));
		}
		
		// Place the promotion in the container
		tvVideoContainer.appendChild(tvPromotion.getElement());
	}

	/**
	 * Set-up<br/>
	 * 1. Screen back ground<br/>
	 * 2. Scroll to top <br/>
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#onLoad()
	 */
	@Override
	protected void onLoad() {
		super.onLoad();

		// Bind to the root div of this page
		addFullPageScrolling();
	}

}
