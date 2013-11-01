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
package com.pronoiahealth.olhie.client.pages.bulletinboard.widgets;

//import static com.watopi.chosen.client.Chosen.Chosen;
//import static com.google.gwt.query.client.GQuery.$;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Hero;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

/**
 * HelpVideoWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 31, 2013
 * 
 */
public class HelpVideoWidget extends Composite {

	@Inject
	UiBinder<Widget, HelpVideoWidget> binder;

	@UiField
	public Hero hero;

	@UiField
	public Frame videoFrame;

	/**
	 * Constructor
	 * 
	 */
	public HelpVideoWidget() {
	}

	@PostConstruct
	protected void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		
		// Set up display
		hero.getElement().setAttribute("style", "padding: 20px; height: 575px; width: 630px;");
		hero.setStyleName("draggableWidget", true);
		
		// Video Frame
		videoFrame.getElement().setAttribute("type", "text/html");
		videoFrame.setUrl("http://www.youtube.com/embed/tR_ZWbiFBqs?enablejsapi=1&autoplay=0&cc_load_policy=0&iv_load_policy=1&loop=0&modestbranding=1&rel=1&showinfo=1&theme=dark&wmode=opaque&vq=&controls=2&");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}
}
