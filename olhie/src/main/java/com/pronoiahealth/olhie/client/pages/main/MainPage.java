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

import org.jboss.errai.bus.client.api.BusLifecycleEvent;
import org.jboss.errai.bus.client.api.BusLifecycleListener;
import org.jboss.errai.bus.client.api.TransportError;
import org.jboss.errai.bus.client.api.base.DefaultErrorCallback;
import org.jboss.errai.bus.client.api.base.TransportIOException;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.common.client.protocols.MessageParts;
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
import com.pronoiahealth.olhie.client.pages.newbook.NewBookDialog;
import com.pronoiahealth.olhie.client.pages.register.RegisterDialog;
import com.pronoiahealth.olhie.client.shared.events.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.events.LoggedInPingEvent;
import com.pronoiahealth.olhie.client.shared.events.LoginResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.LogoutResponseEvent;
import com.pronoiahealth.olhie.client.shared.events.NewsItemsRequestEvent;
import com.pronoiahealth.olhie.client.shared.events.local.ClientUserUpdatedEvent;
import com.pronoiahealth.olhie.client.shared.events.local.WindowResizeEvent;
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
public class MainPage extends AbstractComposite implements BusLifecycleListener {

	@Inject
	UiBinder<Widget, MainPage> binder;

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
	protected MessageBus bus;

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
	 * 1. Set up ErraiBus default error handlers<br/>
	 * 2. Set up screen timer<br/>
	 * 3. Initial sidebar menu <br/>
	 */
	@AfterInitialization
	public void postInit() {

		// Default bus error handler
		bus.subscribe(DefaultErrorCallback.CLIENT_ERROR_SUBJECT,
				new MessageCallback() {

					@Override
					public void callback(Message message) {
						try {
							Throwable caught = message.get(Throwable.class,
									MessageParts.Throwable);
							throw caught;
						} catch (TransportIOException e) {
							// thrown in case the server can't be reached or an
							// unexpected status code was returned

						} catch (Throwable throwable) {
							// handle system errors (e.g response marshaling
							// errors) - that of course should never happen :)

						}
					}
				});

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
		screenTimer.schedule(screenTimeout);

		// Create global event listener
		// Any events (mouse or keyboard) will reset the timer
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
	}

	/**
	 * When the user logs out the ClientUserToken is updated, the pind service
	 * is stopped, and the ClientUserUpdatedEvent is fired.
	 * 
	 * @param logoutResponseEvent
	 */
	public void observesLogoutResponseEvent(
			@Observes LogoutResponseEvent logoutResponseEvent) {
		clientUserToken.clear();
		cancelPing();
		clientUserUpdatedEvent.fire(new ClientUserUpdatedEvent());
		navigator.showDefaultPage();
	}

	/**
	 * When a login event is observed the clientUserToken is updated, the ping
	 * service starts, and the ClientUserUpdatedEvent is fired.
	 * 
	 * @param loginResponseEvent
	 */
	public void observesLoginResponseEvent(
			@Observes LoginResponseEvent loginResponseEvent) {
		User user = loginResponseEvent.getUser();
		clientUserToken.setFullName(user.getFirstName() + " "
				+ user.getLastName());
		clientUserToken.setLoggedIn(true);
		clientUserToken.setUserId(user.getUserId());
		clientUserToken.setRole(user.getRole());
		startPinging();
		clientUserUpdatedEvent.fire(new ClientUserUpdatedEvent());
	}

	/**
	 * Captures the ErraiBus event
	 * 
	 * @see org.jboss.errai.bus.client.api.BusLifecycleListener#busAssociating(org.jboss.errai.bus.client.api.BusLifecycleEvent)
	 */

	/**
	 * Captures the ErraiBus event
	 * 
	 * @see org.jboss.errai.bus.client.api.BusLifecycleListener#busAssociating(org.jboss.errai.bus.client.api.BusLifecycleEvent)
	 */
	@Override
	public void busAssociating(BusLifecycleEvent e) {
		clientErrorEvent.fire(new ClientErrorEvent(e.getReason()
				.getErrorMessage()));
	}

	/**
	 * Captures the ErraiBus event
	 * 
	 * @see org.jboss.errai.bus.client.api.BusLifecycleListener#busDisassociating(org.jboss.errai.bus.client.api.BusLifecycleEvent)
	 */
	@Override
	public void busDisassociating(BusLifecycleEvent e) {
		clientErrorEvent.fire(new ClientErrorEvent(e.getReason()
				.getErrorMessage()));
	}

	/**
	 * Captures the ErraiBus event
	 * 
	 * @see org.jboss.errai.bus.client.api.BusLifecycleListener#busOnline(org.jboss.errai.bus.client.api.BusLifecycleEvent)
	 */
	@Override
	public void busOnline(BusLifecycleEvent e) {
		clientErrorEvent.fire(new ClientErrorEvent(e.getReason()
				.getErrorMessage()));
	}

	/**
	 * Captures the ErraiBus event
	 * 
	 * @see org.jboss.errai.bus.client.api.BusLifecycleListener#busOffline(org.jboss.errai.bus.client.api.BusLifecycleEvent)
	 */
	@Override
	public void busOffline(BusLifecycleEvent e) {
		clientErrorEvent.fire(new ClientErrorEvent(e.getReason()
				.getErrorMessage()));
	}

	/**
	 * Gets the error message from and ErraiBuss error event
	 * 
	 * @param e
	 * @return
	 */
	private String getErrMsg(BusLifecycleEvent e) {
		TransportError err = e.getReason();
		if (err != null) {
			return err.getErrorMessage();
		} else {
			return null;
		}
	}

	/**
	 * After a user logs in the start pinging the server.
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
}
