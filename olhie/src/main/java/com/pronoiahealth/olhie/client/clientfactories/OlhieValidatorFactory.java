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
package com.pronoiahealth.olhie.client.clientfactories;

import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.CalendarRequest;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;

/**
 * OlhieValidatorFactory.java<br/>
 * Responsibilities:<br/>
 * 1. Default validation factory
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 8, 2013
 * 
 */
public final class OlhieValidatorFactory extends AbstractGwtValidatorFactory {

	@GwtValidation(value = { RegistrationForm.class, Book.class,
			CalendarRequest.class }, groups = { Default.class })
	public interface GwtValidator extends Validator {
	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}
}