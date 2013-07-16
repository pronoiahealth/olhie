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
package com.pronoiahealth.olhie.client.pages.main;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pronoiahealth.olhie.client.shared.events.offers.SendMessageEvent;

/**
 * ChatDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 15, 2013
 * 
 */
@Dependent
public class ChatDialog extends DialogBox {
	public enum BoxShade {
		ME, PEER
	};

	@Inject
	private Event<SendMessageEvent> sendMessageEvent;

	private String dialogChannelId;
	private ChatDialogCloseHandler dialogCloseHandler;
	private String caption;
	private TextBox txtBox;
	private HTMLPanel chatTxtPanel;
	private MessageBus bus = ErraiBus.get();
	private boolean listenerConnected;

	@Inject
	public ChatDialog() {
		super();

		// Dialog properties
		setAutoHideEnabled(false);
		setModal(false);
		setAnimationEnabled(true);

		// Style
		getElement().setAttribute("style", "z-index: 30000; opacity: .70;");

		// Set up the dialog box contents
		VerticalPanel vp = new VerticalPanel();
		vp.setHeight("450px");
		vp.setWidth("250px");
		vp.setSpacing(10);
		// vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		// vp.add(new Label("Test"));

		// Build Chat widget
		chatTxtPanel = new HTMLPanel((String) null);
		vp.add(chatTxtPanel);
		chatTxtPanel.setStyleName("ph-ChatDialog-Txt", true);

		// Build entry and exit button
		HorizontalPanel hp = new HorizontalPanel();
		vp.add(hp);
		txtBox = new TextBox();
		hp.add(txtBox);
		txtBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String msg = txtBox.getText();
					addMePanel(msg);
					txtBox.setText("");
					txtBox.setFocus(true);
					sendMessageEvent.fire(new SendMessageEvent(dialogChannelId,
							msg, caption));

				}
			}
		});
		txtBox.setStyleName("ph-ChatDialog-TxtBox", true);
		txtBox.setWidth("175px");

		// Button
		Button endBtn = new Button("End");
		endBtn.setIcon(IconType.SIGNOUT);
		endBtn.setSize(ButtonSize.SMALL);
		endBtn.setType(ButtonType.PRIMARY);
		endBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogCloseHandler.closeDialog(dialogChannelId);
			}
		});
		hp.add(endBtn);

		// Set widget
		setWidget(vp);
	}

	/**
	 * Called by ChatDialogFactory
	 * 
	 * @param caption
	 * @param channelId
	 * @param closeHandler
	 */
	public void initDialog(String caption, String channelId,
			ChatDialogCloseHandler closeHandler) {
		this.dialogChannelId = channelId;
		this.dialogCloseHandler = closeHandler;
		this.caption = caption;
		this.setText(caption);
	}

	private void addMePanel(String txt) {
		HTMLPanel panel = this.createPanel(txt, BoxShade.ME);
		chatTxtPanel.add(panel);
	}

	private void addPeerPanel(String txt) {
		HTMLPanel panel = this.createPanel(txt, BoxShade.PEER);
		chatTxtPanel.add(panel);
	}

	private HTMLPanel createPanel(String txt, BoxShade shade) {
		HTMLPanel panel = new HTMLPanel(txt);
		if (shade == BoxShade.ME) {
			panel.setStyleName("ph-ChatDialog-BoxShade-Me", true);
		} else {
			panel.setStyleName("ph-ChatDialog-BoxShade-Peer", true);
		}
		return panel;
	}

	private void createListener() {
		bus.subscribe(this.dialogChannelId, new MessageCallback() {
			@Override
			public void callback(Message message) {

			}
		});
	}

	public void destroyListener() {
		if (isListenerConnected() == true) {
			bus.unsubscribeAll(this.dialogChannelId);
		}
	}

	public boolean isListenerConnected() {
		return this.listenerConnected;
	}
}
