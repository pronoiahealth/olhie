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
package com.pronoiahealth.olhie.client.pages.newbook;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageHiding;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageState;

import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Hero;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavPills;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.AuthorRole;
import com.pronoiahealth.olhie.client.pages.PageShownSecureAbstractPage;
import com.pronoiahealth.olhie.client.shared.annotations.NewBook;
import com.pronoiahealth.olhie.client.shared.events.BookFindByIdEvent;
import com.pronoiahealth.olhie.client.shared.events.BookFindResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.local.BookContentUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.DownloadBookAssetEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageHidingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageShowingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewAssetModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.widgets.DownloadBookassetButton;
import com.pronoiahealth.olhie.client.widgets.FlexTableExt;

/**
 * NewBookPage.java<br/>
 * Responsibilities:<br/>
 * 1. Handle the creation of a new Book<br/>
 * 2. Handle the updating of a Book<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Page(role = { AuthorRole.class })
public class NewBookPage extends PageShownSecureAbstractPage {

	private static final DateTimeFormat dtf = DateTimeFormat
			.getFormat("MM/dd/yyyy");

	@Inject
	UiBinder<Widget, NewBookPage> binder;

	@UiField
	public HTMLPanel newBookContainer;

	@UiField
	public PageHeader bookTitle;

	@PageState
	private String bookId;

	@UiField
	public Heading authorLbl;

	@UiField
	public FluidRow createdPublishedCategoryRow;

	@UiField
	public HTML createdPublishedCategoryLbl;

	@UiField
	public Heading introductionHeader;

	@UiField
	public Hero introductionHero;

	@UiField
	public ScrollPanel introductionPanel;

	@UiField
	public HTML introductionTxt;

	@UiField
	public Hero tocHero;

	@UiField
	public Heading tocHeader;

	@UiField
	public HTMLPanel addTOCElementContainer;

	@UiField
	public NavPills tocAddElementContainer;

	@UiField
	public NavLink tocAddElement;

	@Inject
	private Event<NewBookPageShowingEvent> newBookPageShowingEvent;

	@Inject
	private Event<NewBookPageHidingEvent> newBookPageHidingEvent;

	@Inject
	private Event<ShowNewAssetModalEvent> showNewAssetModalEvent;

	@Inject
	private Event<DownloadBookAssetEvent> downloadBookAssetEvent;

	@Inject
	@NewBook
	private Event<BookFindByIdEvent> bookFindByIdEvent;

	private ClickHandler downloadClickHandler;

	/**
	 * Default Constructor
	 * 
	 */
	public NewBookPage() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		bookTitle.setStyleName("ph-NewBook-BookTitle", true);
		authorLbl.setStyleName("ph-NewBook-Author", true);
		createdPublishedCategoryRow.setStyleName(
				"ph-NewBook-Created-Published-Category-Row", true);
		createdPublishedCategoryLbl.setStyleName(
				"ph-NewBook-Created-Published-Category", true);
		introductionHeader.setStyleName(
				"ph-NewBook-Introduction-Hero-Introduction-Header", true);
		introductionTxt.setStyleName(
				"ph-NewBook-Introduction-Hero-IntroductionTxt", true);
		tocHeader.setStyleName("ph-NewBook-Introduction-Hero-TOC-Header", true);
		tocAddElementContainer.setStyleName("ph-NewBook-TOC-Element-Container",
				true);

		this.downloadClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object obj = event.getSource();
				if (obj instanceof DownloadBookassetButton) {
					DownloadBookassetButton but = (DownloadBookassetButton) obj;
					String bookAssetId = but.getBookassetId();
					downloadBookAssetEvent.fire(new DownloadBookAssetEvent(
							bookAssetId));
				}
			}
		};
	}

	/**
	 * When the page is hiding tell the Header so it can add the new book button
	 * back to the menu.
	 */
	@PageHiding
	protected void pageHidden() {
		newBookPageHidingEvent.fire(new NewBookPageHidingEvent());
	}

	/**
	 * When the page is showing tell the Header so it can hide the new book
	 * button.
	 */
	@PageShowing
	protected void pageShowing() {
		newBookPageShowingEvent.fire(new NewBookPageShowingEvent());
	}

	/**
	 * Called when the page is shown. Sub class annotates the method that this
	 * method is invoked from with the @PageShown annotation. The size of the
	 * Heros are adjusted here also.
	 * 
	 * @see com.pronoiahealth.olhie.client.pages.PageShownSecureAbstractPage#whenPageShownCalled()
	 */
	@Override
	protected boolean whenPageShownCalled() {
		if (super.whenPageShownCalled() == true) {
			bookFindByIdEvent.fire(new BookFindByIdEvent(bookId));
			adjustSize();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Watches for the book content update event. If the book id matchs the one
	 * showing then asks for the new data.
	 * 
	 * @param bookContentUpdatedEvent
	 */
	protected void observersBookContentUpdatedEvent(
			@Observes BookContentUpdatedEvent bookContentUpdatedEvent) {

		// Test to see if the book id's match
		// If so ask for updated data
		String id = bookContentUpdatedEvent.getBookId();
		if (id != null && id.equals(bookId)) {
			bookFindByIdEvent.fire(new BookFindByIdEvent(bookId));
		}
	}

	/**
	 * From the bookId a request is make for the book from the
	 * whenPageShownCalled method. The response is received here and processed.
	 * 
	 * @param bookFindResponseEvent
	 */
	protected void observesBookFindResponse(
			@Observes BookFindResponseEvent bookFindResponseEvent) {
		// Set page background to the book cover background
		setPageBackgroundStyle(bookFindResponseEvent.getBookDisplay()
				.getBookCover().getImgUrl());

		// Set the fields
		Book book = bookFindResponseEvent.getBookDisplay().getBook();
		bookTitle.setText(book.getBookTitle());
		authorLbl.setText("by "
				+ bookFindResponseEvent.getBookDisplay().getAuthorFullName());
		String createdDateFt = book.getCreatedDate() != null ? dtf.format(book
				.getCreatedDate()) : "";
		String publDateFt = book.getPublishedDate() != null ? dtf.format(book
				.getPublishedDate()) : "Not yet published";
		createdPublishedCategoryLbl.setHTML(NewBookMessages.INSTANCE
				.setCreatedPublishedCategoryLbl(createdDateFt, publDateFt,
						book.getCategory()));
		introductionTxt.setHTML(NewBookMessages.INSTANCE
				.setIntroductionText(book.getIntroduction()));

		// TOC container elements
		// Clear current values
		addTOCElementContainer.clear();
		FlexTableExt tocTable = new FlexTableExt();
		List<Bookassetdescription> descriptions = bookFindResponseEvent
				.getBookDisplay().getBookAssetDescriptions();
		for (int i = 0; i < descriptions.size(); i++) {
			Bookassetdescription bad = descriptions.get(i);
			Object[] data = new Object[3];
			data[0] = NewBookMessages.INSTANCE.createTOCNumber("" + (i + 1));
			data[1] = bad.getDescription();
			String createdDt = dtf.format(bad.getCreatedDate());
			String baId = bad.getBookAssets().get(0).getId();
			ButtonGroup btns = new ButtonGroup();
			btns.add(new DownloadBookassetButton(downloadClickHandler, baId));
			//data[2] = new DownloadBookassetButton(downloadClickHandler, baId);
			data[2] = btns;
			tocTable.addRow(data,
					new String[] { "ph-NewBook-TOC-NumberCol",
							"ph-NewBook-TOC-DescriptionCol",
							"ph-NewBook-TOC-NumberCol" }, "Details",
					NewBookMessages.INSTANCE.setCreatedDateText(createdDt), 0);
		}
		addTOCElementContainer.add(tocTable);
	}

	/**
	 * Need to adjust the Heros dynamically
	 * 
	 * @param event
	 */
	protected void observesWindowResizeEvent(@Observes WindowResizeEvent event) {
		adjustSize();
	}

	/**
	 * Adjusts component sizes in response to window size changes
	 */
	private void adjustSize() {
		if (isAttached() == true) {
			// Difference from window height
			int wndHeight = this.getPageContainerHeight();

			// 145 at top + footer at bottom
			if (wndHeight <= 300) {
				wndHeight = 300;
			}
			int newHeroHeight = wndHeight - 300;

			// introduction
			introductionHero.setHeight("" + newHeroHeight + "px");
			introductionPanel.setHeight("" + (newHeroHeight - 40) + "px");

			// TOC
			tocHero.setHeight("" + newHeroHeight + "px");
			addTOCElementContainer.setHeight("" + (newHeroHeight - 55) + "px");
		}
	}

	/**
	 * Open the new asset dialog and send it the book Id
	 * 
	 * @param event
	 */
	@UiHandler("tocAddElement")
	public void tocAddElementClicked(ClickEvent event) {
		showNewAssetModalEvent.fire(new ShowNewAssetModalEvent(bookId));
	}
}
