package com.pronoiahealth.olhie.client.widgets.booklist3d;

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
import static com.arcbees.gquery.tooltip.client.Tooltip.Tooltip;
import static com.google.gwt.query.client.GQuery.$;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.core.client.Duration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.pronoiahealth.olhie.client.clientfactories.FileTypeViewableContent;
import com.pronoiahealth.olhie.client.shared.events.book.CheckBookIsAuthorRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.AddBookToMyCollectionEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.AddBookToMyCollectionEvent.ADD_RESPONSE_TYPE;
import com.pronoiahealth.olhie.client.shared.events.bookcase.RemoveBookFromMyCollectionEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.RemoveBookFromMyCollectionEvent.REMOVE_RESPONSE_TYPE;
import com.pronoiahealth.olhie.client.shared.events.local.DownloadBookAssetEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddBookCommentModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowViewBookassetDialogEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;

/**
 * BookList3D_3.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 17, 2013
 * 
 */
@Dependent
@Templated("#bookLstRoot")
public class BookList3D extends Composite {
	private int currentBookCnt;
	
	@Inject
	private ClientUserToken clientUserToken;

	@DataField("bookList")
	private Element bookList = DOM.createElement("ul");
	
	@Inject
	@FileTypeViewableContent
	private Map<String, String> fileViewableContent;

	@Inject
	private javax.enterprise.event.Event<DownloadBookAssetEvent> downloadBookAssetEvent;

	@Inject
	private javax.enterprise.event.Event<ShowViewBookassetDialogEvent> showViewBookassetDialogEvent;

	@Inject
	private javax.enterprise.event.Event<AddBookToMyCollectionEvent> addBookToMyCollectionEvent;

	@Inject
	private javax.enterprise.event.Event<RemoveBookFromMyCollectionEvent> removeBookFromMyCollectionEvent;

	@Inject
	private javax.enterprise.event.Event<ShowAddBookCommentModalEvent> showAddBookCommentModalEvent;

	@Inject
	private javax.enterprise.event.Event<CheckBookIsAuthorRequestEvent> checkBookIsAuthorRequestEvent;

	private GQuery books;

	private Map<String, BookLIWidget> bliwMap;

	/**
	 * Constructor
	 * 
	 */
	public BookList3D() {
	}

	/**
	 * Attach required elements
	 */
	@PostConstruct
	private void postConstruct() {
		bliwMap = new HashMap<String, BookLIWidget>();
	}

	/**
	 * Clean up objects create through instance factory
	 */
	@PreDestroy
	private void preDestroy() {
		removeEventsFromLst();
		bliwMap.clear();
		clearPhysicalBookList();
	}

	/**
	 * Create the list of books
	 * 
	 * @param books
	 * @param appendToCurrent
	 *            - If true append to the current books which will cause the
	 *            jQuery attached events to be removed first, the books added to
	 *            the current list and then the events attached again. If false,
	 *            a new list will be created and the old one will be destroyed
	 *            first.
	 */
	public void build(List<BookDisplay> books, boolean appendToCurrent) {
		if (books != null && books.size() > 0) {
			removeEventsFromLst();
			if (appendToCurrent == false) {
				bliwMap.clear();
				clearPhysicalBookList();
			}

			// Add the list for books
			for (BookDisplay book : books) {
				BookLIWidget bliw = new BookLIWidget();
				String bookId = bliw.build(book, clientUserToken.isLoggedIn(), fileViewableContent);
				bookList.appendChild(bliw.getElement());
				bliwMap.put(bookId, bliw);
			}
			attachEventsToLst();
		}
	}

	/**
	 * Removes the BookListItemWidget elements from the DOM
	 */
	private void clearPhysicalBookList() {
		if (bookList != null) {
			while (bookList.hasChildNodes()) {
				bookList.removeChild(bookList.getFirstChild());
			}
		}
	}

	/**
	 * Respond to the add or remove from my collection button event. The fired
	 * event will inform the handling service to respond with a Add or Remove
	 * from my collection response vent which is listened for in this class.
	 * 
	 * @param bookId
	 * @param AddToCollection
	 */
	private void adjustMyCollection(String bookId, boolean addToCollection) {
		if (addToCollection == true) {
			addBookToMyCollectionEvent.fire(new AddBookToMyCollectionEvent(
					bookId, ADD_RESPONSE_TYPE.ADD_RESPONSE));
		} else {
			removeBookFromMyCollectionEvent
					.fire(new RemoveBookFromMyCollectionEvent(bookId,
							REMOVE_RESPONSE_TYPE.REMOVE_RESPONSE));
		}
	}

