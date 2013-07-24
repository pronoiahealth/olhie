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
import com.pronoiahealth.olhie.client.shared.vo.BookComment;

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
	public static List<BookComment> getBookCommentsByBookId(String bookId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
				"select from BookComment where bookId = :bId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		List<BookComment> bResult = ooDbTx.command(bQuery).execute(bparams);
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
	public static List<BookComment> getBookCommentsByBookIdUserId(
			String bookId, String authorId, OObjectDatabaseTx ooDbTx)
			throws Exception {
		OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
				"select from BookComment where bookId = :bId and authorId = :aId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		bparams.put("aId", authorId);
		List<BookComment> bResult = ooDbTx.command(bQuery).execute(bparams);
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
	public static BookComment addBookComment(String bookId, String authorId,
			String comment, OObjectDatabaseTx ooDbTx, boolean handleTransaction) throws Exception {

		if (handleTransaction == true) {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
		}

		BookComment com = new BookComment();
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

}
