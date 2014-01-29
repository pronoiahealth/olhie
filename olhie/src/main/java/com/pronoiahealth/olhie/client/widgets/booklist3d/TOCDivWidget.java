package com.pronoiahealth.olhie.client.widgets.booklist3d;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.ComplexPanel;

public class TOCDivWidget extends ComplexPanel {
	private DivElement root;
	private DivElement tocItemsTable;
	private DivElement myCollectionsButContainer;
	private AnchorElement myCollectionBut;
	private Element myCollectionIElement;
	private DivElement crButContainer;
	private AnchorElement crBut;
	private Element crIElement;

	public TOCDivWidget() {
		Document doc = Document.get();
		root = doc.createDivElement();
		setElement(root);

		// Root Div
		root.setAttribute("class", "bk-content bk-content-current bk-toc");

		// TOC Title
		DivElement tocElement = doc.createDivElement();
		root.appendChild(tocElement);
		tocElement.setAttribute("style", "text-align: center;");

		// TOC span
		SpanElement tocSpan = doc.createSpanElement();
		tocElement.appendChild(tocSpan);
		tocElement.setAttribute("style",
				"text-decoration: underline; font-weight: bold;");
		tocElement.setInnerText("Table of Contents");

		// TOC Items
		DivElement tocItems = doc.createDivElement();
		root.appendChild(tocItems);
		tocItems.setAttribute("style", "height: 260px; overflow: auto;");

		// TOC Items table
		tocItemsTable = doc.createDivElement();
		tocItems.appendChild(tocItemsTable);
		tocItemsTable.setAttribute("style", "display: table;");

		// Button container
		DivElement butContainer = doc.createDivElement();
		root.appendChild(butContainer);
		butContainer
				.setAttribute("style",
						"text-align: center; width: 100%; margin-left: auto; margin-right: auto;");

		// MyCollections button Container
		myCollectionsButContainer = doc.createDivElement();
		butContainer.appendChild(myCollectionsButContainer);
		myCollectionsButContainer.setAttribute("style", "float: left;");

		// Mycollections button
		myCollectionBut = doc.createAnchorElement();
		myCollectionsButContainer.appendChild(myCollectionBut);
		myCollectionBut
				.setClassName("btn btn-mini btn-success bk-tocPage-myCollectionsBtn");
		myCollectionBut.setAttribute("rel", "tooltip");
		myCollectionBut.setTitle("Add/Remove from my collection");
		myCollectionIElement = doc.createElement("i");
		myCollectionBut.appendChild(myCollectionIElement);
		myCollectionIElement.setClassName("icon-thumbs-up");

		// Comment Rating button container
		crButContainer = doc.createDivElement();
		butContainer.appendChild(crButContainer);
		crButContainer.setAttribute("style", "float: right;");

		// Comment Rating button button
		crBut = doc.createAnchorElement();
		crButContainer.appendChild(crBut);
		crBut.setClassName("btn btn-mini btn-success bk-tocPage-commentRatingBtn");
		crBut.setAttribute("rel", "tooltip");
		crBut.setTitle("Add or change a comment");
		crIElement = doc.createElement("i");
		crBut.appendChild(crIElement);
		crIElement.setClassName("icon-pencil");
	}

	/**
	 * @param count
	 *            - 1 based
	 * @param itemDesc
	 */
	public void addItem(int count, String itemDesc, String hoursOfWork,
			boolean highLight) {
		DivElement div = Document.get().createDivElement();
		div.setClassName("bk-toc-item");
		div.setAttribute("item-ref", "" + count);
		div.setInnerText("" + count + ". " + itemDesc + " (" + hoursOfWork
				+ ")");
		tocItemsTable.appendChild(div);
	}

	/**
	 * @param bookId
	 */
	public void setTOCBookId(String bookId) {
		root.setAttribute("bookId", bookId);
	}

	public void setMyCollectionButId(String btnId) {
		myCollectionBut.setId(btnId + "_myCollectionBtnId");
	}

	public void setAddToMyCollectionBtn(boolean displayOnly) {
		myCollectionBut.removeAttribute("style");
		myCollectionBut
				.setClassName("btn btn-mini btn-success bk-tocPage-myCollectionsBtn");
		myCollectionIElement.setClassName("icon-thumbs-up");
	}

	public void setRemoveFromMyCollectionBtn(boolean displayOnly) {
		myCollectionBut.removeAttribute("style");
		myCollectionBut
				.setClassName("btn btn-mini btn-danger bk-tocPage-myCollectionsBtn");
		myCollectionIElement.setClassName("icon-thumbs-down");
	}

	public void setHideMyCollectionBtn() {
		myCollectionBut.setAttribute("style", "display: none;");
	}

	public void setHideCommentRatingBtn() {
		crBut.setAttribute("style", "display: none;");
	}

	public void setShowCommentRatingBtn() {
		crBut.removeAttribute("style");
	}

}