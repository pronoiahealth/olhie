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

import static com.google.gwt.query.client.GQuery.$;
import gwtquery.plugins.draggable.client.events.BeforeDragStartEvent;
import gwtquery.plugins.draggable.client.events.BeforeDragStartEvent.BeforeDragStartEventHandler;
import gwtquery.plugins.draggable.client.events.DragStopEvent;
import gwtquery.plugins.draggable.client.events.DragStopEvent.DragStopEventHandler;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.clientfactories.FileTypeViewableContent;
import com.pronoiahealth.olhie.client.pages.newbook.NewBookMessages;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;

/**
 * BookItemDisplay.java<br/>
 * Responsibilities:<br/>
 * 1. Creates a Book item widget which contains the order number of the item,
 * its description, and a set of appropriate action buttons (download, view,
 * remove).
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 14, 2013
 * 
 */
@Templated("#root")
public class BookItemDisplay extends DraggableWidget<Widget> {

	/**
	 * Static class to handle dragging and dropping events
	 * 
	 * @author John DeStefano
	 * @version 1.0
	 * @since Nov 15, 2013
	 * 
	 */
	private static class DraggablePositionHandler implements
			BeforeDragStartEventHandler, DragStopEventHandler {

		/**
		 * before that the drag operation starts, we will "visually" detach the
		 * draggable by setting it css position to absolute.
		 */
		public void onBeforeDragStart(BeforeDragStartEvent event) {
			// "detach" visually the element of the parent
			$(event.getDraggable()).css("position", "absolute").css("opacity",
					"0.5");
		}

		public void onDragStop(DragStopEvent event) {
			// "reattach" the element
			$(event.getDraggable()).css("position", "relative")
					.css("top", null).css("left", null).css("opacity", "1.0");
		}
	}

	/**
	 * Used of all instances
	 */
	private static DraggablePositionHandler HANDLER = new DraggablePositionHandler();

	@Inject
	@FileTypeViewableContent
	private Map<String, String> fileTypeviewableContent;

	@DataField
	private Element itemDescriptionContainer = DOM.createDiv();

	@DataField
	private Element itemOrderContainer = DOM.createDiv();

	@DataField
	private Element buttonGroupContainer = DOM.createDiv();

	@DataField
	private Element itemIcon = DOM.createDiv();

	@Inject
	private DownloadAssetButtonWidget downloadAssetBtn;

	@Inject
	private ViewAssetButtonWidget viewAssetBtn;

	@Inject
	private RemoveAssetButtonWidget removeAssetBtn;

	@Inject
	private BookdescriptionDetailButtonWidget assetDetailBtn;

	@Inject
	private LinkToAssetButtonWidget linkToAssetButtonWidget;

	private String baId;

	private String badId;

	private String itemType;

	private Label itemPosLbl;

	private Label itemDescLbl;

	private int itemDescriptionPos;

	/**
	 * Constructor
	 * 
	 * Creates object and configures drag and drop handlers
	 * 
	 */
	public BookItemDisplay() {
		setup();
	}

