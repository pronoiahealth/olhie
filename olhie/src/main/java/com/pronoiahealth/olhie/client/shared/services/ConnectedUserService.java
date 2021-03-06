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
package com.pronoiahealth.olhie.client.shared.services;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;

import com.pronoiahealth.olhie.client.shared.vo.ConnectedUser;

/**
 * ConnectedUserService.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Sep 8, 2013
 *
 */
@Remote
public interface ConnectedUserService {
	public List<ConnectedUser> getConnectedUsers(String qry);
}
