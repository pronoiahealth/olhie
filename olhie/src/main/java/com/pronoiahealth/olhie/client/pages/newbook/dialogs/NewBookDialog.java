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
package com.pronoiahealth.olhie.client.pages.newbook.dialogs;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.errai.databinding.client.BindableProxy;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.databinding.client.api.InitialState;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.SplitDropdownButton;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.impl.Validation;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.shared.annotations.New;
import com.pronoiahealth.olhie.client.shared.annotations.Update;
import com.pronoiahealth.olhie.client.shared.constants.ModeEnum;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.events.BookCategoryListRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCategoryListResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCoverListRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.BookCoverListResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.BookUpdateCommittedEvent;
import com.pronoiahealth.olhie.client.shared.events.BookUpdateEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewBookModalEvent;
import com.pronoiahealth.olhie.client.shared.exceptions.DataValidationException;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.shared.vo.RegistrationForm;
import com.pronoiahealth.olhie.client.widgets.bookcat.BookCategoryListWidget;
import com.pronoiahealth.olhie.client.widgets.bookcover.BookCoverListWidget;
import com.pronoiahealth.olhie.client.widgets.booklargeshow.LargeBookWidget;

/**
 * NewBookDialog.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 5, 2013
 * 
 */
public class NewBookDialog extends Composite {

	@Inject
	UiBinder<Widget, NewBookDialog> binder;

	@Inject
	private DataBinder<Book> dataBinder;

	@Inject
	private ClientUserToken clientToken;

	private Book book;

	/**
	 * Is this a new book or are we editing a previously created one
	 */
	private ModeEnum mode;

	@UiField
	public Modal newBookModal;

	@UiField
	public WellForm newBookForm;

	@UiField
	public TextBox bookTitle;

	@UiField
	public ControlGroup bookTitleCG;

	@UiField
	public TextArea introduction;

	@UiField
	public ControlGroup introductionCG;

	@UiField
	public TextBox keywords;

	@UiField
	public ControlGroup keywordsCG;

	@UiField
	public SplitDropdownButton catagoryDropDown;

	@UiField
	public ControlGroup categoryCG;

	@UiField
	public SplitDropdownButton bookCoverDropDown;

	@UiField
	public ControlGroup bookCoverCG;

	@UiField
	public Column bookDesignCol;

	@UiField
	public CheckBox publishCB;

	@UiField
	public Label bookDisplayTitle;

	@UiField
	public LargeBookWidget largeBookWidget;

	@UiField
	public Button submitButton;

	private ClickHandler categoryClickedHandler;

	private ClickHandler coverClickedHandler;

	@Inject
	private PageNavigator nav;

	@Inject
	private Event<BookCategoryListRequestEvent> bookCategoryListRequestEvent;

	@Inject
	private Event<BookCoverListRequestEvent> bookCoverListRequestEvent;

	@Inject
	@New
	private Event<BookUpdateEvent> newBookUpdateEvent;

	@Inject
	@Update
	private Event<BookUpdateEvent> updateBookUpdateEvent;

	/**
	 * Constructor
	 * 
	 */
	public NewBookDialog() {
	}

