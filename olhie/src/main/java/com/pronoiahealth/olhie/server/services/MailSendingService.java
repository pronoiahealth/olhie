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

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.deltaspike.core.api.config.ConfigProperty;

/**
 * MailSendingService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 * 
 */
@ApplicationScoped
public class MailSendingService {

	@Inject
	private Logger log;

	@Inject
	@ConfigProperty(name = "MAIL_SERVER_ADDRESS", defaultValue = "smtp.gmail.com")
	private String smtpSever;

	@Inject
	@ConfigProperty(name = "MAIL_FROM_PWD", defaultValue = "jjdestef3@gmail.com")
	private String fromAddress;

	@Inject
	@ConfigProperty(name = "MAIL_FROM_PWD")
	private String fromPwd;

	@Inject
	@ConfigProperty(name = "MAIL_SMTP_PORT", defaultValue = "587")
	private String smtpPort;

	@Inject
	@ConfigProperty(name = "MAIL_TLS_ENABLED", defaultValue = "true")
	private String tlsEnabled;
	
	@Inject
	@ConfigProperty(name = "MAIL_DEBUG", defaultValue = "false")
	private String debugEnabled;

	/**
	 * Constructor
	 * 
	 */
	public MailSendingService() {
	}

	/**
	 * Sends a password reset email to the email address provided
	 * 
	 * @param toEmail
	 * @param newPwd
	 * @throws Exception
	 */
	public void sendPwdResetMailFromApp(String toEmail, String newPwd)
			throws Exception {
		Email email = new SimpleEmail();
		email.setSmtpPort(Integer.parseInt(smtpPort));
		email.setAuthenticator(new DefaultAuthenticator(fromAddress, fromPwd));
		email.setDebug(Boolean.parseBoolean(debugEnabled));
		email.setHostName(smtpSever);
		email.setFrom(fromAddress);
		email.setSubject("Reset Olhie Password");
		email.setMsg("You have requested that your password be reset. Your new Olhie password is "
				+ newPwd);
		email.addTo(toEmail);
		email.setTLS(Boolean.parseBoolean(tlsEnabled));
		email.setSocketTimeout(10000);
		email.setSocketConnectionTimeout(12000);
		email.send();
	}

	/**
	 * Testing purposes
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MailSendingService srv = new MailSendingService();
			srv.setDefaults();
			srv.sendPwdResetMailFromApp("jjdestef3@gmail.com", "sometestpwd");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used for testing with main
	 */
	private void setDefaults() {
		smtpSever = "smtp.gmail.com";
		fromPwd = "myunclejimmy2";
		fromAddress = "jjdestef3@gmail.com";
		smtpPort = "587";
		tlsEnabled = "true";
	}
}
