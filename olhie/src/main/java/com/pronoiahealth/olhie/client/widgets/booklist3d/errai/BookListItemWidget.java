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
package com.pronoiahealth.olhie.client.widgets.booklist3d.errai;

import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.clientfactories.FileTypeViewableContent;
import com.pronoiahealth.olhie.client.shared.constants.BookImageSizeEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
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
@Dependent
@Templated("BookListItemWidget.html#bookListItem")
public class BookListItemWidget extends Composite {

	private static final DateTimeFormat dtf = DateTimeFormat
			.getFormat("MM/dd/yyyy");

	@Inject
	private ClientUserToken clientToken;

	@DataField("bookIdFld")
	private Element bookIdFld = DOM.createDiv();

	@DataField("bookFrontCover")
	private Element bookFrontCover = DOM.createDiv();

	@Inject
	@DataField("myCollectionIndicator")
	private MyCollectionButtonWidget myCollectionIndicator;

	@DataField("bookCoverBackColor")
	private Element bookCoverBackColor = DOM.createDiv();

	@DataField("bookPage")
	// private Element bookPage = DOM.createDiv();
	private HTMLPanel bookPage = new HTMLPanel("");;

	@DataField("bookBackCover")
	private Element bookBackCover = DOM.createDiv();

	@DataField("bookBackCoverDescription")
	private Element bookBackCoverDescription = DOM.createElement("p");

	@DataField("bookLeft")
	private Element bookLeft = DOM.createDiv();

	@DataField("bookLeftColor")
	private Element bookLeftColor = DOM.createElement("h2");

	@DataField("bookLeftAuthorId")
	private Element bookLeftAuthorId = DOM.createSpan();

	@DataField("bookLeftBookTitle")
	private Element bookLeftBookTitle = DOM.createSpan();

	@DataField("ratingContainer")
	private Element ratingContainer = DOM.createDiv();

	@DataField("bookHoursOfWork")
	private Element bookHoursOfWork = DOM.createDiv();

	@Inject
	private Instance<BookContentWidget> bookContentInstance;

	@Inject
	private Disposer<BookContentWidget> bookContentInstanceDisposer;

	@Inject
	private TOCWidget tocWidget;

	@Inject
	@FileTypeViewableContent
	private Map<String, String> fileViewableContent;

	private String bookId;

	private String bookTitle;

	/**
	 * Constructor
	 * 
	 */
	public BookListItemWidget() {
	}

	/**
	 * Dispose of BookContentWidget instance that where created
	 */
	@PreDestroy
	private void preDestroy() {
		if (bookPage != null && bookPage.getWidgetCount() > 0) {
			int cnt = bookPage.getWidgetCount();
			for (int i = 0; i < cnt; i++) {
				Widget w =  bookPage.getWidget(0);
				if (w != null && w instanceof BookContentWidget) {
					bookContentInstanceDisposer.dispose((BookContentWidget) w);
				}
				
				// Remove the widget
				bookPage.remove(w);
			}
		}
	}

	/**
	 * Return the uniques bookId;
	 */
	protected String build(BookDisplay bookDisplay) {
		Book book = bookDisplay.getBook();
		String catColor = bookDisplay.getBookCategory().getColor();
		String catColorTxt = bookDisplay.getBookCategory().getTextColor();
		int rating = bookDisplay.getBookRating();
		this.bookId = bookDisplay.getBook().getId();
		this.bookTitle = bookDisplay.getBook().getBookTitle();

		// Set properties
		// Id
		bookIdFld.setAttribute("bookid", bookId);

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
		if (clientToken.isLoggedIn() == true) {
			// Set up collection button
			if (rels.contains(UserBookRelationshipEnum.MYCOLLECTION)) {
				myCollectionIndicator.setAddToMyCollectionBtn(true);
				tocWidget.setMyCollectionBtnRemoveFromCollection(false);
				tocWidget.setCommentRatingBtnShow();
			} else if (rels.contains(UserBookRelationshipEnum.COAUTHOR)
					|| rels.contains(UserBookRelationshipEnum.CREATOR)) {
				myCollectionIndicator.setHideMyCollectionBtn();
				tocWidget.setMyCollectionBtnHide();
				tocWidget.setCommentRatingBtnHide();
			} else {
				myCollectionIndicator.setHideMyCollectionBtn();
				tocWidget.setMyCollectionBtnAddToCollection(false);
				tocWidget.setCommentRatingBtnShow();
			}
		} else {
			myCollectionIndicator.setHideMyCollectionBtn();
			tocWidget.setMyCollectionBtnHide();
			tocWidget.setCommentRatingBtnHide();
		}

		// Back cover of front cover
		bookCoverBackColor.setAttribute("style", "background-color: "
				+ catColor + ";");

		// Create TOC and iterate over book asset descriptions
		// bookPage.appendChild(tocWidget.getElement());
		bookPage.add(tocWidget);
		int counter = 1;
		for (Bookassetdescription bookAssetDesc : bookDisplay
				.getBookAssetDescriptions()) {
			String desc = bookAssetDesc.getDescription();
			BookContentWidget bContent = bookContentInstance.get();
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
			bookPage.add(bContent);
			tocWidget.addItem(counter++, desc, hrsStr, bookDisplay
					.bookassetdescriptionInSrchResults(bookAssetDesc.getId()));
		}

		// Back cover
		bookBackCover
				.setAttribute(
						"style",
						"background-image: url('"
								+ Utils.buildRestServiceForBookBackCoverDownloadLink(bookId)
								+ "'); background-repeat: no-repeat; background-size: contain; overflow: auto;");
		bookBackCoverDescription.setInnerText(book.getIntroduction());

		// Book left
		bookLeft.setAttribute("style", "background-color: " + catColor + ";");
		bookLeftColor.setAttribute("style", "color: " + catColorTxt + ";");
		bookLeftAuthorId.setInnerText(book.getAuthorId());
		bookLeftBookTitle.setInnerText(book.getBookTitle());

		// Star rating
		StarRating sr = new StarRating(5, true);
		sr.setRating(rating);
		Element srElem = sr.getElement();
		srElem.setAttribute("style", "display: inline-block;");
		ratingContainer.appendChild(srElem);

		// Book hours of work
		bookHoursOfWork.setInnerText("" + bookDisplay.getBookHoursOfWork());

		// Return the bookId;
		return bookId;
	}

	public TOCWidget getTocWidget() {
		return this.tocWidget;
	}

	public MyCollectionButtonWidget getMyCollectionIndicator() {
		return this.myCollectionIndicator;
	}

	public String getBookTitle() {
		return this.bookTitle;
	}
}
