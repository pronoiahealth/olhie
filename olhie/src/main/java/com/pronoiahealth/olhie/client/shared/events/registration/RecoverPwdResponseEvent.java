package com.pronoiahealth.olhie.client.shared.events.registration;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * RecoverPwdResponseEvent.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * <p>
 * Fired By: <br/>
 * Observed By: <br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 22, 2013
 *
 */
@Portable
@Conversational
public class RecoverPwdResponseEvent {
	private boolean sentNewPwdEmail;
	private String errMsg;

	/**
	 * Constructor
	 *
	 */
	public RecoverPwdResponseEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param sentNewPwdEmail
	 * @param errMsg
	 */
	public RecoverPwdResponseEvent(boolean sentNewPwdEmail, String errMsg) {
		super();
		this.sentNewPwdEmail = sentNewPwdEmail;
		this.errMsg = errMsg;
	}

	public boolean isSentNewPwdEmail() {
		return sentNewPwdEmail;
	}

	public void setSentNewPwdEmail(boolean sentNewPwdEmail) {
		this.sentNewPwdEmail = sentNewPwdEmail;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