	/**
	 * Set the items data elements
	 * 
	 * @param itemOrder
	 * @param itemDescription
	 * @param buttonGroup
	 */
	public void initData(Bookassetdescription bad,
			BookassetActionClickCallbackHandler assetDetailClickHandler,
			BookassetActionClickCallbackHandler downloadClickHandler,
			BookassetActionClickCallbackHandler removeClickHandler,
			BookassetActionClickCallbackHandler viewClickHandler) {
		// item Position
		itemPosLbl = createLabel(NewBookMessages.INSTANCE.createTOCNumber(""
				+ bad.getPosition()));

		// Item Description
		itemDescLbl = createLabel(bad.getDescription());

		// Button Group
		Bookasset ba = bad.getBookAssets().get(0);
		baId = ba.getId();
		badId = bad.getId();
		itemDescriptionPos = bad.getPosition();
		itemType = ba.getItemType();

		// Type Icon
		Element icon = DOM.createElement("i");
		String iconClass = BookAssetDataType.valueOf(itemType)
				.getBootstrapClass();
		icon.setClassName(iconClass);
		itemIcon.appendChild(icon);

		// Create the buttons and add to the buttonGroupContainer
		makeButtonGroupInButtonGroupContainer(ba.getContentType(),
				ba.getItemType(), ba.getLinkRef(), badId, baId,
				assetDetailClickHandler, downloadClickHandler,
				removeClickHandler, viewClickHandler);

		// Set the data
		itemOrderContainer.appendChild(itemPosLbl.getElement());
		itemDescriptionContainer.appendChild(itemDescLbl.getElement());

		// Add mouse over and mouse out handler
		// Mouse over add background
		// Mouse out remove it
		$(itemDescLbl.getElement()).bind(Event.ONMOUSEOVER, new Function() {
			@Override
			public boolean f(Event e) {
				$(e).parent().parent().parent().parent()
						.addClass("ph-NewBook-ItemsListBox-BookItem-Highlight");
				return super.f(e);
			}
		});

		$(itemDescLbl.getElement()).bind(Event.ONMOUSEOUT, new Function() {
			@Override
			public boolean f(Event e) {
				$(e).parent()
						.parent()
						.parent()
						.parent()
						.removeClass(
								"ph-NewBook-ItemsListBox-BookItem-Highlight");
				return super.f(e);
			}
		});
	}

	/**
	 * Create label
	 * 
	 * @param str
	 * @return
	 */
	private Label createLabel(String str) {
		Label l = new Label(str);
		l.setWordWrap(true);
		return l;
	}

