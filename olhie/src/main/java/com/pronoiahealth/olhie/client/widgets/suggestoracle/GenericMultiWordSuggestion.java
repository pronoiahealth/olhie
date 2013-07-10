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

import com.google.gwt.user.client.ui.MultiWordSuggestOracle.MultiWordSuggestion;

public class GenericMultiWordSuggestion<T> extends MultiWordSuggestion {
	private T pojo;

	public GenericMultiWordSuggestion(T pojo) {
		super(pojo.toString(), pojo.toString());
		this.pojo = pojo;
	}

	public T getPojo() {
		return pojo;
	}

}
