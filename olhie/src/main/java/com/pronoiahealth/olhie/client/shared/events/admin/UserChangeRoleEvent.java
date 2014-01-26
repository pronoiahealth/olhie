package com.pronoiahealth.olhie.client.shared.events.admin;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * UserChangeRoleEvent.java<br/>
 * Responsibilities:<br/>
 * 
 * <p>
 * Fired From: UserManagementWidget <br/>
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
public class UserChangeRoleEvent {
	private String userId;
	private String newRole;

	/**
	 * Constructor
	 *
	 */
	public UserChangeRoleEvent() {
	}

	/**
	 * Constructor
	 *
	 * @param userId
	 * @param newRole
	 */
	public UserChangeRoleEvent(String userId, String newRole) {
		super();
		this.userId = userId;
		this.newRole = newRole;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNewRole() {
		return newRole;
	}

	public void setNewRole(String newRole) {
		this.newRole = newRole;
	}
}
