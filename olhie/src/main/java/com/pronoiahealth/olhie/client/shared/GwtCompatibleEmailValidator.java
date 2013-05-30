package com.pronoiahealth.olhie.client.shared;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.gwt.regexp.shared.RegExp;

/**
 * GwtCompatibleEmailValidator.java<br/>
 * Responsibilities:<br/>
 * 1. Custom so email validation works on client and server
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 29, 2013
 *
 */
public class GwtCompatibleEmailValidator implements
		ConstraintValidator<Email, String> {

	private static String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
	private static String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
	private static String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

	private RegExp pattern = RegExp.compile("^" + ATOM + "+(\\." + ATOM
			+ "+)*@" + DOMAIN + "|" + IP_DOMAIN + ")$", "i");

	@Override
	public void initialize(Email constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.length() == 0) {
			return true;
		}
		return pattern.test(value);
	}
}