	/**
	 * Init the display and set its style. Load the category list box
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));
		newBookModal.setStyleName("ph-NewBook-Modal", true);
		newBookModal.setStyleName("ph-NewBook-Modal-Size", true);
		bookDesignCol.getElement().setAttribute("style", "margin-left: 60px;");
		publishCB.setStyleName("ph-NewBook-Published-CB", true);

		// Set up click events
		// When the user clicks one of the categories then set the text of the
		// drop down to the category name and change the largeBookWidget
		categoryClickedHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object obj = event.getSource();
				if (obj instanceof IconAnchor) {
					IconAnchor a = (IconAnchor) obj;

					// Update the label in the drop down
					catagoryDropDown.setText(a.getName());

					// update the book display
					String binderColor = a.getElement().getAttribute(
							BookCategoryListWidget.BINDER_COLOR_HOLDER);
					largeBookWidget.setBinderColor(binderColor);
				}
			}
		};

		// Update the drop down after a cover is selected and change the
		// largeBookWidget
		coverClickedHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Object obj = event.getSource();
				if (obj instanceof IconAnchor) {
					IconAnchor a = (IconAnchor) obj;

					// Set text in buton
					bookCoverDropDown.setText(a.getName());

					// Change book background
					String imgUrl = a.getElement().getAttribute(
							BookCoverListWidget.IMG_URL_HOLDER);
					largeBookWidget.setBackground(imgUrl);
				}
			}
		};

		// Lose focus handler to bookTile to update book title display
		bookTitle.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				bookDisplayTitle.setText(bookTitle.getText());
			}
		});

		// Bind fields
		book = dataBinder.bind(bookTitle, "bookTitle")
				.bind(introduction, "introduction").bind(keywords, "keywords")
				.bind(catagoryDropDown, "category")
				.bind(bookCoverDropDown, "coverName").bind(publishCB, "active")
				.getModel();
	}

	/**
	 * Watches for a list of categories to be returned and then loads the
	 * category list
	 * 
	 * @param bookCategoryListResponseEvent
	 */
	protected void observesBookCategoryListResponseEvent(
			@Observes BookCategoryListResponseEvent bookCategoryListResponseEvent) {
		catagoryDropDown.setText("Select a category");
		catagoryDropDown.getMenuWiget().clear();
		List<BookCategory> bookCategories = bookCategoryListResponseEvent
				.getBokkCategories();
		if (bookCategories != null) {
			for (BookCategory cat : bookCategories) {
				BookCategoryListWidget nav = new BookCategoryListWidget(cat);
				nav.addClickHandler(categoryClickedHandler);
				catagoryDropDown.getMenuWiget().add(nav);

				// If editing set the binder color
				if (mode.equals(ModeEnum.EDIT)
						&& cat.getCatagory().equals(book.getCategory())) {
					largeBookWidget.setBinderColor(cat.getColor());
				}
			}
		}
	}

	/**
	 * Watches for a list of covers to be returned and then loads the cover list
	 * 
	 * @param bookCoverListResponseEvent
	 */
	protected void observesBookCoverListResponseEvent(
			@Observes BookCoverListResponseEvent bookCoverListResponseEvent) {
		bookCoverDropDown.setText("Select a book cover");
		bookCoverDropDown.getMenuWiget().clear();
		List<BookCover> bookCovers = bookCoverListResponseEvent.getBookCover();
		if (bookCovers != null) {
			for (BookCover cover : bookCovers) {
				BookCoverListWidget nav = new BookCoverListWidget(cover);
				nav.addClickHandler(coverClickedHandler);
				bookCoverDropDown.getMenuWiget().add(nav);

				// If editing set the backgroup
				if (mode.equals(ModeEnum.EDIT)
						&& cover.getCoverName().equals(book.getCoverName())) {
					largeBookWidget.setBackground(cover.getImgUrl());
				}
			}
		}
	}

	/**
	 * When the update is committed the close the dialog
	 * 
	 * @param bookUpdateCommittedEvent
	 */
	protected void observesBookUpdateCommittedEvent(
			@Observes BookUpdateCommittedEvent bookUpdateCommittedEvent) {
		newBookModal.hide();
		Multimap<String, Object> map = ArrayListMultimap.create();
		map.put("bookId", bookUpdateCommittedEvent.getBookId());
		nav.performTransition(NavEnum.NewBookPage.toString(), map);
	}

	/**
	 * Shows the modal dialog
	 */
	public void show() {
		// Show modal
		newBookModal.show();
	}

	/**
	 * Resets the form for a new book entry
	 */
	private void resetFormForNewMode() {
		// / Set mode
		this.mode = ModeEnum.NEW;

		// Clear form
		this.book = new Book();
		this.dataBinder.setModel(this.book, InitialState.FROM_MODEL);
		newBookForm.reset();
		clearErrors();

		// Clear form display
		bookDisplayTitle.setText(null);
		largeBookWidget.setBackground(null);
		largeBookWidget.setBinderColor(null);

		// Get the lists
		bookCategoryListRequestEvent.fire(new BookCategoryListRequestEvent());
		bookCoverListRequestEvent.fire(new BookCoverListRequestEvent());
	}

