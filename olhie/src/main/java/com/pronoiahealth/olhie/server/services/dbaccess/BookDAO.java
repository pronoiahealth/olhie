package com.pronoiahealth.olhie.server.services.dbaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.client.shared.vo.UserBookRelationship;
import com.pronoiahealth.olhie.server.services.TempCoverBinderHolder;

public class BookDAO {

	public static BookDisplay getBookDisplayById(String bookId,
			OObjectDatabaseTx ooDbTx, String userId,
			TempCoverBinderHolder holder) throws Exception {
		// Find Book
		Book book = ooDbTx.detach(getBookById(bookId, ooDbTx));

		// Find author
		User user = UserDAO.getUserByUserId(book.getAuthorId(), ooDbTx);
		String authorName = user.getFirstName() + " " + user.getLastName();

		// We need to check that if the book is not yet published only an
		// author or co-author can see it.
		boolean canView = false;
		if (book.getActive() == false) {
			if (userId != null && userId.length() > 0) {
				// Get UserBookRelatioship
				OSQLSynchQuery<UserBookRelationship> rQuery = new OSQLSynchQuery<UserBookRelationship>(
						"select from UserBookRelationship where bookId = :bId");
				HashMap<String, String> rparams = new HashMap<String, String>();
				rparams.put("bId", bookId);
				List<UserBookRelationship> rResult = ooDbTx.command(rQuery)
						.execute(rparams);
				if (rResult != null && rResult.size() > 0) {
					for (UserBookRelationship r : rResult) {
						if (r.getUserId().equals(userId)) {
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

		if (canView == false) {
			// No user to check against and trying to get a
			// non-published book. That's a no go
			throw new Exception("You don't have rights to view this book.");
		}

		// Find the cover and the category
		BookCover cover = holder.getCoverByName(book.getCoverName());
		BookCategory cat = holder.getCategoryByName(book.getCategory());

		// Get a list of Bookassetdescriptions
		List<Bookassetdescription> baResult = getBookassetdescriptionByBookId(
				bookId, ooDbTx);

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

		BookDisplay bookDisplay = new BookDisplay(book, cat, cover, authorName,
				retBaResults);
		
		// Logo?
		String logoFileName = book.getLogoFileName();
		bookDisplay
				.setBookLogo((logoFileName != null && logoFileName
						.length() > 0) ? true : false);

		// Return the display
		return bookDisplay;
	}

	public static Book getBookById(String bookId, OObjectDatabaseTx ooDbTx)
			throws Exception {
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

	public static List<Bookassetdescription> getBookassetdescriptionByBookId(
			String bookId, OObjectDatabaseTx ooDbTx) throws Exception {
		OSQLSynchQuery<Bookassetdescription> baQuery = new OSQLSynchQuery<Bookassetdescription>(
				"select from Bookassetdescription where bookId = :bId");
		HashMap<String, String> baparams = new HashMap<String, String>();
		baparams.put("bId", bookId);
		List<Bookassetdescription> baResult = ooDbTx.command(baQuery).execute(
				baparams);
		return baResult;
	}

	public static Set<UserBookRelationshipEnum> getActiveBookRealtionshipForUser(
			String userId, boolean loggedIn, String bookId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		Set<UserBookRelationshipEnum> rels = new TreeSet<UserBookRelationshipEnum>();

		// relationship
		if (userId != null && userId.length() > 0 && loggedIn == true) {
			OSQLSynchQuery<UserBookRelationship> bQuery = new OSQLSynchQuery<UserBookRelationship>(
					"select from UserBookRelationship where userId = :uId and bookId = :bId and activeRelationship = true");
			HashMap<String, String> bparams = new HashMap<String, String>();
			bparams.put("uId", userId);
			bparams.put("bId", bookId);
			List<UserBookRelationship> bResult = ooDbTx.command(bQuery)
					.execute(bparams);

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

}