	/**
	 * Add or update book comments by calling the AddCommentRatingDialog
	 * 
	 * @param bookId
	 */
	private void addUpdateComment(String bookId) {
		BookLIWidget bookListItemWidget = bliwMap.get(bookId);
		String bookTitle = bookListItemWidget.getBookTitle();
		showAddBookCommentModalEvent.fire(new ShowAddBookCommentModalEvent(
				bookId, bookTitle, true));
	}

	/**
	 * Used to add books in after the initial display
	 * 
	 * @param bookDiv
	 */
	public void attachEventsToLst() {
		this.books = $("#bk-list > li > div.bk-book",
				bookList.getParentElement());
		this.currentBookCnt = books.length();

		books.each(new Function() {
			@Override
			public void f(Element e) {
				final GQuery book = $(e);
				final GQuery other = books.not(book);
				final GQuery parent = book.parent();
				final GQuery page = book.children("div.bk-page");
				final GQuery bookview = parent.find("button.bk-bookview");
				final GQuery flipAction = parent.find("button.bk-bookback");
				final GQuery content = page.children("div.bk-content");
				final GQuery toc = page.find("div.bk-toc");
				final GQuery tocPageMyCollectionsBtn = toc
						.find("a.bk-tocPage-myCollectionsBtn");
				final GQuery tocPageCommentRatingBtn = toc
						.find("a.bk-tocPage-commentRatingBtn");
				final GQuery tocItems = page.find("div.bk-toc-item");
				final GQuery tocLinks = page.find("div.bk-toc-link");
				final GQuery downloadContentBtns = page
						.find("a.bk-download-btn");
				final GQuery viewContentBtns = page.find("a.bk-view-btn");
				final GQuery linkContentBtns = page.find("a.bk-link-btn");
				final IntHolder current = new IntHolder();

				// Book Id
				final String bookId = book.attr("bookId");

				// Bind the call back
				book.bind(Event.ONCLICK, new Function() {
					@Override
					public boolean f(Event e) {
						checkBookIsAuthorRequestEvent
								.fire(new CheckBookIsAuthorRequestEvent(bookId));
						return false;
					}
				});

				flipAction.bind(Event.ONCLICK, new Function() {
					@Override
					public boolean f(Event e) {
						// Toggle the button
						if (flipAction.hasClass("bk-bookback-pressed") == true) {
							flipAction.removeClass("bk-bookback-pressed");
						} else {
							flipAction.addClass("bk-bookback-pressed");
						}

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

						// If the view back cover was pressed need to reset
						// the button
						if (flipAction.hasClass("bk-bookback-pressed") == true) {
							flipAction.removeClass("bk-bookback-pressed");
						}

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
							parent.css(CSS.ZINDEX.with(currentBookCnt++));
							current.setIntVal(0);
							content.removeClass("bk-content-current")
									.eq(current.getIntVal())
									.addClass("bk-content-current");
						}
						return true;
					}
				});

				// If it is a btn-success buton we want to add the book to the
				// users collection, otherwise remove for the collection.
				// Clear results container
				tocPageMyCollectionsBtn.bind(Event.ONCLICK, new Function() {
					@Override
					public boolean f(Event e) {
						GQuery anchor = $(e);
						boolean addToCollection = anchor
								.hasClass("btn-success");
						adjustMyCollection(bookId, addToCollection);
						return false;
					}
				});

				tocPageCommentRatingBtn.bind(Event.ONCLICK, new Function() {
					@Override
					public boolean f(Event e) {
						addUpdateComment(bookId);
						return false;
					}
				});

				if (content.length() > 1) {
					GQuery navPrev = $("<span class=\"bk-page-prev\">&lt;</span>");
					GQuery navNext = $("<span class=\"bk-page-next\">&gt;</span>");
					page.append($("<nav></nav>").append(navPrev)
							.append(navNext));

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

					// TOC items
					tocItems.each(new Function() {
						@Override
						public void f(Element e) {
							GQuery item = $(e);
							item.bind(Event.ONCLICK, new Function() {
								@Override
								public boolean f(Event e) {
									GQuery thisItem = $(e);
									String refStr = thisItem.attr("item-ref");
									int refInt = Integer.parseInt(refStr);
									if (refInt <= content.length() - 1) {
										current.setIntVal(refInt);
										content.removeClass(
												"bk-content-current")
												.eq(current.getIntVal())
												.addClass("bk-content-current");
									}
									return false;
								}
							});
						}
					});

					// TOC link
					tocLinks.each(new Function() {
						@Override
						public void f(Element e) {
							GQuery item = $(e);
							item.bind(Event.ONCLICK, new Function() {
								@Override
								public boolean f(Event e) {
									current.setIntVal(0);
									content.removeClass("bk-content-current")
											.eq(current.getIntVal())
											.addClass("bk-content-current");
									return false;
								}
							});
						}
					});

					// Link buttons
					linkContentBtns.each(new Function() {
						@Override
						public void f(Element e) {
							GQuery btn = $(e);
							btn.bind(Event.ONCLICK, new Function() {
								@Override
								public boolean f(Event e) {
									GQuery thisAnchor = $(e);
									String href = thisAnchor.attr("href");
									Window.open(href, "_black", "");
									return false;
								}
							});
						}
					});

					// Download content buttons
					downloadContentBtns.each(new Function() {
						@Override
						public void f(Element e) {
							GQuery btn = $(e);
							btn.bind(Event.ONCLICK, new Function() {
								@Override
								public boolean f(Event e) {
									GQuery thisAnchor = $(e);
									String assetId = thisAnchor
											.attr("bookassetid");
									downloadBookAssetEvent
											.fire(new DownloadBookAssetEvent(
													assetId));
									return false;
								}
							});
						}
					});

					// view content buttons
					viewContentBtns.each(new Function() {
						@Override
						public void f(Element e) {
							GQuery btn = $(e);

							// fire action if they are not disabled
							btn.bind(Event.ONCLICK, new Function() {
								@Override
								public boolean f(Event e) {
									GQuery thisAnchor = $(e);
									String disabledStr = thisAnchor
											.attr("disabled");
									boolean disabled = (disabledStr != null ? Boolean
											.parseBoolean(disabledStr) : false);
									if (disabled == false) {
										String assetId = thisAnchor
												.attr("bookassetid");
										String contentTypeKey = thisAnchor
												.attr("viewable-content-key");
										showViewBookassetDialogEvent
												.fire(new ShowViewBookassetDialogEvent(
														assetId, contentTypeKey));
									}
									return false;
								}
							});
						}
					});
				}
			}
		});

