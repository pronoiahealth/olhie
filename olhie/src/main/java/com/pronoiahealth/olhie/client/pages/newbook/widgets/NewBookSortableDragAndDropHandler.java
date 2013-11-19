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

import gwtquery.plugins.droppable.client.events.DropEvent;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.widgets.dnd.SortableDragAndDropHandler;

/**
 * CustomSortableDragAndDropHandler.java<br/>
 * Responsibilities:<br/>
 * 1. The purpose of this class is to handle drag and drop events. The basic
 * ones are handled by the super class.<br/>
 * 2. This class over rides the drop handling of the super class to set the
 * order on the list and tell the server about it.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 17, 2013
 * 
 */
public class NewBookSortableDragAndDropHandler extends
		SortableDragAndDropHandler {

	/**
	 * Constructor
	 * 
	 * @param panel
	 */
	public NewBookSortableDragAndDropHandler(FlowPanel panel) {
		super(panel);
	}

	/**
	 * Make sure to call super classes onDrop first. Then handle to re-order of
	 * the display items.
	 * 
	 * @see com.pronoiahealth.olhie.client.widgets.dnd.SortableDragAndDropHandler#onDrop(gwtquery.plugins.droppable.client.events.DropEvent)
	 */
	@Override
	public void onDrop(DropEvent event) {
		super.onDrop(event);

		// Change numbering
		Widget w = event.getDroppableWidget().asWidget();
		if (w instanceof NewBookDroppablePanel) {
			Map<Integer, String> assetOrderMap = new HashMap<Integer, String>();
			NewBookDroppablePanel dp = (NewBookDroppablePanel) w;
			FlowPanel fp = dp.getInnerPanel();
			int cnt = fp.getWidgetCount();
			for (int i = 0; i < cnt; i++) {
				// if the child is a BookItemDisplay
				Widget bidW = fp.getWidget(i);
				if (bidW instanceof BookItemDisplay) {
					BookItemDisplay bid = (BookItemDisplay) bidW;
					
					// Reset the number
					bid.setItemPosLbl("" + (i+1));

					// Save in map
					assetOrderMap.put(i, bid.getBaId());
				}
			}

			// Send map to server for update. If there is a problem the server
			// will respond with an error caught by the Server error feature
		}
	}
}
