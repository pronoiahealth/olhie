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
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
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
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.admin.UserChangeRoleEvent;
import com.pronoiahealth.olhie.client.shared.events.admin.UserResetPWEvent;
import com.pronoiahealth.olhie.client.shared.vo.User;

/**
 * UserManagementWidget.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jan 26, 2014
 * 
 */
@Templated("#root")
@Dependent
public class UserManagementWidget extends Composite {
	private CellTable<User> userMgmtGrid;
	private SimplePager pager;
	private ListDataProvider<User> dataProvider;
	private static final String roles[] = new String[] {
			SecurityRoleEnum.ANONYMOUS.toString(),
			SecurityRoleEnum.REGISTERED.toString(),
			SecurityRoleEnum.AUTHOR.toString(),
			SecurityRoleEnum.ADMIN.toString() };

	@Inject
	@DataField
	private SimplePanel authorRequestTbl;

	@Inject
	@DataField
	private SimplePanel pagerContainer;

	@Inject
	@DataField
	private Button refreshBtn;

	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<User> KEY_PROVIDER = new ProvidesKey<User>() {
		@Override
		public Object getKey(User item) {
			return item == null ? null : item.getId();
		}
	};

	@Inject
	private Event<UserChangeRoleEvent> userChangeRoleEvent;

	@Inject
	private Event<UserResetPWEvent> userResetPWEvent;

	/**
	 * Constructor
	 * 
	 */
	public UserManagementWidget() {
	}

	/**
	 * Build grid
	 */
	@PostConstruct
	protected void postConstruct() {

		// Data Provider
		dataProvider = new ListDataProvider<User>();

		// Grid configuration
		userMgmtGrid = new CellTable<User>(10, KEY_PROVIDER);
		userMgmtGrid.setWidth("100%");
		userMgmtGrid.setAutoHeaderRefreshDisabled(true);
		userMgmtGrid.setAutoFooterRefreshDisabled(true);
		userMgmtGrid.setEmptyTableWidget(new Label("Nothing to show"));
		userMgmtGrid
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		userMgmtGrid.setWidth("100%", true);

		// Selection Model
		final SelectionModel<User> selectionModel = new SingleSelectionModel<User>(
				KEY_PROVIDER);
		userMgmtGrid.setSelectionModel(selectionModel,
				DefaultSelectionEventManager.<User> createCheckboxManager());

		// Init columns
		// Id
		TextColumn<User> idColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getId();
			}
		};
		idColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		userMgmtGrid.addColumn(idColumn, "ID");
		userMgmtGrid.setColumnWidth(idColumn, 5, Unit.PCT);

		// First Name
		TextColumn<User> firstColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getFirstName();
			}
		};
		firstColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		userMgmtGrid.addColumn(firstColumn, "First");
		userMgmtGrid.setColumnWidth(firstColumn, 7, Unit.PCT);

		// Last Name
		TextColumn<User> lastColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getLastName();
			}
		};
		lastColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		userMgmtGrid.addColumn(lastColumn, "Last");
		userMgmtGrid.setColumnWidth(lastColumn, 12, Unit.PCT);

		// userId
		TextColumn<User> userIdColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getUserId();
			}
		};
		userIdColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		userMgmtGrid.addColumn(userIdColumn, "UserId");
		userMgmtGrid.setColumnWidth(userIdColumn, 10, Unit.PCT);

		// eMail
		TextColumn<User> eMailColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getEmail();
			}
		};
		eMailColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		userMgmtGrid.addColumn(eMailColumn, "eMail");
		userMgmtGrid.setColumnWidth(eMailColumn, 15, Unit.PCT);

		// Organization
		TextColumn<User> orgColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getOrganization();
			}
		};
		orgColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		userMgmtGrid.addColumn(orgColumn, "Organization");
		userMgmtGrid.setColumnWidth(orgColumn, 15, Unit.PCT);

		// Role
		List<String> roleNames = Arrays.asList(roles);
		SelectionCell roleCell = new SelectionCell(roleNames);
		Column<User, String> roleColumn = new Column<User, String>(roleCell) {
			@Override
			public String getValue(User object) {
				return object.getRole();
			}
		};
		roleColumn.setCellStyleNames("ph-Admin-tbl-cell-text-align-left");
		userMgmtGrid.addColumn(roleColumn, "Role");

		// Status column updated
		roleColumn.setFieldUpdater(new FieldUpdater<User, String>() {
			@Override
			public void update(int index, User object, String value) {
				object.setRole(value);
				dataProvider.refresh();

				// Tell the AdminService about the change
				userChangeRoleEvent.fire(new UserChangeRoleEvent(object
						.getUserId(), value));

			}
		});
		userMgmtGrid.setColumnWidth(roleColumn, 40, Unit.PX);

		// Reset password
		Column<User, Boolean> resetPwColumn = new Column<User, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(User object) {
				return object.isResetPwd();
			}
		};
		userMgmtGrid.addColumn(resetPwColumn, "PW Reset");

		// Status column updated
		resetPwColumn.setFieldUpdater(new FieldUpdater<User, Boolean>() {
			@Override
			public void update(int index, User object, Boolean value) {
				object.setResetPwd(value);
				dataProvider.refresh();
				// Tell the AdminService about the change
				userResetPWEvent.fire(new UserResetPWEvent(object.getUserId(),
						value));

			}
		});
		userMgmtGrid.setColumnWidth(resetPwColumn, 40, Unit.PX);

		// Set the dataprovider
		dataProvider.addDataDisplay(userMgmtGrid);

		// Pager
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(userMgmtGrid);
		pager.setPageSize(5);
		pager.getElement().setAttribute("style", "margin: auto;");

		// Redraw the grid
		userMgmtGrid.redraw();
		userMgmtGrid.setVisible(true);

		// Add to container
		authorRequestTbl.add(userMgmtGrid);
		pagerContainer.add(pager);
	}

}
