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
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import com.pronoiahealth.olhie.client.OfferDialogFactory;
import com.pronoiahealth.olhie.client.shared.constants.OfferActionEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.AcceptOfferEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.AcceptOfferResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.CloseOfferEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.RejectOfferEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.widgets.chat.AcceptOfferDialog;
import com.pronoiahealth.olhie.client.widgets.chat.AcceptOfferDialogCloseHandler;
import com.pronoiahealth.olhie.client.widgets.chat.ChatDialog;
import com.pronoiahealth.olhie.client.widgets.chat.ChatDialogCloseHandler;

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
	private OfferDialogFactory cdFactory;

	private Map<String, ChatDialog> chats;

	private Map<String, AcceptOfferDialog> acceptOffers;

	private boolean listenerActive;

	private ChatDialogCloseHandler closeHandler;

	private AcceptOfferDialogCloseHandler acceptCloseHandler;

	@Inject
	private Event<RejectOfferEvent> rejectOfferEvent;

	@Inject
	private Event<AcceptOfferEvent> acceptOfferEvent;

	@Inject
	private Event<CloseOfferEvent> closeOfferEvent;

	public OfferHandler() {
	}

	@PostConstruct
	private void postConstruct() {
		chats = new HashMap<String, ChatDialog>();
		acceptOffers = new HashMap<String, AcceptOfferDialog>();
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
								handleOfferAction(channelId, name, role);
								break;
							case PEER_DISCONNECTED:
								break;
							case REJECTED:
								break;
							case CLOSE:
								handleClose(channelId);
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
				public void closeDialog(String channelId, String partnerName) {
					closeOfferEvent.fire(new CloseOfferEvent(channelId,
							partnerName));
					removeChatDlg(channelId);
				}
			};

			// Accept close handler
			acceptCloseHandler = new AcceptOfferDialogCloseHandler() {
				@Override
				public void close(String channelId, String offererName,
						boolean accepted) {
					// Send response to server
					if (accepted == true) {
						acceptOfferEvent.fire(new AcceptOfferEvent(channelId,
								offererName));
					} else {
						rejectOfferEvent.fire(new RejectOfferEvent(channelId));
					}

					// Clean up dialog
					removeAcceptDlg(channelId);
				}
			};
		}
	}

	/**
	 * Messages received on this subject for a peer disconnect come from the
	 * OfferService observesCloseOfferEvent method. This happens when an offerer
	 * close the dialog before the peer has had a chance to response. The
	 * AcceptOfferDialog will be destroyed.
	 * 
	 * @param channelId
	 */
	private void handleClose(String channelId) {
		boolean acceptDlgShowing = removeAcceptDlg(channelId);

		// No accept dialog, look for a ChatDialog
		if (acceptDlgShowing == false) {
			ChatDialog cd = chats.get(channelId);
			if (cd != null) {
				cd.handleDisconnect("Other side has disconnected.");
			}
		}
	}

	private void handleCreatedAction(String channelId, String name,
			OfferRoleEnum role) {
		if (role == OfferRoleEnum.OFFERER) {
			ChatDialog cd = cdFactory.createChatDialog(name, channelId,
					closeHandler);
			chats.put(channelId, cd);
			cd.center();
		}
	}

	private void handleOfferAction(String channelId, String name,
			OfferRoleEnum role) {
		if (role == OfferRoleEnum.PEER) {
			AcceptOfferDialog ad = cdFactory.createAcceptOfferDialog(channelId,
					name, acceptCloseHandler);
			acceptOffers.put(channelId, ad);
			ad.center();
		}
	}

	/**
	 * When user logs out stop subscription. Only logged in users can receive
	 * offers. There is a similar observer in the main class that handles
	 * various view elements as well as informing the server that the client wishes to
	 * log out.
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
			for (String key : chats.keySet()) {
				removeChatDlg(key);
			}
			chats.clear();
		}

		if (acceptOffers.size() > 0) {
			for (String key : acceptOffers.keySet()) {
				removeAcceptDlg(key);
			}
			acceptOffers.clear();
		}
	}

	/**
	 * Destroy the chat dialog. Return true if found in chats
	 * 
	 * @param channelId
	 * @return
	 */
	private boolean removeChatDlg(String channelId) {
		boolean ret = false;
		ChatDialog chat = chats.get(channelId);
		if (chat != null) {
			ret = true;
			chat.hide();
			chat.removeFromParent();
			chat = null;
		}
		chats.remove(channelId);

		return ret;
	}

	/**
	 * Destroy the accept dialog. Return true if in acceptOffers map.
	 * 
	 * @param channelId
	 * @return
	 */
	private boolean removeAcceptDlg(String channelId) {
		boolean ret = false;
		AcceptOfferDialog dlg = acceptOffers.get(channelId);
		if (dlg != null) {
			ret = true;
			dlg.hide();
			dlg.removeFromParent();
			dlg = null;
		}
		acceptOffers.remove(channelId);

		return ret;
	}

	/**
	 * The client sends an AcceptOfferEvent to the server (OfferService). The
	 * server will inform the Offerer that the offer has been accepted. It will
	 * then fire the AcceptOfferResponseEvent. This will create the chat dialog.
	 * It will also set the dialog to the offer accepted state. After that the
	 * dialig will be added to the chats map and shown, centered on the screen.
	 * 
	 * @param acceptOfferResponseEvent
	 */
	protected void observesAcceptOfferResponseEvent(
			@Observes AcceptOfferResponseEvent acceptOfferResponseEvent) {
		String channelId = acceptOfferResponseEvent.getChannelId();
		String name = acceptOfferResponseEvent.getName();
		ChatDialog cd = cdFactory.createChatDialog(name, channelId,
				closeHandler);
		cd.handleAccept();
		chats.put(channelId, cd);
		cd.center();
	}
}
