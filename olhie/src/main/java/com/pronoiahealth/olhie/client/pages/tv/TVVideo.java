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
package com.pronoiahealth.olhie.client.pages.tv;

import static com.google.gwt.query.client.GQuery.$;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.SourceElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.media.client.Video;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

/**
 * TVVideo.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 25, 2013
 * 
 */
@Dependent
@Templated("#root")
public class TVVideo extends Composite {

	@DataField
	private Video video = Video.createIfSupported();

	private SourceElement src1;
	private SourceElement src2;
	private SourceElement src3;
	private GQuery videoQry;
	private NumberFormat nf;

	/**
	 * Constructor
	 * 
	 */
	public TVVideo() {
		src1 = video.addSource("");
		src1.setType("video/mp4");
		src2 = video.addSource("");
		src2.setType("video/webm");
		src3 = video.addSource("");
		src3.setType("video/ogg");
	}

	@PostConstruct
	protected void postConstruct() {
		videoQry = $(video);
		
		nf = NumberFormat.getDecimalFormat();
		nf.overrideFractionDigits(2);
	}
	
	@PreDestroy
	protected void preDestroy() {
		if (videoQry != null) {
			videoQry.unbind("loadedmetadata");
		}
	}

	/**
	 * Load the media
	 */
	public void loadMedia() {
		video.load();
	}

	/**
	 * Unbind the loadedmetadata event from the video
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#onUnload()
	 */
	@Override
	protected void onUnload() {
		super.onUnload();

		if (videoQry != null) {
			videoQry.unbind("loadedmetadata");
		}
	}

	/**
	 * Sets the movie source
	 * 
	 * @param programRef
	 */
	public void setVideoRef(String programRef) {
		src1.setSrc(programRef + ".mp4");
		src2.setSrc(programRef + ".webm");
		src3.setSrc(programRef + ".ogv");
	}

}
