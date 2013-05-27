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
package com.pronoiahealth.olhie.client.widgets.sidebarnav;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Hyperlink;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;

/**
 * Navigation widget that is part of the SibebarMenu<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since 5/2/2013
 * 
 */
public class SideBarNavWidget extends ListItemWidget {
	private String navToPageName;
	private Hyperlink link;
	private Element iTag;
	private Element spanTag;
	private HandlerRegistration clickHandlerReg;
	private SecurityRoleEnum securityRole;

	/**
	 * Constructor
	 * 
	 */
	public SideBarNavWidget() {
		super();
		createElements();
	}

	/**
	 * Constructor
	 * 
	 * @param aName
	 *            - Link Name
	 * @param aRef
	 *            - Link ref
	 * @param iconName
	 *            - Name of font-awesome icon
	 * @param navName
	 *            - Text for the widget
	 */
	public SideBarNavWidget(String navToPageName, String aName, String aRef,
			String iconName, String navName) {
		this(navToPageName, aName, aRef, iconName, navName,
				SecurityRoleEnum.ANONYMOUS);
	}

	/**
	 * Constructor
	 *
	 * @param navToPageName
	 * @param aName
	 * @param aRef
	 * @param iconName
	 * @param navName
	 * @param securityRole
	 */
	public SideBarNavWidget(String navToPageName, String aName, String aRef,
			String iconName, String navName, SecurityRoleEnum securityRole) {
		super();
		this.navToPageName = navToPageName;
		link = new Hyperlink(aName, aRef);
		add(link);
		iTag = DOM.createElement("i");
		iTag.setAttribute("class", iconName);
		link.getElement().getChild(0).appendChild(iTag);
		spanTag = DOM.createSpan();
		spanTag.setInnerText(navName);
		link.getElement().getChild(0).appendChild(spanTag);
		this.securityRole = securityRole;
	}

	private void createElements() {
		link = new Hyperlink();
		iTag = DOM.createElement("i");
		spanTag = DOM.createSpan();
	}

	/**
	 * Returns the name of the page that clicking this widget should navigate to
	 * 
	 * @return
	 */
	public String getNavToPageName() {
		return navToPageName;
	}

	public HandlerRegistration getClickHandlerReg() {
		return clickHandlerReg;
	}

	public void setClickHandlerReg(HandlerRegistration clickHandlerReg) {
		this.clickHandlerReg = clickHandlerReg;
	}

	public SecurityRoleEnum getSecurityRole() {
		return securityRole;
	}

	public void setSecurityRole(SecurityRoleEnum securityRole) {
		this.securityRole = securityRole;
	}
}
