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

import static com.google.gwt.query.client.GQuery.$;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageHidden;
import org.jboss.errai.ui.nav.client.local.PageHiding;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.nav.client.local.PageState;

import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Hero;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavPills;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventListener;
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
import com.pronoiahealth.olhie.client.pages.newbook.widgets.BaseBookassetActionButtonWidget;
import com.pronoiahealth.olhie.client.pages.newbook.widgets.BookIntroductionDetailWidget;
import com.pronoiahealth.olhie.client.pages.newbook.widgets.NewBookButtonHolderWidget;
import com.pronoiahealth.olhie.client.pages.newbook.widgets.NewBookDroppablePanel;
import com.pronoiahealth.olhie.client.shared.constants.BookImageSizeEnum;
import com.pronoiahealth.olhie.client.shared.constants.ModeEnum;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.DestroyPageWhenHiddenEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageHidingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.NewBookPageShowingEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddBookCommentModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowAddLogoModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowBookCommentsModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewAssetModalEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewBookModalEvent;
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
@Dependent
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
	private Event<DestroyPageWhenHiddenEvent> destroyPageWhenHiddenEvent;

	@UiField
	public HTMLPanel buttonGrpHolder;

	public GQuery authorViewCommentsButton;

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

	@Inject
	private NewBookButtonHolderWidget newBookButtonHolderWidget;

	@Inject
	private Instance<BaseBookassetActionButtonWidget> baseBookassetActionButtonWidgetFac;

	@Inject
	private Disposer<BaseBookassetActionButtonWidget> baseBookassetActionButtonWidgetDisposer;

	@Inject
	private Instance<NewBookPageEventHandler> newBookEventHandlerFac;

	@Inject
	private Disposer<NewBookPageEventHandler> newBookEventHandlerDisposer;

	private NewBookPageEventHandler currentNewBookEventHandler;

	/**
	 * Default Constructor
	 * 
	 */
	public NewBookPage_2() {
	}

	/**
	 * Clean up BaseBookassetActionButtonWidget created with instance factory.
	 * Destroy both the logical part (the widget) and the physical part (the DOM
	 * elements.
	 */
	@Override
	protected void onUnload() {
		super.onUnload();
		
		// Remove the event handler
		if (currentNewBookEventHandler != null) {
			newBookEventHandlerDisposer.dispose(currentNewBookEventHandler);
		}
		
		// Remove the attached widgets
		if (newBookButtonHolderWidget != null) {
			// Destroy the BaseBookassetActionButtonWidget
			Element el = newBookButtonHolderWidget.getElement();
			int cnt = DOM.getChildCount(el);
			for (int i = 0; i < cnt; i++) {
				Element e = DOM.getChild(el, i);
				EventListener listener = DOM
						.getEventListener((com.google.gwt.user.client.Element) e);
				// No listener attached to the element, so no widget exist for
				// this element
				if (listener != null
						&& listener instanceof BaseBookassetActionButtonWidget) {
					baseBookassetActionButtonWidgetDisposer
							.dispose((BaseBookassetActionButtonWidget) listener);
				}
			}

			// Remove the elements from the DOM
			while (el.hasChildNodes()) {
				el.removeChild(el.getFirstChild());
			}
		}
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

		// build event handler
		currentNewBookEventHandler = newBookEventHandlerFac.get();
		currentNewBookEventHandler.attach(this);
	}

	/**
	 * Author buttons
	 */
	private void buildAndSetAuthorBtns() {
		// logo
		BaseBookassetActionButtonWidget but = baseBookassetActionButtonWidgetFac
				.get();
		Element lE = but.createAndBindButton("btn-primary", "Add a Logo image",
				"icon-picture", null, "icon-small", false);
		newBookButtonHolderWidget.getElement().appendChild(lE);
		$(lE).bind(com.google.gwt.user.client.Event.ONCLICK, new Function() {
			@Override
			public boolean f(com.google.gwt.user.client.Event e) {
				showAddLogoModalEvent.fire(new ShowAddLogoModalEvent(
						currentBookDisplay.getBook().getId()));
				return false;
			}
		});

		// comments
		but = baseBookassetActionButtonWidgetFac.get();
		Element authorViewCommentsButtonElement = but.createAndBindButton(
				"btn-primary", "View book comments", "icon-comments", null,
				"icon-small", false);
		newBookButtonHolderWidget.getElement().appendChild(
				authorViewCommentsButtonElement);
		this.authorViewCommentsButton = $(authorViewCommentsButtonElement);
		authorViewCommentsButton.bind(com.google.gwt.user.client.Event.ONCLICK,
				new Function() {
					@Override
					public boolean f(com.google.gwt.user.client.Event e) {
						String val = authorViewCommentsButton.attr("disabled");
						if (!val.equals("disabled")) {
							showBookCommentsModalEvent
									.fire(new ShowBookCommentsModalEvent(
											currentBookDisplay.getBook()
													.getId(),
											currentBookDisplay.getBook()
													.getBookTitle()));
						}
						return false;
					}
				});

		// Edit
		but = baseBookassetActionButtonWidgetFac.get();
		Element eE = but
				.createAndBindButton("btn-primary", "Edit the Book details",
						"icon-edit", null, "icon-small", false);
		newBookButtonHolderWidget.getElement().appendChild(eE);
		$(eE).bind(com.google.gwt.user.client.Event.ONCLICK, new Function() {
			@Override
			public boolean f(com.google.gwt.user.client.Event e) {
				showNewBookModalEvent.fire(new ShowNewBookModalEvent(
						ModeEnum.EDIT, currentBookDisplay.getBook()));
				return false;
			}
		});

		// Add to the button holder
		// buttonGrpHolder.add(authorBtns);
		buttonGrpHolder.add(newBookButtonHolderWidget);
	}

	@PageHidden
	protected void pageHidden() {
		destroyPageWhenHiddenEvent.fire(new DestroyPageWhenHiddenEvent(
				NavEnum.NewBookPage_2));
	}

	/**
	 * When the page is hiding tell the Header so it can add the new book button
	 * back to the menu. Also, deactivate all the features activated in
	 * pageShown.
	 */
	@PageHiding
	protected void pageHiding() {
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
	 * Sync returned book and display
	 * 
	 * @param bookDisplay
	 */
	public void setCurrentBookInDisplay(BookDisplay bookDisplay) {
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
			authorViewCommentsButton.attr("disabled", Boolean.FALSE);
		} else {
			authorViewCommentsButton.attr("disabled", Boolean.TRUE);
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
		// authorBtns.setVisible(isUserAuthorOrCoAuthor);
		newBookButtonHolderWidget.setVisible(isUserAuthorOrCoAuthor);
		tocAddElementContainer.setVisible(isUserAuthorOrCoAuthor);

	}

	/**
	 * Adjusts component sizes in response to window size changes
	 */
	public void adjustSize() {
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