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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;

/**
 * UserBookRelationshipDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 11, 2013
 * 
 */
public class UserBookRelationshipDAO {

	public static Set<UserBookRelationshipEnum> getUserBookRelationshipByUserIdBookId(
			String bookId, String userId, boolean activeOnly,
			OObjectDatabaseTx ooDbTx) {

		Set<UserBookRelationshipEnum> retSet = new TreeSet<UserBookRelationshipEnum>();
		if (userId != null && bookId != null) {
			OSQLSynchQuery<UserBookRelationship> rQuery = null;
			if (activeOnly == true) {
				rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where bookId = :bId and userId = :uId and activeRelationship = true");
			} else {
				rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where bookId = :bId and userId = :uId");
			}
			HashMap<String, String> rparams = new HashMap<String, String>();
			rparams.put("bId", bookId);
			rparams.put("uId", userId);
			List<UserBookRelationship> rResult = ooDbTx.command(rQuery)
					.execute(rparams);
			if (rResult != null && rResult.size() > 0) {
				for (UserBookRelationship r : rResult) {
					retSet.add(UserBookRelationshipEnum.valueOf(r
							.getUserRelationship()));
				}
			} else {
				retSet.add(UserBookRelationshipEnum.NONE);
			}
		} else {
			retSet.add(UserBookRelationshipEnum.NONE);
		}

		return retSet;
	}

	public static Set<UserBookRelationshipEnum> getUserBooksRelationshipByUserId(
			String userId, boolean activeOnly, OObjectDatabaseTx ooDbTx) {

		Set<UserBookRelationshipEnum> retSet = new TreeSet<UserBookRelationshipEnum>();
		if (userId != null) {
			OSQLSynchQuery<UserBookRelationship> rQuery = null;
			if (activeOnly == true) {
				rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where userId = :uId and activeRelationship = true");
			} else {
				rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where userId = :uId");
			}
			HashMap<String, String> rparams = new HashMap<String, String>();
			rparams.put("uId", userId);
			List<UserBookRelationship> rResult = ooDbTx.command(rQuery)
					.execute(rparams);
			if (rResult != null && rResult.size() > 0) {
				for (UserBookRelationship r : rResult) {
					retSet.add(UserBookRelationshipEnum.valueOf(r
							.getUserRelationship()));
				}
			}
		}

		return retSet;
	}

	/**
	 * Null may be returned if the userId is null or there are not relationships
	 * for the user.
	 * 
	 * @param userId
	 * @param activeOnly
	 * @param ooDbTx
	 * @return
	 */
	public static List<UserBookRelationship> getUserBooksRelationshipLstByUserId(
			String userId, boolean activeOnly, OObjectDatabaseTx ooDbTx) {

		if (userId != null) {
			OSQLSynchQuery<UserBookRelationship> rQuery = null;
			if (activeOnly == true) {
				rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where userId = :uId and activeRelationship = true");
			} else {
				rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where userId = :uId");
			}
			HashMap<String, String> rparams = new HashMap<String, String>();
			rparams.put("uId", userId);
			return ooDbTx.command(rQuery).execute(rparams);
		} else {
			return null;
		}
	}

	public static UserBookRelationship getBookForUserWithActiveMyCollectionRelationship(
			String bookId, String userId, OObjectDatabaseTx ooDbTx)
			throws Exception {
		OSQLSynchQuery<UserBookRelationship> rQuery = new OSQLSynchQuery<UserBookRelationship>(
				"select from UserBookRelationship where bookId = :bId and userId = :uId and activeRelationship = true");
		HashMap<String, String> rparams = new HashMap<String, String>();
		rparams.put("bId", bookId);
		rparams.put("uId", userId);
		List<UserBookRelationship> rResult = ooDbTx.command(rQuery).execute(
				rparams);
		// Should only be one active relationship in MyCollection for an
		// individual book
		if (rResult != null && rResult.size() == 1) {
			return rResult.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Gets active relationships a user has with a book.
	 * 
	 * @param userId
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 */
	public static List<UserBookRelationship> getActiveUserBookRelationships(
			String userId, String bookId, OObjectDatabaseTx ooDbTx) {
		OSQLSynchQuery<UserBookRelationship> bQuery = new OSQLSynchQuery<UserBookRelationship>(
				"select from UserBookRelationship where userId = :uId and bookId = :bId and activeRelationship = true");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("uId", userId);
		bparams.put("bId", bookId);
		return ooDbTx.command(bQuery).execute(bparams);
	}

	/**
	 * Update the last viewed date on the user book relationship
	 * 
	 * @param userId
	 * @param bookId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public static void setLastViewedOnUserBookRelationship(String userId,
			String bookId, Date now, OObjectDatabaseTx ooDbTx) throws Exception {
		if (userId != null && userId.length() > 0 && bookId != null
				&& bookId.length() > 0) {
			try {
				// Start transaction
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				List<UserBookRelationship> bResult = getActiveUserBookRelationships(
						userId, bookId, ooDbTx);

				// update records
				if (bResult != null && bResult.size() > 0) {
					for (UserBookRelationship r : bResult) {
						r.setLastViewedDate(now);
						ooDbTx.save(r);
					}
				}

				// Commit changes
				ooDbTx.commit();
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public static List<UserBookRelationship> getUserBookRelationshipByBookId(
			String bookId, OObjectDatabaseTx ooDbTx) {
		// Get UserBookRelatioship
		OSQLSynchQuery<UserBookRelationship> bQuery = new OSQLSynchQuery<UserBookRelationship>(
				"select from UserBookRelationship where bookId = :bId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		return ooDbTx.command(bQuery).execute(bparams);
	}
	
	/**
	 * Returns the relationships for a user and book.
	 * 
	 * @param bookId
	 * @param userId
	 * @param ooDbTx
	 * @return
	 */
	public static List<UserBookRelationship> getUserBookRelationshipByUserIdBookId(
			String bookId, String userId, OObjectDatabaseTx ooDbTx) {
		// Get UserBookRelatioship
		OSQLSynchQuery<UserBookRelationship> bQuery = new OSQLSynchQuery<UserBookRelationship>(
				"select from UserBookRelationship where bookId = :bId and userId = :uId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		bparams.put("uId", userId);
		return ooDbTx.command(bQuery).execute(bparams);
	}
}
