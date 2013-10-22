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
package com.pronoiahealth.olhie.client.widgets.scrolldiv;

import static com.google.gwt.query.client.GQuery.$;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

/**
 * ScrollDiv.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Oct 21, 2013
 * 
 */
@Templated("#root")
public class ScrollDiv extends Composite {

	@DataField("root")
	private Element root = DOM.createDiv();

	private GQuery scrollLink;

	/**
	 * Constructor
	 * 
	 */
	public ScrollDiv() {
	}

	/**
	 * Access to the root element
	 * 
	 * @return
	 */
	public Element getRootElement() {
		return root;
	}

	@PostConstruct
	protected void postConstruct() {
		// Activate full page scrolling
		// Get the root div
		final GQuery gObj = $(root);

		// Make sure its overflow is set to auto
		gObj.css(CSS.OVERFLOW.with(Overflow.AUTO));

		// Create the scroll link
		scrollLink = $("<a href=\"#\" class=\"ph-BulletinBoard-Scrollup\">Scroll</a>");

		// Append the link to the root div
		gObj.append(scrollLink);

		// Bind the scroll event
		gObj.bind(Event.ONSCROLL, new Function() {
			@Override
			public boolean f(Event e) {
				GQuery rootDiv = $(e);
				int scrollTop = rootDiv.scrollTop();
				if (scrollTop > 100) {
					scrollLink.fadeIn(500, new Function() {
						@Override
						public void f(com.google.gwt.dom.client.Element e) {
							$(e).css(Properties.create("opacity: 1.0;"));
							// super.f(e);
						}
					});
				} else {
					scrollLink.fadeOut(500, new Function() {
						@Override
						public void f(com.google.gwt.dom.client.Element e) {
							$(e).css(Properties.create("opacity: 0.0;"));
							// super.f(e);
						}
					});
				}
				return super.f(e);
			}
		});

		// Bind to the scrollup link
		scrollLink.bind(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				gObj.animate("scrollTop: 0", 500, (Function) null);
				return false;
			}
		});
	}

	@PreDestroy
	protected void preDestroy() {
		$(root).unbind(Event.ONSCROLL);
		scrollLink.unbind(Event.ONCLICK);
		scrollLink.remove();
	}

}
