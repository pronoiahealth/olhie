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

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import com.pronoiahealth.olhie.client.shared.constants.OfferActionEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferEnum;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.AcceptOfferResponseEvent;

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

	public OfferHandler() {
	}

	/**
	 * When user logs in start listening for offers.
	 * 
	 * @param loginResponseEvent
	 */
	protected void observesLoginResponseEvent(
			@Observes LoginResponseEvent loginResponseEvent) {
		bus.subscribe(OfferEnum.CLIENT_OFFER_LISTENER.toString(),
				new MessageCallback() {
					@Override
					public void callback(Message message) {
						String channelId = message.get(String.class,
								OfferEnum.CHANNEL_ID);
						String offererName = message.get(String.class,
								OfferEnum.OFFERER_NAME);
						String offerType = message.get(String.class,
								OfferEnum.OFFER_TYPE);
						OfferActionEnum action = message.get(OfferActionEnum.class,
								OfferEnum.OFFER_ACTION);
						String role = message.get(String.class,
								OfferEnum.OFFER_ROLE);
					}
				});
	}

	/**
	 * When user logs out stop subscription. Only logged in users can receive
	 * offers.
	 * 
	 * @param clientLogoutResponseEvent
	 */
	protected void observesClientLogoutRequestEvent(
			@Observes ClientLogoutRequestEvent clientLogoutResponseEvent) {
		bus.unsubscribeAll(OfferEnum.CLIENT_OFFER_LISTENER.toString());
	}

	protected void observesAcceptOfferResponseEvent(
			@Observes AcceptOfferResponseEvent acceptOfferResponseEvent) {

	}

}
