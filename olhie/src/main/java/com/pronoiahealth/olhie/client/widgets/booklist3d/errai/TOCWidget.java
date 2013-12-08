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
package com.pronoiahealth.olhie.client.widgets.booklist3d.errai;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;

/**
 * TOCWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 17, 2013
 * 
 */
@Dependent
@Templated("#toc")
public class TOCWidget extends Composite {
	private Element rootElement;

	@DataField("tocItems")
	private Element tocItems = DOM.createDiv();

	@DataField("tocItemsTable")
	private Element tocItemsTable = DOM.createDiv();

	@Inject
	@DataField("myCollectionButtonContainer")
	private MyCollectionButtonWidget myCollectionBtnContainer;

	@Inject
	@DataField("commentRatingButtonContainer")
	private CommentRatingButtonWidget commentRatingButtonContainer;

	@Inject
	private Instance<TOCDescriptionItemWidget> tOCDescriptionItemWidgetFac;

	/**
	 * Constructor
	 * 
	 */
	public TOCWidget() {
	}

	@PostConstruct
	protected void postConstruct() {
		rootElement = getElement();
	}

	/**
	 * @param count
	 *            - 1 based
	 * @param itemDesc
	 */
	public void addItem(int count, String itemDesc, String hoursOfWork,
			boolean highLight) {
		TOCDescriptionItemWidget widget = tOCDescriptionItemWidgetFac.get();
		// widget.setData(count, itemDesc, hoursOfWork);
		// Element div = DOM.createDiv();
		// div.setClassName("bk-toc-item");
		// div.setAttribute("item-ref", "" + count);
		// div.setInnerText("" + count + ". " + itemDesc + " (" + hoursOfWork +
		// ")");
		tocItemsTable.appendChild(widget.setData(count, itemDesc, hoursOfWork,
				highLight));
	}

	/**
	 * @param bookId
	 */
	public void setTOCBookId(String bookId) {
		rootElement.setAttribute("bookId", bookId);
	}

	public void setMyCollectionBtnAddToCollection(boolean displayOnly) {
		myCollectionBtnContainer.setAddToMyCollectionBtn(displayOnly);
	}

	public void setMyCollectionBtnRemoveFromCollection(boolean displayOnly) {
		myCollectionBtnContainer.setRemoveFromMyCollectionBtn(displayOnly);
	}

	public void setMyCollectionBtnHide() {
		myCollectionBtnContainer.setHideMyCollectionBtn();
	}

	public void setCommentRatingBtnHide() {
		commentRatingButtonContainer.setHideCommentRatingBtn();
	}

	public void setCommentRatingBtnShow() {
		commentRatingButtonContainer.setShowCommentRatingBtn();
	}
}
