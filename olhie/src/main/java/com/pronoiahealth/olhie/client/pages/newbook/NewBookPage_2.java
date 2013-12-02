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

import static com.arcbees.gquery.tooltip.client.Tooltip.Tooltip;
import static com.google.gwt.query.client.GQuery.$;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageHiding;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.nav.client.local.PageState;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Hero;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavPills;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.AuthorRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;
import com.pronoiahealth.olhie.client.pages.newbook.features.AddFileDialogHandlerFeature;
import com.pronoiahealth.olhie.client.pages.newbook.features.AddLinkDialogHandlerFeature;
import com.pronoiahealth.olhie.client.pages.newbook.features.AddLogoDialogHandlerFeature;
import com.pronoiahealth.olhie.client.pages.newbook.features.AddYouTubeDialogHandlerFeature;
import com.pronoiahealth.olhie.client.pages.newbook.features.BookDescriptionDetailDialogHandlerFeature;
import com.pronoiahealth.olhie.client.pages.newbook.features.NewAssetDialogHandlerFeature;
import com.pronoiahealth.olhie.client.pages.newbook.widgets.BookIntroductionDetailWidget;
import com.pronoiahealth.olhie.client.pages.newbook.widgets.NewBookDroppablePanel;
import com.pronoiahealth.olhie.client.shared.constants.BookImageSizeEnum;
import com.pronoiahealth.olhie.client.shared.constants.ModeEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.FindAuthorsBookByIdEvent;
import com.pronoiahealth.olhie.client.shared.events.book.RemoveBookassetdescriptionResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.local.BookContentUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageHidingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageShowingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddBookCommentModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddLogoModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowBookCommentsModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewAssetModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewBookModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.utils.Utils;
import com.pronoiahealth.olhie.client.widgets.rating.StarRating;

