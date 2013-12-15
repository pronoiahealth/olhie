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
import static gwtquery.plugins.ui.Ui.Ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Disposer;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.pronoiahealth.olhie.client.shared.constants.BookImageSizeEnum;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedEvent;
import com.pronoiahealth.olhie.client.shared.events.book.BookListBookSelectedResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.bookcase.BookcaseBookWidgetReorderEvent;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookcaseDisplay;
import com.pronoiahealth.olhie.client.utils.Utils;
import com.pronoiahealth.olhie.client.widgets.booklist3d.errai.BookList3DEventObserver;
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
/**
 * BookCaseContainerWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 2, 2013
 * 
 */
@Dependent
@Templated("#root")
public class BookCaseContainerWidget extends Composite {

	@DataField
	private Element sortableContainer = DOM.createElement("ul");

	@DataField
	private HTMLPanel bookDetailContainer = new HTMLPanel("");

	@Inject
	private javax.enterprise.event.Event<BookListBookSelectedEvent> bookListBookSelectedEvent;

	@Inject
	private javax.enterprise.event.Event<BookcaseBookWidgetReorderEvent> bookcaseBookWidgetReorderEvent;

	private Function bookClickFunction;

	@Inject
	private Instance<BookList3D_3> bookList3DFac;

	BookList3D_3 currentInstanceBookList3D_3;

	@Inject
	private Disposer<BookList3D_3> bookList3DDisposer;

	@Inject
	private Instance<BookCaseDraggableBookWidget> bookCaseDraggableBookWidgetFac;

	@Inject
	private Disposer<BookCaseDraggableBookWidget> bookCaseDraggableBookWidgetDisposer;

	@Inject
	private BookList3DEventObserver bookListEventObserver;

	@Inject
	private SyncBeanManager syncManager;

	/**
	 * Constructor
	 * 
	 */
	public BookCaseContainerWidget() {
	}

	/**
	 * Clean up constructed instances
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#onUnload()
	 */
	@Override
	public void onUnload() {
		super.onUnload();
		disposeBookList();
		disposeBookCaseDraggableBookWidgets();
		bookListEventObserver = null;
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

		// Get list
		BookDisplay bookDisplay = bookListBookSelectedResponseEvent
				.getBookDisplay();

		// Clean up panel
		disposeBookList();

		if (bookDisplay != null) {
			// Create a new book list
			// Add a book list3D to the container
			List<BookDisplay> bookDisplayList = new ArrayList<BookDisplay>();
			bookDisplayList.add(bookDisplay);
			currentInstanceBookList3D_3 = bookList3DFac.get();
			currentInstanceBookList3D_3.build(bookDisplayList, false);
			currentInstanceBookList3D_3.getElement().addClassName(
					"ph-Bookcase-BookDetail-Panel");
			bookListEventObserver.attachBookList(currentInstanceBookList3D_3,
					"BookCaseContainerWidget");
			bookDetailContainer.add(currentInstanceBookList3D_3);
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
				String userBookRelationshipId = bc.getUserBookRelationshipId();
				widget.setData(Utils
						.buildRestServiceForBookFrontCoverDownloadLink(bookId,
								BookImageSizeEnum.SMALL), bookId,
						userBookRelationshipId, bc.getBookTitle());
				sortableContainer.appendChild(widget.getElement());
			}
			config();
		}
	}

	/**
	 * Configure the update when a users moves a book in the display and
	 * configure the button click on a book display.
	 */
	private void config() {
		// Configure the sortable container
		// JSONObject obj = new JSONObject();
		// configSortable(this, sortableContainer, obj.getJavaScriptObject());

		// Configure sortable
		GQuery sortableContainerQry = $(sortableContainer);
		sortableContainerQry.as(Ui).sortable()
				.bind("sortupdate", new Function() {
					@Override
					public boolean f(Event e, Object data) {
						GQuery lstQry = $(sortableContainer).find(
								".ui-state-default");
						int size = lstQry.length();
						Map<String, Integer> map = new HashMap<String, Integer>();
						for (int i = 0; i < size; i++) {
							com.google.gwt.dom.client.Element ret = lstQry
									.get(i);
							GQuery retQry = $(ret).find(
									".bookCase-Detail-Button");
							String val = retQry.attr("userBookRelationshipId");
							map.put(val, Integer.valueOf(i));
						}

						// Tell the server to update the data
						bookcaseBookWidgetReorderEvent
								.fire(new BookcaseBookWidgetReorderEvent(map));

						// Event propagation stops here
						return false;
					}
				});

		// Configure the click events, bind the bookClickFunction to each
		// element
		GQuery sortableQry = sortableContainerQry
				.find(".bookCase-Detail-Button");
		sortableQry.each(new Function() {
			@Override
			public void f(com.google.gwt.dom.client.Element e) {
				$(e).bind(Event.ONCLICK, bookClickFunction);
			}
		});
	}

	/**
	 * Clears out book detail panel
	 */
	private void disposeBookList() {
		if (bookDetailContainer.getWidgetCount() > 0) {
			bookDetailContainer.clear();
			if (currentInstanceBookList3D_3 != null) {
				bookList3DDisposer.dispose(currentInstanceBookList3D_3);
				currentInstanceBookList3D_3 = null;
			}
		}

		// Are there any other reference to the booklist
		// This is a hack for now.
		// Collection<IOCBeanDef<BookList3D_3>> book3DList = syncManager
		//		.lookupBeans(BookList3D_3.class);
		//if (book3DList != null && book3DList.size() > 0) {
		//	for (IOCBeanDef<BookList3D_3> b : book3DList) {
		//		syncManager.destroyBean(b.getInstance());
		//	}
		//}
	}

	/**
	 * Clears book item widgets
	 */
	private void disposeBookCaseDraggableBookWidgets() {
		if (sortableContainer != null) {
			int cnt = DOM.getChildCount(sortableContainer);
			for (int i = 0; i < cnt; i++) {
				Element e = DOM.getChild(sortableContainer, i);
				EventListener listener = DOM
						.getEventListener((com.google.gwt.user.client.Element) e);
				// No listener attached to the element, so no widget exist for
				// this element
				if (listener != null
						&& listener instanceof BookCaseDraggableBookWidget) {
					bookCaseDraggableBookWidgetDisposer
							.dispose((BookCaseDraggableBookWidget) listener);
				}
			}
		}
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