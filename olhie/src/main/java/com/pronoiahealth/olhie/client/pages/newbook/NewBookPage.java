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
import java.util.Map;
import java.util.Set;

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
import com.github.gwtbootstrap.client.ui.Image;
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
import com.pronoiahealth.olhie.client.clientfactories.ViewableContentType;
import com.pronoiahealth.olhie.client.navigation.AnonymousRole;
import com.pronoiahealth.olhie.client.pages.AbstractPage;
import com.pronoiahealth.olhie.client.shared.annotations.NewBook;
import com.pronoiahealth.olhie.client.shared.constants.BookAssetDataType;
import com.pronoiahealth.olhie.client.shared.constants.ModeEnum;
import com.pronoiahealth.olhie.client.shared.constants.UserBookRelationshipEnum;
import com.pronoiahealth.olhie.client.shared.events.book.AddBookToMyCollectionEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindByIdEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookFindResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.book.NewStarRatingEvent;
import com.pronoiahealth.olhie.client.shared.events.book.RemoveBookassetdescriptionEvent;
import com.pronoiahealth.olhie.client.shared.events.book.RemoveBookassetdescriptionResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.RemoveBookFromMyCollectionEvent;
import com.pronoiahealth.olhie.client.shared.events.local.BookContentUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.DownloadBookAssetEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageHidingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageShowingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddLogoModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewAssetModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewBookModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowViewBookassetDialogEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.utils.Utils;
import com.pronoiahealth.olhie.client.widgets.DownloadBookassetButton;
import com.pronoiahealth.olhie.client.widgets.FlexTableExt;
import com.pronoiahealth.olhie.client.widgets.RemoveBookassetdescriptionButton;
import com.pronoiahealth.olhie.client.widgets.ViewBookassetButton;
import com.pronoiahealth.olhie.client.widgets.rating.StarRating;
import com.pronoiahealth.olhie.client.widgets.rating.StarRatingStarClickedHandler;

/**
 * NewBookPage.java<br/>
 * Responsibilities:<br/>
 * 1. Handle the creation of a new Book<br/>
 * 2. Handle the updating of a Book<br/>
 * 3. Handle the viewing of a books contents<br/>
 * 
 * <p>
 * This page will set the view mode (edit or view) based on the relationship
 * between the book (from bookId) and the user who is currently logged in.
 * </p>
 * 
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 * 
 */
@Page(role = { AnonymousRole.class })
public class NewBookPage extends AbstractPage {

	private static final DateTimeFormat dtf = DateTimeFormat
			.getFormat("MM/dd/yyyy");

	@Inject
	private ClientUserToken clientUser;

	@Inject
	@ViewableContentType
	private Map<String, String> viewableContentType;

	@Inject
	UiBinder<Widget, NewBookPage> binder;

	@UiField
	public HTMLPanel newBookContainer;

	@UiField
	public HTMLPanel ratingWidgetContainer;

	@UiField
	public HTMLPanel starRatingPanel;

	@UiField
	public PageHeader bookTitle;

	@UiField
	public Button editBookButton;

	@UiField
	public Button logoBookButton;
	
	@UiField
	public Button commentBookButton;

	@UiField
	public Button addToCollectionBookButton;

	@UiField
	public Button removeFromCollectionBookButton;

	@PageState
	private String bookId;

	private ModeEnum editMode;

	private BookDisplay currentBookDisplay;

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
	public HTMLPanel logoImageDisplayDiv;

	@UiField
	public Image logoImageDisplay;

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
	private Event<RemoveBookassetdescriptionEvent> removeBookassetdescriptionEvent;

	@Inject
	private Event<ShowViewBookassetDialogEvent> showViewBookassetDialogEvent;

	@Inject
	private Event<ShowNewBookModalEvent> showNewBookModalEvent;

	@Inject
	private Event<ShowAddLogoModalEvent> showAddLogoModalEvent;

	@Inject
	private Event<BookListBookSelectedEvent> bookListBookSelectedEvent;

	@Inject
	private Event<AddBookToMyCollectionEvent> addBookToMyCollectionEvent;

	@Inject
	private Event<RemoveBookFromMyCollectionEvent> removeBookFromMyCollectionEvent;

	@Inject
	private Event<NewStarRatingEvent> newStarRatingEvent;

	@Inject
	@NewBook
	private Event<BookFindByIdEvent> bookFindByIdEvent;

