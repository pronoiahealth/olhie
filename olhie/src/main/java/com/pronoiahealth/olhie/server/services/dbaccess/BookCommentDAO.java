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

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.Bookcomment;

/**
 * BookCommentDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 16, 2013
 * 
 */
public class BookCommentDAO {

	public BookCommentDAO() {
	}

	/**
	 * Get all the comments for a book
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public static List<Bookcomment> getBookCommentsByBookId(String bookId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
				"select from BookComment where bookId = :bId order by createDT desc");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		List<Bookcomment> bResult = ooDbTx.command(bQuery).execute(bparams);
		return bResult;
	}

	/**
	 * Get all the comments for a book made by a specific user
	 * 
	 * @param bookId
	 * @param authorId
	 * @param ooDbTx
	 * @param handleTransaction
	 * @return
	 * @throws Exception
	 */
	public static List<Bookcomment> getBookCommentsByBookIdUserId(
			String bookId, String authorId, OObjectDatabaseTx ooDbTx)
			throws Exception {
		OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
				"select from BookComment where bookId = :bId and authorId = :aId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		bparams.put("aId", authorId);
		List<Bookcomment> bResult = ooDbTx.command(bQuery).execute(bparams);
		return bResult;
	}

	/**
	 * Add book comment
	 * 
	 * @param bookId
	 * @param authorId
	 * @param comment
	 * @param ooDbTx
	 * @param handleTransaction
	 * @return
	 */
	public static Bookcomment addBookComment(String bookId, String authorId,
			String comment, OObjectDatabaseTx ooDbTx, boolean handleTransaction)
			throws Exception {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		Bookcomment com = new Bookcomment();
		com.setAuthorId(authorId);
		com.setBookId(bookId);
		com.setComment(comment);
		com.setCreatedDT(new Date());
		com = ooDbTx.save(com);

		if (handleTransaction == true) {
			ooDbTx.commit();
			com = ooDbTx.detach(com, true);
		}

		return com;
	}

	/**
	 * Returns true if the book has comments, false otherwise
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public static boolean bookHasComments(String bookId, OObjectDatabaseTx ooDbTx)
			throws Exception {
		List<Bookcomment> comments = getBookCommentsByBookId(bookId, ooDbTx);
		if (comments != null && comments.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
