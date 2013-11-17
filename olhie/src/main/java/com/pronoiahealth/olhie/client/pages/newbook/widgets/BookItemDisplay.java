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

import com.github.gwtbootstrap.client.ui.Popover;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.clientfactories.ViewableContentType;
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

	private static final DateTimeFormat dtf = DateTimeFormat
			.getFormat("MM/dd/yyyy");

	@Inject
	@ViewableContentType
	private Map<String, String> viewableContentType;

	@DataField
	private Element itemDescriptionContainer = DOM.createDiv();

	@DataField
	private Element itemOrderContainer = DOM.createDiv();

	@DataField
	private Element buttonGroupContainer = DOM.createDiv();

	@Inject
	private DownloadAssetButtonWidget downloadAssetBtn;

	@Inject
	private ViewAssetButtonWidget viewAssetBtn;

	@Inject
	private RemoveAssetButtonWidget removeAssetBtn;

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
	public void setData(Bookassetdescription bad,
			BookassetActionClickCallbackHandler downloadClickHandler,
			BookassetActionClickCallbackHandler removeClickHandler,
			BookassetActionClickCallbackHandler viewClickHandler) {
		// item Position
		Widget itemPosLbl = createCellWidget(NewBookMessages.INSTANCE
				.createTOCNumber("" + (bad.getPosition())));
		String createdDt = dtf.format(bad.getCreatedDate());
		setupPopover(itemPosLbl,
				NewBookMessages.INSTANCE.setCreatedDateText(createdDt),
				"Details");

		// Item Description
		Widget itemDescLbl = createCellWidget(bad.getDescription());

		// Button Group
		Bookasset ba = bad.getBookAssets().get(0);
		String baId = ba.getId();
		String badId = bad.getId();

		// Create the buttons and add to the buttonGroupContainer
		makeButtonGroupInButtonGroupContainer(ba.getContentType(),
				ba.getItemType(), badId, baId, downloadClickHandler,
				removeClickHandler, viewClickHandler);

		// Set the data
		itemOrderContainer.appendChild(itemPosLbl.getElement());
		itemDescriptionContainer.appendChild(itemDescLbl.getElement());

		// Add mouse over and mosue ot handler
		// Mouse over add background
		// Mouse oue remove it
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
	 * Sets a popover on a widget
	 * 
	 * @param w
	 * @param message
	 * @param heading
	 */
	private Popover setupPopover(Widget w, String message, String heading) {
		Popover popover = new Popover();
		popover.setWidget(w);
		popover.setText(message);
		popover.setHeading(heading);
		popover.setPlacement(Placement.TOP);
		popover.setContainer("body");
		popover.reconfigure();
		return popover;
	}

	/**
	 * Create cell widget for a cell
	 * 
	 * @param cellObject
	 * @return
	 */
	private Widget createCellWidget(Object cellObject) {
		Widget widget = null;
		if (cellObject instanceof Widget) {
			widget = (Widget) cellObject;
		} else {
			Label l = new Label(cellObject.toString());
			l.setWordWrap(true);
			widget = l;
		}
		return widget;
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
			String itemTypeStr, final String badId, final String baId,
			final BookassetActionClickCallbackHandler downloadClickHandler,
			final BookassetActionClickCallbackHandler removeClickHandler,
			final BookassetActionClickCallbackHandler viewClickHandler) {

		// Download button
		BookAssetDataType itemType = BookAssetDataType.valueOf(itemTypeStr);
		if (itemType.equals(BookAssetDataType.FILE)) {
			Element dBut = downloadAssetBtn.setAndBind(baId);
			buttonGroupContainer.appendChild(dBut);

			GQuery dButQry = $(dBut);
			dButQry.bind(Event.ONCLICK, new Function() {
				@Override
				public boolean f(Event e) {
					return downloadClickHandler.handleButtonClick(e, null,
							baId, null);
				}
			});
		}

		// Can this content be viewed in an iFrame
		final String val = viewableContentType.get(contentType);
		if (val != null) {
			Element vBut = viewAssetBtn.setAndBind(baId);
			buttonGroupContainer.appendChild(vBut);
			GQuery dButQry = $(vBut);
			dButQry.bind(Event.ONCLICK, new Function() {
				@Override
				public boolean f(Event e) {
					return viewClickHandler.handleButtonClick(e, badId, baId,
							val);
				}
			});
		}

		// Remove button
		Element rBut = removeAssetBtn.setAndBind(baId);
		buttonGroupContainer.appendChild(rBut);
		GQuery dButQry = $(rBut);
		dButQry.bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				return removeClickHandler
						.handleButtonClick(e, null, baId, null);
			}
		});

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

}
