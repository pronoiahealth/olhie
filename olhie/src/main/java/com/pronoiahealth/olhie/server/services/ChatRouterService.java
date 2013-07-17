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
package com.pronoiahealth.olhie.server.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.cdi.server.events.EventConversationContext;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.protocols.MessageParts;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.OfferActionEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.SendMessageEvent;
import com.pronoiahealth.olhie.client.shared.vo.Offer;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.OfferDAO;

@RequestScoped
public class ChatRouterService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private SessionTracker sessionTracker;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private RequestDispatcher dispatcher;

	public ChatRouterService() {
	}

	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
		SecurityRoleEnum.REGISTERED })
	protected void observesSendMessageEvent(
			@Observes SendMessageEvent sendMessageEvent) {
		try {
			String channelId = sendMessageEvent.getChannelId();
			String msg = sendMessageEvent.getMsg();
			String sendToName = sendMessageEvent.getSendToName();
			String senderUserId = userToken.getUserId();
			String currentUserSessionId = EventConversationContext.get()
					.getSessionId();

			// Look up the channel in the Offer entity
			Offer offer = OfferDAO.getOfferByChannelId(channelId, ooDbTx);

			// Determine if offerer can be used to chat
			if (offer.canChat()) {
				// Determine if offerer or peer is sending message
				// Add that the session is still active
				String sendToSessionId = null;
				OfferRoleEnum offerRole = null;
				if (senderUserId.equals(offer.getOffererId())) {
					offerRole = OfferRoleEnum.OFFERER;
					sendToSessionId = sessionTracker.isSessionActive(offer
							.getPeerSessionId()) == true ? offer
							.getPeerSessionId() : null;
				} else {
					offerRole = OfferRoleEnum.PEER;
					sendToSessionId = sessionTracker.isSessionActive(offer
							.getOffererSessionId()) == true ? offer
							.getOffererSessionId() : null;
				}

				// Test returned session id
				// If its not null then send the message.
				// If it is null tell the sender the session has expired.
				if (sendToSessionId != null) {
					sendMessage(channelId, sendToName, sendToSessionId,
							OfferTypeEnum.CHAT, offerRole,
							OfferActionEnum.NEW_MSG, msg);
				} else {
					sendMessage(channelId, sendToName, currentUserSessionId,
							OfferTypeEnum.CHAT, offerRole,
							OfferActionEnum.PEER_DISCONNECTED,
							"Your chat partner has disconnected.");
				}
			}
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

	}

	private void sendMessage(String channelId, String name, String sessionId,
			OfferTypeEnum offerType, OfferRoleEnum role,
			OfferActionEnum action, String msg) {
		MessageBuilder.createMessage().toSubject(channelId).signalling()
				.with(MessageParts.SessionID, sessionId)
				.with(OfferEnum.NAME, name)
				.with(OfferEnum.OFFER_TYPE, offerType)
				.with(OfferEnum.OFFER_ROLE, role)
				.with(OfferEnum.OFFER_ACTION, action).with(OfferEnum.MSG, msg)
				.errorsHandledBy(new ErrorCallback() {
					@Override
					public boolean error(Object message, Throwable throwable) {
						return false;
					}
				}).sendNowWith(dispatcher);
	}
}