	private ClickHandler downloadClickHandler;

	private ClickHandler removeClickHandler;

	private ClickHandler viewClickHandler;

	private FlexTableExt tocTable;

	private StarRating starRating;

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

		// Intro Hero
		introductionHero.setStyleName("ph-NewBook-Hero", true);
		tocHero.setStyleName("ph-NewBook-Hero", true);

		// logo display
		logoImageDisplay.getElement().setAttribute("style",
				"max-height: 50px; max-width: 100px;");

		// Create star rating
		starRating = new StarRating(5, false,
				new StarRatingStarClickedHandler() {
					@Override
					public void startClicked(int star) {
						// Add event to save data here
						newStarRatingEvent.fire(new NewStarRatingEvent(star,
								bookId));
					}
				});
		// Irritating Chrome issue fix
		starRating.getElement().setAttribute("style", "display: inline-block;");

		// Add it to the display
		starRatingPanel.add(starRating);

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

		this.removeClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object obj = event.getSource();
				if (obj instanceof RemoveBookassetdescriptionButton) {
					if (tocTable != null) {
						tocTable.clear();
					}
					RemoveBookassetdescriptionButton but = (RemoveBookassetdescriptionButton) obj;
					String bookAssetdescriptionId = but
							.getBookassetdescriptionId();
					removeBookassetdescriptionEvent
							.fire(new RemoveBookassetdescriptionEvent(
									bookAssetdescriptionId));
				}
			}
		};

		this.viewClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object obj = event.getSource();
				if (obj instanceof ViewBookassetButton) {
					ViewBookassetButton but = (ViewBookassetButton) obj;
					String bookassetId = but.getBookassetId();
					String viewType = but.getBookassetViewType();
					showViewBookassetDialogEvent
							.fire(new ShowViewBookassetDialogEvent(bookassetId,
									viewType));
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

	@PageShown
	protected void pageShown() {
		// Put the page components in view only state until the users role
		// with the book is determined
		setState(ModeEnum.VIEW);
		bookListBookSelectedEvent.fire(new BookListBookSelectedEvent(bookId));
	}

	/**
	 * Looks for the BookListBookSelectedResponseEvent in order to tell if the
	 * user is the author or co-author of the book. If author or co-author the
	 * page is set to the edit mode. If not the page is set to the view mode.
	 * Then sync the returned BookDisplay with the page view.
	 * 
	 * @param bookListBookSelectedResponseEvent
	 */
	protected void observesBookListBookSelectedResponseEvent(
			@Observes BookListBookSelectedResponseEvent bookListBookSelectedResponseEvent) {
		boolean isAuthorSelected = bookListBookSelectedResponseEvent
				.isAuthorSelected();
		if (isAuthorSelected == true) {
			setState(ModeEnum.EDIT);
		} else {
			setState(ModeEnum.VIEW);
		}

		// Set the book in display
		setCurrentBookInDisplay(
				bookListBookSelectedResponseEvent.getBookDisplay(),
				isAuthorSelected);

		// Set the buttons for adding to my collection
		if (editMode == ModeEnum.VIEW && clientUser.isLoggedIn()) {
			setForAddToMyCollection(bookListBookSelectedResponseEvent.getRels());
		} else {
			setAddToCollectionBookButtonVisibility(false);
			setRemoveFromMyCollectionBookButtonVisibility(false);
		}

		// Set the rating widget state
		setRatingWidgetState(bookListBookSelectedResponseEvent.getRels());
	}

	/**
	 * Set page components to match the editMode
	 * 
	 * @param mode
	 */
	private void setState(ModeEnum mode) {
		if (mode == ModeEnum.EDIT) {
			this.editMode = ModeEnum.EDIT;
			tocAddElementContainer.setVisible(true);
			editBookButton.setVisible(true);
			logoBookButton.setVisible(true);
		} else {
			this.editMode = ModeEnum.VIEW;
			tocAddElementContainer.setVisible(false);
			editBookButton.setVisible(false);
			logoBookButton.setVisible(false);
		}
	}

	private void setRatingWidgetState(Set<UserBookRelationshipEnum> rels) {
		// In the edit mode the widget is only displayed
		// When in the edit mode you are logged in
		if (this.editMode == ModeEnum.EDIT) {
			ratingWidgetContainer.setVisible(true);
			starRating.setReadOnly(true);
		} else {
			if (clientUser.isLoggedIn() == true) {
				if (hasBookRelationship(rels) == true
						&& (rels.contains(UserBookRelationshipEnum.COAUTHOR) || rels
								.contains(UserBookRelationshipEnum.CREATOR))) {
					ratingWidgetContainer.setVisible(true);
					starRating.setReadOnly(true);
				} else {
					// You are logged in and have a MYCOLLECTION relationship
					ratingWidgetContainer.setVisible(true);
					starRating.setReadOnly(false);
				}
			} else {
				ratingWidgetContainer.setVisible(false);
			}
		}
	}

	/**
	 * Sets the addToCollectionBookButton visibility
	 * 
	 * @param rel
	 */
	private void setForAddToMyCollection(Set<UserBookRelationshipEnum> rels) {
		// Edit mode means you are the author or co-author. The book is
		// automatically in your collection
		if (this.editMode == ModeEnum.EDIT) {
			setAddToCollectionBookButtonVisibility(false);
			setRemoveFromMyCollectionBookButtonVisibility(false);
		} else {
			if (clientUser.isLoggedIn()) {
				// You have no relation and you are a logged in user
				if (hasBookRelationship(rels) == true
						&& rels.contains(UserBookRelationshipEnum.NONE)) {
					setAddToCollectionBookButtonVisibility(true);
					setRemoveFromMyCollectionBookButtonVisibility(false);
				} else if (hasBookRelationship(rels) == true
						&& rels.contains(UserBookRelationshipEnum.MYCOLLECTION)) {
					// You are logged in and have a MYCOLLECTION relationship
					setAddToCollectionBookButtonVisibility(false);
					setRemoveFromMyCollectionBookButtonVisibility(true);
				} else {
					// Can't determine it, don't show anything
					setAddToCollectionBookButtonVisibility(false);
					setRemoveFromMyCollectionBookButtonVisibility(false);
				}
			} else {
				setAddToCollectionBookButtonVisibility(false);
				setRemoveFromMyCollectionBookButtonVisibility(false);
			}
		}
	}

	/**
	 * Does the relationship set have any values
	 * 
	 * @param rels
	 * @return
	 */
	private boolean hasBookRelationship(Set<UserBookRelationshipEnum> rels) {
		if (rels != null && rels.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Set button visibility
	 * 
	 * @param visible
	 */
	private void setAddToCollectionBookButtonVisibility(boolean visible) {
		addToCollectionBookButton.setVisible(visible);
	}

	/**
	 * Set button visibility
	 * 
	 * @param visible
	 */
	private void setRemoveFromMyCollectionBookButtonVisibility(boolean visible) {
		removeFromCollectionBookButton.setVisible(visible);
	}

	/**
	 * Watches for the book content update event. If the book id matchs the one
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
			bookFindByIdEvent.fire(new BookFindByIdEvent(bookId));
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
		setCurrentBookInDisplay(bookFindResponseEvent.getBookDisplay(),
				bookFindResponseEvent.isAuthorSelected());

		// Set the buttons for adding to my collection
		if (editMode == ModeEnum.VIEW && clientUser.isLoggedIn()) {
			setForAddToMyCollection(bookFindResponseEvent.getRels());
		} else {
			setAddToCollectionBookButtonVisibility(false);
		}

		// Set the rating widget state
		setRatingWidgetState(bookFindResponseEvent.getRels());
	}

	/**
	 * Sync returned book and display
	 * 
	 * @param bookDisplay
	 */
	private void setCurrentBookInDisplay(BookDisplay bookDisplay,
			boolean isAuthorCoauthorViewing) {
		// Set page background to the book cover background
		setPageBackgroundStyle(bookDisplay.getBookCover().getImgUrl());

		// Set the fields
		this.currentBookDisplay = bookDisplay;
		Book book = currentBookDisplay.getBook();
		bookTitle.setText(book.getBookTitle());
		authorLbl.setText("by " + bookDisplay.getAuthorFullName());
		String createdDateFt = book.getCreatedDate() != null ? dtf.format(book
				.getCreatedDate()) : "";
		String publDateFt = book.getActDate() != null ? dtf.format(book
				.getActDate()) : "Not yet published";
		createdPublishedCategoryLbl.setHTML(NewBookMessages.INSTANCE
				.setCreatedPublishedCategoryLbl(createdDateFt, publDateFt,
						book.getCategory()));

		// Set the logo if there
		if (bookDisplay.isBookLogo() == true) {
			logoImageDisplay.setUrl(Utils
					.buildRestServiceForLogoDownloadLink(bookId));
		} else {
			logoImageDisplay.setUrl("");
		}

		// Introduction text
		introductionTxt.setHTML(NewBookMessages.INSTANCE
				.setIntroductionText(book.getIntroduction()));

		// TOC container elements
		// Clear current values
		addTOCElementContainer.clear();
		tocTable = new FlexTableExt();
		List<Bookassetdescription> descriptions = bookDisplay
				.getBookAssetDescriptions();
		for (int i = 0; i < descriptions.size(); i++) {
			Bookassetdescription bad = descriptions.get(i);
			Object[] data = new Object[3];
			data[0] = NewBookMessages.INSTANCE.createTOCNumber("" + (i + 1));
			data[1] = bad.getDescription();
			String createdDt = dtf.format(bad.getCreatedDate());
			Bookasset ba = bad.getBookAssets().get(0);
			String baId = ba.getId();
			String badId = bad.getId();
			ButtonGroup btns = this.makeButtonGroup(ba.getContentType(),
					ba.getItemType(), badId, baId);
			data[2] = btns;
			tocTable.addRow(data, new String[] { "ph-NewBook-TOC-NumberCol",
					"ph-NewBook-TOC-DescriptionCol",
					"ph-NewBook-TOC-ButtonsCol" }, "Details",
					NewBookMessages.INSTANCE.setCreatedDateText(createdDt), 0);
		}
		addTOCElementContainer.add(tocTable);

		// Set star rating here
		if (isAuthorCoauthorViewing == true) {
			starRating.setRating(bookDisplay.getBookRating());
		} else {
			starRating.setRating(bookDisplay.getUserBookRating());
		}

		// Adjust display size
		adjustSize();
	}

	/**
	 * Make a button grpup with the correbt buttons based on the params
	 * 
	 * @param contentType
	 * @param itemTypeStr
	 * @param baId
	 * @return
	 */
	private ButtonGroup makeButtonGroup(String contentType, String itemTypeStr,
			String badId, String baId) {
		// Group of buttons to return
		ButtonGroup btns = new ButtonGroup();

		// Download button
		BookAssetDataType itemType = BookAssetDataType.valueOf(itemTypeStr);
		if (itemType.equals(BookAssetDataType.FILE)) {
			btns.add(new DownloadBookassetButton(downloadClickHandler, baId));
		}

		// Can this content be viewed in an iFrame
		String val = viewableContentType.get(contentType);
		if (val != null) {
			btns.add(new ViewBookassetButton(viewClickHandler, baId, val));
		}

		// Always add remove when editing
		if (editMode == ModeEnum.EDIT) {
			btns.add(new RemoveBookassetdescriptionButton(removeClickHandler,
					badId));
		}

		// Return the buttons
		return btns;
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
		if (editMode == ModeEnum.EDIT) {
			showNewAssetModalEvent.fire(new ShowNewAssetModalEvent(bookId));
		}
	}

	@UiHandler("editBookButton")
	public void editBookButtonClicked(ClickEvent event) {
		if (editMode == ModeEnum.EDIT) {
			showNewBookModalEvent.fire(new ShowNewBookModalEvent(ModeEnum.EDIT,
					this.currentBookDisplay.getBook()));
		}
	}

	@UiHandler("logoBookButton")
	public void editLogoButtonClicked(ClickEvent event) {
		if (editMode == ModeEnum.EDIT) {
			showAddLogoModalEvent.fire(new ShowAddLogoModalEvent(
					this.currentBookDisplay.getBook().getId()));
		}
	}
	
	@UiHandler("commentBookButton")
	public void commentBookButtonClicked(ClickEvent event) {
		if (editMode == ModeEnum.VIEW) {
			
		}
	}

	@UiHandler("addToCollectionBookButton")
	public void addToCollectionBookButtonClicked(ClickEvent event) {
		if (editMode == ModeEnum.VIEW) {
			addBookToMyCollectionEvent.fire(new AddBookToMyCollectionEvent(
					this.currentBookDisplay.getBook().getId()));
		}
	}

	@UiHandler("removeFromCollectionBookButton")
	public void removeFromCollectionBookButtonClicked(ClickEvent event) {
		if (editMode == ModeEnum.VIEW) {
			this.removeBookFromMyCollectionEvent
					.fire(new RemoveBookFromMyCollectionEvent(
							this.currentBookDisplay.getBook().getId()));
		}
	}
}
