/*******************************************************************************
 * Copyright 2010 The gwtquery plugins team.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *********************************************************************************/
package com.pronoiahealth.olhie.client.widgets.dnd;

import gwtquery.plugins.droppable.client.gwt.DroppableWidget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Droppable Panel containing the portlets.
 * 
 * @author Julien Dramaix (julien.dramaix@gmail.com)
 * 
 *         <p>
 *         Additional customizations added - John DeStefano
 *         </p>
 * 
 */
public class DroppablePanel extends DroppableWidget<FlowPanel> {

	private FlowPanel innerPanel;

	public DroppablePanel() {
		init();
		initWidget(innerPanel);
		setupDrop();
	}

	public void addWidget(Widget w) {
		innerPanel.add(w);
	}

	/**
	 * Initialize the panel style and class. The class names defines the drop
	 * target used by gwtQuery dnd.
	 */
	private void init() {
		innerPanel = new FlowPanel();
		innerPanel.addStyleName("droppablePanel");

		// Default
		setPanelStyle("padding-bottom: 20px; width: 570px; float: left; margin-left: 10px;");
	}

	/**
	 * Set the style of the panel. Should define the bottom padding, width,
	 * float, and left margin.
	 * 
	 * @param styleStr
	 */
	public void setPanelStyle(String styleStr) {
		innerPanel.getElement().setAttribute("style", styleStr);
	}

	/**
	 * Set the class name for the contained panel.<br/>
	 * 
	 * if override is true the default style is removed
	 * 
	 * @param cssClassName
	 */
	public void setPanelClass(String cssClassName, boolean override) {
		innerPanel.getElement().setClassName(cssClassName);

		if (override == true) {
			setPanelStyle("");
		}
	}

	/**
	 * Optionally set what widgets this droppable will accept
	 * 
	 * @param className
	 */
	public void setAcceptClass(String className) {
		this.setAccept(className);
	}

	/**
	 * Register drop handler
	 */
	private void setupDrop() {
		SortableDragAndDropHandler sortableHandler = new SortableDragAndDropHandler(
				innerPanel);
		addDropHandler(sortableHandler);
		addOutDroppableHandler(sortableHandler);
		addOverDroppableHandler(sortableHandler);
	}

}