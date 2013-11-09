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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.Hero;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.SplitDropdownButton;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.resources.MessageFormatResources;

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
	public SplitDropdownButton helpTopic;

	@UiField
	public HTML topicTitle;

	@UiField
	public Frame videoFrame;

	private Map<String, String> helpTopicMap;

	private String[] displayList;

	/**
	 * Constructor
	 * 
	 */
	public HelpVideoWidget() {
		// helpTopicMap = new HashMap<String, String>();
		// displayList = loadMap();
		displayList = HelpVideoConstants.INSTANCE.displayList();
		helpTopicMap = HelpVideoConstants.INSTANCE.urlMap();
	}

	@PostConstruct
	protected void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Build NavLinks
		final ClickHandler topicClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IconAnchor a = (IconAnchor) event.getSource();
				String linkText = a.getText();
				String url = helpTopicMap.get(linkText.trim());
				if (url != null && url.length() > 0) {
					topicTitle.setHTML(MessageFormatResources.INSTANCE
							.setHelpTopicTitle(linkText));
					videoFrame.setUrl(url);
				}
			}
		};

		for (String name : displayList) {
			NavLink link = new NavLink(name);
			link.addClickHandler(topicClickHandler);
			helpTopic.getMenuWiget().add(link);
		}

		// Set up display
		hero.getElement().setAttribute("style",
				"padding: 20px; height: 625px; width: 607px;");
		hero.setStyleName("draggableWidget", true);

		// Topic title
		String firstTitle = "";
		if (displayList != null && displayList.length > 0) {
			firstTitle = displayList[0];
		}
		topicTitle.setHTML(MessageFormatResources.INSTANCE
				.setHelpTopicTitle(displayList[0]));

		// Video Frame
		videoFrame.getElement().setAttribute("type", "text/html");
		if (firstTitle != null && firstTitle.length() > 0) {
			videoFrame.setUrl(helpTopicMap.get(firstTitle));
		}
	}

	/**
	 * Ok for now, this should be drive off the database
	 */
	private List<String> loadMap() {
		List<String> displayList = new ArrayList<String>();
		displayList.add("Introduction");
		helpTopicMap
				.put("Introduction",
						"http://www.youtube.com/embed/tR_ZWbiFBqs?enablejsapi=1&autoplay=0&cc_load_policy=0&iv_load_policy=1&loop=0&modestbranding=1&rel=1&showinfo=1&theme=dark&wmode=opaque&vq=&controls=2&");

		displayList.add("Technical Overview");
		helpTopicMap
				.put("Technical Overview",
						"http://www.youtube.com/embed/cNB2R2FuTUc?enablejsapi=1&autoplay=0&cc_load_policy=0&iv_load_policy=1&loop=0&modestbranding=1&rel=1&showinfo=1&theme=dark&wmode=opaque&vq=&controls=2&");

		displayList.add("Demonstration Part 1");
		helpTopicMap
				.put("Demonstration Part 1",
						"http://www.youtube.com/embed/VN_YXKcLiZE?enablejsapi=1&autoplay=0&cc_load_policy=0&iv_load_policy=1&loop=0&modestbranding=1&rel=1&showinfo=1&theme=dark&wmode=opaque&vq=&controls=2&");

		displayList.add("Demonstration Part 2");
		helpTopicMap
				.put("Demonstration Part 2",
						"http://www.youtube.com/embed/VBkuSXz32h8?enablejsapi=1&autoplay=0&cc_load_policy=0&iv_load_policy=1&loop=0&modestbranding=1&rel=1&showinfo=1&theme=dark&wmode=opaque&vq=&controls=2&");

		// return display list
		return displayList;
	}
}
