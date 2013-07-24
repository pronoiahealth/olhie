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

import java.util.Set;
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

import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.OfferActionEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferRoleEnum;
import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.AcceptOfferEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.AcceptOfferResponseErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.AcceptOfferResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.CloseOfferEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.CreateOfferEvent;
import com.pronoiahealth.olhie.client.shared.events.offers.RejectOfferEvent;
import com.pronoiahealth.olhie.client.shared.exceptions.PeerNotLoggedInException;
import com.pronoiahealth.olhie.client.shared.vo.Offer;
import com.pronoiahealth.olhie.server.dataaccess.orient.OODbTx;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.security.ServerUserToken;
import com.pronoiahealth.olhie.server.services.dbaccess.LoggedInSessionDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.OfferDAO;

/**
 * OfferService.java<br/>
 * Responsibilities:<br/>
 * 1. Observe for AcceptOfferEvent.<br/>
 * 2. Observe for CreateOfferEvent.<br/>
 * 3. Observe for CloseOfferEvent.<br/>
 * 4. Observe for RejectOfferEvent.<br/>
 * 
 * 
 * <p>
 * This service, in combination with the OfferHandler class and other client
 * side classes handle the work-flow for creating Offers. An Offer represents
 * one user asking another user if they would like to engage in an on-line
 * conversation. Currently, chat is supported but additional conversations
 * (video/audio) will be supported in the near future.
 * </p>
 * 
 * <p>
 * Users can choose a peer to connect with using client side peer look-up
 * features (LookupUserDialog). Only currently logged-in users will be shown.
 * The application scoped SessionTracker class tracks actively logged-in users.
 * The lookup uses the ConnectedUserServiceImpl to return a list of connected
 * users whose name starts with a specific submitted query string. This supports
 * type ahead features present in the client lookup dialogs.
 * </p>
 * 
 * <p>
 * Selecting a user from the returned list on the client side will initiate the
 * process of creating an Offer. To do the the client will fire the
 * CreateOfferEvent. This event is observed by this class
 * (observesCreateOfferEvent method). Processing the event will cause the
 * following actions to be taken:<br/>
 * 1. Create a record in the database for the offer (Offer entity).<br/>
 * 2. Create a unique channelId for the conversation.<br/>
 * 3. Inform the Offerer, using the message bus, of the channelId.<br/>
 * 4. Inform the peer (who the offerer wants to talk to) of the channelId and
 * who the offerer is, again using the message bus.<br/>
 * </p>
 * 
 * <p>
 * For the Offerer, on the client side, once the channelId has been received, a
 * chatbox will open and tuned to the channelId provided. For the peer, once
 * they receive the offer, a dialogbox will appear asking if they wish to engage
 * in a chat with the offerer. If they answer yes, an AcceptOfferEvent will be
 * fired. The observer in this class will check the Offer in the database to
 * make sure it is still valid and check to see if the Offerer is still logged
 * in. If both those conditions are meet then an AcceptOfferResponse will be
 * fired back to the peer. The chatbox will appear tuned to the provided
 * channelId.
 * </p>
 * 
 * <p>
 * If they answer no, a RejectOfferEvent will be fired containing the channelId.
 * This class will process that event in the observesRejectOfferEvent method.
 * The database will be updated with a timestamp in the Offer entity's rejectdDT
 * attribute. From that entity the offerer id will be used, by way of the
 * SessionTracker, to get the offerer sessionId (Errai Session ID). Using the
 * message bus the Offer will be informed that the peer has rejected the offer.
 * This will effectively cancel any further communication on the channelId.
 * </p>
 * 
 * <p>
 * At any time the user may end the conversation on the client by closing the
 * chatdialog. This will cause the CloseOfferEvent to be fired. This event is
 * observed for in this class. The observing method will update the database. It
 * will then check to see if the paired chatter (the one that did not close the
 * window) is still online. If so the message bus will be used to send a message
 * to the channel indicating that the other party has closed the channel.
 * </p>
 * 
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 10, 2013
 * 
 */
@RequestScoped
public class OfferService {

	@Inject
	private Logger log;

	@Inject
	private ServerUserToken userToken;

	@Inject
	private SessionTracker sessionTracker;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	private Event<AcceptOfferResponseEvent> acceptOfferResponseEvent;

	@Inject
	private Event<AcceptOfferResponseErrorEvent> acceptOfferResponseErrorEvent;

