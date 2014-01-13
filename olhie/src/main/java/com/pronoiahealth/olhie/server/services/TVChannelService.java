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
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.tv.TVChannelProgrammingRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.tv.TVChannelProgrammingResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.ChannelProgram;
import com.pronoiahealth.olhie.client.shared.vo.ChannelProgramList;

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
	@SuppressWarnings("unchecked")
	protected void observersTVChannelProgrammingRequestEvent(
			@Observes TVChannelProgrammingRequestEvent tVChannelProgrammingRequestEvent) {
		try {
			// Get list of directories under base directory
			Collection<File> dirs = FileUtils.listFiles(new File(
					tvChannelBaseDir), new NotFileFilter(
					TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);

			// For each directory get the names of the files
			List<ChannelProgramList> channelProgramList = new ArrayList<ChannelProgramList>();
			for (File dir : dirs) {
				ChannelProgramList cpLst = new ChannelProgramList();
				String dirName = dir.getName();
				cpLst.setChannelName(dirName);
				cpLst.setProgramList(new ArrayList<ChannelProgram>());
				List<ChannelProgram> cps = cpLst.getProgramList();
				Collection<File> fileLst = FileUtils.listFiles(new File(
						tvChannelBaseDir), new NotFileFilter(
						TrueFileFilter.INSTANCE), null);
				for (File file : fileLst) {
					ChannelProgram cp = new ChannelProgram();
					String fName = file.getName();
					cp.setProgramName(file.getName());
					cp.setProgramKey(dirName + "/" + fName);
					cps.add(cp);
				}
				channelProgramList.add(cpLst);
			}

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

	public void main(String[] args) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
