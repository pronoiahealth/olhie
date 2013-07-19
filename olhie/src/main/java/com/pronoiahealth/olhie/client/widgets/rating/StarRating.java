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
package com.pronoiahealth.olhie.client.widgets.rating;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * StarRating is a widget that creates an interactive rating control.<br/>
 * 
 * Changes: <br/>
 * destefano - added readonly feature<br/>
 * 
 * @author Florian Maul (f.maul@sourceforge.net)
 */
public class StarRating extends Widget {

	/**
	 * Private subclass that represents a single star in the rating widget.
	 * 
	 * @author Florian Maul (f.maul@sourceforge.net)
	 */
	private class StarImage extends Widget {

		/**
		 * Stores if the star is currently highlighted by the user (mouseover).
		 */
		private boolean highlighted = false;

		/**
		 * Stores if the star is currently activated (marked as active).
		 */
		private boolean active = false;

		/**
		 * Sets the current star as activated and updates it's appearance.
		 * 
		 * @param isActive
		 *            a bool that indicates whether the star is active.
		 */
		public void setActive(final boolean isActive) {
			if (this.active != isActive) {
				this.active = isActive;
				updateImage();
			}
		}

		/**
		 * Sets the current star as highlighted and updates it's appearance.
		 * 
		 * @param isHighlighted
		 *            a bool that indicates whether the star is highlighted.
		 */
		public void setHighlighted(final boolean isHighlighted) {
			if (this.highlighted != isHighlighted) {
				this.highlighted = isHighlighted;
				updateImage();
			}
		}

		/**
		 * Updates the appearance (css style class) of the star.
		 */
		private void updateImage() {
			if (active) {
				if (highlighted) {
					setStyleName("starrating-active-marked");
				} else {
					setStyleName("starrating-active-unmarked");
				}
			} else {
				if (highlighted) {
					setStyleName("starrating-inactive-marked");
				} else {
					setStyleName("starrating-inactive-unmarked");
				}
			}
		}

		/**
		 * Initializes the a star object.
		 */
		public StarImage() {
			super();
			setElement(DOM.createSpan());
			active = false;
			highlighted = false;
			updateImage();
		}
	}

	// ------------------------------------------------------------------------------

	/**
	 * An array with star objects that each represent a star image.
	 */
	private StarImage[] stars;

	/**
	 * Stores the number of stars that are currently selected.
	 */
	private int starsActive = 0;

	/**
	 * Stores the position of the star that is currently highlighted by the
	 * user.
	 */
	private int starsHighlighted = 0;

	/**
	 * The label which shows the ratio of selected stars, e.g. (8/10).
	 */
	private Label lblRating;

	/**
	 * An animated image which is used as a progress indicator.
	 */
	private Image imgProgress;

	/**
	 * A string which allows a user of this component to store information about
	 * the object that is rated by this control. The object type can specifiy an
	 * entity or any other user specific string data.
	 */
	private String ratedObjectType = null;

	/**
	 * A string which allows a user of this component to store information about
	 * the object that is rated by this control. The object id can specifiy an
	 * ID of a database object or any other user specific integer data.
	 */
	private Integer ratedObjectId = null;

	private boolean readOnly;

	private StarRatingStarClickedHandler starClickedHandler;

	/**
	 * Creates a new StarRating-Object, sets the id and type fields but doesn't
	 * bind it to a RatingService.
	 * 
	 * @param theNumberOfStars
	 *            The number of stars the control will show.
	 * @param theRatedObjectId
	 *            Custom ID that identifies the object the rating belongs to.
	 * @param theRatedObjectType
	 *            Custom string, e.g. an object type.
	 */
	public StarRating(final int theNumberOfStars,
			final Integer theRatedObjectId, final String theRatedObjectType,
			boolean readOnly, StarRatingStarClickedHandler starClickedHandler) {
		super();
		this.readOnly = readOnly;
		setRatedObjectId(theRatedObjectId);
		setRatedObjectType(theRatedObjectType);
		initializeComponent();
		initializeDisplay(theNumberOfStars);
		if (readOnly == false) {
			doEventCapture();
		}
		this.starClickedHandler = starClickedHandler;

	}

	/**
	 * Creates a new StarRating-Object without any object ids or service
	 * binding.
	 * 
	 * @param theNumberOfStars
	 *            The number of stars the control will show.
	 */
	public StarRating(final int theNumberOfStars, boolean readOnly) {
		this(theNumberOfStars, null, null, readOnly, null);
	}

	public StarRating(final int theNumberOfStars, boolean readOnly,
			StarRatingStarClickedHandler starClickedHandler) {
		this(theNumberOfStars, null, null, readOnly, starClickedHandler);
	}

