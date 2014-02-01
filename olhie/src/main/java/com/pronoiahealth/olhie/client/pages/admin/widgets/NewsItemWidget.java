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
package com.pronoiahealth.olhie.client.pages.admin.widgets;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pronoiahealth.olhie.client.shared.events.admin.NewsItemsRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.admin.NewsItemsResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.admin.RemoveNewsItemRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.admin.UpdateNewsItemActiveRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ShowNewNewsItemModalEvent;
import com.pronoiahealth.olhie.client.shared.vo.NewsItem;

/**
 * NewItemWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jan 31, 2014
 * 
 */
@Templated("#root")
@Dependent
public class NewsItemWidget extends Composite {

	private CellTable<NewsItem> newsItemGrid;
	private SimplePager pager;
	private ListDataProvider<NewsItem> dataProvider;

	private static final String reset[] = new String[] { "YES", "NO" };

	@Inject
	@DataField
	private SimplePanel newsItemTbl;

	@Inject
	@DataField
	private SimplePanel pagerContainer;

	@Inject
	@DataField
	private Button refreshBtn;

	@Inject
	@DataField
	private Button addNewBtn;

	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<NewsItem> KEY_PROVIDER = new ProvidesKey<NewsItem>() {
		@Override
		public Object getKey(NewsItem item) {
			return item == null ? null : item.getId();
		}
	};

	@Inject
	private Event<ShowNewNewsItemModalEvent> showNewNewsItemModalEvent;

	@Inject
	private Event<NewsItemsRequestEvent> newsItemsRequestEvent;

	@Inject
	private Event<RemoveNewsItemRequestEvent> removeNewsItemRequestEvent;

	@Inject
	private Event<UpdateNewsItemActiveRequestEvent> updateNewsItemActiveRequestEvent;

	/**
	 * Constructor
	 * 
	 */
	public NewsItemWidget() {
	}

	/**
	 * Build grid
	 */
	@PostConstruct
	protected void postConstruct() {

		// Data Provider
		dataProvider = new ListDataProvider<NewsItem>();

		// Grid configuration
		newsItemGrid = new CellTable<NewsItem>(10, KEY_PROVIDER);
		newsItemGrid.setWidth("100%");
		newsItemGrid.setAutoHeaderRefreshDisabled(true);
		newsItemGrid.setAutoFooterRefreshDisabled(true);
		newsItemGrid.setEmptyTableWidget(new Label("Nothing to show"));
		newsItemGrid
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		newsItemGrid.setWidth("100%", true);

		// Selection Model
		final SelectionModel<NewsItem> selectionModel = new SingleSelectionModel<NewsItem>(
				KEY_PROVIDER);
		newsItemGrid
				.setSelectionModel(selectionModel, DefaultSelectionEventManager
						.<NewsItem> createCheckboxManager());

		// Init columns
		// Id
		TextColumn<NewsItem> idColumn = new TextColumn<NewsItem>() {
			@Override
			public String getValue(NewsItem object) {
				return object.getId();
			}
		};
		idColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		newsItemGrid.addColumn(idColumn, "ID");
		newsItemGrid.setColumnWidth(idColumn, 5, Unit.PCT);
		
		// Author id
		TextColumn<NewsItem> authorColumn = new TextColumn<NewsItem>() {
			@Override
			public String getValue(NewsItem object) {
				return object.getAuthorId();
			}
		};
		authorColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		newsItemGrid.addColumn(authorColumn, "Author");
		newsItemGrid.setColumnWidth(authorColumn, 10, Unit.PCT);
		
		// href
		TextColumn<NewsItem> hrefColumn = new TextColumn<NewsItem>() {
			@Override
			public String getValue(NewsItem object) {
				return object.getHref();
			}
		};
		hrefColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		newsItemGrid.addColumn(hrefColumn, "Link");
		newsItemGrid.setColumnWidth(hrefColumn, 25, Unit.PCT);
		
		// Story
		TextColumn<NewsItem> storyColumn = new TextColumn<NewsItem>() {
			@Override
			public String getValue(NewsItem object) {
				return object.getStory();
			}
		};
		storyColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		newsItemGrid.addColumn(storyColumn, "Story");
		newsItemGrid.setColumnWidth(storyColumn, 40, Unit.PCT);
		

		// Active
		List<String> activeNames = Arrays.asList(reset);
		SelectionCell activeCell = new SelectionCell(activeNames);
		Column<NewsItem, String> activeColumn = new Column<NewsItem, String>(
				activeCell) {
			@Override
			public String getValue(NewsItem object) {
				boolean val = object.getActive();
				if (val == true) {
					return "YES";
				} else {
					return "NO";
				}
			}
		};
		activeColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		newsItemGrid.addColumn(activeColumn, "Active");

		// active column updated
		activeColumn.setFieldUpdater(new FieldUpdater<NewsItem, String>() {
			@Override
			public void update(int index, NewsItem object, String value) {
				boolean retVal = false;
				if (value.equals("YES")) {
					retVal = true;
					object.setActive(true);
				} else {
					object.setActive(false);
				}
				dataProvider.refresh();

				// Tell the AdminService about the change
				updateNewsItemActiveRequestEvent
						.fire(new UpdateNewsItemActiveRequestEvent(object
								.getId(), retVal));
			}
		});
		newsItemGrid.setColumnWidth(activeColumn, 18, Unit.PCT);

		// Set the dataprovider
		dataProvider.addDataDisplay(newsItemGrid);

		// Pager
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(newsItemGrid);
		pager.setPageSize(10);
		pager.getElement().setAttribute("style", "margin: auto;");

		// Redraw the grid
		newsItemGrid.redraw();
		newsItemGrid.setVisible(true);

		// Add to container
		newsItemTbl.add(newsItemGrid);
		pagerContainer.add(pager);
	}

	/**
	 * Click event - Refresh the data
	 * 
	 * @param evt
	 */
	@EventHandler("refreshBtn")
	private void handleRefreshClick(ClickEvent evt) {
		newsItemsRequestEvent.fire(new NewsItemsRequestEvent());
	}

	@EventHandler("addNewBtn")
	private void handleAddNewClick(ClickEvent evt) {
		showNewNewsItemModalEvent.fire(new ShowNewNewsItemModalEvent());
	}

	/**
	 * Get the list of unprocessed request
	 */
	@Override
	protected void onLoad() {
		super.onLoad();

		// Fire request for items
		newsItemsRequestEvent.fire(new NewsItemsRequestEvent());
	}

	/**
	 * Add News Items to the grid
	 * 
	 * @param authorPendingResponseEvent
	 */
	protected void observesNewsItemsResponseEvent(
			@Observes NewsItemsResponseEvent newsItemsResponseEvent) {
		List<NewsItem> nItems = newsItemsResponseEvent.getNewsItems();
		newsItemGrid.setRowCount(0);
		dataProvider.setList(nItems);
		dataProvider.refresh();
	}

}
