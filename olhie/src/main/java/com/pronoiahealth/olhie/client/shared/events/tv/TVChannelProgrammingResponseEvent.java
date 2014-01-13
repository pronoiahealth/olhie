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
package com.pronoiahealth.olhie.client.shared.events.tv;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

import com.pronoiahealth.olhie.client.shared.vo.ChannelProgramList;

/**
 * TVChannelProgrammingResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jan 12, 2014
 *
 */
@Portable
@Conversational
public class TVChannelProgrammingResponseEvent {
	private List<ChannelProgramList> channelGuide;

	/**
	 * Constructor
	 *
	 */
	public TVChannelProgrammingResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param channelGuide
	 */
	public TVChannelProgrammingResponseEvent(
			List<ChannelProgramList> channelGuide) {
		super();
		this.channelGuide = channelGuide;
	}

	public List<ChannelProgramList> getChannelGuide() {
		return channelGuide;
	}

	public void setChannelGuide(List<ChannelProgramList> channelGuide) {
		this.channelGuide = channelGuide;
	}
}
