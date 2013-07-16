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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import com.pronoiahealth.olhie.client.shared.constants.OfferActionEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.AcceptOfferResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;

/**
 * OfferHandler.java<br/>
 * Responsibilities:<br/>
 * 1. Listens for offers. <br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 10, 2013
 * 
 */
@Singleton
public class OfferHandler {
	@Inject
	private MessageBus bus;

	@Inject
	private ClientUserToken userToken;

	@Inject
	private ChatDialogFactory cdFactory;

	private Map<String, ChatDialog> chats;

	private boolean listenerActive;

	private ChatDialogCloseHandler closeHandler;

	public OfferHandler() {
	}

	@PostConstruct
	private void postConstruct() {
		chats = new HashMap<String, ChatDialog>();
	}

	/**
	 * When user logs in start listening for offers.
	 * 
	 * @param loginResponseEvent
	 */
	protected void observesClientUserUpdatedEvent(
			@Observes ClientUserUpdatedEvent clientUserUpdatedEvent) {
		if (userToken.isLoggedIn() == true) {
			bus.subscribe(OfferEnum.CLIENT_OFFER_LISTENER.toString(),
					new MessageCallback() {
						@Override
						public void callback(Message message) {
							String channelId = message.get(String.class,
									OfferEnum.CHANNEL_ID);
							String name = message.get(String.class,
									OfferEnum.NAME);
							OfferTypeEnum offerType = message.get(
									OfferTypeEnum.class, OfferEnum.OFFER_TYPE);
							OfferActionEnum action = message.get(
									OfferActionEnum.class,
									OfferEnum.OFFER_ACTION);
							OfferRoleEnum role = message.get(
									OfferRoleEnum.class, OfferEnum.OFFER_ROLE);

							switch (action) {
							case CREATED:
								handleCreatedAction(channelId, name, role);
								break;
							case ACCEPTED:
								// handled by event observation
								break;
							case OFFER:
								break;
							case PEER_DISCONNECTED:
								break;
							case REJECTED:
								break;
							default:
								break;

							}
						}
					});
			this.listenerActive = true;

			// Close handler
			closeHandler = new ChatDialogCloseHandler() {
				@Override
				public void closeDialog(String channelId) {
					ChatDialog chatDlg = chats.get(channelId);
					removeChatDlg(chatDlg);
				}
			};
		}
	}

	private void handleCreatedAction(String channelId, String name,
			OfferRoleEnum role) {
		ChatDialog cd = cdFactory.createChatDialog(name, channelId,
				closeHandler);
		chats.put(channelId, cd);
		cd.center();
	}

	/**
	 * When user logs out stop subscription. Only logged in users can receive
	 * offers.
	 * 
	 * @param clientLogoutResponseEvent
	 */
	protected void observesClientLogoutRequestEvent(
			@Observes ClientLogoutRequestEvent clientLogoutResponseEvent) {
		if (this.listenerActive) {
			bus.unsubscribeAll(OfferEnum.CLIENT_OFFER_LISTENER.toString());
		}

		// Destroy dialog boxes
		if (chats.size() > 0) {
			for (ChatDialog chat : chats.values()) {
				removeChatDlg(chat);
			}
		}
	}

	private void removeChatDlg(ChatDialog chat) {
		if (chat != null) {
			chat.hide();
			chat.removeFromParent();
			chat = null;
		}
	}

	protected void observesAcceptOfferResponseEvent(
			@Observes AcceptOfferResponseEvent acceptOfferResponseEvent) {

	}

}
