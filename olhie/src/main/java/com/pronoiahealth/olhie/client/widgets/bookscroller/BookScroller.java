package com.pronoiahealth.olhie.client.widgets.bookscroller;

import java.util.List;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;

public class BookScroller extends Widget {

	/**
	 * Some components that use this class will be ApplicationScoped. We don't
	 * want to do a re-bind of gQuery events on every call to this methods
	 * onLoad handler. We only want to do it once.
	 */
	private boolean hasBeenAttached = false;

	private String containerId;

	/**
	 * Root element
	 */
	private DivElement rootDiv;

	public BookScroller(String containerId, List<BookDisplay> books) {
		super();
		this.containerId = containerId;

		// Create root element
		Document doc = Document.get();
		this.rootDiv = doc.createDivElement();
		this.setElement(rootDiv);
		rootDiv.setId(containerId);
		rootDiv.setClassName("ca-container");

		// Now the wrapper
		DivElement wrapperDiv = doc.createDivElement();
		rootDiv.appendChild(wrapperDiv);
		wrapperDiv.setClassName("ca-wrapper");

		for (BookDisplay book : books) {
			// Item
			DivElement itemDiv = doc.createDivElement();
			wrapperDiv.appendChild(itemDiv);
			itemDiv.setClassName("ca-item");

			// Main for item
			DivElement mainDiv = doc.createDivElement();
			itemDiv.appendChild(mainDiv);
			mainDiv.addClassName("ca-item-main");

			// Book (icon) view for mainDiv
			DivElement bookIconDiv = doc.createDivElement();
			mainDiv.appendChild(bookIconDiv);
			bookIconDiv.addClassName("ca-icon");

			// h3 with title
			HeadingElement h3Title = doc.createHElement(3);
			mainDiv.appendChild(h3Title);
			h3Title.setInnerText(book.getBook().getBookTitle());

			// h4 with author name
			HeadingElement h4Author = doc.createHElement(4);
			mainDiv.appendChild(h4Author);
			SpanElement h4SpanChild = doc.createSpanElement();
			h4Author.appendChild(h4SpanChild);
			h4SpanChild.setInnerText(book.getAuthorFullName());

			// link to more content
			AnchorElement aMore = doc.createAnchorElement();
			mainDiv.appendChild(aMore);
			aMore.setHref("#");
			aMore.setClassName("ca-more");
			aMore.setInnerText("more...");

			// Contenet wrapper
			DivElement contentWrapperDiv = doc.createDivElement();
			itemDiv.appendChild(contentWrapperDiv);
			contentWrapperDiv.addClassName("ca-content-wrapper");

			// Content
			DivElement contentDiv = doc.createDivElement();
			contentWrapperDiv.appendChild(contentDiv);
			contentDiv.addClassName("ca-content");

			// h6 Title again
			HeadingElement h6Content = doc.createHElement(6);
			contentDiv.appendChild(h6Content);
			h6Content.setInnerHTML(book.getBook().getBookTitle());

			// link to close
			AnchorElement aClose = doc.createAnchorElement();
			contentDiv.appendChild(aClose);
			aClose.setHref("#");
			aClose.setClassName("ca-close");
			aClose.setInnerText("close");

			// Introduction div
			DivElement contentTxt = doc.createDivElement();
			contentDiv.appendChild(contentTxt);
			contentTxt.setClassName("ca-content-text");

			// Introduction
			ParagraphElement intro = doc.createPElement();
			contentTxt.appendChild(intro);
			contentTxt.setInnerText(book.getBook().getIntroduction());

			// list
			UListElement list = doc.createULElement();
			contentDiv.appendChild(list);

			// Link to open directly
			LIElement li = doc.createLIElement();
			list.appendChild(li);
			AnchorElement aBookLink = doc.createAnchorElement();
			li.appendChild(aBookLink);
			aBookLink.setHref("#");
			aBookLink.setInnerText("View Book");
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		if (this.hasBeenAttached == false) {
			this.hasBeenAttached = true;
		}
	}

	private void navigate(int dir, GQuery $el, GQuery $wrapper,
			BookScrollerOptions opts, BookScrollerCache cache) {

	}

	private void openItem(GQuery $wrapper, GQuery $item,
			BookScrollerOptions opts, BookScrollerCache cache) {

	}

	private void openItems(GQuery $wrapper, GQuery $openedItem,
			BookScrollerOptions opts, BookScrollerCache cache) {
	}

	private void toggleMore(GQuery $item, boolean show) {
	}

	private void closeItems(GQuery $wrapper, GQuery $openedItem,
			BookScrollerOptions opts, BookScrollerCache cache) {
	}

	private int getWinPos(int val, BookScrollerCache cache) {
		if (val == cache.getItemW()) {
			return 2;
		} else if (val == (cache.getItemW() * 2)) {
			return 3;
		} else {
			return 1;
		}
	}

}
