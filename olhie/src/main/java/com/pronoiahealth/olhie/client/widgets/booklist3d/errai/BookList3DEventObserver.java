package com.pronoiahealth.olhie.client.widgets.booklist3d.errai;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.events.book.CheckBookIsAuthorResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.AddBookToMyCollectionResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.RemoveBookFromMyCollectionResponseEvent;

@Dependent
public class BookList3DEventObserver {
	@Inject
	protected PageNavigator nav;
	
	private BookList3D_3 bookList;

	public BookList3DEventObserver() {
	}
	
	public void attachBookList(BookList3D_3 bookList) {
		this.bookList = bookList;
	}

	/**
	 * Watch for events where the book has been added to the users collection
	 * 
	 * @param addBookToMyCollectionResponseEvent
	 */
	protected void observersAddBookToMyCollectionResponseEvent(
			@Observes AddBookToMyCollectionResponseEvent addBookToMyCollectionResponseEvent) {
		String bookId = addBookToMyCollectionResponseEvent.getBookId();
		BookListItemWidget bookListItemWidget = bookList.getBliwMap().get(bookId);

		// Toggle the button so the user can now remove the book from his
		// collection
		bookListItemWidget.getTocWidget()
				.setMyCollectionBtnRemoveFromCollection(false);

		// Set the icon on the front of the book indicating that the book is in
		// the users collection
		bookListItemWidget.getMyCollectionIndicator().setAddToMyCollectionBtn(
				true);
	}

	/**
	 * Watch for events where the book has been removed from the users
	 * collection
	 * 
	 * @param removeBookFromMyCollectionResponseEvent
	 */
	protected void observesRemoveBookFromMyCollectionResponseEvent(
			@Observes RemoveBookFromMyCollectionResponseEvent removeBookFromMyCollectionResponseEvent) {
		String bookId = removeBookFromMyCollectionResponseEvent.getBookId();
		BookListItemWidget bookListItemWidget = bookList.getBliwMap().get(bookId);

		// Toggle the button so the user can now add the book to his
		// collection
		bookListItemWidget.getTocWidget().setMyCollectionBtnAddToCollection(
				false);

		// Hide the icon on the front of the book indicating that the book is
		// not in
		// the users collection
		bookListItemWidget.getMyCollectionIndicator().setHideMyCollectionBtn();
	}

	/**
	 * The corresponding request event is fired when the user clicks the book.
	 * When this happens a check is done to see if the user clicking the book is
	 * the author or co-author of the book. If yes then the user will be
	 * navigated to the NewBookPage where they cab edit the contents of the
	 * book.
	 * 
	 * 
	 * @param checkBookIsAuthorResponseEvent
	 */
	protected void observesCheckBookIsAuthorResponseEvent(
			@Observes CheckBookIsAuthorResponseEvent checkBookIsAuthorResponseEvent) {
		boolean isAuthorCoAuthor = checkBookIsAuthorResponseEvent
				.isAuthorCoAuthor();
		if (isAuthorCoAuthor == true) {
			Multimap<String, Object> map = ArrayListMultimap.create();
			map.put("bookId", checkBookIsAuthorResponseEvent.getBookId());
			nav.performTransition(NavEnum.NewBookPage_2.toString(), map);
		}
	}

}