	/**
	 * Make a button group with the correct buttons based on the params
	 * 
	 * @param contentType
	 * @param itemTypeStr
	 * @param baId
	 * @return
	 */
	private void makeButtonGroupInButtonGroupContainer(String contentType,
			String itemTypeStr, String hRef, final String badId,
			final String baId,
			final BookassetActionClickCallbackHandler assetDetailClickHandler,
			final BookassetActionClickCallbackHandler downloadClickHandler,
			final BookassetActionClickCallbackHandler removeClickHandler,
			final BookassetActionClickCallbackHandler viewClickHandler) {

		// Detail button
		Element adBut = assetDetailBtn.bindButton();
		buttonGroupContainer.appendChild(adBut);
		GQuery adButQry = $(adBut);
		adButQry.bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				return assetDetailClickHandler.handleButtonClick(e, badId,
						baId, null);
			}
		});

		// On mouse over cause icon to spin
		adButQry.bind(Event.ONMOUSEOVER, new Function() {
			@Override
			public boolean f(Event e) {
				$(e).children().addClass("icon-spin");
				return false;
			}
		});

		// On mouse out cause icon to stop spinning
		adButQry.bind(Event.ONMOUSEOUT, new Function() {
			@Override
			public boolean f(Event e) {
				$(e).children().removeClass("icon-spin");
				return false;
			}
		});

		// Download button
		BookAssetDataType dt = BookAssetDataType.valueOf(itemTypeStr);
		if (dt == BookAssetDataType.FILE || dt == BookAssetDataType.VIDEO) {
			BookAssetDataType itemType = BookAssetDataType.valueOf(itemTypeStr);
			if (itemType.equals(BookAssetDataType.FILE)) {
				Element dBut = downloadAssetBtn.bindButton();
				buttonGroupContainer.appendChild(dBut);

				GQuery dButQry = $(dBut);
				dButQry.bind(Event.ONCLICK, new Function() {
					@Override
					public boolean f(Event e) {
						return downloadClickHandler.handleButtonClick(e, null,
								baId, null);
					}
				});

				// On mouse over cause icon to spin
				dButQry.bind(Event.ONMOUSEOVER, new Function() {
					@Override
					public boolean f(Event e) {
						$(e).children().addClass("icon-spin");
						return false;
					}
				});

				// On mouse out cause icon to stop spinning
				dButQry.bind(Event.ONMOUSEOUT, new Function() {
					@Override
					public boolean f(Event e) {
						$(e).children().removeClass("icon-spin");
						return false;
					}
				});
			}
		}

		// Link button
		if (dt == BookAssetDataType.LINK && hRef != null && hRef.length() > 0) {
			linkToAssetButtonWidget.setLink(hRef);
			Element lBut = linkToAssetButtonWidget.bindButton();
			buttonGroupContainer.appendChild(lBut);

			GQuery lButQry = $(lBut);

			// On mouse over cause icon to spin
			lButQry.bind(Event.ONMOUSEOVER, new Function() {
				@Override
				public boolean f(Event e) {
					$(e).children().addClass("icon-spin");
					return false;
				}
			});

			// On mouse out cause icon to stop spinning
			lButQry.bind(Event.ONMOUSEOUT, new Function() {
				@Override
				public boolean f(Event e) {
					$(e).children().removeClass("icon-spin");
					return false;
				}
			});
		}

		// Can this content be viewed in an iFrame
		final String val = fileTypeviewableContent.get(contentType);
		if (val != null) {
			Element vBut = viewAssetBtn.bindButton();
			buttonGroupContainer.appendChild(vBut);
			GQuery vButQry = $(vBut);
			vButQry.bind(Event.ONCLICK, new Function() {
				@Override
				public boolean f(Event e) {
					return viewClickHandler.handleButtonClick(e, badId, baId,
							val);
				}
			});

			// On mouse over cause icon to spin
			vButQry.bind(Event.ONMOUSEOVER, new Function() {
				@Override
				public boolean f(Event e) {
					$(e).children().addClass("icon-spin");
					return false;
				}
			});

			// On mouse out cause icon to stop spinning
			vButQry.bind(Event.ONMOUSEOUT, new Function() {
				@Override
				public boolean f(Event e) {
					$(e).children().removeClass("icon-spin");
					return false;
				}
			});

		}

		// Remove button
		// Only show if the user is the author or co-author
		if (removeClickHandler != null) {
			Element rBut = removeAssetBtn.bindButton();
			buttonGroupContainer.appendChild(rBut);
			GQuery rButQry = $(rBut);
			rButQry.bind(Event.ONCLICK, new Function() {
				@Override
				public boolean f(Event e) {
					return removeClickHandler.handleButtonClick(e, badId, baId,
							null);
				}
			});

			// On mouse over cause icon to spin
			rButQry.bind(Event.ONMOUSEOVER, new Function() {
				@Override
				public boolean f(Event e) {
					$(e).children().addClass("icon-spin");
					return false;
				}
			});

			// On mouse out cause icon to stop spinning
			rButQry.bind(Event.ONMOUSEOUT, new Function() {
				@Override
				public boolean f(Event e) {
					$(e).children().removeClass("icon-spin");
					return false;
				}
			});
		}
	}

	/**
	 * Called in construct to perform drag and drop configuration.
	 */
	private void setup() {
		// opacity of the portlet during the drag
		setDraggingOpacity(new Float(0.8));

		// zIndex of the portlet during the drag
		setDraggingZIndex(1000);

		// add position handler
		addBeforeDragHandler(HANDLER);
		addDragStopHandler(HANDLER);
	}

	/**
	 * Reset the item position label
	 * 
	 * @param str
	 */
	public void setItemPosLbl(int pos) {
		if (itemPosLbl != null) {
			itemDescriptionPos = pos;
			itemPosLbl.setText(NewBookMessages.INSTANCE.createTOCNumber(""
					+ pos));
		}
	}

	/**
	 * Return the book asset id set through calling the initData method.
	 * 
	 * @return
	 */
	public String getBaId() {
		return baId;
	}

	/**
	 * Return the book asset description id set through calling the initData
	 * method.
	 * 
	 * @return
	 */
	public String getBadId() {
		return badId;
	}

	public void setBadId(String badId) {
		this.badId = badId;
	}

	public int getItemDescriptionPos() {
		return itemDescriptionPos;
	}

	/**
	 * Return the book description relative position number used for ordering
	 * set through calling the initData method.
	 * 
	 * @param itemDescriptionPos
	 */
	public void setItemDescriptionPos(int itemDescriptionPos) {
		this.itemDescriptionPos = itemDescriptionPos;
	}
}