	/**
	 * Creates the base DOM element.
	 */
	private void initializeComponent() {
		setElement(DOM.createSpan());
		this.setStyleName("starrating");
	}

	/**
	 * Capture the onmouseover, onmouseout and onclick events
	 */
	private void doEventCapture() {
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONCLICK);
	}

	/**
	 * Un-Capture the onmouseover, onmouseout and onclick events
	 */
	private void doEventUnCapture() {
		unsinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONCLICK);
	}

	/**
	 * Set read only and activate or in-activate events
	 * 
	 * @param changeReadOnly
	 */
	/*
	public void setReadOnly(boolean changeReadOnly) {
		if (this.readOnly == true && changeReadOnly == false) {
			this.readOnly = changeReadOnly;
			doEventUnCapture();
		} else if (this.readOnly == false && changeReadOnly == true) {
			this.readOnly = changeReadOnly;
			doEventCapture();
		}
	}
	*/
	public void setReadOnly(boolean changeReadOnly) {
		if (changeReadOnly == true) {
			this.readOnly = changeReadOnly;
			doEventUnCapture();
		} else {
			this.readOnly = changeReadOnly;
			doEventCapture();
		}
	}

	/**
	 * Creates the Stars, the Label and initializes event handling.
	 * 
	 * @param theNumberOfStars
	 *            The number of stars to display.
	 */
	private void initializeDisplay(final int theNumberOfStars) {
		stars = new StarImage[theNumberOfStars];
		for (int i = 0; i < theNumberOfStars; i++) {
			StarImage star = new StarImage();
			stars[i] = star;
			add(star);
		}

		lblRating = new Label();
		add(lblRating);
		lblRating.setStyleName("starrating-label");

		imgProgress = new Image("Olhie/images/star_indicator_20px.gif");
		imgProgress.setStyleName("starrating-indicator-hidden");
		add(imgProgress);

		updateDisplay();
	}

	/**
	 * Convenience method to add child widgets under the base DOM element.
	 * 
	 * @param widget
	 *            The widget to add as a child.
	 */
	private void add(final Widget widget) {
		DOM.appendChild(getElement(), widget.getElement());
	}

	/**
	 * Determines the number of the star from the DOM element.
	 * 
	 * @param star
	 *            DOM element of a star.
	 * @return the number the star, starting with 1
	 */
	private int getStarNumber(final Element star) {
		for (int i = 0; i < stars.length; i++) {
			if (DOM.compare(stars[i].getElement(), star)) {
				return i + 1;
			}
		}
		return 0;
	}

	/**
	 * Sets the clicked star as active and contacts the RatingService.
	 * 
	 * @param starNumber
	 *            The number of the star that was clicked on
	 */
	private void starClicked(final int starNumber) {
		setRating(starNumber);
	}

	/**
	 * Displays a hover effect when the mouse hovers over a star.
	 * 
	 * @param starNumber
	 *            The number of the star that was hovered over
	 */
	private void starHovered(final int starNumber) {
		starsHighlighted = starNumber;
		updateDisplay();
	}

	/**
	 * Redisplays all stars based on their current state.
	 */
	private void updateDisplay() {
		for (int i = 0; i < stars.length; i++) {
			stars[i].setHighlighted(i < starsHighlighted);
			stars[i].setActive(i < starsActive);
		}
		lblRating.setText("(" + starsActive + "/" + stars.length + ")");
	}

	/**
	 * Handles the browser events.
	 * 
	 * @param event
	 *            a browser event
	 * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
	 */
	public final void onBrowserEvent(final Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:
			starHovered(getStarNumber(DOM.eventGetTarget(event)));
			break;
		case Event.ONMOUSEOUT:
			starHovered(0);
			break;
		case Event.ONCLICK:
			int starInt = getStarNumber(DOM.eventGetTarget(event));
			starClicked(starInt);
			if (starClickedHandler != null) {
				starClickedHandler.startClicked(starInt);
			}

			break;
		default:
			super.onBrowserEvent(event);
		}
	}

	/**
	 * Returns the current rating as a fraction.
	 * 
	 * @return fraction, a float between 0 and 1
	 */
	public final float getFractionRating() {
		return starsActive / stars.length;
	}

	public Integer getRatedObjectId() {
		return ratedObjectId;
	}

	public void setRatedObjectId(Integer ratedObjectId) {
		this.ratedObjectId = ratedObjectId;
	}

	public String getRatedObjectType() {
		return ratedObjectType;
	}

	public void setRatedObjectType(String ratedObjectType) {
		this.ratedObjectType = ratedObjectType;
	}

	public int getRating() {
		return starsActive;
	}

	public void setRating(int number) {
		this.starsActive = number;
		updateDisplay();
	}
}