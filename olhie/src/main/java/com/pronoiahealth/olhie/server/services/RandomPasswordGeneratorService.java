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
package com.pronoiahealth.olhie.server.services;

import java.util.Random;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

/**
 * RandomPasswordGeneratorService.java<br/>
 * Responsibilities:<br/>
 * 1. Application scoped password generator
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 * 
 */
@ApplicationScoped
public class RandomPasswordGeneratorService {
	@Inject
	private Logger log;

	@Inject
	@ConfigProperty(name = "PWD_ALPHA_CAPS", defaultValue = "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
	private String ALPHA_CAPS;

	@Inject
	@ConfigProperty(name = "PWD_ALPHA", defaultValue = "abcdefghijklmnopqrstuvwxyz")
	private String ALPHA;

	@Inject
	@ConfigProperty(name = "PWD_NUM", defaultValue = "0123456789")
	private String NUM;

	@Inject
	@ConfigProperty(name = "PWD_SPL_CHARS", defaultValue = "0123456789")
	private String SPL_CHARS;

	/**
	 * Constructor
	 * 
	 */
	public RandomPasswordGeneratorService() {
	}

	/**
	 * Default for Olhie (1 cap, 10 to 16 in length, 1 number)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateApplicationDefaultPwd() throws Exception {
		return new String(generatePswd(8, 10, 1, 1, 0));
	}

	/**
	 * Generic generator
	 * 
	 * @param minLen
	 * @param maxLen
	 * @param noOfCAPSAlpha
	 * @param noOfDigits
	 * @param noOfSplChars
	 * @return
	 * @throws Exception
	 */
	public char[] generatePswd(int minLen, int maxLen, int noOfCAPSAlpha,
			int noOfDigits, int noOfSplChars) throws Exception {
		if (minLen > maxLen) {
			throw new Exception("Min. Length > Max. Length!");
		}

		if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen) {
			throw new Exception(
					"Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
		}

		Random rnd = new Random();
		int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
		char[] pswd = new char[len];
		int index = 0;

		for (int i = 0; i < noOfCAPSAlpha; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
		}

		for (int i = 0; i < noOfDigits; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
		}

		for (int i = 0; i < noOfSplChars; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
		}

		for (int i = 0; i < len; i++) {
			if (pswd[i] == 0) {
				pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
			}
		}
		return pswd;
	}

	/**
	 * Used by generator (generatePswd)
	 * 
	 * @param rnd
	 * @param len
	 * @param pswd
	 * @return
	 */
	private int getNextIndex(Random rnd, int len, char[] pswd) {
		int index = rnd.nextInt(len);
		while (pswd[index = rnd.nextInt(len)] != 0)
			;
		return index;
	}

	/**
	 * Used for testing
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RandomPasswordGeneratorService srv = new RandomPasswordGeneratorService();
			srv.setDefaults();
			System.out.println(srv.generateApplicationDefaultPwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used in conjunction with main test
	 */
	private void setDefaults() {
		ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		ALPHA = "abcdefghijklmnopqrstuvwxyz";
		NUM = "0123456789";
		SPL_CHARS = "!@#$%^&*_=+-/";
	}

}
