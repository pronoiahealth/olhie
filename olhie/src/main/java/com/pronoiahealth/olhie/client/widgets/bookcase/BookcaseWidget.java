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
package com.pronoiahealth.olhie.client.widgets.bookcase;

import static com.google.gwt.query.client.GQuery.$;

import java.util.List;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.vo.BookForDisplay;
import com.pronoiahealth.olhie.client.shared.vo.BookCollection;
import com.pronoiahealth.olhie.client.shared.vo.BookState;
import com.pronoiahealth.olhie.client.shared.vo.Bookcase;

/**
 * BookcaseWidget.java<br/>
 * Responsibilities:<br/>
 * 1. Creates the bookcase view<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public class BookcaseWidget extends Widget {
	private Bookcase bookCase;
	private String id;
	private DivElement bookShelfElem;
	private DivElement panelTitleElem;
	private DivElement panelSliderElem;
	private DivElement panelBarElem;
	private int initHeight;
	private String parentContainerSelector;
	private MessageBus bus;

	/**
	 * Constructor
	 *
	 * @param bookCase
	 * @param initHeight
	 */
	public BookcaseWidget(Bookcase bookCase, int initHeight) {
		this("bookshelf_slider", bookCase, ".center-background", initHeight);
	}

	/**
	 * Constructor
	 *
	 * @param bookCase
	 * @param parentContainerSelector
	 */
	public BookcaseWidget(Bookcase bookCase, String parentContainerSelector) {
		this("bookshelf_slider", bookCase, parentContainerSelector, 440);
	}

	/**
	 * Constructor
	 *
	 * @param bookCase
	 */
	public BookcaseWidget(Bookcase bookCase) {
		this("bookshelf_slider", bookCase, ".center-background", 440);
	}

	/**
	 * Constructor
	 *
	 * @param id
	 * @param bookCase
	 * @param parentContainerSelector
	 * @param initHeight
	 */
	public BookcaseWidget(String id, Bookcase bookCase,
			String parentContainerSelector, int initHeight) {
		super();
		this.bus = ErraiBus.get();
		this.bookCase = bookCase;
		this.id = id;
		this.initHeight = initHeight;
		this.parentContainerSelector = parentContainerSelector;

		// Build primary element
		bookShelfElem = Document.get().createDivElement();
		setElement(bookShelfElem);
		bookShelfElem.setId(id);
		bookShelfElem.setAttribute("style", "10%;");

		// Now construct the different parts
		bookShelfElem.appendChild(buildPanelTitle());
		bookShelfElem.appendChild(buildPanelSlider(bookCase));
		bookShelfElem.appendChild(buildPanelBar(bookCase.getBookCollections()));
	}

	private DivElement buildPanelTitle() {
		Document doc = Document.get();

		// Main Div
		panelTitleElem = doc.createDivElement();
		panelTitleElem.setClassName("panel_title");

		// selected_title_box
		DivElement selectedTitleBoxElem = doc.createDivElement();
		selectedTitleBoxElem.addClassName("selected_title_box");
		panelTitleElem.appendChild(selectedTitleBoxElem);
		DivElement selectedTitleElem = doc.createDivElement();
		selectedTitleBoxElem.appendChild(selectedTitleElem);
		selectedTitleElem.addClassName("selected_title");
		selectedTitleElem.setInnerText("Selected Title");

		// menu top
		DivElement menuTopElem = doc.createDivElement();
		menuTopElem.setClassName("menu_top");
		panelTitleElem.appendChild(menuTopElem);
		UListElement ulElem = doc.createULElement();
		menuTopElem.appendChild(ulElem);
		LIElement liElem = doc.createLIElement();
		ulElem.appendChild(liElem);
		liElem.setClassName("show_hide_titles");
		AnchorElement aElem = doc.createAnchorElement();
		liElem.appendChild(aElem);
		aElem.setHref("#");
		aElem.setInnerText("Titles");

		return panelTitleElem;
	}

	private DivElement buildPanelSlider(Bookcase bookCase) {
		Document doc = Document.get();

		// Built panel slider
		panelSliderElem = doc.createDivElement();
		panelSliderElem.setClassName("panel_slider");
		DivElement panelItemsElem = doc.createDivElement();
		panelItemsElem.setClassName("panel_items");
		panelSliderElem.appendChild(panelItemsElem);

		// A slide animate for each collection
		int numbColls = bookCase.getBookCollections().size();
		for (int i = 0; i < numbColls; i++) {
			DivElement slideAnimateElem = doc.createDivElement();
			slideAnimateElem.setClassName("slide_animate");
			panelItemsElem.appendChild(slideAnimateElem);

			// Create products box
			DivElement productsBoxElem = doc.createDivElement();
			productsBoxElem.setClassName("products_box");
			productsBoxElem.setId("products_box_" + (i + 1));
			slideAnimateElem.appendChild(productsBoxElem);

			// Add books to products box
			BookCollection currColl = bookCase.getBookCollections().get(i);
			for (BookForDisplay book : currColl.getBooks()) {
				DivElement productElem = doc.createDivElement();
				productElem.addClassName("product");
				productElem.setAttribute("data-type", "");
				productElem.setAttribute("data-popup", "");
				productElem.setAttribute("data-url", book.getId());
				productElem.setAttribute("title", book.getTitle());
				productElem.setAttribute("background-color",
						"white");
				productElem.setAttribute("binder-color", book.getCatagory()
						.getColor());
				productElem.setAttribute("background-pattern", book.getBookCover().getImgUrl());
				productElem.setAttribute("title", book.getTitle());
				productElem
						.setAttribute("private",
								BookState.BOOK_STATE_INVISIBLE == book
										.getBookState() ? "yes" : "no");
				ImageElement iElem = doc.createImageElement();
				productElem.appendChild(iElem);
				// TODO: Refactor - This is an image thats covered by DIVs, see
				// how to get
				// rid of it in js.
				iElem.setSrc("Olhie/images/book2.png");
				iElem.setWidth(100);
				iElem.setHeight(1);
				iElem.setAlt("");
				productsBoxElem.appendChild(productElem);
			}
		}

		return panelSliderElem;
	}

	private DivElement buildPanelBar(List<BookCollection> collLst) {
		Document doc = Document.get();

		// Main Div
		panelBarElem = doc.createDivElement();
		panelBarElem.setClassName("panel_bar");
		DivElement buttonsContElem = doc.createDivElement();
		panelBarElem.appendChild(buttonsContElem);
		buttonsContElem.setClassName("buttons_container");
		DivElement arrowBoxElem = doc.createDivElement();
		arrowBoxElem.setId("arrow_box");
		buttonsContElem.appendChild(arrowBoxElem);
		DivElement arrowMenuElem = doc.createDivElement();
		arrowMenuElem.setId("arrow_menu");
		arrowBoxElem.appendChild(arrowMenuElem);

		// Button items
		DivElement buttonItemsElem = doc.createDivElement();
		buttonItemsElem.setClassName("button_items");
		buttonsContElem.appendChild(buttonItemsElem);

		for (int i = 0; i < collLst.size(); i++) {
			DivElement btnItemElem = doc.createDivElement();
			btnItemElem.setId("btn" + (i + 1));
			btnItemElem.setClassName("button_bar");
			AnchorElement aRef = doc.createAnchorElement();
			aRef.setHref("#");
			btnItemElem.appendChild(aRef);
			String colName = collLst.get(i).getCollectionName();
			aRef.setInnerText(colName);
			buttonItemsElem.appendChild(btnItemElem);
		}

		return panelBarElem;
	}

	public Bookcase getBookCase() {
		return bookCase;
	}

	public void setBookCase(Bookcase bookCase) {
		this.bookCase = bookCase;
	}

	public void attachProductClickedEvent() {
		$("#bookshelf_slider .product").click(new Function() {

			@Override
			public void f(Element e) {
				String value = $(e).attr("data-url");
				MessageBuilder.createMessage()
						.toSubject("BookCaseWidgetBookClicked").signalling()
						.with("bookId", value).noErrorHandling()
						.sendNowWith(bus);
			}
		});
	}

	@Override
	public void onAttach() {
		super.onAttach();
		// Should fill parent container
		int parentWidth = $(parentContainerSelector).width();
		attachBookCase(parentWidth, initHeight);
		attachProductClickedEvent();
	}

	private static final native void attachBookCase(JSONObject options) /*-{
		$wnd.$.bookshelfSlider('#bookshelf_slider', options);
	}-*/;

	private static final native void attachBookCase(int width, int height) /*-{
		$wnd.$.bookshelfSlider('#bookshelf_slider', {
			'parent_selector' : '.center-background',
			'item_width' : width,
			'item_height' : height,
			'products_box_margin_left' : 30,
			'product_title_textcolor' : '#ffffff',
			'product_title_bgcolor' : '#c33b4e',
			'product_margin' : 20,
			'product_show_title' : true,
			'show_title_in_popup' : true,
			'show_selected_title' : true,
			'show_icons' : false,
			'buttons_margin' : 10,
			'buttons_align' : 'center', // left, center, right
			'slide_duration' : 2000,
			'slide_easing' : 'easeOutElastic',
			'arrow_duration' : 1000,
			'arrow_easing' : 'easeInOutExpo',
			'video_width_height' : [ 500, 290 ],
			'iframe_width_height' : [ 500, 330 ]
		});
	}-*/;
}
