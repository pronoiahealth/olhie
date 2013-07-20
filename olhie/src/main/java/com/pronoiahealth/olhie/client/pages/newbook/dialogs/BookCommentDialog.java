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
package com.pronoiahealth.olhie.client.pages.newbook.dialogs;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * CommentDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 20, 2013
 *
 */
public class BookCommentDialog extends Composite {
	
	@Inject
	UiBinder<Widget, BookCommentDialog> binder;

	@UiField
	public Modal commentModal;
	
	@UiField
	public TextArea comment;

	@UiField
	public ControlGroup commentCG;

	/**
	 * Constructor
	 *
	 */
	public BookCommentDialog() {
	}
	
	@PostConstruct
	public void postConstruct() {
		initWidget(binder.createAndBindUi(this));
	}

}
