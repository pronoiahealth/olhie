package com.pronoiahealth.olhie.server.services.dbaccess;

import com.pronoiahealth.olhie.client.shared.vo.User;

/**
 * BaseDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Oct 2, 2013
 *
 */
public interface BaseDAO {
	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public User getUserByUserId(String userId) throws Exception;

}
