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

import static com.google.gwt.query.client.GQuery.$;
import static gwtquery.plugins.ratings.client.Ratings.Ratings;

import java.util.List;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.utils.Utils;
import com.pronoiahealth.olhie.client.widgets.rating.StarRating;

/**
 * BookList3D.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 29, 2013
 * 
 */
public class BookList3D extends Widget {
	private BookSelectCallBack bookSelectCallBack;

	/**
	 * Some components that use this class will be ApplicationScoped. We don't
	 * want to do a re-bind of gQuery events on every call to this methods
	 * onLoad handler. We only want to do it once.
	 */
	private boolean hasBeenAttached = false;

	public BookList3D(List<BookDisplay> books,
			BookSelectCallBack bookSelectCallBack) {
		super();
		this.bookSelectCallBack = bookSelectCallBack;

		// bookLst = new ArrayList<DivElement>();
		Document doc = Document.get();
		UListElement ulElem = doc.createULElement();

		// Create the booklist element
		this.setElement(ulElem);
		ulElem.setId("bk-list");
		ulElem.setClassName("bk-list clearfix");
		int cnt = 0;
		for (BookDisplay bookDisplay : books) {
			cnt++;
			Book book = bookDisplay.getBook();
			String bookCoverUrl = bookDisplay.getBookCover().getImgUrl();
			String catColor = bookDisplay.getBookCategory().getColor();
			String catColorTxt = bookDisplay.getBookCategory().getTextColor();

			// Add the list element
			LIElement li = doc.createLIElement();
			ulElem.appendChild(li);

			// Add the main book div
			String bookId = book.getId();
			DivElement bookDiv = doc.createDivElement();
			li.appendChild(bookDiv);
			bookDiv.setClassName("bk-book book-1 bk-bookdefault");
			// Set the book id
			bookDiv.setAttribute("bookId", bookId);

			// Add the front and cover
			DivElement bookFrontDiv = doc.createDivElement();
			bookDiv.appendChild(bookFrontDiv);
			bookFrontDiv.setClassName("bk-front");

			// Now the cover
			DivElement bookCoverDiv = doc.createDivElement();
			bookFrontDiv.appendChild(bookCoverDiv);
			bookCoverDiv.setClassName("bk-cover");
			// Set the book cover image
			bookCoverDiv.setAttribute("style", "background-image: url('"
					+ bookCoverUrl + "'); overflow:auto;");
			// Set the logo
			DivElement bookCoverlogoDiv = doc.createDivElement();
			bookCoverDiv.appendChild(bookCoverlogoDiv);
			bookCoverlogoDiv
					.setAttribute("style",
							"padding-top: 20px; padding-left: 25px; background: transparent;");

			// Logo image
			ImageElement bookCoverLogoImageElement = doc.createImageElement();
			bookCoverlogoDiv.appendChild(bookCoverLogoImageElement);
			if (bookDisplay.isBookLogo() == true) {
				bookCoverLogoImageElement.setSrc(Utils
						.buildRestServiceForLogoDownloadLink(bookId));
			}
			bookCoverLogoImageElement.setAttribute("style",
					"max-height: 100px; max-width: 200px;");

			// Heading element for Author and title
			HeadingElement h2AuthorTitleElement = doc.createHElement(2);
			bookCoverDiv.appendChild(h2AuthorTitleElement);
			// Author
			SpanElement authorSpan = doc.createSpanElement();
			h2AuthorTitleElement.appendChild(authorSpan);
			authorSpan.setInnerText(bookDisplay.getAuthorFullName());
			// Title
			SpanElement titleSpan = doc.createSpanElement();
			h2AuthorTitleElement.appendChild(titleSpan);
			String bookTitle = book.getBookTitle();
			int titleLen = bookTitle.length();
			bookTitle = titleLen > 30 ? bookTitle.substring(0, 30) : bookTitle;
			titleSpan.setInnerText(bookTitle);

			// Back cover
			DivElement bookBackCoverkDiv = doc.createDivElement();
			bookFrontDiv.appendChild(bookBackCoverkDiv);
			bookBackCoverkDiv.setClassName("bk-cover-back");
			bookBackCoverkDiv.setAttribute("style", "background-color: "
					+ catColor + ";");

			// Add the pages to the book
			DivElement bookPages = doc.createDivElement();
			bookDiv.appendChild(bookPages);
			bookPages.addClassName("bk-page");
			boolean bookCurrentPageSet = false;
			List<Bookassetdescription> descs = bookDisplay
					.getBookAssetDescriptions();
			if (descs != null && descs.size() > 0) {
				for (Bookassetdescription desc : descs) {
					DivElement bookContentkDiv = doc.createDivElement();
					bookPages.appendChild(bookContentkDiv);
					if (bookCurrentPageSet == false) {
						bookContentkDiv
								.setClassName("bk-content bk-content-current");
					} else {
						bookContentkDiv.setClassName("bk-content");
					}
					ParagraphElement pageContentElem = doc.createPElement();
					bookContentkDiv.appendChild(pageContentElem);
					pageContentElem.setInnerText(desc.getDescription());
				}
			}

			// Back of book now
			DivElement bookBackElem = doc.createDivElement();
			bookDiv.appendChild(bookBackElem);
			bookBackElem.setClassName("bk-back");
			bookBackElem.setAttribute("style", "background-image: url('"
					+ bookCoverUrl + "'); overflow:auto;");
			ParagraphElement bookIntroElem = doc.createPElement();
			bookBackElem.appendChild(bookIntroElem);
			bookIntroElem.setInnerText(book.getIntroduction());

			// Right of book
			DivElement bookRightElem = doc.createDivElement();
			bookDiv.appendChild(bookRightElem);
			bookRightElem.addClassName("bk-right");

			// Left of book (binding)
			DivElement bookLeftElem = doc.createDivElement();
			bookDiv.appendChild(bookLeftElem);
			bookLeftElem.addClassName("bk-left");
			bookLeftElem.setAttribute("style", "background-color: " + catColor
					+ ";");
			HeadingElement h2AuthorTitleLeftElement = doc.createHElement(2);
			bookLeftElem.appendChild(h2AuthorTitleLeftElement);
			h2AuthorTitleLeftElement.setAttribute("style", "color: "
					+ catColorTxt + ";");
			// Author
			SpanElement authorLeftSpan = doc.createSpanElement();
			h2AuthorTitleLeftElement.appendChild(authorLeftSpan);
			authorLeftSpan.setInnerText(book.getAuthorId());
			// Title
			SpanElement titleLeftSpan = doc.createSpanElement();
			h2AuthorTitleLeftElement.appendChild(titleLeftSpan);
			titleLeftSpan.setInnerText(bookTitle);

			// Book top
			DivElement bookTopElem = doc.createDivElement();
			bookDiv.appendChild(bookTopElem);
			bookTopElem.addClassName("bk-top");

			// Book top
			DivElement bookBottomElem = doc.createDivElement();
			bookDiv.appendChild(bookBottomElem);
			bookBottomElem.addClassName("bk-bottom");

			// Book info section, this gets added to the list item element
			DivElement bookInfoElem = doc.createDivElement();
			li.appendChild(bookInfoElem);
			bookInfoElem.addClassName("bk-info");
			// Flip button
			ButtonElement flipButtonElem = doc.createPushButtonElement();
			bookInfoElem.appendChild(flipButtonElem);
			flipButtonElem.addClassName("bk-bookback");
			flipButtonElem.setInnerText("Back Cover");
			// Look inside button
			ButtonElement lookInsideButtonElem = doc.createPushButtonElement();
			bookInfoElem.appendChild(lookInsideButtonElem);
			lookInsideButtonElem.addClassName("bk-bookview");
			lookInsideButtonElem.setInnerText("Look Inside");

			// Star rating
			DivElement rateContainerElem = doc.createDivElement();
			bookInfoElem.appendChild(rateContainerElem);
			rateContainerElem.setAttribute("style", "margin-top: 15px;");
			DivElement rateContainerTextElem = doc.createDivElement();
			rateContainerElem.appendChild(rateContainerTextElem);
			rateContainerTextElem.setInnerHTML("<b>Rating: </b>");
			rateContainerTextElem.setAttribute("style",
					"float: left; padding-right: 10px;");
			DivElement ratingStarContainerElem = doc.createDivElement();
			rateContainerElem.appendChild(ratingStarContainerElem);
			StarRating sr = new StarRating(5, true);
			sr.setRating(4);
			Element starElement = sr.getElement();
			starElement.setAttribute("style", "display: inline-block;");
			ratingStarContainerElem.appendChild(starElement);

			/*
			 * // Put the rating in here for (int i = 0; i < 5; i++) {
			 * InputElement cInputElem = doc
			 * .createRadioInputElement("starRating" + cnt);
			 * ratingStarContainerElem.appendChild(cInputElem);
			 * cInputElem.setClassName("star"); cInputElem.setAttribute("style",
			 * ""); cInputElem.setAttribute("disabled", "disabled");
			 * cInputElem.setValue("" + (i+1)); if (i == 2) {
			 * cInputElem.setAttribute("checked", "checked"); } }
			 */
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		if (this.hasBeenAttached == false) {
			final GQuery books = $("#bk-list > li > div.bk-book");
			final int booksCount = books.length();
			books.each(new Function() {
				@Override
				public void f(Element e) {
					final GQuery book = $(e);
					final GQuery other = books.not(book);
					final GQuery parent = book.parent();
					final GQuery ratings = parent.children("div.bk-info").find(
							"input.star");
					final GQuery page = book.children("div.bk-page");
					final GQuery bookview = parent.find("button.bk-bookview");
					final GQuery content = page.children("div.bk-content");
					final IntHolder current = new IntHolder();

					// ratings.as(Ratings).rate();

					// Bind the call back
					book.bind(Event.ONCLICK, new Function() {
						@Override
						public boolean f(Event e) {
							String bookId = book.attr("bookId");
							bookSelectCallBack.onBookSelect(bookId);
							return true;
						}
					});

					GQuery flipAction = parent.find("button.bk-bookback");
					flipAction.bind(Event.ONCLICK, new Function() {
						@Override
						public boolean f(Event e) {
							bookview.removeClass("bk-active");
							boolean flipVal = false;
							Object flipObj = book.data("flip");
							if (flipObj != null) {
								flipVal = (Boolean) flipObj;
							}
							if (flipVal == true) {
								book.data("opened", false).data("flip", false);
								book.removeClass("bk-viewback");
								book.addClass("bk-bookdefault");
							} else {
								book.data("opened", false).data("flip", true);
								book.removeClass("bk-viewinside").removeClass(
										"bk-bookdefault");
								book.addClass("bk-viewback");
							}
							return true;
						}
					});

					bookview.bind(Event.ONCLICK, new Function() {
						@Override
						public boolean f(Event e) {
							GQuery thisPt = $(e);
							other.data("opened", false);
							other.removeClass("bk-viewinside");
							GQuery otherParent = other.parent().css(
									CSS.ZINDEX.with(0));
							otherParent.find("button.bk-bookview").removeClass(
									"bk-active");

							if (!other.hasClass("bk-viewback")) {
								other.addClass("bk-bookdefault");
							}

							boolean openedVal = false;
							Object openedObj = book.data("opened");
							if (openedObj != null) {
								openedVal = (Boolean) openedObj;
							}
							if (openedVal == true) {
								thisPt.removeClass("bk-active");
								book.data("opened", false).data("flip", false);
								book.removeClass("bk-viewinside");
								book.addClass("bk-bookdefault");
							} else {
								thisPt.addClass("bk-active");
								book.data("opened", true).data("flip", false);
								book.removeClass("bk-viewback").removeClass(
										"bk-bookdefault");
								book.addClass("bk-viewinside");
								parent.css(CSS.ZINDEX.with(booksCount));
								current.setIntVal(0);
								content.removeClass("bk-content-current")
										.eq(current.getIntVal())
										.addClass("bk-content-current");
							}
							return true;
						}
					});

					if (content.length() > 1) {
						GQuery navPrev = $("<span class=\"bk-page-prev\">&lt;</span>");
						GQuery navNext = $("<span class=\"bk-page-next\">&gt;</span>");
						page.append($("<nav></nav>").append(navPrev).append(
								navNext));

						navPrev.bind(Event.ONCLICK, new Function() {
							@Override
							public boolean f(Event e) {
								if (current.getIntVal() > 0) {
									current.subOne();
									content.removeClass("bk-content-current")
											.eq(current.getIntVal())
											.addClass("bk-content-current");
								}
								return false;
							}
						});

						navNext.bind(Event.ONCLICK, new Function() {
							@Override
							public boolean f(Event e) {
								if (current.getIntVal() < content.length() - 1) {
									current.plusOne();
									content.removeClass("bk-content-current")
											.eq(current.getIntVal())
											.addClass("bk-content-current");
								}
								return false;
							}
						});
					}
				}
			});

			// Did it the first time mark it as done
			this.hasBeenAttached = true;
		}
	}
}
