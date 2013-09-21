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

import java.util.List;

import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;
import com.pronoiahealth.olhie.client.shared.vo.Offer;

/**
 * OfferDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 14, 2013
 * 
 */
public interface OfferDAO {

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
	public String createNewOffer(String offererId, String offererSessionId,
			String peerId, OfferTypeEnum offerType) throws Exception;

	/**
	 * Accept an offer. Can't accept and offer that has already been accepted,
	 * closed, expired, or rejected.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public Offer acceptOffer(String channelId, String peerSessionId)
			throws Exception;

	/**
	 * Reject an offer. Can't reject an offer that has already been rejected, is
	 * closed, is expired, or has been accepted.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public Offer rejectOffer(String channelId) throws Exception;

	/**
	 * Close a previously accepted offer. Can't close an offer that has already
	 * been rejected, or expired.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public Offer closeOffer(String channelId) throws Exception;

	/**
	 * Set the expired date on the offer. This will occur if an offer has not
	 * been rejected and not been closed but the users session has ended.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public Offer expireOffer(String channelId) throws Exception;

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
	public void expireOfferByUserId(String userId, OfferTypeEnum... types)
			throws Exception;


	/**
	 * Set the closed date on the offer. This will occur if an offer has not
	 * been rejected and not been closed but the users session has ended.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public void closeOfferByUserId(String userId, String erraiSessionId,
			OfferTypeEnum type) throws Exception;

	/**
	 * Return the Offer associated with the channel
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public Offer getOfferByChannelId(String channelId) throws Exception;
	
	/**
	 * Set the expired date on the offer. This will occur if an offer has not
	 * been rejected and not been closed but the users session has ended.
	 * 
	 * @param channelId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public void expireOfferByUserId(String userId, OfferTypeEnum type)
			throws Exception;
	
	/**
	 * Get the list of offers where the peerId or the offererId equals the
	 * pasted in ID and the offer has not been closed or expired.
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public List<Offer> getUnacceptedOffersById(String userId, OfferTypeEnum type) throws Exception;
}
