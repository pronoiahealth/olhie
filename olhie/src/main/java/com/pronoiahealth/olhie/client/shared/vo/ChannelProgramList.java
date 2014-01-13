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

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * ChannelProgramList.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jan 12, 2014
 * 
 */
@Portable
public class ChannelProgramList {
	private String channelName;
	private List<ChannelProgram> programList;

	/**
	 * Constructor
	 * 
	 */
	public ChannelProgramList() {
	}

	/**
	 * Constructor
	 *
	 * @param channelName
	 * @param programList
	 */
	public ChannelProgramList(String channelName,
			List<ChannelProgram> programList) {
		super();
		this.channelName = channelName;
		this.programList = programList;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public List<ChannelProgram> getProgramList() {
		return programList;
	}

	public void setProgramList(List<ChannelProgram> programList) {
		this.programList = programList;
	}
}
