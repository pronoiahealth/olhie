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
package com.pronoiahealth.olhie.client.pages.bookcase.widgets;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Node;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.pronoiahealth.olhie.client.shared.constants.BookImageSizeEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookcaseDisplay;
import com.pronoiahealth.olhie.client.utils.BookList3DCreationalHandler;
import com.pronoiahealth.olhie.client.utils.Utils;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3D_3;

/**
 * BookCaseContainerWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Nov 29, 2013
 * 
 */
@Templated("#root")
public class BookCaseContainerWidget extends Composite {

	@DataField
	private Element sortableContainer = DOM.createElement("ul");

	@DataField
	private Element bookDetailContainer = DOM.createDiv();

	@Inject
	private Instance<BookCaseDraggableBookWidget> bookCaseDraggableBookWidgetFac;

	@Inject
	private javax.enterprise.event.Event<BookListBookSelectedEvent> bookListBookSelectedEvent;

	private Function bookClickFunction;

	private BookList3D_3 bookList3D;

	/**
	 * Constructor
	 * 
	 */
	public BookCaseContainerWidget() {
	}

	/**
	 * Builds the bookClickFunction function. This function will call the
	 * BookListBookSelectedEvent with the bookId. The bookId is set as an
	 * attribute on the button component of the BookCaseDraggableBookWidget.
	 */
	@PostConstruct
	protected void postConstruct() {
		bookClickFunction = new Function() {
			@Override
			public boolean f(Event e) {
				// Fire the event which will get the clicked book for
				// display
				String bookId = $(e).attr("bookId");
				if (bookId != null && bookId.length() > 0) {
					bookListBookSelectedEvent
							.fire(new BookListBookSelectedEvent(bookId));
				}
				return false;
			}
		};
	}

	/**
	 * Observes for the response events from the selecting of a book. Once the
	 * BookDisplay is returned it will be set in the BookDetail Component
	 * 
	 * @param bookListBookSelectedResponseEvent
	 */
	protected void observesBookListBookSelectedResponseEvent(
			@Observes BookListBookSelectedResponseEvent bookListBookSelectedResponseEvent) {

		BookDisplay bookDisplay = bookListBookSelectedResponseEvent
				.getBookDisplay();
		if (bookDisplay != null) {
			// Remove all children from the bookDetailContainer
			int cnt = bookDetailContainer.getChildCount();
			while (bookDetailContainer.getChildCount() != 0) {
				Node n = bookDetailContainer.getFirstChild();
				bookDetailContainer.removeChild(n);
				n = null;
			}

			// Add a book list3D to the container
			List<BookDisplay> bookDisplayList = new ArrayList<BookDisplay>();
			bookDisplayList.add(bookDisplay);
			Utils.cleanUpAndInitBookList3D(bookList3D, bookDisplayList,
					new BookList3DCreationalHandler() {
						@Override
						public void bookListCreationalCallback(
								BookList3D_3 currentBookList3D) {

							// Add this class to the root element to adjust the
							// position in the display
							currentBookList3D.getElement().addClassName(
									"ph-Bookcase-BookDetail-Panel");
							bookDetailContainer.appendChild(currentBookList3D
									.getElement());
						}
					});
		}
	}

	/**
	 * Configure the widget and attach to jQuery sortable
	 * 
	 * @param bookcaseDisplayLst
	 */
	public void loadDataAndInit(List<BookcaseDisplay> bookcaseDisplayLst) {
		if (bookcaseDisplayLst != null && bookcaseDisplayLst.size() > 0) {
			for (BookcaseDisplay bc : bookcaseDisplayLst) {
				BookCaseDraggableBookWidget widget = bookCaseDraggableBookWidgetFac
						.get();
				String bookId = bc.getBookId();
				widget.setData(Utils
						.buildRestServiceForBookFrontCoverDownloadLink(bookId,
								BookImageSizeEnum.SMALL), bookId);
				sortableContainer.appendChild(widget.getElement());
			}

			config();
		}
	}

	private void config() {
		// Configure the sortable container
		JSONObject obj = new JSONObject();
		configSortable(this, sortableContainer, obj.getJavaScriptObject());

		// Configure the click events, bind the bookClickFunction to each
		// element
		GQuery sortableQry = $(sortableContainer).find(
				".bookCase-Detail-Button");
		sortableQry.each(new Function() {
			@Override
			public void f(com.google.gwt.dom.client.Element e) {
				$(e).bind(Event.ONCLICK, bookClickFunction);
			}
		});
	}

	/**
	 * Native method to configure the JQuery sortable ui widget on the
	 * sortableContainer. Only call this after all elements have been attached
	 * to the container.
	 * 
	 * @param x
	 * @param e
	 * @param options
	 */
	private native void configSortable(BookCaseContainerWidget x, Element e,
			JavaScriptObject options) /*-{
		options.update = function(event, ui) {
			x.@com.pronoiahealth.olhie.client.pages.bookcase.widgets.BookCaseContainerWidget::fireOnSortableUpdateEvent(Lcom/google/gwt/user/client/Event;Lcom/google/gwt/core/client/JavaScriptObject;)(event, ui);
		};
		$wnd.jQuery(e).sortable(options);
		$wnd.jQuery(e).disableSelection();
	}-*/;

	/**
	 * Receives the Update event from the attached jQuery sortable widget. See
	 * the config method for details.
	 * 
	 * @param evt
	 * @param values
	 */
	private void fireOnSortableUpdateEvent(
			com.google.gwt.user.client.Event evt, JavaScriptObject values) {
		GQuery lstQry = $(sortableContainer).find(".ui-state-default");
		int size = lstQry.length();
		for (int i = 0; i < size; i++) {
			com.google.gwt.dom.client.Element ret = lstQry.get(i);
			GQuery retQry = $(ret);
		}
	}

}
