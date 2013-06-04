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
package com.pronoiahealth.olhie.client.shared.constants;

/**
 * Application constants. We are using DeltaSpike on the back end so these
 * constants could be moved to the DeltaSpike config file. When they are needed
 * we could get them from a producer method. <br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since 5/26/2013
 * 
 */
// TODO: Investigate putting contants in DeltaSpike config file and getting
// through producer method on server?
public class AppConstants {
	private static final String HOSTED = "?gwt.codesvr=127.0.0.1:9997";
	public static String APP_URL = "http://localhost:8080/olhie/index.html"
			+ HOSTED;
	public static String FRONT_PAGE = "http://localhost:8080/olhie/";
	public static int SCREEN_TIMEOUT = 900000;
	public static int PING_FIRE_INTERVAL = 10000;
	public static int NEWS_FADE_INTERVAL = 5000;
}