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
package com.pronoiahealth.olhie.client.pages.admin;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageHiding;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.pronoiahealth.olhie.client.navigation.AdminRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;
import com.pronoiahealth.olhie.client.pages.admin.features.AddNewsItemDialogHandlerFeature;
import com.pronoiahealth.olhie.client.pages.admin.widgets.AuthorRequestWidget;
import com.pronoiahealth.olhie.client.pages.admin.widgets.NewsItemWidget;
import com.pronoiahealth.olhie.client.pages.admin.widgets.UserManagementWidget;

/**
 * AdminPage.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jan 19, 2014
 * 
 */
@Templated("#root")
@Dependent
@Page(role = { AdminRole.class })
public class AdminPage extends AbstractPage {

	@Inject
	@DataField
	private AuthorRequestWidget authorRequestTab;

	@Inject
	@DataField
	private UserManagementWidget userMgmtTab;

	@Inject
	@DataField
	private NewsItemWidget newsItemTab;

	@Inject
	private AddNewsItemDialogHandlerFeature addNewsItemDialogFeature;

	/**
	 * Constructor
	 * 
	 */
	public AdminPage() {
	}

	/**
	 * When page is shown
	 */
	@PageShowing
	protected void onPageShowing() {
		// Set the background which will make the containing div overflow
		setPageBackgroundClass("ph-Admin-Bkg");

		// Activate features
		addNewsItemDialogFeature.activate();
	}

	/**
	 * When page is hidden
	 */
	@PageHiding
	protected void pageHiding() {
		// deactivate features
		addNewsItemDialogFeature.deactivate();
	}
}