	@Inject
	@OODbTx
	private OObjectDatabaseTx ooDbTx;

	@Inject
	private RequestDispatcher dispatcher;

	/**
	 * Constructor
	 * 
	 */
	public OfferService() {
	}

	/**
	 * When a CreateOfferEvent is observed an offer is created in the db. This
	 * returns a channelId that will be used by the offerer and the peer. The
	 * channelId is returned to the offerer. The channelId is sent to the peer
	 * who must accept the offer to start a two way conversation.
	 * 
	 * @param createOfferEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
			SecurityRoleEnum.REGISTERED })
	protected void observesCreateOfferEvent(
			@Observes CreateOfferEvent createOfferEvent) {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);

			// Get arguments
			String peerId = createOfferEvent.getPeerId();
			OfferTypeEnum offerType = createOfferEvent.getOfferType();
			String peerName = createOfferEvent.getPeerName();
			String userId = userToken.getUserId();
			String sessionId = EventConversationContext.get().getSessionId();

			// Create offer in DB and get back a channelId
			// Handle transaction in this class
			String channelId = OfferDAO.createNewOffer(userId, sessionId,
					peerId, offerType, ooDbTx, false);

			try {
				// Tell the peer about it
				forwardOffer(channelId, peerId, createNameFromCurrentUser(),
						ceateSessionTrackerLookupKey(peerName, peerId),
						offerType, OfferRoleEnum.PEER, OfferActionEnum.OFFER);

				// Tell the offerer about it
				sendMessage(OfferEnum.CLIENT_OFFER_LISTENER.toString(),
						channelId, peerName, sessionId, offerType,
						OfferRoleEnum.OFFERER, OfferActionEnum.CREATED);
			} catch (PeerNotLoggedInException pe) {
				// Clean up the database
				OfferDAO.expireOffer(channelId, ooDbTx, false);

				// Tell the offerer that the peer has disconnected
				sendMessage(OfferEnum.CLIENT_OFFER_LISTENER.toString(),
						channelId, peerName, sessionId, offerType,
						OfferRoleEnum.OFFERER,
						OfferActionEnum.PEER_DISCONNECTED);
			}

			ooDbTx.commit();
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

	/**
	 * This method will look up an offer in the persistence store and decide if
	 * the offer is valid for acceptance. If it is then the offerer will be
	 * informed that the offer has been accepted, if they are still logged in.
	 * An AcceptOfferResponseEvent will be fired to the peer who sent the
	 * AcceptOfferEvent. Finally, the database will be updated.
	 * 
	 * @param acceptOfferEvent
	 */
	protected void observesAcceptOfferEvent(
			@Observes AcceptOfferEvent acceptOfferEvent) {
		try {
			String channelId = acceptOfferEvent.getChannelId();
			String offererKey = acceptOfferEvent.getOffererKey();
			String sessionId = EventConversationContext.get().getSessionId();

			try {
				// Get the offer
				Offer offer = OfferDAO.getOfferByChannelId(channelId, ooDbTx);
				if (offer.canAccept() == true) {
					// Check to see if the Offerer is still logged in and
					// tell the Offerer that the offer has been accepted
					// this.
					// For this situation the method takes the channelId, the
					// user who sent the message name, the lookup key for the
					// offerer, the role of peer and the action accepted.
					sendMessage(channelId, channelId, "",
							offer.getOffererSessionId(), OfferTypeEnum.CHAT,
							OfferRoleEnum.PEER, OfferActionEnum.ACCEPTED);

					// Fire the AcceptOfferResponseEvent back to the peer
					acceptOfferResponseEvent.fire(new AcceptOfferResponseEvent(
							channelId, offererKey));

					// Update the database
					OfferDAO.acceptOffer(channelId, sessionId, ooDbTx, true);
				}
			} catch (Exception e) {
				acceptOfferResponseErrorEvent
						.fire(new AcceptOfferResponseErrorEvent(channelId));

				// Need to clean up db if required
				throw e;
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

	protected void observesRejectOfferEvent(
			@Observes RejectOfferEvent rejectOfferEvent) {
		try {
			// Args
			String channelId = rejectOfferEvent.getChannelId();
			String sessionId = EventConversationContext.get().getSessionId();

			// Find the offer and mark it rejected
			Offer offer = OfferDAO.rejectOffer(channelId, ooDbTx, true);

			// Tell the Offerer about it
			// The offerer will be tuned into the already set up channelId
			sendMessage(channelId, channelId, "", offer.getOffererSessionId(),
					OfferTypeEnum.CHAT, OfferRoleEnum.PEER,
					OfferActionEnum.REJECTED);

		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

	/**
	 * Always tell the OfferHandler about the close event
	 * 
	 * @param closeOfferEvent
	 */
	protected void observesCloseOfferEvent(
			@Observes CloseOfferEvent closeOfferEvent) {
		try {
			String sendingUserSessionId = EventConversationContext.get()
					.getSessionId();
			String channelId = closeOfferEvent.getChannelId();
			String partnerName = closeOfferEvent.getPartnerName();
			Offer offer = OfferDAO.closeOffer(channelId, ooDbTx, true);

			// If the offerer is closing the conversation tell the peer
			// else tell the offerer
			String peerSessionId = offer.getPeerSessionId();
			if (peerSessionId != null && peerSessionId.length() > 0) {
				String sendToSessionId = null;
				if (offer.getOffererSessionId().equals(sendingUserSessionId)) {
					sendToSessionId = sessionTracker
							.isSessionActive(peerSessionId) == true ? peerSessionId
							: null;
				} else {
					// Tell offerer
					sendToSessionId = sessionTracker.isSessionActive(offer
							.getOffererSessionId()) == true ? offer
							.getOffererSessionId() : null;
				}

				if (sendToSessionId != null) {
					sendMessage(OfferEnum.CLIENT_OFFER_LISTENER.toString(),
							channelId, "", sendToSessionId, OfferTypeEnum.CHAT,
							OfferRoleEnum.PEER, OfferActionEnum.CLOSE);
				}
			} else {
				// Appears the peer has not yet connected.
				// Try to send a message out to any connected user sessions with
				// the peer id.
				String userKey = ceateSessionTrackerLookupKey(partnerName,
						offer.getPeerId());
				forwardOffer(channelId, offer.getPeerId(), partnerName, userKey,
						OfferTypeEnum.CHAT, OfferRoleEnum.PEER,
						OfferActionEnum.CLOSE);

			}

		} catch (Exception e) {
			String msg = e.getMessage();
			log.log(Level.SEVERE, msg, e);
			ooDbTx.rollback();
			serviceErrorEvent.fire(new ServiceErrorEvent(msg));
		}
	}

	/**
	 * Forward an offer to the appropriate session id and user combination. The
	 * client must have the ClientOfferListener installed and instantiated.
	 * 
	 * @param channelId
	 * @param offererName
	 * @param peerKey
	 * @throws Exception
	 */
	private void forwardOffer(String channelId, String userId, String name,
			String key, OfferTypeEnum offerType, OfferRoleEnum role,
			OfferActionEnum action) throws Exception {
		Set<String> sessions = LoggedInSessionDAO
				.getSessionIdsForACtiveSessionsByUserId(userId, ooDbTx);
		if (sessions != null && sessions.size() > 0) {
			for (String sessionId : sessions) {
				sendMessage(OfferEnum.CLIENT_OFFER_LISTENER.toString(),
						channelId, name, sessionId, offerType, role, action);
			}
		} else {
			throw new PeerNotLoggedInException();
		}
	}

	/**
	 * Send the message
	 * 
	 * @param channelId
	 * @param name
	 * @param sessionId
	 * @param offerType
	 * @param role
	 */
	private void sendMessage(String toSubject, String channelId, String name,
			String sessionId, OfferTypeEnum offerType, OfferRoleEnum role,
			OfferActionEnum action) {
		MessageBuilder.createMessage().toSubject(toSubject).signalling()
				.with(MessageParts.SessionID, sessionId)
				.with(OfferEnum.CHANNEL_ID, channelId)
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
	 * Name that will be sent to the peer. This is the name of the user that
	 * initiated the offer.
	 * 
	 * @return
	 */
	private String createNameFromCurrentUser() {
		StringBuilder sb = new StringBuilder();
		return sb.append(userToken.getUserFirstName()).append(" ")
				.append(userToken.getUserLastName()).append(" (")
				.append(userToken.getUserId()).append(")").toString();
	}

	/**
	 * Create a lookup key
	 * 
	 * @param peerName
	 * @param peerId
	 * @return
	 */
	private String ceateSessionTrackerLookupKey(String peerName, String peerId) {
		StringBuilder sb = new StringBuilder();
		return sb.append(peerName).append(" (").append(peerId).append(")")
				.toString();
	}

}
