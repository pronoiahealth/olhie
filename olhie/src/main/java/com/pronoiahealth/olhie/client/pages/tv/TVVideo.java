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

import javax.enterprise.context.Dependent;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.SourceElement;
import com.google.gwt.media.client.Video;
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

	public void loadMedia() {
		String cp = video.canPlayType("video/mp4");
		cp = video.canPlayType("video/webm");
		cp = video.canPlayType("video/ogg");
		
		
		video.load();
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
