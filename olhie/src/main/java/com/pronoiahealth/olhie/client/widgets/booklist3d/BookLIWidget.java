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
package com.pronoiahealth.olhie.client.widgets.booklist3d;

import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.pronoiahealth.olhie.client.shared.constants.BookImageSizeEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.utils.Utils;
import com.pronoiahealth.olhie.client.widgets.rating.StarRating;

/**
 * BookListItemWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 30, 2013
 * 
 */
public class BookLIWidget extends ComplexPanel {
	private LIElement root;
	private DivElement bkBook;
	private DivElement bkCoverBack;
	private DivElement bookFrontCover;
	private AnchorElement myCollectionBtnIndicator;
	private Element myCollectionBtnIndicatorIcon;
	private DivElement bkPage;
	private DivElement bkBack;
	private DivElement bkLeft;
	private ParagraphElement bookDescription;
	private HeadingElement bookBinding;
	private SpanElement bookBindingAuthor;
	private SpanElement bookBindingTitle;
	private DivElement ratingHolder;
	private DivElement bookHoursHolder;
	
	
	private String bookId;
	private String bookTitle;
	
	private TOCDivWidget tocWidget;
	
	private static final DateTimeFormat dtf = DateTimeFormat
			.getFormat("MM/dd/yyyy");
	
	/**
	 * Constructor
	 * 
	 */
	public BookLIWidget() {
		// Create the tocWidget
		tocWidget = new TOCDivWidget();
		
		// Create the root
		Document doc = Document.get();
		root = doc.createLIElement();
		setElement(root);

		// Book id div
		bkBook = createDivWithClassStyle(doc,
				"bk-book book-1 bk-bookdefault", null);
		root.appendChild(bkBook);

		// Book front
		DivElement bookFront = createDivWithClassStyle(doc, "bk-front", null);
		bkBook.appendChild(bookFront);

		// Front Cover
		bookFrontCover = createDivWithClassStyle(doc, "bk-cover", null);
		bookFront.appendChild(bookFrontCover);

		// MyCollection Indicator
		DivElement cd1 = createDivWithClassStyle(doc, null,
				"float: right; margin: 10px;");
		bookFrontCover.appendChild(cd1);
		DivElement cd2 = createDivWithClassStyle(doc, null, null);
		cd1.appendChild(cd2);
		myCollectionBtnIndicator = doc.createAnchorElement();
		cd2.appendChild(myCollectionBtnIndicator);
		myCollectionBtnIndicatorIcon = doc.createElement("i");
		myCollectionBtnIndicator.appendChild(myCollectionBtnIndicatorIcon);

		// BK Cover Back
		bkCoverBack = this.createDivWithClassStyle(doc,
				"bk-cover-back", "background-color: #FFFF00;");
		bookFront.appendChild(bkCoverBack);

		// Book Page
		bkPage = createDivWithClassStyle(doc, "bk-page", null);
		bkBook.appendChild(bkPage);

		// Book Back
		bkBack = this.createDivWithClassStyle(doc, "bk-back", null);
		bkBook.appendChild(bkBack);
		bookDescription = doc.createPElement();
		bkBack.appendChild(bookDescription);

		// Book Right
		DivElement bkRight = createDivWithClassStyle(doc, "bk-right", null);
		bkBook.appendChild(bkRight);

		// Left
		bkLeft = createDivWithClassStyle(doc, "bk-left",
				"background-color: #FFFF00;");
		bkBook.appendChild(bkLeft);
		bookBinding = doc.createHElement(2);
		bkLeft.appendChild(bookBinding);
		bookBindingAuthor = doc.createSpanElement();
		bookBinding.appendChild(bookBindingAuthor);
		bookBindingTitle = doc.createSpanElement();
		bookBinding.appendChild(bookBindingTitle);

		// Book top and botton
		bkBook.appendChild(createDivWithClassStyle(doc, "bk-top", null));
		bkBook.appendChild(createDivWithClassStyle(doc, "bk-bottom", null));

		// Book info
		DivElement bkInfo = createDivWithClassStyle(doc, "bk-info", null);
		root.appendChild(bkInfo);

		// Back button
		ButtonElement bkBookback = doc.createButtonElement();
		bkBookback.setClassName("bk-bookback");
		bkInfo.appendChild(bkBookback);

		// Front button
		ButtonElement bkBookview = doc.createButtonElement();
		bkBookview.setClassName("bk-bookview");
		bkInfo.appendChild(bkBookview);

		// Rating
		DivElement rh = createDivWithClassStyle(doc, null, "margin-top: 15px;");
		bkInfo.appendChild(rh);
		DivElement rh1 = createDivWithClassStyle(doc, null,
				"float: left; padding-right: 10px;");
		rh.appendChild(rh1);
		Element rh11 = doc.createElement("b");
		rh1.appendChild(rh11);
		rh11.setInnerText("Rating:");

		// Holder for rating
		ratingHolder = doc.createDivElement();
		rh.appendChild(ratingHolder);

		// Book hours
		DivElement bh = createDivWithClassStyle(doc, null, "margin-top: 15px;");
		bkInfo.appendChild(bh);
		DivElement bh1 = createDivWithClassStyle(doc, null,
				"float: left; padding-right: 10px;");
		bh.appendChild(bh1);
		Element bh11 = doc.createElement("b");
		bh1.appendChild(bh11);
		bh11.setInnerText("Book Hours:");

		// Holder for hours
		bookHoursHolder = doc.createDivElement();
		bh.appendChild(bookHoursHolder);
	}