		// Add Tooltips
		$("[rel=tooltip]", bookList.getParentElement()).as(Tooltip).tooltip();
	}

	/**
	 * Remove all events from list
	 */
	public void removeEventsFromLst() {
		if (books != null) {
			books.each(new Function() {
				@Override
				public void f(Element e) {
					final GQuery book = $(e);
					final GQuery page = book.children("div.bk-page");
					final GQuery parent = book.parent();
					final GQuery bookview = parent.find("button.bk-bookview");
					final GQuery flipAction = parent.find("button.bk-bookback");
					final GQuery toc = page.find("div.bk-toc");
					final GQuery tocPageMyCollectionsBtn = toc
							.find("a.bk-tocPage-myCollectionsBtn");
					final GQuery content = page.children("div.bk-content");
					final GQuery downloadContentBtns = page
							.find("a.bk-download-btn");
					final GQuery viewContentBtns = page.find("a.bk-view-btn");
					final GQuery tocItems = page.find("div.bk-toc-item");
					final GQuery tocLinks = page.find("div.bk-toc-link");

					book.unbind(Event.ONCLICK);
					flipAction.unbind(Event.ONCLICK);
					bookview.unbind(Event.ONCLICK);
					if (content.length() > 1) {
						GQuery navPrev = page.find(".bk-page-prev");
						if (navPrev != null) {
							navPrev.unbind(Event.ONCLICK);
							navPrev.remove();
						}

						GQuery navNext = page.find(".bk-page-next");
						if (navNext != null) {
							navNext.unbind(Event.ONCLICK);
							navNext.remove();
						}

						// Unbind TOC items
						tocItems.each().unbind(Event.ONCLICK);
					}
					tocPageMyCollectionsBtn.unbind(Event.ONCLICK);
					tocItems.each(new Function() {
						@Override
						public void f(com.google.gwt.dom.client.Element e) {
							$(e).unbind(Event.ONCLICK);
						}
					});
					tocLinks.each(new Function() {
						@Override
						public void f(com.google.gwt.dom.client.Element e) {
							$(e).unbind(Event.ONCLICK);
						}
					});
					viewContentBtns.each(new Function() {
						@Override
						public void f(com.google.gwt.dom.client.Element e) {
							$(e).unbind(Event.ONCLICK);
						}
					});
					downloadContentBtns.each(new Function() {
						@Override
						public void f(com.google.gwt.dom.client.Element e) {
							$(e).unbind(Event.ONCLICK);
						}
					});
				}
			});
		}
	}

	/**
	 * Return the maps that tracts BookListItemWidget instances
	 * 
	 * @return
	 */
	public Map<String, BookLIWidget> getBliwMap() {
		return bliwMap;
	}
}