	private void setFormForEdit(Book theBook) {
		this.mode = ModeEnum.EDIT;
		this.book = theBook;
		this.dataBinder.setModel(this.book, InitialState.FROM_MODEL);

		// Clear form display
		bookDisplayTitle.setText(book.getBookTitle());
		largeBookWidget.setBackground(null);
		largeBookWidget.setBinderColor(null);

		// Get the lists
		bookCategoryListRequestEvent.fire(new BookCategoryListRequestEvent());
		bookCoverListRequestEvent.fire(new BookCoverListRequestEvent());
	}

	/**
	 * Validates the form and fires a BookUpdateRequestEvent with the validated
	 * data.
	 * 
	 * @param clickEvt
	 */
	@UiHandler("submitButton")
	public void handleSubmitButtonClick(ClickEvent clickEvt) {
		try {
			// Validate form
			Book book = validateForm();

			// Add creator id
			book.setAuthorId(clientToken.getUserId());

			// Fire event and wait for BookUpdateCommittedEvent
			if (mode.equals(ModeEnum.NEW)) {
				newBookUpdateEvent.fire(new BookUpdateEvent(book));
			} else {
				updateBookUpdateEvent.fire(new BookUpdateEvent(book));
			}
		} catch (Exception e) {
			// Intentionally blank. The Form will post error
			// messages to the form during the validation process
		}
	}

	/**
	 * Validates the form and sets errors when needed
	 * 
	 * @return
	 * @throws DataValidationException
	 */
	public Book validateForm() throws DataValidationException {
		clearErrors();
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();

		// proxy needs to be unwrapped to work with the validator
		Book book = getUnwrappedModelData();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);

		// Check the easy stuff
		boolean foundError = false;
		for (ConstraintViolation<Book> cv : violations) {
			String prop = cv.getPropertyPath().toString();
			if (prop.equals("bookTitle")) {
				bookTitleCG.setType(ControlGroupType.ERROR);
				foundError = true;
			} else if (prop.equals("introduction")) {
				introductionCG.setType(ControlGroupType.ERROR);
				foundError = true;
			} else if (prop.equals("keywords")) {
				keywordsCG.setType(ControlGroupType.ERROR);
				foundError = true;
			}
		}

		// Check the category and book drop down
		String cat = catagoryDropDown.getText().trim();
		if (cat.trim().equals("Select a category")) {
			categoryCG.setType(ControlGroupType.ERROR);
			foundError = true;
		} else {
			book.setCategory(cat);
		}

		String cov = bookCoverDropDown.getText().trim();
		if (cov.trim().equals("Select a book cover")) {
			bookCoverCG.setType(ControlGroupType.ERROR);
			foundError = true;
		} else {
			book.setCoverName(cov);
		}

		if (foundError == false) {
			return book;
		} else {
			throw new DataValidationException();
		}
	}

	/**
	 * Clear form errors
	 */
	private void clearErrors() {
		bookTitleCG.setType(ControlGroupType.NONE);
		introductionCG.setType(ControlGroupType.NONE);
		keywordsCG.setType(ControlGroupType.NONE);
		categoryCG.setType(ControlGroupType.NONE);
		bookCoverCG.setType(ControlGroupType.NONE);
	}

	/**
	 * Get the current data in the form. This requires "unwrapping" the data
	 * model from the dataBinder.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Book getUnwrappedModelData() {
		return (Book) ((BindableProxy<RegistrationForm>) dataBinder.getModel())
				.unwrap();
	}

	/**
	 * Shows the modal dialog. If we are editing then sync up the model.
	 * 
	 * @param showNewBookModalEvent
	 */
	protected void observersShowNewBookModalEvent(
			@Observes ShowNewBookModalEvent showNewBookModalEvent) {
		Book theBook = showNewBookModalEvent.getEditBook();
		if (theBook != null) {
			setFormForEdit(theBook);
		} else {
			resetFormForNewMode();
		}
		show();
	}

}
