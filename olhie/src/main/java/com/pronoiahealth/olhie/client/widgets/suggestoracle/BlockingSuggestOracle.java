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
package com.pronoiahealth.olhie.client.widgets.suggestoracle;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * BlockingSuggestOracle.java<br/>
 * Responsibilities:<br/>
 * 1. Blocks the sending of data from a suggest text box until previously sent
 * request is returned. This prevent denial of service issues.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 9, 2013
 * 
 */
public abstract class BlockingSuggestOracle extends SuggestOracle {
	private HasText suggester;
	private Callback currentCallback;
	private Request pendingRequest;
	private Callback pendingCallback;

	private Callback wrappedCallback = new Callback() {
		public void onSuggestionsReady(Request request, Response response) {
			if (suggester.getText().equals(request.getQuery())) {
				currentCallback.onSuggestionsReady(request, response);
				pendingRequest = null;
				pendingCallback = null;
			}
			currentCallback = null;
			if (pendingCallback != null) {
				requestSuggestions(pendingRequest, pendingCallback);
				pendingRequest = null;
				pendingCallback = null;
			}
		}

	};

	/**
	 * Constructor
	 * 
	 */
	public BlockingSuggestOracle() {
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		assert suggester != null : "Must call setSuggestWidget before requesting suggestions";
		if (currentCallback == null) {
			currentCallback = callback;
			sendRequest(request, wrappedCallback);
		} else {
			pendingRequest = request;
			pendingCallback = callback;
		}
	}

	public abstract void sendRequest(Request request, Callback callback);

	/**
	 * Sets the widget who is using the {@link SuggestOracleOverride}. Usually
	 * it is a {@link SuggestBoxOverride}, however to support the use of custom
	 * suggest widgets, any widget that implements HasText can be passed in
	 * here.
	 * 
	 * @param suggestBox
	 *            the widget calling to this oracle.
	 */
	public void setSuggestWidget(HasText suggestBox) {
		suggester = suggestBox;
	}
}
