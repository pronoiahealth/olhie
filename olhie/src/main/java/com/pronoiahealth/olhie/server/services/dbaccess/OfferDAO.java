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
package com.pronoiahealth.olhie.server.services.dbaccess;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.vo.Offer;

/**
 * OfferDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 10, 2013
 * 
 */
public class OfferDAO {

	/**
	 * Creates a new Offer in the database and returns a channelId for the
	 * offerer and the peer to use.
	 * 
	 * @param offererId
	 * @param peerId
	 * @param offerType
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public static String createNewOffer(String offererId,
			String offererSessionId, String peerId, OfferTypeEnum offerType,
			OObjectDatabaseTx ooDbTx, boolean handleTransaction)
			throws Exception {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		// Need a channel ID to return
		String channelId = UUID.randomUUID().toString();

		// Create offer
		Offer offer = new Offer();
		offer.setChannelId(channelId);
		offer.setPeerId(peerId);
		offer.setOffererId(offererId);
		offer.setOffererSessionId(offererSessionId);
		offer.setCreatedDT(new Date());
		offer.setOfferType(offerType.toString());
		ooDbTx.save(offer);

		if (handleTransaction == true) {
			ooDbTx.commit();
		}

		// Return the id
		return channelId;
	}

	/**
	 * Accept an offer. Can't accept and offer that has already been accepted,
	 * closed, expired, or rejected.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public static Offer acceptOffer(String channelId, String peerSessionId,
			OObjectDatabaseTx ooDbTx, boolean handleTransaction)
			throws Exception {

		Offer offer = getOfferByChannelId(channelId, ooDbTx);
		if (offer.getAcceptedDT() == null && offer.getRejectedDT() == null
				&& offer.getClosedDT() == null && offer.getExpiredDT() == null) {
			if (handleTransaction == true) {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
			}

			offer.setAcceptedDT(new Date());
			offer.setPeerSessionId(peerSessionId);
			offer = ooDbTx.save(offer);

			if (handleTransaction == true) {
				ooDbTx.commit();
				offer = ooDbTx.detach(offer, true);
			}

			return offer;
		} else {
			throw new Exception(
					"Can't accept and offer that has already been accepted, closed, expired, or rejected.");
		}
	}

	/**
	 * Reject an offer. Can't reject an offer that has already been rejected, is
	 * closed, is expired, or has been accepted.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public static Offer rejectOffer(String channelId, OObjectDatabaseTx ooDbTx,
			boolean handleTransaction) throws Exception {
		Offer offer = getOfferByChannelId(channelId, ooDbTx);
		if (offer.getRejectedDT() == null && offer.getClosedDT() == null
				&& offer.getAcceptedDT() == null
				&& offer.getExpiredDT() == null) {
			if (handleTransaction == true) {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
			}

			offer.setRejectedDT(new Date());
			offer = ooDbTx.save(offer);

			if (handleTransaction == true) {
				ooDbTx.commit();
				offer = ooDbTx.detach(offer, true);
			}

			return offer;
		} else {
			throw new Exception(
					"Can't reject an offer that has already been rejected, is closed, or has been accepted.");
		}
	}

	/**
	 * Close a previously accepted offer. Can't close an offer that has already
	 * been rejected, or expired.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public static Offer closeOffer(String channelId, OObjectDatabaseTx ooDbTx,
			boolean handleTransaction) throws Exception {
		Offer offer = getOfferByChannelId(channelId, ooDbTx);
		if (offer.getClosedDT() == null) {
			if (offer.getRejectedDT() == null && offer.getExpiredDT() == null) {

				if (handleTransaction == true) {
					ooDbTx.begin(TXTYPE.OPTIMISTIC);
				}

				offer.setClosedDT(new Date());
				offer = ooDbTx.save(offer);

				if (handleTransaction == true) {
					ooDbTx.commit();
					offer = ooDbTx.detach(offer, true);
				}
			}
		}
		
		return offer;
	}

	/**
	 * Set the expired date on the offer. This will occur if an offer has not
	 * been rejected and not been closed but the users session has ended.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public static Offer expireOffer(String channelId, OObjectDatabaseTx ooDbTx,
			boolean handleTransaction) throws Exception {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		Offer offer = getOfferByChannelId(channelId, ooDbTx);
		if (offer.getRejectedDT() == null && offer.getClosedDT() == null) {
			offer.setExpiredDT(new Date());
			offer = ooDbTx.save(offer);
		}

		if (handleTransaction == true) {
			ooDbTx.commit();
			offer = ooDbTx.detach(offer, true);
		}

		return offer;
	}

	/**
	 * Set the expired date on the offer. This will occur if an offer has not
	 * been rejected and not been closed but the users session has ended.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public static void expireOfferByUserId(String userId,
			OObjectDatabaseTx ooDbTx, boolean handleTransaction)
			throws Exception {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		List<Offer> offers = getUnacceptedOffersById(userId, ooDbTx);
		if (offers != null && offers.size() > 0) {
			for (Offer offer : offers) {
				offer.setExpiredDT(new Date());
				ooDbTx.save(offer);
			}
		}

		if (handleTransaction == true) {
			ooDbTx.commit();
		}
	}

	/**
	 * Return the Offer associated with the channel
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public static Offer getOfferByChannelId(String channelId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		OSQLSynchQuery<Offer> rQuery = new OSQLSynchQuery<Offer>(
				"select from Offer where channelId = :cId");
		HashMap<String, String> rparams = new HashMap<String, String>();
		rparams.put("cId", channelId);
		List<Offer> rResult = ooDbTx.command(rQuery).execute(rparams);
		if (rResult != null && rResult.size() > 0) {
			Offer offer = rResult.get(0);
			return ooDbTx.detach(offer, true);
		} else {
			throw new Exception("Offer with given channel id (" + channelId
					+ ") does not exist.");
		}
	}

	/**
	 * Get the list of offers where the peerId or the offererId equals the
	 * pasted in ID and the offer has not been closed or expired.
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	private static List<Offer> getUnacceptedOffersById(String userId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		OSQLSynchQuery<Offer> rQuery = new OSQLSynchQuery<Offer>(
				"select from Offer where (peerId = :pId or offererId = :ofId) and expiredDT = NULL or closedDT = null or rejectedDT = null");
		HashMap<String, String> rparams = new HashMap<String, String>();
		rparams.put("pId", userId);
		rparams.put("ofId", userId);
		return ooDbTx.command(rQuery).execute(rparams);
	}

}
