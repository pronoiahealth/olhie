package com.pronoiahealth.olhie.server.services.dbaccess;

import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.server.dataaccess.vo.Password;

public class PasswordDAO {

	public static Password getPwdByUserId(String userId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		OSQLSynchQuery<Password> query = new OSQLSynchQuery<Password>(
				"select from Password where userId = :uId");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("uId", userId);
		List<Password> pResult = ooDbTx.command(query).execute(params);
		if (pResult != null && pResult.size() == 1) {
			return pResult.get(0);
		} else {
			throw new Exception("Can't find passowrd for userId");
		}
	}

}
