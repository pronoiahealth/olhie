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
package com.pronoiahealth.olhie.server.services.dbaccess.orient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetActionType;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.shared.vo.Bookcomment;
import com.pronoiahealth.olhie.client.shared.vo.Bookstarrating;
import com.pronoiahealth.olhie.client.shared.vo.Comment;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;
import com.pronoiahealth.olhie.server.services.TempThemeHolder;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

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
public class OrientBookDAOImpl extends OrientBaseTxDAO implements BookDAO {

	public OrientBookDAOImpl() {
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#addƒBook(com.pronoiahealth.olhie.client.shared.vo.Book)
	 */
	@Override
	public Book addBook(Book book) throws Exception {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			book = ooDbTx.save(book);
			ooDbTx.commit();
			return ooDbTx.detach(book, true);
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @param book
	 * @param ooDbTx
	 * @param userId
	 * @param holder
	 * @return
	 * @throws Exception
	 */
	@Override
	public BookDisplay getBookDisplayByBook(Book book, String userId,
			TempThemeHolder holder, boolean returnNonProxyed)
			throws Exception {

		// Proxied or un-Proxied instance
		// Remember if proxyed then you must call the get method to access the
		// data
		// If Errai marshalling is involved it will eventually access all daat
		// for return
		if (returnNonProxyed == true) {
			book = ooDbTx.detachAll(book, true);
		}

		// Get rid of unwanted return data
		book = clearUnNeeded(book);

		// Used for querying
		String bookId = book.getId();

		// Find author
		User authorUser = getUserByUserId(book.getAuthorId());
		String authorName = authorUser.getFirstName() + " "
				+ authorUser.getLastName();

		// We need to check that if the book is not yet published only an
		// author or co-author can see it.
		User user = null;
		if (userId != null && userId.length() > 0) {
			user = this.getUserByUserId(userId);
		}
		boolean canView = false;
		if (book.getActive() == false) {
			if (user != null) {
				// Get UserBookRelatioship
				List<UserBookRelationship> rResult = this
						.getUserBookRelationshipByUserIdBookId(bookId, userId);
				if (rResult != null && rResult.size() > 0) {
					for (UserBookRelationship r : rResult) {
						String relationship = r.getUserRelationship();
						if (relationship
								.equals(UserBookRelationshipEnum.CREATOR
										.toString())
								|| relationship
										.equals(UserBookRelationshipEnum.COAUTHOR
												.toString())) {
							// You are an author or co-author
							// You can see it.
							canView = true;
							break;
						}
					}
				} else {
					// Current relationships for this book.
					// This shouldn't happen?
					canView = false;
				}
			} else {
				// Non published book and you are not logged in.
				canView = false;
			}
		} else {
			// Book is active, no worries
			canView = true;
		}

		// If we can't view it return null
		if (canView == false) {
			return null;
		}

		// Find the cover and the category
		BookCover cover = holder.getCoverByName(book.getCoverName());
		BookCategory cat = holder.getCategoryByName(book.getCategory());

		// Get a list of Bookassetdescriptions
		List<Bookassetdescription> baResult = getBookassetdescriptionByBookId(bookId);

		List<Bookassetdescription> retBaResults = new ArrayList<Bookassetdescription>();
		// Need to detach them. We don't want to pull back the entire object
		// tree
		if (baResult != null) {
			for (Bookassetdescription bad : baResult) {
				if (bad.getRemoved().booleanValue() == false) {
					Bookassetdescription retBad = new Bookassetdescription();
					retBad.setBookId(bad.getBookId());
					retBad.setCreatedDate(bad.getCreatedDate());
					retBad.setDescription(bad.getDescription());
					retBad.setId(bad.getId());
					Bookasset ba = bad.getBookAssets().get(0);
					Bookasset retBa = new Bookasset();
					retBa.setId(ba.getId());
					retBa.setContentType(ba.getContentType());
					retBa.setItemType(ba.getItemType());
					ArrayList<Bookasset> retbookAssets = new ArrayList<Bookasset>();
					retbookAssets.add(retBa);
					retBad.setBookAssets(retbookAssets);
					retBaResults.add(retBad);
				}
			}
		}

		// Get book rating and user book rating
		int bookRating = getAvgBookRating(bookId);
		int userBookRating = 0;
		if (user != null) {
			userBookRating = getUserBookRating(user.getUserId(), bookId);
		}

		// Create BookDisplay
		BookDisplay bookDisplay = new BookDisplay(book, cover, cat, authorName,
				retBaResults, bookRating, userBookRating);

		// Logo?
		String logoFileName = book.getLogoFileName();
		bookDisplay
				.setBookLogo((logoFileName != null && logoFileName.length() > 0) ? true
						: false);

		// Comments?
		boolean hasComments = bookHasComments(bookId);
		bookDisplay.setHasComments(hasComments);

		// Return the display
		return bookDisplay;
	}

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
	@Override
	public BookDisplay getBookDisplayById(String bookId, String userId,
			TempThemeHolder holder, boolean returnNonProxyed)
			throws Exception {
		// Find Book
		Book book = ooDbTx.detach(getBookById(bookId), true);

		// Return the Display object
		return getBookDisplayByBook(book, userId, holder, returnNonProxyed);
	}

	/**
	 * Gets a Book entity by the book id.
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public Book getBookById(String bookId) throws Exception {
		OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
				"select from Book where @rid = :bId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);
		if (bResult != null && bResult.size() == 1) {
			return bResult.get(0);
		} else {
			throw new Exception("Could not find book.");
		}
	}

	/**
	 * Gets the contents of a book.
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Bookassetdescription> getBookassetdescriptionByBookId(
			String bookId) throws Exception {
		OSQLSynchQuery<Bookassetdescription> baQuery = new OSQLSynchQuery<Bookassetdescription>(
				"select from Bookassetdescription where bookId = :bId");
		HashMap<String, String> baparams = new HashMap<String, String>();
		baparams.put("bId", bookId);
		List<Bookassetdescription> baResult = ooDbTx.command(baQuery).execute(
				baparams);
		return baResult;
	}

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
	@Override
	public List<Book> getActiveBooksByTitle(String title, int startPos,
			int limit) {
		OSQLSynchQuery<Book> bQuery = null;
		HashMap<String, Object> bparams = new HashMap<String, Object>();
		bparams.put("title", "%" + title.toLowerCase() + "%");

		if (limit != 0 && limit > 0) {
			bQuery = new OSQLSynchQuery<Book>(
					"select from Book where bookTitle.toLowerCase() like :title and active = true SKIP "
							+ startPos + " LIMIT " + limit);
		} else {
			bQuery = new OSQLSynchQuery<Book>(
					"select from Book where bookTitle.toLowerCase() like :title and active = true");
		}
		List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);
		return bResult;
	}

	/**
	 * Gets a list of book ids
	 * 
	 * @param title
	 * @param startPos
	 * @param limit
	 * @param ooDbTx
	 * @return
	 */
	@Override
	public List<String> getActiveBooksIdsByTitle(String title, int startPos,
			int limit) {
		List<String> retLst = new ArrayList<String>();
		List<Book> bResult = getActiveBooksByTitle(title, startPos, limit);
		if (bResult != null && bResult.size() > 0) {
			for (Book book : bResult) {
				retLst.add(book.getId());
			}
		}
		return retLst;
	}

	/**
	 * Update the lastUpdated attribute of a book.
	 * 
	 * @param bookId
	 * @param updateDT
	 * @param ooDbTx
	 * @throws Exception
	 */
	@Override
	public void setLastUpdatedDT(String bookId, Date updateDT) throws Exception {
		if (bookId != null && bookId.length() > 0) {
			try {
				// Start transaction
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				Book book = getBookById(bookId);
				book.setLastUpdated(updateDT);
				ooDbTx.save(book);
				ooDbTx.commit();
			} catch (Exception e) {
				throw e;
			}
		}

	}

	/**
	 * Determines if the userId is the book author or co-author
	 * 
	 * @param userId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isAuthorSelected(String userId, String bookId,
			Set<UserBookRelationshipEnum> userBookRelationshipEnums)
			throws Exception {

		boolean authorSelected = false;
		if (userId != null && userId.length() > 0) {
			if (userBookRelationshipEnums != null
					&& userBookRelationshipEnums.size() > 0) {
				for (UserBookRelationshipEnum r : userBookRelationshipEnums) {
					if (r.equals(UserBookRelationshipEnum.CREATOR)
							|| r.equals(UserBookRelationshipEnum.COAUTHOR)) {
						authorSelected = true;
						break;
					}
				}
			}
		}

		return authorSelected;
	}

	// Comments
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Get all the comments for a book
	 * 
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Bookcomment> getBookCommentsByBookId(String bookId)
			throws Exception {
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
	@Override
	public List<Bookcomment> getBookCommentsByBookIdUserId(String bookId,
			String authorId) throws Exception {
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
	@Override
	public Bookcomment addBookComment(String bookId, String authorId,
			String comment, boolean handleTransaction) throws Exception {

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
	public boolean bookHasComments(String bookId) throws Exception {
		List<Bookcomment> comments = getBookCommentsByBookId(bookId);
		if (comments != null && comments.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////

	// Ratings
	// //////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param userId
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getUserBookRating(String userId, String bookId) throws Exception {

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
	@Override
	public int getAvgBookRating(String bookId) throws Exception {

		int stars = 0;

		OSQLSynchQuery<Bookstarrating> query = new OSQLSynchQuery<Bookstarrating>(
				"select from Bookstarrating where bookId = :bookId");
		HashMap<String, String> uparams = new HashMap<String, String>();
		uparams.put("bookId", bookId);
		List<Bookstarrating> resultList = ooDbTx.command(query)
				.execute(uparams);

		if (resultList != null && resultList.size() > 0) {
			for (Bookstarrating bookstarrating : resultList) {
				stars += bookstarrating.getStars();
			}
			return Math.round(stars / resultList.size());
		} else {
			return 0;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////

	// Relationship
	// /////////////////////////////////////////////////////////////////////////////
	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserBookRelationshipDAO#addUserBookRelationship(com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship)
	 */
	@Override
	public UserBookRelationship addUserBookRelationship(Book book,
			boolean activeRelationship, String userId,
			UserBookRelationshipEnum relationship) throws Exception {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			Date now = new Date();
			UserBookRelationship ubRel = new UserBookRelationship();
			ubRel.setActiveRelationship(activeRelationship);
			ubRel.setBookId(book.getId());
			ubRel.setTheBook(book);
			ubRel.setTheUser(getUserByUserId(userId));
			ubRel.setUserId(userId);
			ubRel.setUserRelationship(relationship.toString());
			ubRel.setEffectiveDate(now);
			ubRel.setLastViewedDate(now);
			ooDbTx.save(ubRel);
			ooDbTx.commit();
			return ooDbTx.detach(ubRel, true);
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserBookRelationshipDAO#getUserBooksRelationshipByUserId(java.lang.String,
	 *      boolean)
	 */
	@Override
	public Set<UserBookRelationshipEnum> getUserBooksRelationshipByUserId(
			String userId, boolean activeOnly) {

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
	@Override
	public List<UserBookRelationship> getUserBooksRelationshipLstByUserId(
			String userId, boolean activeOnly) {

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

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserBookRelationshipDAO#getBookForUserWithActiveMyCollectionRelationship(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public UserBookRelationship getBookForUserWithActiveMyCollectionRelationship(
			String bookId, String userId) throws Exception {
		OSQLSynchQuery<UserBookRelationship> rQuery = new OSQLSynchQuery<UserBookRelationship>(
				"select from UserBookRelationship where userId = :uId and bookId = :bId and activeRelationship = true");
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
	@Override
	public List<UserBookRelationship> getActiveUserBookRelationships(
			String userId, String bookId) {
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
	@Override
	public void setLastViewedOnUserBookRelationship(String userId,
			String bookId, Date now) throws Exception {
		if (userId != null && userId.length() > 0 && bookId != null
				&& bookId.length() > 0) {
			try {
				// Start transaction
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				List<UserBookRelationship> bResult = getActiveUserBookRelationships(
						userId, bookId);

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

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserBookRelationshipDAO#getUserBookRelationshipByBookId(java.lang.String)
	 */
	@Override
	public List<UserBookRelationship> getUserBookRelationshipByBookId(
			String bookId) {
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
	@Override
	public List<UserBookRelationship> getUserBookRelationshipByUserIdBookId(
			String bookId, String userId) {
		// Get UserBookRelatioship
		OSQLSynchQuery<UserBookRelationship> bQuery = new OSQLSynchQuery<UserBookRelationship>(
				"select from UserBookRelationship where userId = :uId and bookId = :bId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookId);
		bparams.put("uId", userId);
		return ooDbTx.command(bQuery).execute(bparams);
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.UserBookRelationshipDAO#addBookToUserCollection(java.util.Date,
	 *      boolean,
	 *      com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum,
	 *      java.lang.String, com.pronoiahealth.olhie.client.shared.vo.Book,
	 *      com.pronoiahealth.olhie.client.shared.vo.User, java.lang.String,
	 *      java.util.Date, java.util.Date)
	 */
	@Override
	public UserBookRelationship addBookToUserCollection(Date date,
			boolean activeRelationship,
			UserBookRelationshipEnum userRelationship, String bookId,
			String userId, Date effectiveDate, Date lastViewedDate)
			throws Exception {
		Book book = getBookById(bookId);
		User user = getUserByUserId(userId);
		UserBookRelationship rel = new UserBookRelationship();
		rel.setActiveRelationship(activeRelationship);
		rel.setUserRelationship(userRelationship.toString());
		rel.setBookId(bookId);
		rel.setTheBook(book);
		rel.setTheUser(user);
		rel.setUserId(userId);
		rel.setEffectiveDate(date);
		rel.setLastViewedDate(date);
		return ooDbTx.save(rel);
	}

	/**
	 * Gets the relationships a user has with a book.
	 * 
	 * @param userId
	 * @param loggedIn
	 * @param bookId
	 * @param ooDbTx
	 * @return
	 * @throws Exception
	 */
	@Override
	public Set<UserBookRelationshipEnum> getActiveBookRealtionshipForUser(
			String userId, boolean loggedIn, String bookId) throws Exception {
		Set<UserBookRelationshipEnum> rels = new TreeSet<UserBookRelationshipEnum>();

		// relationship
		if (userId != null && userId.length() > 0 && loggedIn == true) {
			List<UserBookRelationship> bResult = getActiveUserBookRelationships(
					userId, bookId);

			// Set up return
			if (bResult != null && bResult.size() > 0) {
				for (UserBookRelationship r : bResult) {
					String rel = r.getUserRelationship();
					UserBookRelationshipEnum retRel = UserBookRelationshipEnum
							.valueOf(rel);
					rels.add(retRel);
				}
			} else {
				rels.add(UserBookRelationshipEnum.NONE);
			}
		} else {
			rels.add(UserBookRelationshipEnum.NONLOGGEDINUSER);
		}
		return rels;
	}

	/*
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#
	 * getUserBookRelationshipByUserIdBookId(java.lang.String, java.lang.String,
	 * boolean)
	 */
	@Override
	public Set<UserBookRelationshipEnum> getUserBookRelationshipByUserIdBookId(
			String bookId, String userId, boolean activeOnly) {

		Set<UserBookRelationshipEnum> retSet = new TreeSet<UserBookRelationshipEnum>();
		if (userId != null && bookId != null) {
			OSQLSynchQuery<UserBookRelationship> rQuery = null;
			if (activeOnly == true) {
				rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where userId = :uId and bookId = :bId and activeRelationship = true");
			} else {
				rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where userId = :uId and bookId = :bId");
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

	// ////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#inactivateBookAssetDescriptionFromBook(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Bookassetdescription inactivateBookAssetDescriptionFromBook(
			String userId, String bookAssetdescriptionId) throws Exception {
		// Find the book asset description
		OSQLSynchQuery<Bookassetdescription> badQuery = new OSQLSynchQuery<Bookassetdescription>(
				"select from Bookassetdescription where @rid = :badId");
		HashMap<String, String> uparams = new HashMap<String, String>();
		uparams.put("badId", bookAssetdescriptionId);
		List<Bookassetdescription> badResult = ooDbTx.command(badQuery)
				.execute(uparams);
		Bookassetdescription currentBad = null;
		if (badResult != null && badResult.size() == 1) {
			// Got the user
			currentBad = badResult.get(0);
		} else {
			throw new Exception(
					"Can't find the book asset description to remove.");
		}

		// Update the data
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			// Get bookId for later
			String bookId = currentBad.getBookId();

			// Set it to removed and save it
			currentBad.setRemoved(true);
			currentBad.setRemovedDate(new Date());
			currentBad.setRemovedBy(userId);

			// Now save it
			currentBad = ooDbTx.save(currentBad);

			// Update the last updated attribute of the book
			setLastUpdatedDT(bookId, new Date());

			// Commit changes
			ooDbTx.commit();
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}

		// Return detached instance
		return ooDbTx.detach(currentBad, true);
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#removeBookFromMyCollection(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public UserBookRelationship removeBookFromMyCollection(String userId,
			String bookId) throws Exception {

		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			UserBookRelationship rel = getBookForUserWithActiveMyCollectionRelationship(
					bookId, userId);
			if (rel != null) {
				rel.setActiveRelationship(false);
				rel.setInactiveDate(new Date());
				ooDbTx.save(rel);
				ooDbTx.commit();
			}
			return ooDbTx.detach(rel, true);
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#getBookasset(java.lang.String)
	 */
	@Override
	public Bookasset getBookasset(String bookassetId) throws Exception {
		OSQLSynchQuery<Bookasset> bQuery = new OSQLSynchQuery<Bookasset>(
				"select from Bookasset where @rid = :bId");
		HashMap<String, String> bparams = new HashMap<String, String>();
		bparams.put("bId", bookassetId);
		List<Bookasset> bResult = ooDbTx.command(bQuery).execute(bparams);
		Bookasset bookasset = null;
		if (bResult != null && bResult.size() == 1) {
			bookasset = bResult.get(0);
		} else {
			bookasset = null;
		}

		return bookasset;
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#addUpdateBookasset(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, long)
	 */
	@Override
	public void addUpdateBookasset(String description, String bookId,
			String contentType, String data, String action, String fileName,
			long size) throws Exception {
		if (BookAssetActionType.valueOf(action).equals(BookAssetActionType.NEW)) {
			// Find Book
			OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
					"select from Book where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", bookId);
			List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);
			Book book = null;
			if (bResult != null && bResult.size() == 1) {
				book = bResult.get(0);
			} else {
				throw new Exception(String.format(
						"Could not find Book for id %s", bookId));
			}

			// Create Bookassetdescription
			Date now = new Date();
			Bookassetdescription bad = new Bookassetdescription();
			try {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				bad.setBookId(bookId);
				bad.setCreatedDate(now);
				bad.setDescription(description);
				bad.setRemoved(Boolean.FALSE);
				bad = ooDbTx.save(bad);
				ooDbTx.commit();
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				ooDbTx.rollback();
				throw new Exception(e);
			}

			// Bookasset and add to bad list
			Bookasset ba = new Bookasset();
			try {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				ba.setAuthorId(book.getAuthorId());
				ba.setBookassetdescriptionId(bad.getId());
				ba.setContentType(contentType);
				ba.setCreatedDate(now);
				ba.setItemName(fileName);
				ba.setItemType(BookAssetDataType.FILE.name());
				ba.setSize(size);
				ba.setBase64Data(data);
				ba = ooDbTx.save(ba);
				ooDbTx.commit();
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				ooDbTx.rollback();
				throw new Exception(e);
			}

			// Add Bookasset to BookasssetDescription list
			try {
				ooDbTx.begin(TXTYPE.OPTIMISTIC);
				List<Bookasset> baLst = new LinkedList<Bookasset>();
				baLst.add(ba);
				bad.setBookAssets(baLst);
				bad = ooDbTx.save(bad);
				ooDbTx.commit();
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				ooDbTx.rollback();
				throw new Exception(e);
			}
		}
	}

	// Comments
	// ////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#saveComment(com.pronoiahealth.olhie.client.shared.vo.Comment)
	 */
	@Override
	public Comment saveComment(Comment comment) throws Exception {
		try {
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			comment.setSubmittedDate(new Date());
			comment = ooDbTx.save(comment);
			ooDbTx.commit();
			return ooDbTx.detach(comment, true);
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////

	// Comments
	// ////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#addBookRating(java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	public Bookstarrating addUpdateBookRating(String userId, String bookId,
			int rating) throws Exception {

		try {
			Date now = new Date();
			ooDbTx.begin(TXTYPE.OPTIMISTIC);
			// Find the book stars
			OSQLSynchQuery<Bookstarrating> query = new OSQLSynchQuery<Bookstarrating>(
					"select from Bookstarrating where bookId = :bookId and userId = :userId");
			HashMap<String, String> uparams = new HashMap<String, String>();
			uparams.put("bookId", bookId);
			uparams.put("userId", userId);
			List<Bookstarrating> resultList = ooDbTx.command(query).execute(
					uparams);
			Bookstarrating current = null;
			if (resultList != null && resultList.size() == 1) {
				// Got the book star rating
				current = resultList.get(0);
				current.setUpdatedDate(now);
				current.setStars(rating);
			} else {
				current = new Bookstarrating();
				current.setBookId(bookId);
				current.setUserId(userId);
				current.setStars(rating);
				current.setUpdatedDate(now);
			}

			// Now save it
			current = ooDbTx.save(current);
			ooDbTx.commit();

			// Return the Bookstarrating
			return ooDbTx.detach(current, true);
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a non-proxyed instance of the book
	 * 
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#addLogo(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, long)
	 */
	@Override
	public Book addLogo(String bookId, String contentType, String data,
			String fileName, long size) throws Exception {

		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			// Find Book
			OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
					"select from Book where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", bookId);
			List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);
			Book book = null;
			if (bResult != null && bResult.size() == 1) {
				book = bResult.get(0);
			} else {
				throw new Exception(String.format(
						"Could not find Book for id %s", bookId));
			}

			// Update the book
			book.setLogoFileName(fileName);
			book.setBase64LogoData(data);
			book = ooDbTx.save(book);
			ooDbTx.commit();

			return ooDbTx.detach(book, true);
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#addLogoAndFrontCover(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, long,
	 *      java.lang.String)
	 */
	@Override
	public Book addLogoAndFrontCover(String bookId, String contentType,
			String data, String fileName, long size, String encodedFrontCover)
			throws Exception {
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			// Find Book
			OSQLSynchQuery<Book> bQuery = new OSQLSynchQuery<Book>(
					"select from Book where @rid = :bId");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("bId", bookId);
			List<Book> bResult = ooDbTx.command(bQuery).execute(bparams);
			Book book = null;
			if (bResult != null && bResult.size() == 1) {
				book = bResult.get(0);
			} else {
				throw new Exception(String.format(
						"Could not find Book for id %s", bookId));
			}

			// Update the book
			book.setLogoFileName(fileName);
			book.setBase64LogoData(data);
			book.setBase64FrontCover(encodedFrontCover);
			book = ooDbTx.save(book);
			ooDbTx.commit();

			return ooDbTx.detach(book, true);
		} catch (Exception e) {
			ooDbTx.rollback();
			throw e;
		}
	}

	/**
	 * @see com.pronoiahealth.olhie.server.services.dbaccess.BookDAO#getAuthorName()
	 */
	@Override
	public String getAuthorName(String authorId) throws Exception {
		User user = getUserByUserId(authorId);
		return getBookAuthorName(user.getLastName(), user.getFirstName());
	}

	/**
	 * @param lastName
	 * @param firstName
	 * @return
	 */
	private String getBookAuthorName(String lastName, String firstName) {
		return String.format("%1$s %2$s", firstName != null ? firstName : "",
				lastName != null ? lastName : "");
	}

	/**
	 * Detachs the Book data except for the base64Logo field
	 * 
	 * @param book
	 */
	private Book clearUnNeeded(Book book) {
		book.setBase64LogoData(null);
		book.setSolrUpdate(null);
		return book;
	}
}
