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

import com.pronoiahealth.olhie.client.widgets.dnd.DroppablePanel;
import com.pronoiahealth.olhie.client.widgets.dnd.SortableDragAndDropHandler;

/**
 * CustomDroppablePanel.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 17, 2013
 * 
 */
public class NewBookDroppablePanel extends DroppablePanel {

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
		return new NewBookSortableDragAndDropHandler(getInnerPanel());
	}

}
