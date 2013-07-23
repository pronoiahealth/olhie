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
package com.pronoiahealth.olhie.client.pages.main;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ioc.client.api.AfterInitialization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.clientfactories.PingFireTime;
import com.pronoiahealth.olhie.client.clientfactories.ScreenTimeout;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.pages.AbstractComposite;
import com.pronoiahealth.olhie.client.pages.comments.CommentsDialog;
import com.pronoiahealth.olhie.client.pages.error.ErrorDisplayDialog;
import com.pronoiahealth.olhie.client.pages.login.LoginDialog;
import com.pronoiahealth.olhie.client.pages.lookupuser.LookupUserDialog;
import com.pronoiahealth.olhie.client.pages.newbook.dialogs.AddBookCommentDialog;
import com.pronoiahealth.olhie.client.pages.newbook.dialogs.AddFileDialog;
import com.pronoiahealth.olhie.client.pages.newbook.dialogs.AddLogoDialog;
import com.pronoiahealth.olhie.client.pages.newbook.dialogs.NewAssetDialog;
import com.pronoiahealth.olhie.client.pages.newbook.dialogs.NewBookDialog;
import com.pronoiahealth.olhie.client.pages.newbook.dialogs.ViewBookassetDialog;
import com.pronoiahealth.olhie.client.pages.register.RegisterDialog;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SyncPageToMenuEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoggedInPingEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LogoutResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.news.NewsItemsRequestEvent;
import com.pronoiahealth.olhie.client.shared.rest.TestRest;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.client.widgets.DownloadFrame;
import com.pronoiahealth.olhie.client.widgets.newsdisplay.NewsDisplay;

/**
 * MainPage.java<br/>
 * Responsible for:<br />
 * 1. Screen timeout <br />
 * 2. Bus life cycle listener <br />
 * 3. Login and logout alert <br />
 * 4. Update the Client user token when login and logout happen<br/>
 * 5. Fire the ClientUserUpdatedEvent<br/>
 * 6. Observes for the LoginResponseEvent (this means a successful login)<br/>
 * 7. Observes for the LogoutResponseEvent (this means the user has logged out)<br/>
 * 8. Fire the NewsItemsRequestEvent after initialization.<br/>
 * 
 * @author johndestefano
 * @Version 1.0
 * @since 1/4/2013
 */
public class MainPage extends AbstractComposite {

	@Inject
	UiBinder<Widget, MainPage> binder;

	@Inject
	private OfferHandler offerHandler;

	@Inject
	public AppNavMenu sidebarMenu;

	@UiField
	public HTMLPanel sideBarMenuPlaceHolder;

	@UiField
	public HTMLPanel newsDisplayPlaceHolder;

	@Inject
	private NewsDisplay newsDisplay;

	@Inject
	public Footer footer;

	@Inject
	Header navBar;

	@Inject
	LoginDialog loginDialog;

	@Inject
	RegisterDialog registerDialog;

	@Inject
	CommentsDialog commentsDialog;

	@Inject
	ErrorDisplayDialog errorDisplayDialog;

	@Inject
	NewBookDialog newBookDialog;

	@Inject
	NewAssetDialog newAssetDialog;

	@Inject
	AddFileDialog addFileDialog;

	@Inject
	ViewBookassetDialog viewBookassetDialog;

	@Inject
	AddLogoDialog addLogoDialog;

	@Inject
	LookupUserDialog lookupUserDialog;

	@Inject
	DownloadFrame downloadFrame;

	@Inject
	AddBookCommentDialog addBookCommentDialog;

	@UiField
	public HTMLPanel loginModalPlaceHolder;

	@UiField
	public HTMLPanel errorModalPlaceHolder;

	@UiField
	public HTMLPanel registerModalPlaceHolder;

	@UiField
	public HTMLPanel commentsModalPlaceHolder;

	@UiField
	public HTMLPanel newBookModalPlaceHolder;

	@UiField
	public HTMLPanel newAssetModalPlaceHolder;

	@UiField
	public HTMLPanel addFileModalPlaceHolder;

	@UiField
	public HTMLPanel viewBookassetModalPlaceHolder;

	@UiField
	public HTMLPanel downloadFramePlaceHolder;

	@UiField
	public HTMLPanel addLogoDialogModalPlaceHolder;

	@UiField
	public HTMLPanel lookupUserModalPlaceHolder;

	@UiField
	public HTMLPanel addBookCommentPlaceHolder;

