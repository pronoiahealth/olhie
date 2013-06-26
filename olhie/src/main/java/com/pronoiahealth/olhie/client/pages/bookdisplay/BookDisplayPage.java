package com.pronoiahealth.olhie.client.pages.bookdisplay;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Page;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.navigation.AnonymousRole;
import com.pronoiahealth.olhie.client.pages.MenuSyncSecureAbstractPage;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.widgets.booklist3d.BookList3D;

@Page(role = { AnonymousRole.class })
public class BookDisplayPage extends MenuSyncSecureAbstractPage {

	@Inject
	UiBinder<Widget, BookDisplayPage> binder;

	@UiField
	public HTMLPanel bookListContainer;

	@UiField
	public HTMLPanel container;

	private BookList3D bookList;

	public BookDisplayPage() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		container.getElement().setAttribute("style", "overflow-y: auto;");
		bookListContainer.getElement().setAttribute("style",
				"margin-top: 60px;");

		// Sample
		// Some books
		/*
		List<Book> books = new ArrayList<Book>();
		for (int i = 0; i < 10; i++) {
			Book book = new Book();
			book.setAuthorId("jdestef");
			book.setBookTitle("This is a book title");
			book.setIntroduction("This is the books introduction.");
			List<Bookassetdescription> descs = new ArrayList<Bookassetdescription>();
			book.setBookDescriptions(descs);
			Bookassetdescription baDesc = new Bookassetdescription();
			baDesc.setDescription("This is a test book description");
			Bookassetdescription baDesc2 = new Bookassetdescription();
			baDesc2.setDescription("This is a test book description2 ");
			descs.add(baDesc);
			descs.add(baDesc2);
			books.add(book);
		}

		// List
		bookList = new BookList3D(books);
		bookListContainer.add(bookList);
		*/
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		setPageBackgroundClass("ph-BulletinBoard-Background");
		adjustSize();
	}

	/**
	 * Need to adjust the search results panel dynamically
	 * 
	 * @param event
	 */
	public void observesWindowResizeEvent(@Observes WindowResizeEvent event) {
		adjustSize();
	}

	private void adjustSize() {
		if (isAttached() == true) {
			// Difference from window height
			int wndHeight = this.getPageContainerHeight();

			container.setHeight(""
					+ wndHeight + "px");
		}
	}
}
