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
package com.pronoiahealth.olhie.server.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.tv.TVChannelProgrammingRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.tv.TVChannelProgrammingResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.ChannelProgram;
import com.pronoiahealth.olhie.client.shared.vo.ChannelProgramList;
import com.pronoiahealth.olhie.server.security.SecureAccess;

/**
 * TVChannelService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jan 12, 2014
 * 
 */
@RequestScoped
public class TVChannelService {
	@Inject
	private Logger log;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	@Inject
	@ConfigProperty(name = "OLHIE_TV_BASE_DIR")
	private String tvChannelBaseDir;

	@Inject
	private Event<TVChannelProgrammingResponseEvent> tVChannelProgrammingResponseEvent;

	/**
	 * Constructor
	 * 
	 */
	public TVChannelService() {
	}

	/**
	 * App server user must have access to the base directory. This method will
	 * read the directories in the base directory and sort them by name. They
	 * will end up being the channels. The files in the channel will be the
	 * selections for the channel.
	 * 
	 * @param tVChannelProgrammingRequestEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR,
		SecurityRoleEnum.REGISTERED, SecurityRoleEnum.ANONYMOUS })
	protected void observersTVChannelProgrammingRequestEvent(
			@Observes TVChannelProgrammingRequestEvent tVChannelProgrammingRequestEvent) {
		try {
			// Get list of directories under base directory
			List<ChannelProgramList> channelProgramList = this.getList();

			// Return the list
			tVChannelProgrammingResponseEvent
					.fire(new TVChannelProgrammingResponseEvent(
							channelProgramList));
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}
	}

	/**
	 * Gets the channel list
	 * 
	 * @return
	 */
	private List<ChannelProgramList> getList() {
		// Get list of directories under base directory
		List<ChannelProgramList> channelProgramCatelog = new ArrayList<ChannelProgramList>();
		File root = new File(tvChannelBaseDir);
		File[] files = root.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				ChannelProgramList cpLst = new ChannelProgramList();
				String dirName = f.getName();
				cpLst.setChannelName(dirName);
				cpLst.setProgramList(new ArrayList<ChannelProgram>());
				List<ChannelProgram> cps = cpLst.getProgramList();
				// One level deep only
				for (File sf : f.listFiles()) {
					if (sf.isDirectory()) {
						ChannelProgram cp = new ChannelProgram();
						String sfName = sf.getName();
						cp.setProgramName(sfName);
						cp.setProgramKey((dirName + "|" + sfName).replace(' ', '_'));
						cps.add(cp);
					}
				}
				channelProgramCatelog.add(cpLst);
			}
		}

		return channelProgramCatelog;
	}

	public static void main(String[] args) {
		try {
			TVChannelService srv = new TVChannelService();
			srv.tvChannelBaseDir = "/Users/johndestefano/olhietv";
			List<ChannelProgramList> lst = srv.getList();
			System.out.println(lst.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
