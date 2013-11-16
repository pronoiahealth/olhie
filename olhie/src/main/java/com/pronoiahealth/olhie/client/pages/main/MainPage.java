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

import org.jboss.errai.ioc.client.api.AfterInitialization;

import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.features.FeatureCallbackHandler;
import com.pronoiahealth.olhie.client.features.impl.AddBookCommentsHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.CommentsDialogHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.DownloadWidgetHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.ErrorDialogHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.EventCalendarRequestHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.LoggedInUserServerPingFeature;
import com.pronoiahealth.olhie.client.features.impl.LoginDialogHandlingFeature;
import com.pronoiahealth.olhie.client.features.impl.LookupUserDialogHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.MainWindowResizeFeature;
import com.pronoiahealth.olhie.client.features.impl.NewBookDialogHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.RegisterDialogHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.ScreenInactivityTimeoutFeature;
import com.pronoiahealth.olhie.client.features.impl.SleepDetectionFeature;
import com.pronoiahealth.olhie.client.features.impl.UnhandledExceptionHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.ViewBookCommentsHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.ViewBookassetDialogHandlerFeature;
import com.pronoiahealth.olhie.client.features.impl.WindowCloseTrappingFeature;
import com.pronoiahealth.olhie.client.navigation.PageNavigator;
import com.pronoiahealth.olhie.client.pages.AbstractComposite;
import com.pronoiahealth.olhie.client.shared.constants.NavEnum;
import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientLogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.SyncPageToMenuEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LoginResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.loginout.LogoutRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.news.NewsItemsRequestEvent;
import com.pronoiahealth.olhie.client.shared.vo.ClientUserToken;
import com.pronoiahealth.olhie.client.shared.vo.User;
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

	@UiField
	public HTMLPanel reloadModalPlaceHolder;

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
	private Event<ClientUserUpdatedEvent> clientUserUpdatedEvent;

	@Inject
	private Event<NewsItemsRequestEvent> newsItemsRequestEvent;

	@Inject
	private Event<ClientErrorEvent> clientErrorEvent;

	@Inject
	private Event<LogoutRequestEvent> logoutRequestEvent;

	@Inject
	private UnhandledExceptionHandlerFeature unhandledExceptionHandlerFeature;

	@Inject
	private ScreenInactivityTimeoutFeature screenInactivityTimeoutFeature;

	@Inject
	private WindowCloseTrappingFeature windowCloseTrappingFeature;

	@Inject
	private MainWindowResizeFeature mainWindowResizeFeature;

	@Inject
	private LoggedInUserServerPingFeature loggedInUserServerPingFeature;

	@Inject
	private SleepDetectionFeature sleepDetectionFeature;

	@Inject
	private LoginDialogHandlingFeature loginDialogFeature;

	@Inject
	private ErrorDialogHandlerFeature errorDialogFeature;

	@Inject
	private CommentsDialogHandlerFeature commentsDialogFeature;

	@Inject
	private RegisterDialogHandlerFeature registerDialogFeature;

	@Inject
	private NewBookDialogHandlerFeature newBookDialogFeature;

	@Inject
	private DownloadWidgetHandlerFeature downloadFeature;

	@Inject
	private LookupUserDialogHandlerFeature lookupUserDialogFeature;

	@Inject
	private ViewBookassetDialogHandlerFeature viewBookassetDialogFeature;

	@Inject
	private ViewBookCommentsHandlerFeature viewBookCommentsDialogFeature;

	@Inject
	private AddBookCommentsHandlerFeature addBookCommentDialogFeature;

	@Inject
	private EventCalendarRequestHandlerFeature eventCalendarRequestHandlerFeature;

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
	 * 4. Set up inactivity feature 5. set up window resize feature 6. setup
	 * window close trap feature
	 */
	@AfterInitialization
	public void postInit() {

		// Initialize features
		// Unhandled errors
		unhandledExceptionHandlerFeature.standUpAndActivate(null);

		// screen timeout
		screenInactivityTimeoutFeature
				.addStandupCallbackHandler(new FeatureCallbackHandler() {
					@Override
					public void executeCallBack() {
						observesClientLogoutRequestEvent(new ClientLogoutRequestEvent(
								true));
					}
				});
		screenInactivityTimeoutFeature.standUpAndActivate(null);

		// Window close trap
		windowCloseTrappingFeature.standUpAndActivate(null);

		// Not all GWT panel will resize when the window resizes
		// Observing this event gives panel a chance to resize if necessary
		mainWindowResizeFeature.standUpAndActivate(null);

		// Set up the server ping service. Don't start it yet
		loggedInUserServerPingFeature
				.addStandupCallbackHandler(new FeatureCallbackHandler() {
					@Override
					public void executeCallBack() {
						observesClientLogoutRequestEvent(null);
					}
				});
		loggedInUserServerPingFeature.standUp(null);

		// Sleep timer
		sleepDetectionFeature.standUpAndActivate(null);

		// Login dialog
		loginDialogFeature.standUpAndActivate(null);

		// Error Dialog
		errorDialogFeature.standUpAndActivate(null);

		// Comment dialog
		commentsDialogFeature.standUpAndActivate(null);

		// Register Dialog
		registerDialogFeature.standUpAndActivate(null);

		// Register
		newBookDialogFeature.standUpAndActivate(null);

		// Download
		downloadFeature.standUpAndActivate(null);

		// Look up user
		lookupUserDialogFeature.standUpAndActivate(null);

		// View book asset
		viewBookassetDialogFeature.standUpAndActivate(null);

		// View Comments
		viewBookCommentsDialogFeature.standUpAndActivate(null);

		// Add comments
		addBookCommentDialogFeature.standUpAndActivate(null);

		// create a request to add an event to the event calendar
		eventCalendarRequestHandlerFeature.standUpAndActivate(null);

		// Footer
		buildFooter();

		// SidebarMenu
		buildSidebarMenu();

		// Where the news widget goes
		buildRightSideBar();

		// Header
		buildNavBar();

		// Add navigation panel
		navContent.add(navigator.getNavContentPanel());

		// Fire get news event
		newsItemsRequestEvent.fire(new NewsItemsRequestEvent());
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
	 * When the user logs out the ClientUserToken is updated, the ping service
	 * is stopped, and the ClientUserUpdatedEvent is fired. This event is fired
	 * from the Header class along with the LogoutRequestEvent. The
	 * ClientLogoutRequestEvent is processed here while the LogoutRequestEvent
	 * is processed by the server.
	 * 
	 * @param logoutResponseEvent
	 */
	protected void observesClientLogoutRequestEvent(
			@Observes ClientLogoutRequestEvent clientLogoutRequestEvent) {
		clientUserToken.clear();
		clientUserUpdatedEvent.fire(new ClientUserUpdatedEvent());
		navigator.showDefaultPage();
		loggedInUserServerPingFeature.deactivate();
		screenInactivityTimeoutFeature.deactivate();

		// If required, tell the server
		if (clientLogoutRequestEvent.isTellServer() == true) {
			logoutRequestEvent.fire(new LogoutRequestEvent());
		}
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
		loggedInUserServerPingFeature.activate();
		screenInactivityTimeoutFeature.activate();
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