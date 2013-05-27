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
package com.pronoiahealth.olhie.client.widgets.dialogs;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * BaseModalDialog.java<br/>
 * Responsibilities:<br/>
 * 1. Base dialog class<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public abstract class BaseModalDialog extends Modal {
	private HTML msgTxt;

	/**
	 * Constructor
	 *
	 */
	public BaseModalDialog() {
	}

	/**
	 * Constructor
	 *
	 * @param title
	 * @param visible
	 * @param animate
	 * @param bdType
	 * @param keyBoard
	 * @param prefix
	 * @param btnName
	 * @param btnType
	 * @param iconType
	 */
	public BaseModalDialog(String title, boolean visible, boolean animate,
			BackdropType bdType, boolean keyBoard, String prefix,
			String btnName, ButtonType btnType, IconType iconType) {
		setTitle(title);
		setVisible(visible);
		setAnimation(animate);
		setBackdrop(bdType);
		setKeyboard(keyBoard);
		VerticalPanel vPanel = new VerticalPanel();
		add(vPanel);
		vPanel.add(new Label(prefix));
		msgTxt = new HTML();
		vPanel.add(msgTxt);
		ModalFooter footer = new ModalFooter();
		add(footer);
		Button btn = new Button(btnName);
		btn.setType(btnType);
		btn.setIcon(iconType);
		footer.add(btn);
		btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
	}
	
	public void showCenteredMsg(String txt) {
		setMsgText(txt);
		showCentered();
	}

	public void setMsgText(String txt) {
		msgTxt.setText(txt);
	}
	
	public void showCentered() {
		show();
		nativeShowCentered(getElement());
	}
	
	private native void nativeShowCentered(Element e) /*-{
		var cssProps = {
			"position" : "absolute",
			"margin-top" : (($wnd.innerHeight - $wnd.jQuery(e).outerHeight())/2) + "px"
		};
		
		$wnd.jQuery(e).css(cssProps);		
	}-*/;


}
