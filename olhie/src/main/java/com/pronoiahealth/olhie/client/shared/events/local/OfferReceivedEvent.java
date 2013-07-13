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
package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;
import org.jboss.errai.common.client.api.annotations.NonPortable;

import com.pronoiahealth.olhie.client.shared.constants.OfferTypeEnum;

/**
 * OfferReceivedEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 10, 2013
 *
 */
@Local
@NonPortable
public class OfferReceivedEvent {
	private String channelId;
	private OfferTypeEnum offerType;
	private String offererName;

	/**
	 * Constructor
	 *
	 */
	public OfferReceivedEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param channelId
	 * @param offerType
	 * @param offererName
	 */
	public OfferReceivedEvent(String channelId, OfferTypeEnum offerType,
			String offererName) {
		super();
		this.channelId = channelId;
		this.offerType = offerType;
		this.offererName = offererName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public OfferTypeEnum getOfferType() {
		return offerType;
	}

	public void setOfferType(OfferTypeEnum offerType) {
		this.offerType = offerType;
	}

	public String getOffererName() {
		return offererName;
	}

	public void setOffererName(String offererName) {
		this.offererName = offererName;
	}
}