/**
 * NewBookPage.java<br/>
 * Responsibilities:<br/>
 * 1. Handle the creation of a new Book<br/>
 * 2. Handle the updating of a Book<br/>
 * 3. Handle the viewing of a books contents<br/>
 * 
 * <p>
 * This page uses a page state enum to set the view elements for the user and
 * there relationship to the book being viewed. Buttons are grouped according to
 * the state and are built dynamically.
 * </p>
 * 
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Page(role = { AuthorRole.class })
public class NewBookPage_2 extends AbstractPage {

	private static final DateTimeFormat dtf = DateTimeFormat
			.getFormat("MM/dd/yyyy");

	@Inject
	private ClientUserToken clientUser;

	@Inject
	UiBinder<Widget, NewBookPage_2> binder;

	@UiField
	public HTMLPanel newBookContainer;

	@UiField
	public HTMLPanel ratingWidgetContainer;

	@UiField
	public HTMLPanel starRatingPanel;

	@UiField
	public PageHeader bookTitle;

	@PageState
	private String bookId;

	private BookDisplay currentBookDisplay;

	@UiField
	public Heading authorLbl;

	@UiField
	public FluidRow createdPublishedCategoryRow;

	@UiField
	public Heading introductionHeader;

	@UiField
	public Hero introductionHero;

	@UiField
	public HTMLPanel bookIntroductionDetailWidgetContainer;

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

	@Inject
	private NewBookDroppablePanel itemsDndContainer;

	@UiField
	public NavPills tocAddElementContainer;

	@UiField
	public NavLink tocAddElement;

	@Inject
	private BookIntroductionDetailWidget bookIntroductionDetailWidget;

	@Inject
	private Event<NewBookPageShowingEvent> newBookPageShowingEvent;

	@Inject
	private Event<NewBookPageHidingEvent> newBookPageHidingEvent;

	@Inject
	private Event<ShowNewBookModalEvent> showNewBookModalEvent;

	@Inject
	private Event<ShowAddLogoModalEvent> showAddLogoModalEvent;

	@Inject
	private Event<ShowBookCommentsModalEvent> showBookCommentsModalEvent;

	@Inject
	private Event<ShowAddBookCommentModalEvent> showAddBookCommentModalEvent;

	@Inject
	private Event<BookListBookSelectedEvent> bookListBookSelectedEvent;

	@Inject
	private Event<ShowNewAssetModalEvent> showNewAssetModalEvent;

	@Inject
	private Event<FindAuthorsBookByIdEvent> bookFindByIdEvent;

	@UiField
	public HTMLPanel buttonGrpHolder;

	private ButtonGroup authorBtns;

	public Button editBookButton;

	public Button authorViewCommentsButton;

	public Button loggedInMyCollViewCommentsButton;

	public Button loggedInNotMyCollViewCommentsButton;

	public Button notLoggedInViewCommentsButton;

	public Button logoBookButton;

	public Button addCommentBookButton;

	public Button addCommentBookButton1;

	public Button addToCollectionBookButton;

	public Button removeFromCollectionBookButton;

	private StarRating starRating;

	@Inject
	private AddFileDialogHandlerFeature addFileDialogFeature;

	@Inject
	private NewAssetDialogHandlerFeature newAssetDialogFeature;

	@Inject
	private AddLogoDialogHandlerFeature addLogoDialogFeature;

	@Inject
	private BookDescriptionDetailDialogHandlerFeature bookDescriptionDetailDialogHandlerFeature;

	@Inject
	private AddYouTubeDialogHandlerFeature addYouTubeDialogHandlerFeature;

	@Inject
	private AddLinkDialogHandlerFeature addLinkDialogHandlerFeature;

	/**
	 * Default Constructor
	 * 
	 */
	public NewBookPage_2() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		// Initialize the composite
		initWidget(binder.createAndBindUi(this));
		bookTitle.setStyleName("ph-NewBook-BookTitle", true);
		authorLbl.setStyleName("ph-NewBook-Author", true);
		createdPublishedCategoryRow.setStyleName(
				"ph-NewBook-Created-Published-Category-Row", true);
		introductionHeader.setStyleName(
				"ph-NewBook-Introduction-Hero-Introduction-Header", true);
		introductionTxt.setStyleName(
				"ph-NewBook-Introduction-Hero-IntroductionTxt", true);
		tocHeader.setStyleName("ph-NewBook-Introduction-Hero-TOC-Header", true);
		tocAddElementContainer.setStyleName("ph-NewBook-TOC-Element-Container",
				true);

		// Intro Hero
		introductionHero.setStyleName("ph-NewBook-Hero", true);
		tocHero.setStyleName("ph-NewBook-Hero", true);

		// Build button groups
		buildAndSetAuthorBtns();

		// Book introduction
		bookIntroductionDetailWidgetContainer.add(bookIntroductionDetailWidget);

		// Create star rating
		starRating = new StarRating(5, true);

		// Irritating Chrome issue fix
		starRating.getElement().setAttribute("style", "display: inline-block;");

		// Add it to the display
		starRatingPanel.add(starRating);

		// Configure and add droppable panel
		itemsDndContainer.setPanelClass("ph-NewBook-ItemsListBox", true);
		addTOCElementContainer.add(itemsDndContainer);

		// Set page background to the book cover background
		setPageBackgroundClass("ph-NewBook-Background");
	}

	/**
	 * Author buttons
	 */
	private void buildAndSetAuthorBtns() {
		// Buttons for author or co-author
		authorBtns = new ButtonGroup();

		// logo
		logoBookButton = createButton("", IconType.PICTURE, ButtonType.PRIMARY,
				ButtonSize.SMALL, "Add a Logo image");
		logoBookButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showAddLogoModalEvent.fire(new ShowAddLogoModalEvent(
						currentBookDisplay.getBook().getId()));
			}
		});
		GQuery q = $(logoBookButton.getElement());
		$(logoBookButton.getElement()).as(Tooltip).tooltip();
		authorBtns.add(logoBookButton);
		// createTooltip(logoBookButton, "Add a Logo image", Placement.TOP);

		// comments
		authorViewCommentsButton = createButton("", IconType.COMMENTS,
				ButtonType.PRIMARY, ButtonSize.SMALL, "View book comments");
		authorViewCommentsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showBookCommentsModalEvent.fire(new ShowBookCommentsModalEvent(
						currentBookDisplay.getBook().getId(),
						currentBookDisplay.getBook().getBookTitle()));
			}
		});
		authorBtns.add(authorViewCommentsButton);
		// createTooltip(authorViewCommentsButton, "View book comments",
		// Placement.TOP);
		$(authorViewCommentsButton.getElement()).as(Tooltip).tooltip();

		// Edit
		editBookButton = createButton("", IconType.EDIT, ButtonType.PRIMARY,
				ButtonSize.SMALL, "Edit the Book details");
		editBookButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showNewBookModalEvent.fire(new ShowNewBookModalEvent(
						ModeEnum.EDIT, currentBookDisplay.getBook()));
			}
		});
		authorBtns.add(editBookButton);
		// createTooltip(editBookButton, "Edit the Book details",
		// Placement.TOP);
		$(editBookButton.getElement()).as(Tooltip).tooltip();

		// Add to the button holder
		buttonGrpHolder.add(authorBtns);
	}

	/**
	 * Create a button
	 * 
	 * @param caption
	 * @param iType
	 * @param bType
	 * @param bSize
	 * @return
	 */
	private Button createButton(String caption, IconType iType,
			ButtonType bType, ButtonSize bSize, String toolTipTxt) {
		Button btn = null;
		if (caption != null && caption.length() > 0) {
			btn = new Button(caption);
		} else {
			btn = new Button();
		}
		btn.setIcon(iType);
		btn.setType(bType);
		btn.setSize(bSize);
		btn.getElement().setAttribute("rel", "tooltip");
		btn.setTitle(toolTipTxt);
		return btn;
	}

	/**
	 * Set a tool tip on a widget
	 * 
	 * @param w
	 * @param message
	 * @param placement
	 */
	/*
	 * private void createTooltip(Widget w, String message, Placement placement)
	 * { Tooltip tooltip = new Tooltip(); tooltip.setWidget(w);
	 * tooltip.setText(message); tooltip.setPlacement(placement);
	 * //tooltip.reconfigure(); }
	 */

	/**
	 * When the page is hiding tell the Header so it can add the new book button
	 * back to the menu. Also, deactivate all the features activated in
	 * pageShown.
	 */
	@PageHiding
	protected void pageHidden() {
		// Remove page features
		addFileDialogFeature.deactivate();
		newAssetDialogFeature.deactivate();
		addLogoDialogFeature.deactivate();
		bookDescriptionDetailDialogHandlerFeature.deactivate();
		addYouTubeDialogHandlerFeature.deactivate();
		addLinkDialogHandlerFeature.deactivate();

		// Announce that the page is hidden
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
	 * Load features when page shown
	 */
	@PageShown
	protected void pageShown() {
		// Activate page features
		addFileDialogFeature.activate();
		newAssetDialogFeature.activate();
		addLogoDialogFeature.activate();
		bookDescriptionDetailDialogHandlerFeature.activate();
		addYouTubeDialogHandlerFeature.activate();
		addLinkDialogHandlerFeature.activate();

		// Call for the book by its ID
		bookListBookSelectedEvent.fire(new BookListBookSelectedEvent(bookId));
	}

	/**
	 * Looks for the BookListBookSelectedResponseEvent.
	 * 
	 * @param bookListBookSelectedResponseEvent
	 */
	protected void observesBookListBookSelectedResponseEvent(
			@Observes BookListBookSelectedResponseEvent bookListBookSelectedResponseEvent) {

		// Set the book in display
		setCurrentBookInDisplay(bookListBookSelectedResponseEvent
				.getBookDisplay());
	}

	/**
	 * Watches for the book content update event. If the book id matches the one
	 * showing then asks for the new data.
	 * 
	 * @param bookContentUpdatedEvent
	 */
	protected void observesBookContentUpdatedEvent(
			@Observes BookContentUpdatedEvent bookContentUpdatedEvent) {
		String id = bookContentUpdatedEvent.getBookId();
		callBookFindById(id);
	}

	/**
	 * Observes for a positive response to a remove and then fires a find book
	 * event
	 * 
	 * @param removeBookassetdescriptionEvent
	 */
	protected void observesRemoveBookassetdescriptionResponseEvent(
			@Observes RemoveBookassetdescriptionResponseEvent removeBookassetdescriptionResponseEvent) {
		String id = removeBookassetdescriptionResponseEvent.getBookId();
		callBookFindById(id);
	}

	/**
	 * Called by observesRemoveBookassetdescriptionResponseEvent and
	 * observersBookContentUpdatedEvent
	 * 
	 * @param id
	 */
	private void callBookFindById(String id) {
		// Test to see if the book id's match
		// If so ask for updated data
		if (id != null && id.equals(bookId)) {
			bookFindByIdEvent.fire(new FindAuthorsBookByIdEvent(bookId));
		}
	}

	/**
	 * From the bookId a request is made for the book from the
	 * whenPageShownCalled method. The response is received here and processed.
	 * If in the view mode the user-book relationship is checked to see if the
	 * addToMycollections button should be made visible.
	 * 
	 * @param bookFindResponseEvent
	 */
	protected void observesBookFindResponse(
			@Observes BookFindResponseEvent bookFindResponseEvent) {

		// Set page background to the book cover background
		setCurrentBookInDisplay(bookFindResponseEvent.getBookDisplay());
	}

	/**
	 * Sync returned book and display
	 * 
	 * @param bookDisplay
	 */
	private void setCurrentBookInDisplay(BookDisplay bookDisplay) {
		// Check to see if the current user is the author
		boolean isUserAuthorOrCoAuthor = bookDisplay.isUserIsAuthorCoAuthor();

		// Set the fields
		this.currentBookDisplay = bookDisplay;
		Book book = currentBookDisplay.getBook();
		bookTitle.setText(book.getBookTitle());
		authorLbl.setText("by " + bookDisplay.getAuthorFullName());

		// Set the book introduction detail panel
		String createdDateFt = book.getCreatedDate() != null ? dtf.format(book
				.getCreatedDate()) : "";
		String publDateFt = book.getActDate() != null ? dtf.format(book
				.getActDate()) : "Not yet published";
		bookIntroductionDetailWidget.setData(Utils
				.buildRestServiceForBookFrontCoverDownloadLink(bookId,
						BookImageSizeEnum.SMALL), createdDateFt, publDateFt,
				book.getCategory(),
				"" + currentBookDisplay.getBookHoursOfWork());

		// Introduction text
		introductionTxt.setHTML(NewBookMessages.INSTANCE
				.setIntroductionText(book.getIntroduction()));

		itemsDndContainer.setDescriptionItems(bookDisplay,
				isUserAuthorOrCoAuthor);

		// Set star rating here
		starRating.setRating(bookDisplay.getBookRating());

		// Comments
		if (bookDisplay.isHasComments() == true) {
			authorViewCommentsButton.setEnabled(true);
		} else {
			authorViewCommentsButton.setEnabled(false);
		}

		setControlVisibility(isUserAuthorOrCoAuthor);

		// Adjust display size
		adjustSize();
	}

	/**
	 * Sets control visibility based on the user being the author of the book
	 * 
	 * @param isUserAuthorOrCoAuthor
	 */
	private void setControlVisibility(boolean isUserAuthorOrCoAuthor) {
		authorBtns.setVisible(isUserAuthorOrCoAuthor);
		tocAddElementContainer.setVisible(isUserAuthorOrCoAuthor);

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
			if (wndHeight <= 242) {
				wndHeight = 242;
			}
			int newHeroHeight = wndHeight - 242;

			// Introduction
			introductionHero.setHeight("" + newHeroHeight + "px");
			introductionPanel.setHeight("" + (newHeroHeight - 90) + "px");

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