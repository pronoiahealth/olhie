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

/**
 * RejectOfferEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired By: OfferHandler<br/>
 * Observed By: OfferService<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 11, 2013
 *
 */
@Portable
@Conversational
public class RejectOfferEvent {
	private String channelId;

	/**
	 * Constructor
	 *
	 */
	public RejectOfferEvent() {
	}

	public RejectOfferEvent(String channelId) {
		super();
		this.channelId = channelId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
