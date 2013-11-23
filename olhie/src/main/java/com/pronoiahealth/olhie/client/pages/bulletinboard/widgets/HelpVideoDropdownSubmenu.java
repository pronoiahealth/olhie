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

public class HelpVideoDropdownSubmenu extends DropdownSubmenu {

	private Map<String, String> helpTopicMap;
	private String[] helpDisplayList;
	private String name;

	public HelpVideoDropdownSubmenu(String name,
			Map<String, String> helpTopicMapSelection,
			String[] helpDisplayListSelection, final HTML topicTitle,
			final Frame videoFrame) {
		// Set name via superclass
		super(name);

		// Setup list
		this.name = name;
		this.helpTopicMap = helpTopicMapSelection;
		this.helpDisplayList = helpDisplayListSelection;

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

		for (String navName : helpDisplayList) {
			NavLink link = new NavLink(navName);
			link.addClickHandler(topicClickHandler);
			getMenuWiget().add(link);
		}
	}

}
