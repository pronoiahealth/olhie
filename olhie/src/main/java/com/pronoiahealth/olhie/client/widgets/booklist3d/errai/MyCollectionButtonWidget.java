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
package com.pronoiahealth.olhie.client.widgets.booklist3d.errai;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * MyCollectionButtonWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Oct 17, 2013
 *
 */
@Templated("#myCollectionButtonContainer")
public class MyCollectionButtonWidget extends Composite {
	
	@Inject
	@DataField("myCollectionButtonContainer")
	private SimplePanel myCollectionButtonContainer;

	@DataField("myCollectionBtn")
	private Element myCollectionBtn = DOM.createAnchor();

	@DataField("myCollectionBtnIcon")
	private Element myCollectionBtnIcon = DOM.createElement("i");

	/**
	 * Constructor
	 *
	 */
	public MyCollectionButtonWidget() {
	}

	/**
	 * 
	 */
	@PostConstruct
	protected void postConstruct() {
		DOM.sinkEvents(myCollectionBtn, Event.ONCLICK);
	}

	public void setBtnId(String btnId) {
		myCollectionBtn.setId(btnId + "_myCollectionBtnId");
	}
	
	public void setAddToMyCollectionBtn(boolean displayOnly) {
		myCollectionBtn.removeAttribute("style");
		myCollectionBtn.setClassName("btn btn-mini btn-success bk-tocPage-myCollectionsBtn");
		myCollectionBtn.setAttribute("title", "Add to My Collection");
		myCollectionBtnIcon.setClassName("icon-thumbs-up");
	}

	public void setRemoveFromMyCollectionBtn(boolean displayOnly) {
		myCollectionBtn.removeAttribute("style");
		myCollectionBtn.setClassName("btn btn-mini btn-danger bk-tocPage-myCollectionsBtn");
		myCollectionBtn.setAttribute("title", "Remove from My Collection");
		myCollectionBtnIcon.setClassName("icon-thumbs-down");
	}

	public void setHideMyCollectionBtn() {
		myCollectionBtn.setAttribute("style", "display: none;");
	}

}
