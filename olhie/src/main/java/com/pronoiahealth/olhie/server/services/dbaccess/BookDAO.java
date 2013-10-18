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
import java.util.List;
import java.util.Set;

import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.shared.vo.Bookcomment;
import com.pronoiahealth.olhie.client.shared.vo.Bookstarrating;
import com.pronoiahealth.olhie.client.shared.vo.Comment;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;
import com.pronoiahealth.olhie.server.services.TempThemeHolder;

/**
 * BookDAO.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 12, 2013
 * 
 */
public interface BookDAO extends BaseDAO {

	/**
	 * Adds the book to the database. Returns a fully populated (detached)
	 * instance.
	 * 
	 * @param book
	 * @return
	 * @throws Exception
	 */
	public Book addBook(Book book) throws Exception;

	/**
	 * @param book
	 * @param ooDbTx
	 * @param userId
	 * @param holder
	 * @return
	 * @throws Exception
	 */
	public BookDisplay getBookDisplayByBook(Book book, String userId,
			TempThemeHolder holder, boolean returnNonProxyed)
			throws Exception;

	/**
	 * Eventually calls getBookDisplayByBook().
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @param userId
	 * @param holder
	 * @return
	 * @throws Exception
	 */
	public BookDisplay getBookDisplayById(String bookId, String userId,
			TempThemeHolder holder, boolean returnNonProxyed)
			throws Exception;

	/**
	 * Gets a Book entity by the book id.
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public Book getBookById(String bookId) throws Exception;

	/**
	 * Gets the contents of a book.
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public List<Bookassetdescription> getBookassetdescriptionByBookId(
			String bookId) throws Exception;

	/**
	 * Gets a list of books that matches the title or partial title. If either
	 * startPos and limit are 0 then all books that match will be returned.
	 * 
	 * @param title
	 * @param startPos
	 * @param limit
	 * @param ooDbTx
	 * @return
	 */
	public List<Book> getActiveBooksByTitle(String title, int startPos,
			int limit);

	/**
	 * Gets a list of book ids
	 * 
	 * @param title
	 * @param startPos
	 * @param limit
	 * @param ooDbTx
	 * @return
	 */
	public List<String> getActiveBooksIdsByTitle(String title, int startPos,
			int limit);

	/**
	 * Update the lastUpdated attribute of a book.
	 * 
	 * @param bookId
	 * @param updateDT
	 * @param ooDbTx
	 * @throws Exception
	 */
	public void setLastUpdatedDT(String bookId, Date updateDT) throws Exception;

	/**
	 * @param userId
	 * @param bookId
	 * @return
	 * @throws Exception
	 */
	public boolean isAuthorSelected(String userId, String bookId,
			Set<UserBookRelationshipEnum> userBookRelationshipEnums)
			throws Exception;

	/**
	 * Get all the comments for a book
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public List<Bookcomment> getBookCommentsByBookId(String bookId)
			throws Exception;

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
	public List<Bookcomment> getBookCommentsByBookIdUserId(String bookId,
			String authorId) throws Exception;

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
	public Bookcomment addBookComment(String bookId, String authorId,
			String comment, boolean handleTransaction) throws Exception;

	/**
	 * Returns true if the book has comments, false otherwise
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public boolean bookHasComments(String bookId) throws Exception;

	/**
	 * @param userId
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public int getUserBookRating(String userId, String bookId) throws Exception;

	/**
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	public int getAvgBookRating(String bookId) throws Exception;

	/**
	 * Return a fully populated instance.
	 * 
	 * @param userBookRelationship
	 * @return
	 * @throws Exception
	 */
	public UserBookRelationship addUserBookRelationship(Book book,
			boolean activeRelationship, String userId,
			UserBookRelationshipEnum relationship) throws Exception;

	/**
	 * @param bookId
	 * @param userId
	 * @param activeOnly
	 * @return
	 */
	public Set<UserBookRelationshipEnum> getUserBookRelationshipByUserIdBookId(
			String bookId, String userId, boolean activeOnly);

	/**
	 * @param userId
	 * @param activeOnly
	 * @return
	 */
	public Set<UserBookRelationshipEnum> getUserBooksRelationshipByUserId(
			String userId, boolean activeOnly);

