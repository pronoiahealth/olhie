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
package com.pronoiahealth.olhie.client.shared.events.offers;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;

/**
 * CreateOfferEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 10, 2013
 *
 */
@Portable
@Conversational
public class CreateOfferEvent {
	private String peerId;
	private String peerName;
	private OfferTypeEnum offerType;
	
	/**
	 * Constructor
	 *
	 */
	public CreateOfferEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param peerId
	 * @param peerName
	 * @param offerType
	 */
	public CreateOfferEvent(String peerId, String peerName, OfferTypeEnum offerType) {
		super();
		this.peerId = peerId;
		this.peerName = peerName;
		this.offerType = offerType;
	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public String getPeerName() {
		return peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public OfferTypeEnum getOfferType() {
		return offerType;
	}

	public void setOfferType(OfferTypeEnum offerType) {
		this.offerType = offerType;
	}
}
