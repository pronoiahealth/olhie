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
package com.pronoiahealth.olhie.client.widgets.booklist3d.errai;

import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * CommentRatingButtonWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Oct 18, 2013
 *
 */
@Templated("TOCWidget.html#commentRatingButtonContainer")
public class CommentRatingButtonWidget extends Composite {
	
	@Inject
	@DataField("commentRatingButtonContainer")
	private SimplePanel commentRatingButtonContainer;
	
	@DataField("commentRatingBtn")
	private Element commentRatingBtn = DOM.createAnchor();

	/**
	 * Constructor
	 *
	 */
	public CommentRatingButtonWidget() {
	}
	
	public void setHideCommentRatingBtn() {
		commentRatingBtn.setAttribute("style", "display: none;");
	}
	
	public void setShowCommentRatingBtn() {
		commentRatingBtn.setAttribute("style", "display: inline;");
	}

}
