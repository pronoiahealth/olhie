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
 * SendMessageEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * <p>
 * Offered By: <br/>
 * Observed By: <br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 15, 2013
 *
 */
@Portable
@Conversational
public class SendMessageEvent {
	private String channelId;
	private String sendToName;
	private String msg;

	/**
	 * Constructor
	 *
	 */
	public SendMessageEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param channelId
	 * @param msg
	 * @param sendToName
	 */
	public SendMessageEvent(String channelId, String msg, String sendToName) {
		super();
		this.channelId = channelId;
		this.msg = msg;
		this.sendToName = sendToName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSendToName() {
		return sendToName;
	}

	public void setSendToName(String sendToName) {
		this.sendToName = sendToName;
	}
}
