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

	private BookassetdescriptionReorderHanlder bookassetdescriptionReorderHanlder;

	/**
	 * Constructor
	 * 
	 * @param panel
	 */
	public NewBookSortableDragAndDropHandler(
			FlowPanel panel,
			BookassetdescriptionReorderHanlder bookassetdescriptionReorderHanlder) {
		super(panel);
		this.bookassetdescriptionReorderHanlder = bookassetdescriptionReorderHanlder;
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
			bookassetdescriptionReorderHanlder.handleReorder();
		}
	}
}
