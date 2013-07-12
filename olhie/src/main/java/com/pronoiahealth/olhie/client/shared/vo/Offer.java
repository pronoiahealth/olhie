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
package com.pronoiahealth.olhie.client.shared.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * Offer.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 10, 2013
 * 
 */
@Portable
public class Offer {
	@OId
	private String id;

	@OVersion
	private Long version;

	@NotNull
	@Size(min = 6, max = 20, message = "Must be between 6 and 20 characters")
	private String offererId;

	@NotNull
	@Size(min = 6, max = 20, message = "Must be between 6 and 20 characters")
	private String peerId;

	@NotNull
	private Date createdDT;

	private Date acceptedDT;

	private Date rejectedDT;

	private Date closedDT;

	private Date expiredDT;

	@NotNull
	@Size(min = 1, max = 20, message = "Must be between 1 and 20 characters")
	private String offerType;

	@NotNull
	@Size(min = 1, max = 255, message = "Must be between 1 and 255 characters")
	private String channelId;

	/**
	 * Constructor
	 * 
	 */
	public Offer() {
	}

	public String getOffererId() {
		return offererId;
	}

	public void setOffererId(String offererId) {
		this.offererId = offererId;
	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public Date getAcceptedDT() {
		return acceptedDT;
	}

	public void setAcceptedDT(Date acceptedDT) {
		this.acceptedDT = acceptedDT;
	}

	public Date getRejectedDT() {
		return rejectedDT;
	}

	public void setRejectedDT(Date rejectedDT) {
		this.rejectedDT = rejectedDT;
	}

	public Date getClosedDT() {
		return closedDT;
	}

	public void setClosedDT(Date closedDT) {
		this.closedDT = closedDT;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getId() {
		return id;
	}

	public Date getCreatedDT() {
		return createdDT;
	}

	public void setCreatedDT(Date createdDT) {
		this.createdDT = createdDT;
	}

	public Date getExpiredDT() {
		return expiredDT;
	}

	public void setExpiredDT(Date expiredDT) {
		this.expiredDT = expiredDT;
	}

	/**
	 * Offers can be accepted if they are not rejected, closed, expired, or not
	 * already accepted
	 * 
	 * @return
	 */
	public boolean canAccept() {
		if (this.rejectedDT == null && this.closedDT == null
				&& this.expiredDT == null && this.acceptedDT == null) {
			return true;
		} else {
			return false;
		}
	}
}
