package com.pronoiahealth.olhie.client.shared.services;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;

import com.pronoiahealth.olhie.client.shared.vo.ConnectedUser;

@Remote
public interface ConnectedUserService {
	public List<ConnectedUser> getConnectedUsers(String qry);
}
