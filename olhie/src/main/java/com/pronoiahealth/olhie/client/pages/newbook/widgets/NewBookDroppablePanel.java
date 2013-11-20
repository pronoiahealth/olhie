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

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.pronoiahealth.olhie.client.shared.events.book.UpdateBookassetdescriptionOrderEvent;
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

	@Inject
	private Event<UpdateBookassetdescriptionOrderEvent> updateBookassetdescriptionOrderEvent;

	/**
	 * Used to tell the server the order has changed
	 */
	private BookassetdescriptionReorderHanlder bookassetdescriptionReorderHanlder;

	/**
	 * Constructor
	 * 
	 */
	public NewBookDroppablePanel() {
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
			public void handleReorder(Map<String, Integer> posMap) {
				updateBookassetdescriptionOrderEvent
						.fire(new UpdateBookassetdescriptionOrderEvent(posMap));
			}
		};

		return new NewBookSortableDragAndDropHandler(getInnerPanel(),
				bookassetdescriptionReorderHanlder);
	}

}
