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
package com.pronoiahealth.olhie.client.widgets;

import com.github.gwtbootstrap.client.ui.Popover;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * FlexTableExt.java<br/>
 * Responsibilities:<br/>
 * 1. Custom flex table <br/
 * >
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 13, 2013
 * 
 */
public class FlexTableExt extends FlexTable {
	private int rowIndex = 1;
	private static final int HeaderRowIndex = 0;
	private String flexTableOddRowStyle = "FlexTable-OddRow";
	private String flexTableEvenRowStyle = "FlexTable-EvenRow";

	/**
	 * Constructor
	 * 
	 */
	public FlexTableExt() {
		super();
	}

	public void setTableStyle(String styleName) {
		this.setStyleName(styleName, true);
	}

	/**
	 * Add a column
	 * 
	 * @param columnHeading
	 */
	public void addColumn(Object columnHeading, String styleName,
			String cellStyleName) {
		Widget widget = createCellWidget(columnHeading);
		int cell = getCellCount(HeaderRowIndex);
		widget.setWidth("100%");
		widget.addStyleName(styleName);
		setWidget(HeaderRowIndex, cell, widget);
		getCellFormatter().addStyleName(HeaderRowIndex, cell, cellStyleName);
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
	 * Create cell widget for a cell
	 * 
	 * @param cellObject
	 * @return
	 */
	// private Widget createCellWidget(String cellObject) {
	// Widget widget = null;
	// Label l = new Label(cellObject.toString());
	// l.setWordWrap(true);
	// widget = l;
	// return widget;
	// }

	/**
	 * Add a row of string values
	 * 
	 * @param cellObjects
	 *            - Data
	 * @param cellColStyles
	 *            - Names of the style for each cell. Size must match
	 *            cellObjects or you may get inconsistent results
	 * @param popOverTitle
	 *            - Title for popOver
	 * @param popOverTxt
	 *            - Text for popOver
	 * @param popOverCol
	 *            - If its less than 0 the not popup will be placed. The value
	 *            is 0 based.
	 */
	public void addRow(Object[] cellObjects, String[] cellColStyles,
			String popOverTitle, String popOverTxt, int popOverCol) {
		for (int cell = 0; cell < cellObjects.length; cell++) {
			Widget widget = createCellWidget(cellObjects[cell]);
			setWidget(rowIndex, cell, widget);
			getCellFormatter()
					.addStyleName(rowIndex, cell, cellColStyles[cell]);
			if (popOverCol >= 0 && cell == popOverCol) {
				setupPopover(widget, popOverTxt, popOverTitle);
			}
		}
		rowIndex++;
	}

	/**
	 * Sets a popover on a widget
	 * 
	 * @param w
	 * @param message
	 * @param heading
	 */
	private void setupPopover(Widget w, String message, String heading) {
		Popover popover = new Popover();
		popover.setWidget(w);
		popover.setText(message);
		popover.setHeading(heading);
		popover.setPlacement(Placement.TOP);
		popover.reconfigure();
	}

	/**
	 * Apply styles to rows odd and even
	 */
	public void applyDataRowStyles() {
		HTMLTable.RowFormatter rf = getRowFormatter();
		for (int row = 1; row < getRowCount(); ++row) {
			if ((row % 2) != 0) {
				rf.addStyleName(row, flexTableOddRowStyle);
			} else {
				rf.addStyleName(row, flexTableEvenRowStyle);
			}
		}
	}

	/**
	 * Call clear on the widget and set the rowIndex to 1.
	 */
	public void clearTable() {
		clear();
		this.rowIndex = 1;
	}

	public String getFlexTableOddRowStyle() {
		return flexTableOddRowStyle;
	}

	public void setFlexTableOddRowStyle(String flexTableOddRowStyle) {
		this.flexTableOddRowStyle = flexTableOddRowStyle;
	}

	public String getFlexTableEvenRowStyle() {
		return flexTableEvenRowStyle;
	}

	public void setFlexTableEvenRowStyle(String flexTableEvenRowStyle) {
		this.flexTableEvenRowStyle = flexTableEvenRowStyle;
	}
}
