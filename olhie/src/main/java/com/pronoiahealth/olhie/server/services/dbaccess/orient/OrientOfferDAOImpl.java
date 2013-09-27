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
package com.pronoiahealth.olhie.server.services.dbaccess.orient;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.vo.Offer;
import com.pronoiahealth.olhie.server.services.dbaccess.OfferDAO;

/**
 * OrientOfferDAOImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 14, 2013
 * 
 */
public class OrientOfferDAOImpl extends OrientBaseDBFactoryDAO implements
		OfferDAO {

	/**
	 * Constructor
	 * 
	 */
	public OrientOfferDAOImpl() {
	}

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
	@Override
	public String createNewOffer(String offererId, String offererSessionId,
			String peerId, OfferTypeEnum offerType) throws Exception {

		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			ooDbTx.begin(TXTYPE.OPTIMISTIC);

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
			ooDbTx.commit();

			// Return the id
			return channelId;
		} finally {
			closeConnection(ooDbTx);
		}

	}

	/**
	 * Accept an offer. Can't accept and offer that has already been accepted,
	 * closed, expired, or rejected.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	@Override
	public Offer acceptOffer(String channelId, String peerSessionId)
			throws Exception {

		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			Offer offer = getOfferByChannelId(channelId);
			if (offer.getAcceptedDT() == null && offer.getRejectedDT() == null
					&& offer.getClosedDT() == null
					&& offer.getExpiredDT() == null) {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				offer.setAcceptedDT(new Date());
				offer.setPeerSessionId(peerSessionId);
				offer = ooDbTx.save(offer);
				ooDbTx.commit();
				offer = ooDbTx.detach(offer, true);
				return offer;
			} else {
				throw new Exception(
						"Can't accept and offer that has already been accepted, closed, expired, or rejected.");
			}
		} finally {
			closeConnection(ooDbTx);
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
	@Override
	public Offer rejectOffer(String channelId) throws Exception {

		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			Offer offer = getOfferByChannelId(channelId);
			if (offer.getRejectedDT() == null && offer.getClosedDT() == null
					&& offer.getAcceptedDT() == null
					&& offer.getExpiredDT() == null) {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				offer.setRejectedDT(new Date());
				offer = ooDbTx.save(offer);
				ooDbTx.commit();
				offer = ooDbTx.detach(offer, true);
				return offer;
			} else {
				throw new Exception(
						"Can't reject an offer that has already been rejected, is closed, or has been accepted.");
			}
		} finally {
			closeConnection(ooDbTx);
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
	@Override
	public Offer closeOffer(String channelId) throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			Offer offer = getOfferByChannelId(channelId);
			if (offer.getClosedDT() == null) {
				if (offer.getRejectedDT() == null
						&& offer.getExpiredDT() == null) {
					ooDbTx.begin(TXTYPE.OPTIMISTIC);
					offer.setClosedDT(new Date());
					offer = ooDbTx.save(offer);
					ooDbTx.commit();
					offer = ooDbTx.detach(offer, true);
				}
			}
			return offer;
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Set the expired date on the offer. This will occur if an offer has not
	 * been rejected and not been closed but the users session has ended.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	@Override
	public Offer expireOffer(String channelId) throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			Offer offer = getOfferByChannelId(channelId);
			if (offer.getRejectedDT() == null && offer.getClosedDT() == null) {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				offer.setExpiredDT(new Date());
				offer = ooDbTx.save(offer);
				ooDbTx.commit();
				offer = ooDbTx.detach(offer, true);
			}

			return offer;
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Expire specific Offer types based on the types parameter. If types is
	 * null then all unaccepted offers for the suerId will be marked as expired.
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @param handleTransaction
	 * @param types
	 * @throws Exception
	 */
	@Override
	public void expireOfferByUserId(String userId, OfferTypeEnum... types)
			throws Exception {

		if (types != null && types.length > 0) {
			for (OfferTypeEnum type : types) {
				expireOfferByUserId(userId, type);
			}
		} else {
			expireOfferByUserId(userId, (OfferTypeEnum)null);
		}
	}

	/**
	 * Set the closed date on the offer. This will occur if an offer has not
	 * been rejected and not been closed but the users session has ended.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	@Override
	public void closeOfferByUserId(String userId, String erraiSessionId,
			OfferTypeEnum type) throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			List<Offer> offers = getUnacceptedOffersById(userId, type);
			if (offers != null && offers.size() > 0) {
				for (Offer offer : offers) {
					if (offer.getOffererSessionId().equals(erraiSessionId)
							|| offer.getPeerSessionId().equals(erraiSessionId)) {
						ooDbTx.begin(TXTYPE.OPTIMISTIC);
						offer.setClosedDT(new Date());
						ooDbTx.save(offer);
						ooDbTx.commit();
					}
				}
			}
		} finally {
			closeConnection(ooDbTx);
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
	@Override
	public Offer getOfferByChannelId(String channelId) throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
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
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Set the expired date on the offer. This will occur if an offer has not
	 * been rejected and not been closed but the users session has ended.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	@Override
	public void expireOfferByUserId(String userId, OfferTypeEnum type)
			throws Exception {

		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			List<Offer> offers = getUnacceptedOffersById(userId, type);
			if (offers != null && offers.size() > 0) {
				for (Offer offer : offers) {
					ooDbTx.begin(TXTYPE.OPTIMISTIC);
					offer.setExpiredDT(new Date());
					ooDbTx.save(offer);
					ooDbTx.commit();
				}
			}
		} finally {
			closeConnection(ooDbTx);
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
	@Override
	public List<Offer> getUnacceptedOffersById(String userId, OfferTypeEnum type)
			throws Exception {
		OObjectDatabaseTx ooDbTx = null;
		try {
			ooDbTx = getConnection();
			OSQLSynchQuery<Offer> rQuery = null;
			HashMap<String, String> rparams = new HashMap<String, String>();
			if (type != null) {
				rQuery = new OSQLSynchQuery<Offer>(
						"select from Offer where (peerId = :pId or offererId = :ofId) and offerType = :type and expiredDT = null and closedDT = null and rejectedDT = null)");
				rparams.put("pId", userId);
				rparams.put("ofId", userId);
				rparams.put("type", type.toString());
			} else {
				rQuery = new OSQLSynchQuery<Offer>(
						"select from Offer where (peerId = :pId or offererId = :ofId) and expiredDT = null and closedDT = null and rejectedDT = null");
				rparams.put("pId", userId);
				rparams.put("ofId", userId);
			}
			return ooDbTx.command(rQuery).execute(rparams);
		} finally {
			closeConnection(ooDbTx);
		}
	}

}