	/**
	 * Return the uniques bookId;
	 */
	protected String build(BookDisplay bookDisplay, boolean clientLoggedIn, Map<String, String> fileViewableContent) {
		Book book = bookDisplay.getBook();
		String catColor = bookDisplay.getBookCategory().getColor();
		String catColorTxt = bookDisplay.getBookCategory().getTextColor();
		int rating = bookDisplay.getBookRating();
		this.bookId = bookDisplay.getBook().getId();
		this.bookTitle = bookDisplay.getBook().getBookTitle();

		// Set properties
		// Id
		bkBook.setAttribute("bookid", bookId);

		// Front cover
		bookFrontCover
				.setAttribute(
						"style",
						"background-image: url('"
								+ Utils.buildRestServiceForBookFrontCoverDownloadLink(
										bookId, BookImageSizeEnum.LARGE)
								+ "'); background-repeat: no-repeat; background-size: contain; overflow: auto;");

		// My Collection Button
		// Only display buttons if the client is logged in
		Set<UserBookRelationshipEnum> rels = bookDisplay.getRelEnums();
		if (clientLoggedIn == true) {
			// Set up collection button
			if (rels.contains(UserBookRelationshipEnum.MYCOLLECTION)) {
				setAddToMyCollectionBtn(true);
				tocWidget.setRemoveFromMyCollectionBtn(false);
				tocWidget.setShowCommentRatingBtn();
			} else if (rels.contains(UserBookRelationshipEnum.COAUTHOR)
					|| rels.contains(UserBookRelationshipEnum.CREATOR)) {
				setHideMyCollectionBtn();
				tocWidget.setHideMyCollectionBtn();
				tocWidget.setHideCommentRatingBtn();
			} else {
				setHideMyCollectionBtn();
				tocWidget.setAddToMyCollectionBtn(false);
				tocWidget.setShowCommentRatingBtn();
			}
		} else {
			setHideMyCollectionBtn();
			tocWidget.setHideMyCollectionBtn();
			tocWidget.setHideCommentRatingBtn();
		}

		// Back cover of front cover
		bkCoverBack.setAttribute("style", "background-color: "
				+ catColor + ";");

		// Create TOC and iterate over book asset descriptions
		// bookPage.appendChild(tocWidget.getElement());
		bkPage.appendChild(tocWidget.getElement());
		int counter = 1;
		for (Bookassetdescription bookAssetDesc : bookDisplay
				.getBookAssetDescriptions()) {
			String desc = bookAssetDesc.getDescription();
			BookContentDivWidget bContent = new BookContentDivWidget();
			Bookasset currentAsset = bookAssetDesc.getBookAssets().get(0);
			String contentType = currentAsset.getContentType();
			String itemType = currentAsset.getItemType();
			String currentAssetId = currentAsset.getId();
			String linkRef = currentAsset.getLinkRef();

			// Can this content be viewed in an iFrame?
			// If its in the viewable content map it can
			String val = fileViewableContent.get(contentType);
			String addedDate = dtf.format(currentAsset.getCreatedDate());
			int hrsWork = currentAsset.getHoursOfWork();
			String hrsStr = hrsWork == 0 ? "Hours not available" : "" + hrsWork
					+ " hrs";
			bContent.setDescription(desc, addedDate, hrsStr, val == null ? true
					: false, currentAssetId, val == null ? "" : val, itemType,
					linkRef);
			// bookPage.appendChild(bContent.getElement());
			bkPage.appendChild(bContent.getElement());
			tocWidget.addItem(counter++, desc, hrsStr, bookDisplay
					.bookassetdescriptionInSrchResults(bookAssetDesc.getId()));
		}

		// Back cover
		bkBack
				.setAttribute(
						"style",
						"background-image: url('"
								+ Utils.buildRestServiceForBookBackCoverDownloadLink(bookId)
								+ "'); background-repeat: no-repeat; background-size: contain; overflow: auto;");
		bookDescription.setInnerText(book.getIntroduction());

		// Book left
		bkLeft.setAttribute("style", "background-color: " + catColor + ";");
		bookBinding.setAttribute("style", "color: " + catColorTxt + ";");
		bookBindingAuthor.setInnerText(book.getAuthorId());
		bookBindingTitle.setInnerText(book.getBookTitle());

		// Star rating
		StarRating sr = new StarRating(5, true);
		sr.setRating(rating);
		Element srElem = sr.getElement();
		srElem.setAttribute("style", "display: inline-block;");
		ratingHolder.appendChild(srElem);

		// Book hours of work
		bookHoursHolder.setInnerText("" + bookDisplay.getBookHoursOfWork());

		// Return the bookId;
		return bookId;
	}
	
