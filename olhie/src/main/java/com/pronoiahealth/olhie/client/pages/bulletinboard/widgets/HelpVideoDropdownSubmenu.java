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

import java.util.Map;

import com.github.gwtbootstrap.client.ui.DropdownSubmenu;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.pronoiahealth.olhie.resources.MessageFormatResources;

/**
 * HelpVideoDropdownSubmenu.java<br/>
 * Responsibilities:<br/>
 * 1. Is a submenu for the main help drop down selector on the HelpVideoWidget<br/>
 * 2. Coordinates the selection of a topic with the HelpVideoWidget.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 * 
 */
public class HelpVideoDropdownSubmenu extends DropdownSubmenu {

	private Map<String, String> topicMap;
	private String[] displayList;
	private String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param helpTopicMapSelection
	 * @param helpDisplayListSelection
	 * @param topicTitle
	 * @param videoFrame
	 */
	public HelpVideoDropdownSubmenu(String name,
			Map<String, String> topicMapSelection,
			String[] displayListSelection, final HTML topicTitle,
			final Frame videoFrame) {
		// Set name via superclass
		super(name);

		// Setup list
		this.name = name;
		this.topicMap = topicMapSelection;
		this.displayList = displayListSelection;

		// Build NavLinks
		final ClickHandler topicClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IconAnchor a = (IconAnchor) event.getSource();
				String linkText = a.getText();
				String url = topicMap.get(linkText.trim());
				if (url != null && url.length() > 0) {
					topicTitle.setHTML(MessageFormatResources.INSTANCE
							.setHelpTopicTitle(linkText));
					videoFrame.setUrl(url);
				}
			}
		};

		for (String navName : displayList) {
			NavLink link = new NavLink(navName);
			link.addClickHandler(topicClickHandler);
			getMenuWiget().add(link);
		}
	}

}
