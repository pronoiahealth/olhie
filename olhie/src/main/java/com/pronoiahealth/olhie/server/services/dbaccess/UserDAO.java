package com.pronoiahealth.olhie.server.services.dbaccess;

import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.User;

public class UserDAO {

	public static User getUserByUserId(String userId, OObjectDatabaseTx ooDbTx)
			throws Exception {
		OSQLSynchQuery<User> uQuery = new OSQLSynchQuery<User>(
				"select from User where userId = :uId");
		HashMap<String, String> uparams = new HashMap<String, String>();
		uparams.put("uId", userId);
		List<User> uResult = ooDbTx.command(uQuery).execute(uparams);
		User user = null;
		if (uResult != null && uResult.size() == 1) {
			user = uResult.get(0);
		} else {
			throw new Exception("Can't find user.");
		}
		return user;
	}

}
