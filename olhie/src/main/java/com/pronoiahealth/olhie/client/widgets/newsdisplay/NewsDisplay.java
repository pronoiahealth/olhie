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
package com.pronoiahealth.olhie.client.widgets.newsdisplay;

import static com.google.gwt.query.client.GQuery.$;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.pronoiahealth.olhie.client.clientfactories.NewsFadeInterval;
import com.pronoiahealth.olhie.client.shared.events.news.NewsItemsResponseEvent;
import com.pronoiahealth.olhie.client.shared.vo.NewsItem;

/**
 * NewsDisplay.java<br/>
 * Responsibilities:<br/>
 * 1. Displays a rotating list of news items<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 3, 2013
 * 
 */
@Templated("#main")
public class NewsDisplay extends Composite {

	@Inject
	@DataField
	private Anchor newsTitle;

	@Inject
	@DataField
	private Paragraph newsStory;

	@DataField
	private LIElement fadeNewsItem = Document.get().createLIElement().cast();

	@DataField
	private DivElement fadeNewsDisplay = Document.get().createDivElement()
			.cast();

	@Inject
	@NewsFadeInterval
	private Integer newsFadeInterval;

	private Timer fadeTimer;

	private List<NewsItem> newsItems;

	private int currentNewsItem = 0;

	/**
	 * Constructor
	 * 
	 */
	public NewsDisplay() {
	}

	/**
	 * Set up the fade timer and set the initial item to display.
	 */
	@PostConstruct
	private void postConstruct() {
		// Set initial news item
		setCurrentNewsItem(false);

		fadeTimer = new Timer() {
			@Override
			public void run() {
				if (newsItems != null && newsItems.size() > 0) {
					GQuery obj = $("#fadeNewsDisplay");
					obj.show();
					$("#fadeNewsDisplay").as(Effects.Effects).fadeOut(
							newsFadeInterval, new Function() {
								@Override
								public void f(Element e) {
									setCurrentNewsItem(true);
									$(e).show();
								}
							});
				}
			}
		};
		fadeTimer.scheduleRepeating(newsFadeInterval + 2000);
	}

	/**
	 * Change the news item to display
	 * 
	 * @param shouldIncrement
	 */
	private void setCurrentNewsItem(boolean shouldIncrement) {
		if (newsItems != null && newsItems.size() > 0) {
			if (shouldIncrement == true) {
				currentNewsItem++;
			}

			// Size of list
			int size = newsItems.size();

			// Position for next item to display
			if (size == currentNewsItem) {
				currentNewsItem = 0;
			}

			// Load the element
			NewsItem item = newsItems.get(currentNewsItem);
			newsTitle.setText(item.getTitle());
			newsTitle.setHref(item.getHref());
			newsStory.setText(item.getStory());
		}
	}

	/**
	 * Handle the mouse over event on the news item. This will stop the fade of
	 * the element.
	 * 
	 * @param event
	 */
	@EventHandler("fadeNewsItem")
	public void handleMouseOverEvents(MouseOverEvent event) {
		stopFadeAnimation();
	}

	private void stopFadeAnimation() {
		fadeTimer.cancel();
		GQuery elem = $("#fadeNewsDisplay");
		elem.stop(true);
	}

	/**
	 * When the mouse moves out of the new item the fade resumes
	 * 
	 * @param event
	 */
	@EventHandler("fadeNewsItem")
	public void handleMouseOutEvents(MouseOutEvent event) {
		GQuery elem = $("#fadeNewsDisplay");
		elem.css(Properties.create("opacity: 1.0;"));
		fadeTimer.scheduleRepeating(newsFadeInterval + 1000);
	}

	/**
	 * Load the news items
	 * 
	 * @param newsItemsResponseEvent
	 */
	protected void observersNewsItemsResponseEvent(
			@Observes NewsItemsResponseEvent newsItemsResponseEvent) {
		loadNewNewsItems(newsItemsResponseEvent.getNewsItems());
	}

	/**
	 * Stop teh fade animation, load the new list, start the fade animation
	 * 
	 * @param newsItems
	 */
	private void loadNewNewsItems(List<NewsItem> newsItems) {
		if (newsItems != null) {
			stopFadeAnimation();
			currentNewsItem = 0;
			this.newsItems = newsItems;
			setCurrentNewsItem(false);
			fadeTimer.scheduleRepeating(newsFadeInterval + 1000);
		}
	}

	public List<NewsItem> getNewsItems() {
		return newsItems;
	}

	public void setNewsItems(List<NewsItem> newsItems) {
		this.newsItems = newsItems;
	}
}