	/*
	 * Used to time things on screen such as when a key is pressed.
	 */
	private Timer screenTimer;

	@Inject
	@ScreenTimeout
	private Integer screenTimeout;

	private Timer pingTimer;

	@Inject
	@PingFireTime
	private Integer pingFireTime;

	@Inject
	private PageNavigator navigator;

	@Inject
	private ClientUserToken clientUserToken;

	@UiField
	public HTMLPanel footerPlaceHolder;

	@UiField
	public HTMLPanel navBarPanel;

	@UiField
	public HTMLPanel navContent;

	@UiField
	public DockLayoutPanel dockLayoutPanel;

	@Inject
	private Event<WindowResizeEvent> windowResizeEvent;

	@Inject
	private Event<ClientUserUpdatedEvent> clientUserUpdatedEvent;

	@Inject
	private Event<LoggedInPingEvent> loggedInPingEvent;

	@Inject
	private Event<NewsItemsRequestEvent> newsItemsRequestEvent;

	@Inject
	private Event<ClientErrorEvent> clientErrorEvent;

	@Inject
	private Caller<TestRest> testRestService;

	private static final int EAST_PANEL_WIDTH = 180;

	/**
	 * Default Constructor
	 * 
	 */
	public MainPage() {
	}

	/**
	 * Inject the custom resources and create the main gui via uiBinder. Also,
	 * apply the overflow: visible to the north panel. This is necessary to
	 * allow the drop down menu to show.
	 */
	@PostConstruct
	private void postConstruct() {
		initWidget(binder.createAndBindUi(this));

		// Apply overflow: visible to the g:north container
		Element north = dockLayoutPanel.getWidgetContainerElement(navBarPanel);
		north.addClassName("header-North-Overflow");
	}

