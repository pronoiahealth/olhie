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

import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.protocols.MessageParts;

import com.pronoiahealth.olhie.client.shared.constants.OfferActionEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.events.offers.SendMessageEvent;
import com.pronoiahealth.olhie.server.security.ServerUserToken;

public class ChatRouterService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private SessionTracker sessionTracker;

	@Inject
	private RequestDispatcher dispatcher;

	public ChatRouterService() {
	}

	protected void observesSendMessageEvent(
			@Observes SendMessageEvent sendMessageEvent) {

		String channelId = sendMessageEvent.getChannelId();
		String msg = sendMessageEvent.getMsg();
		String sendToName = sendMessageEvent.getSendToName();

		// Look up the channel in the Offer entity

		// Determine if offerer or peer is sending message

		// If offerer the look up session id of peer

		// If peer the lookup session id of offerer

		// Send message with bus

	}

	private void sendMessage(String channelId, String name, String sessionId,
			OfferTypeEnum offerType, OfferRoleEnum role, OfferActionEnum action) {
		MessageBuilder.createMessage()
				.toSubject(channelId)
				.signalling().with(MessageParts.SessionID, sessionId)
				.with(OfferEnum.NAME, name)
				.with(OfferEnum.OFFER_TYPE, offerType)
				.with(OfferEnum.OFFER_ROLE, role)
				.with(OfferEnum.OFFER_ACTION, action)
				.errorsHandledBy(new ErrorCallback() {
					@Override
					public boolean error(Object message, Throwable throwable) {
						return false;
					}
				}).sendNowWith(dispatcher);
	}

	/**
	 * Create a lookup key
	 * 
	 * @param peerName
	 * @param peerId
	 * @return
	 */
	private String ceateSessionTrackerLookupKey(String name, String peerId) {
		StringBuilder sb = new StringBuilder();
		return sb.append(name).append(" (").append(peerId).append(")")
				.toString();
	}

}
