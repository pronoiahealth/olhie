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
package com.pronoiahealth.olhie.server.services.dbaccess;

import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.Bookstarrating;

public class BookRatingDAO {

	/**
	 * @param userId
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public static int getUserBookRating(String userId, String bookId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		
		int stars = 0;
		OSQLSynchQuery<Bookstarrating> query = new OSQLSynchQuery<Bookstarrating>(
				"select from Bookstarrating where bookId = :bookId and userId = :userId");
		HashMap<String, String> uparams = new HashMap<String, String>();
		uparams.put("bookId", bookId);
		uparams.put("userId", userId);
		List<Bookstarrating> resultList = ooDbTx.command(query)
				.execute(uparams);
		Bookstarrating current = null;
		
		if (resultList != null && resultList.size() == 1) {
			current = resultList.get(0);
			stars = current.getStars();
		}
		
		return stars;
	}
	
	/**
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public static int getAvgBookRating(String bookId,
			OObjectDatabaseTx ooDbTx) throws Exception {

		int stars = 0;
		
		OSQLSynchQuery<Bookstarrating> query = new OSQLSynchQuery<Bookstarrating>(
				"select from Bookstarrating where bookId = :bookId");
		HashMap<String, String> uparams = new HashMap<String, String>();
		uparams.put("bookId", bookId);
		List<Bookstarrating> resultList = ooDbTx.command(query)
				.execute(uparams);
		
		if (resultList != null && resultList.size() > 0) {
			for (Bookstarrating bookstarrating : resultList) {
				stars =+ bookstarrating.getStars();
			}
			return Math.round(stars/resultList.size());
		} else {
			return 0;
		}
	}

}
