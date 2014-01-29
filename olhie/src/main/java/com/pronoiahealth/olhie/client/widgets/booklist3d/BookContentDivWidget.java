package com.pronoiahealth.olhie.client.widgets.booklist3d;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;

public class BookContentDivWidget extends ComplexPanel {
	private DivElement root;
	private ParagraphElement title;
	private DivElement addedDiv;
	private DivElement classDataCell;
	private Element classIElement;
	private DivElement typeDiv;
	private DivElement hoursDiv;
	private AnchorElement downLoadBut;
	private AnchorElement linkBut;
	private AnchorElement viewBut;
	
	public BookContentDivWidget() {
		Document doc = Document.get();
		root = doc.createDivElement();
		setElement(root);
		
		// Set Root
		root.setClassName("bk-content");
		
		// Add Holder Div
		DivElement holder = doc.createDivElement();
		root.appendChild(holder);
		holder.setAttribute("style", "height: 265px; overflow: auto;");
		
		// Title
		title = doc.createPElement();
		holder.appendChild(title);
		
		// Table
		DivElement tableDiv = doc.createDivElement();
		holder.appendChild(tableDiv);
		tableDiv.setAttribute("style", "display: table;");
		
		// Added Row
		addedDiv = createRow(doc, tableDiv, "Added");
		
		// Class
		DivElement classRow = doc.createDivElement();
		tableDiv.appendChild(classRow);
		classRow.setAttribute("style", "display: table-row;");
		DivElement labelCell = doc.createDivElement();
		classRow.appendChild(labelCell);
		labelCell.setAttribute("style", "display: table-cell; text-align: right;");
		labelCell.setInnerText("Class:");
		classDataCell = doc.createDivElement();
		classRow.appendChild(classDataCell);
		classDataCell.setAttribute("style", "display: table-cell;");
		SpanElement spanElement = doc.createSpanElement();
		classDataCell.appendChild(spanElement);
		classIElement = doc.createElement("i");
		spanElement.appendChild(classIElement);
		
		// Type
		typeDiv = createRow(doc, tableDiv, "Type");
		
		// Hours
		hoursDiv = createRow(doc, tableDiv, "Hours");
		
		// Controls holder
		DivElement controlsHolder = doc.createDivElement();
		root.appendChild(controlsHolder);
		controlsHolder.setAttribute("style", "text-align: center;");
		
		// Book items buttons
		DivElement bookItemButs = doc.createDivElement();
		controlsHolder.appendChild(bookItemButs);
		
		// Book items buttons container
		DivElement dlHolder = doc.createDivElement();
		bookItemButs.appendChild(dlHolder);
		dlHolder.setAttribute("style", "width: 27px; height: 23px; float: left;");
		
		// Download buttonHolder
		DivElement downLoadButHolder = doc.createDivElement();
		dlHolder.appendChild(downLoadButHolder);
		downLoadBut = createAnchor(doc, "btn btn-mini btn-info bk-download-btn", "Download", "icon-cloud-download");
		downLoadButHolder.appendChild(downLoadBut);
		
		// Book Item link
		DivElement lbHolder = doc.createDivElement();
		dlHolder.appendChild(lbHolder);
		linkBut = createAnchor(doc, "btn btn-mini btn-info bk-link-btn", "Link", "icon-link");
		lbHolder.appendChild(linkBut);
		
		// View Button Divider Holder
		DivElement viewDivHolder = doc.createDivElement();
		bookItemButs.appendChild(viewDivHolder);
		viewDivHolder.setAttribute("style", "width: 27px; height: 23px; float: right;");
		
		// Book Item view
		DivElement viewButHolder = doc.createDivElement();
		viewDivHolder.appendChild(viewButHolder);
		viewBut = createAnchor(doc, "btn btn-mini btn-success bk-view-btn", "View item", "icon-eye-open");
		viewButHolder.appendChild(viewBut);
		
		// TOC link
		DivElement bktoclink = doc.createDivElement();
		controlsHolder.appendChild(bktoclink);
		bktoclink.setAttribute("style", "text-decoration: underline; font-weight: bold;");
		bktoclink.setClassName("bk-toc-link");
		bktoclink.setInnerText("Table of Contents");
	}
	
	/**
	 * Sets up the book asset display:<br/>
	 * 1. Set the description and data elements<br/>
	 * 2. If the content is not viewable then hide the view button<br/>
	 * 3. If the itemType is a link type set the attributes for a direct link,
	 * otherwise, set for download<br/>
	 * 
	 * @param desc
	 */
	public void setDescription(String desc, String addedDateStr, String hrs,
			boolean downLoadOnly, String assetId, String assetType,
			String itemType, String linkRef) {
		addedDiv.setInnerText(addedDateStr);
		typeDiv.setInnerText(assetType);
		BookAssetDataType dataType = BookAssetDataType.valueOf(itemType);
		classIElement.setClassName(dataType.getBootstrapClass());
		title.setInnerText(desc);
		hoursDiv.setInnerText(hrs);

		// Buttons
		// View button
		if (downLoadOnly == true) {
			viewBut.setAttribute("disabled", "true");
			viewBut.setAttribute("style", "display: none;");
		} else {
			viewBut.setAttribute("bookassetid", assetId);
			viewBut.setAttribute("viewable-content-key", assetType);
			viewBut.removeAttribute("disabled");
			viewBut.setAttribute("style", "display: inline-block;");
		}
		
		// Download
		if (dataType == BookAssetDataType.FILE
				|| dataType == BookAssetDataType.VIDEO) {
			downLoadBut.setAttribute("style", "display: inline-block; border: 1px; padding-left: 6px; padding-right: 6px; height: 20px;");
			downLoadBut.setAttribute("bookassetid", assetId);
		} else {
			downLoadBut
					.setAttribute("style",
							"display: none;");
		}

		// Link
		if (dataType == BookAssetDataType.LINK
				|| dataType == BookAssetDataType.YOUTUBE) {
			linkBut.setAttribute("style", "display: inline-block; border: 1px; padding-left: 6px; padding-right: 6px; height: 20px;");
			linkBut.setAttribute("href", linkRef);
			linkBut.setAttribute("target", "_target");
		} else {
			linkBut
					.setAttribute("style",
							"display: none;");
		}
	}
	
	private DivElement createRow(Document doc, Element rowParent, String label) {
		DivElement row = doc.createDivElement();
		row.setAttribute("style", "display: table-row;");
		
		// Label cell
		DivElement labelCell = doc.createDivElement();
		row.appendChild(labelCell);
		labelCell.setAttribute("style", "display: table-cell; text-align: right;");
		labelCell.setInnerText(label+ ":");
		
		// Value cell
		DivElement valueCell = doc.createDivElement();
		row.appendChild(valueCell);
		valueCell.setAttribute("style", "display: table-cell;");
		
		// Add row to parent
		rowParent.appendChild(row);
		
		return valueCell;
	}
	
	private AnchorElement createAnchor(Document doc, String className, String title, String iClassName) {
		AnchorElement aEl = doc.createAnchorElement();
		aEl.setHref("javascript:;");
		aEl.setClassName(className);
		aEl.setTitle(title);
		aEl.setAttribute("rel", "tooltip");
		Element iEl = doc.createElement("i");
		aEl.appendChild(iEl);
		iEl.setClassName(iClassName);
		return aEl;
	}
}