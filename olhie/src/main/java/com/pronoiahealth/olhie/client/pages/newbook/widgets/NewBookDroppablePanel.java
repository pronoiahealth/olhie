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
package com.pronoiahealth.olhie.client.pages.newbook.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.events.book.BookdescriptionDetailRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.book.RemoveBookassetdescriptionEvent;
import com.pronoiahealth.olhie.client.shared.events.book.UpdateBookassetdescriptionsForBookEvent;
import com.pronoiahealth.olhie.client.shared.events.local.DownloadBookAssetEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowViewBookassetDialogEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.widgets.dnd.DroppablePanel;
import com.pronoiahealth.olhie.client.widgets.dnd.SortableDragAndDropHandler;

/**
 * CustomDroppablePanel.java<br/>
 * Responsibilities:<br/>
 * 1. Provide a drop panel and handle movement and reording of of dropped items.<br/>
 * 2. Tell the server if the order has changed.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 17, 2013
 * 
 */
public class NewBookDroppablePanel extends DroppablePanel {

	private BookassetActionClickCallbackHandler assetDetailClickHandler;

	private BookassetActionClickCallbackHandler downloadClickHandler;

	private BookassetActionClickCallbackHandler removeClickHandler;

	private BookassetActionClickCallbackHandler viewClickHandler;

	@Inject
	private Event<UpdateBookassetdescriptionsForBookEvent> updateBookassetdescriptionOrderEvent;

	@Inject
	private Event<DownloadBookAssetEvent> downloadBookAssetEvent;

	@Inject
	private Event<RemoveBookassetdescriptionEvent> removeBookassetdescriptionEvent;

	@Inject
	private Event<ShowViewBookassetDialogEvent> showViewBookassetDialogEvent;

	@Inject
	private Event<BookdescriptionDetailRequestEvent> bookdescriptionDetailRequestEvent;

	/**
	 * Used to tell the server the order has changed
	 */
	private BookassetdescriptionReorderHanlder bookassetdescriptionReorderHanlder;

	@Inject
	private Instance<BookItemDisplay> bookItemDisplayFactory;

	/**
	 * Constructor
	 * 
	 */
	public NewBookDroppablePanel() {
	}

	@PostConstruct
	protected void postConstruct() {
		// handler for clicking download button on a BookItemDisplay
		downloadClickHandler = new BookassetActionClickCallbackHandler() {
			@Override
			public boolean handleButtonClick(
					com.google.gwt.user.client.Event e, String baDescId,
					String baId, String viewType) {
				downloadBookAssetEvent.fire(new DownloadBookAssetEvent(baId));
				return false;
			}
		};

		// handler for clicking asset detail on a BookItemDisplay
		this.assetDetailClickHandler = new BookassetActionClickCallbackHandler() {
			@Override
			public boolean handleButtonClick(
					com.google.gwt.user.client.Event e, String baDescId,
					String baId, String viewType) {
				bookdescriptionDetailRequestEvent
						.fire(new BookdescriptionDetailRequestEvent(baDescId,
								baId));
				return false;
			}
		};

		// handler for clicking remove button on a BookItemDisplay
		this.removeClickHandler = new BookassetActionClickCallbackHandler() {
			@Override
			public boolean handleButtonClick(
					com.google.gwt.user.client.Event e, String baDescId,
					String baId, String viewType) {

				// Find the item in the list
				// and remove it
				FlowPanel fp = getInnerPanel();
				int cnt = fp.getWidgetCount();
				for (int i = 0; i < cnt; i++) {
					// if the child is a BookItemDisplay
					Widget bidW = fp.getWidget(i);
					if (bidW instanceof BookItemDisplay) {
						BookItemDisplay item = (BookItemDisplay) bidW;
						if (item.getBadId().equals(baDescId)) {
							fp.remove(item);
							break;
						}
					}
				}

				// Reorder the remaining items
				Map<String, Integer> assetOrderMap = getReorderMap();

				// Tell the database about the changes
				updateBookassetdescriptionOrderEvent
						.fire(new UpdateBookassetdescriptionsForBookEvent(
								assetOrderMap, baDescId));

				// Indicates that this method will handle the event
				return false;
			}
		};

		// handler for clicking view button on a BookItemDisplay
		this.viewClickHandler = new BookassetActionClickCallbackHandler() {

			@Override
			public boolean handleButtonClick(
					com.google.gwt.user.client.Event e, String baDescId,
					String baId, String viewType) {
				showViewBookassetDialogEvent
						.fire(new ShowViewBookassetDialogEvent(baId, viewType));
				return false;
			}
		};
	}

	/**
	 * Set the custom handler to handle the drop event.
	 * 
	 * @see com.pronoiahealth.olhie.client.widgets.dnd.DroppablePanel#getSortableDragAndDropHandler()
	 */
	@Override
	protected SortableDragAndDropHandler getSortableDragAndDropHandler() {
		// Construct this here as the super class calls it to initialize the
		// drag and drop handler
		this.bookassetdescriptionReorderHanlder = new BookassetdescriptionReorderHanlder() {
			@Override
			public void handleReorder() {
				Map<String, Integer> assetOrderMap = getReorderMap();

				// Send map to server for update. If there is a problem the
				// server
				// will respond with an error caught by the Server error feature
				if (assetOrderMap.size() > 0) {
					updateBookassetdescriptionOrderEvent
							.fire(new UpdateBookassetdescriptionsForBookEvent(
									assetOrderMap));
				}
			}
		};

		return new NewBookSortableDragAndDropHandler(getInnerPanel(),
				bookassetdescriptionReorderHanlder);
	}

	/**
	 * Returns a map of items that have a different position number in the list
	 * than in there order number.
	 * 
	 * @return
	 */
	private Map<String, Integer> getReorderMap() {
		Map<String, Integer> assetOrderMap = new HashMap<String, Integer>();
		FlowPanel fp = getInnerPanel();
		int cnt = fp.getWidgetCount();
		for (int i = 0; i < cnt; i++) {
			// if the child is a BookItemDisplay
			Widget bidW = fp.getWidget(i);
			if (bidW instanceof BookItemDisplay) {
				BookItemDisplay bid = (BookItemDisplay) bidW;

				// Reset the number if different
				int itemPosNumb = bid.getItemDescriptionPos();
				int displayLineNumb = i + 1;
				if (itemPosNumb != displayLineNumb) {
					bid.setItemPosLbl((i + 1));

					// Save in map
					assetOrderMap.put(bid.getBadId(), displayLineNumb);
				}
			}
		}

		return assetOrderMap;
	}

	/**
	 * Add description items to the panel
	 * 
	 * @param bookDisplay
	 */
	public void setDescriptionItems(BookDisplay bookDisplay,
			boolean isUserAuthorOrCoAuthor) {
		// Clear the panel if anything is there
		getOriginalWidget().clear();

		// Place the items in the itemsDndContainer. The list should be
		// returning an ordered list by the book asset description order.
		List<Bookassetdescription> descriptions = bookDisplay
				.getBookAssetDescriptions();
		for (int i = 0; i < descriptions.size(); i++) {
			Bookassetdescription bad = descriptions.get(i);
			BookItemDisplay item = bookItemDisplayFactory.get();
			item.initData(bad, assetDetailClickHandler, downloadClickHandler,
					isUserAuthorOrCoAuthor == true ? removeClickHandler : null,
					viewClickHandler);
			addWidget(item);
		}
	}
}