	/**
	 * @return
	 */
	public TOCDivWidget getTocWidget() {
		return this.tocWidget;
	}

	/**
	 * @return
	 */
	public String getBookTitle() {
		return this.bookTitle;
	}

	/**
	 * @param btnId
	 */
	public void setMyCollectionBtnId(String btnId) {
		myCollectionBtnIndicator.setId(btnId + "_myCollectionBtnId");
	}
	
	/**
	 * @param displayOnly
	 */
	public void setAfterRemoveFromMyCollection() {
		// Hide button on cover
		setHideMyCollectionBtn();
		
		// Set TOC collection button the add mode
		tocWidget.setAddToMyCollectionBtn(true);
	}
	
	public void setAfterAddToMyCollection() {
		// Show thumbs up button on cover
		setAddToMyCollectionBtn(false);
		
		// Set TOC collection button to remove mode
		tocWidget.setRemoveFromMyCollectionBtn(true);
	}

	/**
	 * @param displayOnly
	 */
	public void setAddToMyCollectionBtn(boolean displayOnly) {
		// Set the icon on the front of the book
		myCollectionBtnIndicator.removeAttribute("style");
		myCollectionBtnIndicator
				.setClassName("btn btn-mini btn-success bk-tocPage-myCollectionsBtn");
		myCollectionBtnIndicatorIcon.setClassName("icon-thumbs-up");
	}

	/**
	 * @param displayOnly
	 */
	public void setRemoveFromMyCollectionBtn(boolean displayOnly) {
		// Set the icon on the fron of the book
		myCollectionBtnIndicator.removeAttribute("style");
		myCollectionBtnIndicator
				.setClassName("btn btn-mini btn-danger bk-tocPage-myCollectionsBtn");
		myCollectionBtnIndicatorIcon.setClassName("icon-thumbs-down");
	}

	/**
	 * 
	 */
	public void setHideMyCollectionBtn() {
		myCollectionBtnIndicator.setAttribute("style", "display: none;");
	}

	/**
	 * @param doc
	 * @param className
	 * @param styleName
	 * @return
	 */
	private DivElement createDivWithClassStyle(Document doc, String className,
			String styleName) {
		DivElement el = doc.createDivElement();
		if (className != null && className.length() > 0) {
			el.setClassName(className);
		}

		if (styleName != null && styleName.length() > 0) {
			el.setAttribute("style", styleName);
		}
		return el;
	}
}
