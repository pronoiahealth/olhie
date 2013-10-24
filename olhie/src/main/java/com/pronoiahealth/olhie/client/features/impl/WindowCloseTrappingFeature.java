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
package com.pronoiahealth.olhie.client.features.impl;

import javax.inject.Inject;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.pronoiahealth.olhie.client.features.AbstractClientFeature;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;

/**
 * WindowCloseTrappingFeature.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 24, 2013
 * 
 */
public class WindowCloseTrappingFeature extends AbstractClientFeature {

	@Inject
	private ClientUserToken clientUserToken;

	private HandlerRegistration windowCloseHandler;

	/**
	 * Constructor
	 * 
	 */
	public WindowCloseTrappingFeature() {
	}

	@Override
	public boolean activate() {
		windowCloseHandler = Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(CloseEvent<Window> event) {
				if (clientUserToken.isLoggedIn() == true) {
					windowCloseTrapped();
				}
			}
		});

		return true;
	}

	@Override
	public boolean deactivate() {
		if (windowCloseHandler != null) {
			windowCloseHandler.removeHandler();
			windowCloseHandler = null;
		}

		return true;
	}

	/**
	 * Calls a jQuery method to send a synchronous rest message to the server to
	 * indicate that the user, if logged in, should be marked as logged out. We
	 * must use a synchronous method as Chrome will not handle asynchronous
	 * calls in window closing events. FireFox and IE will, but this approach
	 * works for Chrome as well as FireFox and IE.
	 */
	private native void windowCloseTrapped() /*-{
		$wnd.jQuery.ajax({
			url : "/olhie/rest/windowclose/closeaction",
			async : false,
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			data : ({}),
			type : "POST",
			timeout : 20000,
			success : function() {
				return true;
			},
			error : function() {
				alert('Error');
			}
		});
	}-*/;
}
