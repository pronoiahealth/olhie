package com.pronoiahealth.olhie.client.shared.events.admin;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * UserResetPWEvent.java<br/>
 * Responsibilities:<br/>
 * 
 * <p>
 * Fired From: UserMangementWidget <br/>
 * Observed By: AdminService <br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jan 26, 2014
 *
 */
@Portable
@Conversational
public class UserResetPWEvent {
	private String userId;
	private boolean reset;

	/**
	 * Constructor
	 *
	 */
	public UserResetPWEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param userId
	 * @param reset
	 */
	public UserResetPWEvent(String userId, boolean reset) {
		super();
		this.userId = userId;
		this.reset = reset;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}
}