	/**
	 * Tasks include:<br/>
	 * 1. Set up screen timer<br/>
	 * 2. Initial sidebar menu <br/>
	 * 3. Request news items <br/>
	 */
	@AfterInitialization
	public void postInit() {

		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				// TODO
			}
		});

		// Create timer and schedule
		screenTimer = new Timer() {
			@Override
			public void run() {
			}
		};

		// Create global event listener // Any events (mouse or keyboard)
		// will reset the timer
		com.google.gwt.user.client.Event
				.addNativePreviewHandler(new NativePreviewHandler() {
					@Override
					public void onPreviewNativeEvent(NativePreviewEvent event) {
						screenTimer.cancel();
						screenTimer.schedule(screenTimeout);
					}
				});

		// Not all GWT panel will resize when the window resizes
		// Observing this event gives panel a chance to resize if necessary
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				windowResizeEvent.fire(new WindowResizeEvent());
			}
		});

		// Footer
		buildFooter();

		// SidebarMenu
		buildSidebarMenu();

		// Where the news widget goes
		buildRightSideBar();

		// Header
		buildNavBar();

		// Add main screen dialogs
		// Login, Comments, Register
		buildMainScreenDialogs();

		// Add navigation panel
		navContent.add(navigator.getNavContentPanel());

		// Fire get news event
		newsItemsRequestEvent.fire(new NewsItemsRequestEvent());

		// Test to see if we are on the BulletinBoardPage as the initial page
		// if (!navigator.isCurrentPage(NavEnum.BulletinboardPage.name())) {
		// hideEast();
		// }
	}

	/**
	 * Build the footer
	 */
	private void buildFooter() {
		footerPlaceHolder.add(footer);
	}

	/**
	 * Build the sidebar menu
	 */
	private void buildSidebarMenu() {
		// Attach to view
		sideBarMenuPlaceHolder.add(sidebarMenu);
	}

	/**
	 * Attach the header/navigation bar
	 */
	private void buildNavBar() {
		navBarPanel.add(navBar);
	}

	/**
	 * Where the new widget goes
	 */
	private void buildRightSideBar() {
		newsDisplayPlaceHolder.add(newsDisplay);
	}

	/**
	 * Attach the login, comments, and register dialogs
	 */
	private void buildMainScreenDialogs() {
		loginModalPlaceHolder.add(loginDialog);
		registerModalPlaceHolder.add(registerDialog);
		commentsModalPlaceHolder.add(commentsDialog);
		errorModalPlaceHolder.add(errorDisplayDialog);
		newBookModalPlaceHolder.add(newBookDialog);
		newAssetModalPlaceHolder.add(newAssetDialog);
		addFileModalPlaceHolder.add(addFileDialog);
		viewBookassetModalPlaceHolder.add(viewBookassetDialog);
		downloadFramePlaceHolder.add(downloadFrame);
		lookupUserModalPlaceHolder.add(lookupUserDialog);
		addLogoDialogModalPlaceHolder.add(addLogoDialog);
		addBookCommentPlaceHolder.add(addBookCommentDialog);
	}

	/**
	 * When the user logs out the ClientUserToken is updated, the ping service
	 * is stopped, and the ClientUserUpdatedEvent is fired. This event is fired
	 * from the Header class along with the LogoutRequestEvent. The
	 * ClientLogoutRequestEvent is processed here while the LogoutRequestEvent
	 * is processed by the server.
	 * 
	 * @param logoutResponseEvent
	 */
	protected void observesClientLogoutRequestEvent(
			@Observes ClientLogoutRequestEvent clientLogoutResponseEvent) {
		clientUserToken.clear();
		clientUserUpdatedEvent.fire(new ClientUserUpdatedEvent());
		navigator.showDefaultPage();
		cancelPing();
		cancelScreenTimer();
	}

	/**
	 * A message from the server telling the client that the user has logged out
	 * 
	 * @param logoutResponseEvent
	 */
	// TODO: Do we need this method
	protected void observesLogoutResponseEvent(
			@Observes LogoutResponseEvent logoutResponseEvent) {
	}

	/**
	 * When a login event is observed the clientUserToken is updated, the ping
	 * service starts, and the ClientUserUpdatedEvent is fired.
	 * 
	 * @param loginResponseEvent
	 */
	protected void observesLoginResponseEvent(
			@Observes LoginResponseEvent loginResponseEvent) {
		User user = loginResponseEvent.getUser();
		clientUserToken.setFullName(user.getFirstName() + " "
				+ user.getLastName());
		clientUserToken.setLoggedIn(true);
		clientUserToken.setUserId(user.getUserId());
		clientUserToken.setRole(user.getRole());
		clientUserUpdatedEvent.fire(new ClientUserUpdatedEvent());
		startPinging();
		startScreenTimer();
	}

	protected void observesSyncPageToMenuEvent(
			@Observes SyncPageToMenuEvent syncPageToMenuEvent) {
		if (syncPageToMenuEvent.getPageName().equals(
				NavEnum.BulletinboardPage.toString())) {
			showEast();
		} else {
			hideEast();
		}
	}

	/**
	 * After a user logs in start pinging the server.
	 */
	private void startPinging() {
		ping();
		pingTimer = new Timer() {
			@Override
			public void run() {
				ping();
			}
		};
		pingTimer.scheduleRepeating(pingFireTime);
	}

	/**
	 * After a user logs out the stop pinging
	 */
	private void cancelPing() {
		pingTimer.cancel();
	}

	/**
	 * Fire the LoggedInPingEvent
	 */
	private void ping() {
		loggedInPingEvent.fire(new LoggedInPingEvent());
	}

	private void cancelScreenTimer() {
		screenTimer.cancel();
	}

	private void startScreenTimer() {
		screenTimer.scheduleRepeating(this.screenTimeout);
	}

	/**
	 * Hide east newsDisplayPlaceHolder
	 */
	private void hideEast() {
		if (newsDisplayPlaceHolder != null) {
			int currentWidgetWidth = newsDisplayPlaceHolder.getOffsetWidth();
			if (currentWidgetWidth != 0) {
				dockLayoutPanel.setWidgetSize(newsDisplayPlaceHolder,
						currentWidgetWidth);
				dockLayoutPanel.forceLayout();
				dockLayoutPanel.setWidgetSize(newsDisplayPlaceHolder, 0);
				dockLayoutPanel.animate(1000);
			}
		}
	}

	/**
	 * Show newsDisplayPlaceHolder
	 */
	private void showEast() {
		if (newsDisplayPlaceHolder != null) {
			int currentWidth = newsDisplayPlaceHolder.getOffsetWidth();
			if (currentWidth != EAST_PANEL_WIDTH) {
				dockLayoutPanel.setWidgetSize(newsDisplayPlaceHolder,
						currentWidth);
				dockLayoutPanel.forceLayout();
				dockLayoutPanel.setWidgetSize(newsDisplayPlaceHolder,
						EAST_PANEL_WIDTH);
				dockLayoutPanel.animate(1000);
			}
		}
	}
}
