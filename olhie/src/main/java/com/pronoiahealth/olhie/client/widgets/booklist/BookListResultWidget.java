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
package com.pronoiahealth.olhie.client.widgets.booklist;

import gwtquery.plugins.draggable.client.DraggableOptions;
import gwtquery.plugins.draggable.client.DraggableOptions.HelperType;
import gwtquery.plugins.draggable.client.DraggableOptions.RevertOption;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;
import gwtquery.plugins.droppable.client.events.DropEvent;
import gwtquery.plugins.droppable.client.events.DropEvent.DropEventHandler;
import gwtquery.plugins.droppable.client.gwt.DroppableWidget;

import com.github.gwtbootstrap.client.ui.Hero;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.vo.BookForDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookState;
import com.pronoiahealth.olhie.client.widgets.iosswitch.IOSSwitch;
import com.pronoiahealth.olhie.client.widgets.rating.StarRating;
import com.pronoiahealth.olhie.resources.MessageFormatResources;

/**
 * Used in the search screen results to display book detail<br/>
 * 
 * @author johndestefano
 * @version
 * @since
 * 
 */
public class BookListResultWidget extends Composite {

	interface BookListResultUiBinder extends
			UiBinder<Widget, BookListResultWidget> {
	}

	private static BookListResultUiBinder uiBinder = GWT
			.create(BookListResultUiBinder.class);

	@UiField
	Hero bookHero;

	@UiField
	ScrollPanel titleLabel;

	@UiField
	Label authorLabel;

	@UiField
	Label pubDateLabel;

	@UiField(provided = true)
	StarRating ratingLabel;

	@UiField
	HTMLPanel bookImagePanelHolder;

	@UiField
	HTMLPanel addToCollectionPanelHolder;

	private IOSSwitch checkBox;

	private BookImagePanelWidget bookImagePanelWidget;

	private BookForDisplay book;

	/**
	 * Constructor
	 * 
	 * @param book
	 */
	public BookListResultWidget(BookForDisplay book) {
		this.book = book;
		String dragContainmentMarker = "ph-SearchResults-BookListResult-"
				+ book.getId();
		ratingLabel = new StarRating(5, true);
		ratingLabel.setRating(book.getRating());
		initWidget(uiBinder.createAndBindUi(this));

		// Droppable addToCollectionText
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setStyleName("ph-SearchResults-BookListResult-AddToCollection");
		HTML html = new HTML("Add to your collection?");
		html.setStyleName("ph-SearchResults-BookListResult-AddToCollection-Text");
		vPanel.add(html);
		checkBox = new IOSSwitch();
		vPanel.add(checkBox);
		DroppableWidget<VerticalPanel> dropTarget = new DroppableWidget<VerticalPanel>(
				vPanel);
		dropTarget.setAccept(".ph-SearchResults-BookListResult-BookImagePanel");
		dropTarget
				.setDroppableHoverClass("ph-SearchResults-BookListResult-AddToCollection-HoverOver");
		dropTarget.addDropHandler(new DropEventHandler() {
			@Override
			public void onDrop(DropEvent event) {
				boolean checked = checkBox.isChecked();
				checkBox.setChecked(!checked);
			}
		});
		addToCollectionPanelHolder.add(dropTarget);

		// Draggable book image panel
		bookImagePanelWidget = new BookImagePanelWidget("white", book
				.getBookCover().getImgUrl(), book.getCatagory().getColor());
		if (book.getBookState() == BookState.BOOK_STATE_INVISIBLE) {
			bookImagePanelWidget.setVisible(false);
		}
		DraggableWidget<BookImagePanelWidget> bookImagePanelWidgetDraggable = new DraggableWidget<BookImagePanelWidget>(
				bookImagePanelWidget);
		bookImagePanelWidgetDraggable
				.setDraggableOptions(getDraggableOption(dragContainmentMarker));
		bookImagePanelHolder.add(bookImagePanelWidgetDraggable);

		// Set up styles on book hero
		bookHero.addStyleName("ph-SearchResults-BookListResult");
		bookHero.addStyleName(dragContainmentMarker);

		// Book detail
		titleLabel.add(new HTML(book.getTitle()));
		authorLabel.setText(MessageFormatResources.INSTANCE.setBookAuthor(book
				.getAuthorName()));
		pubDateLabel.setText(MessageFormatResources.INSTANCE
				.setBookPubDate(book.getPublishedDate()));
	}

	private DraggableOptions getDraggableOption(String dragContainmentMarker) {
		DraggableOptions options = new DraggableOptions();
		options.setCursor(Cursor.MOVE);
		options.setZIndex(100000);
		options.setRevert(RevertOption.ON_INVALID_DROP);
		options.setHelper(HelperType.CLONE);
		Float o = new Float(0.7);
		options.setOpacity(o);
		options.setContainment("." + dragContainmentMarker);
		return options;
	}

	public BookForDisplay getBook() {
		return book;
	}

	public void setBook(BookForDisplay book) {
		this.book = book;
	}

	public BookImagePanelWidget getBookImagePanelWidget() {
		return bookImagePanelWidget;
	}

	public void setBookImagePanelWidget(
			BookImagePanelWidget bookImagePanelWidget) {
		this.bookImagePanelWidget = bookImagePanelWidget;
	}
}