	/**
	 * Null may be returned if the userId is null or there are not relationships
	 * for the user.
	 * 
	 * @param userId
	 * @param activeOnly
	 * @param ooDbTx
	 * @return
	 */
	public List<UserBookRelationship> getUserBooksRelationshipLstByUserId(
			String userId, boolean activeOnly);

	/**
	 * @param bookId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public UserBookRelationship getBookForUserWithActiveMyCollectionRelationship(
			String bookId, String userId) throws Exception;

	/**
	 * Gets active relationships a user has with a book.
	 * 
	 * @param userId
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 */
	public List<UserBookRelationship> getActiveUserBookRelationships(
			String userId, String bookId);

	/**
	 * Update the last viewed date on the user book relationship
	 * 
	 * @param userId
	 * @param bookId
	 * @param ooDbTx
	 * @throws Exception
	 */
	public void setLastViewedOnUserBookRelationship(String userId,
			String bookId, Date now) throws Exception;

	/**
	 * @param bookId
	 * @return
	 */
	public List<UserBookRelationship> getUserBookRelationshipByBookId(
			String bookId);

	/**
	 * Returns the relationships for a user and book.
	 * 
	 * @param bookId
	 * @param userId
	 * @param ooDbTx
	 * @return
	 */
	public List<UserBookRelationship> getUserBookRelationshipByUserIdBookId(
			String bookId, String userId);

	/**
	 * @param date
	 * @param activeRelationship
	 * @param userRelationship
	 * @param bookId
	 * @param book
	 * @param user
	 * @param userId
	 * @param effectiveDate
	 * @param lastViewedDate
	 * @return
	 */
	public UserBookRelationship addBookToUserCollection(Date date,
			boolean activeRelationship,
			UserBookRelationshipEnum userRelationship, String bookId,
			String userId, Date effectiveDate, Date lastViewedDate)
			throws Exception;

	/**
	 * @param userId
	 * @param loggedIn
	 * @param bookId
	 * @return
	 * @throws Exception
	 */
	public Set<UserBookRelationshipEnum> getActiveBookRealtionshipForUser(
			String userId, boolean loggedIn, String bookId) throws Exception;

	/**
	 * Sets the bookassetdescription to inactive
	 * 
	 * @param userId
	 * @param bookAssetdescriptionId
	 * @return
	 * @throws Exception
	 */
	public Bookassetdescription inactivateBookAssetDescriptionFromBook(
			String userId, String bookAssetdescriptionId) throws Exception;

	/**
	 * Removes the book from the given users collection
	 * 
	 * @param userId
	 * @param bookId
	 * @return
	 * @throws Exception
	 */
	public UserBookRelationship removeBookFromMyCollection(String userId,
			String bookId) throws Exception;

	/**
	 * Return the Bookasset
	 * 
	 * @param bookassetId
	 * @return
	 * @throws Exception
	 */
	public Bookasset getBookasset(String bookassetId) throws Exception;

	/**
	 * Add or update a bookasset
	 * 
	 * @param description
	 * @param bookId
	 * @param contentType
	 * @param data
	 *            - Base64 encoded
	 * @param action
	 *            - NEW or REVISE, Only NEW works currently
	 * @param fileName
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public void addUpdateBookasset(String description, String bookId,
			String contentType, String data, String action, String fileName,
			long size) throws Exception;

	/**
	 * Add logo
	 * 
	 * @param bookId
	 * @param contentType
	 * @param data
	 *            - Base64 encoded logo file
	 * @param fileName
	 * @param size
	 * @throws Exception
	 */
	public Book addLogo(String bookId, String contentType, String data,
			String fileName, long size) throws Exception;
	
	/**
	 * Add logo and encoded front cover
	 * 
	 * @param bookId
	 * @param contentType
	 * @param data
	 * @param fileName
	 * @param size
	 * @param encodedFrontCover
	 * @return
	 * @throws Exception
	 */
	public Book addLogoAndFrontCover(String bookId, String contentType, String data,
			String fileName, long size, String encodedFrontCover) throws Exception;

	/**
	 * Saves a book comment
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public Comment saveComment(Comment comment) throws Exception;

	/**
	 * Add a rating to a book
	 * 
	 * @param userId
	 * @param bookId
	 * @param rating
	 * @return
	 * @throws Exception
	 */
	public Bookstarrating addUpdateBookRating(String userId, String bookId,
			int rating) throws Exception;

	/**
	 * Returns the author name (first then last).
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAuthorName(String authorId) throws Exception;
}
