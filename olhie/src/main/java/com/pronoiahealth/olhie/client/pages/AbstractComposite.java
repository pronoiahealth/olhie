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
package com.pronoiahealth.olhie.client.pages;
import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.user.client.ui.Composite;

/**
 * AbstractComposite - Abstract<br/>
 * Responsibilities:<br/>
 * 1. Base for displayable pages (see AbstractPage)<br/>
 * 2. Uses GQuery to get the width and height of screen elements<br/>
 * 3. Assists in dynamically sizing pages<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 *
 */
public abstract class AbstractComposite extends Composite {

	public AbstractComposite() {
	}

	/**
	 * Gets the width of the div with class center-background
	 * 
	 * @return
	 */
	public int getPageContainerWidth() {
		return AppSelectors.INSTANCE.getCenterBackground().width();
	}

	/**
	 * Gets the height of the div with the class center-background
	 * 
	 * @return
	 */
	public int getPageContainerHeight() {
		return AppSelectors.INSTANCE.getCenterBackground().height();
	}

	/**
	 * Get width of element
	 * 
	 * @param elementSelector
	 * @return
	 */
	public int getWidthOfElement(String elementSelector) {
		if (isAttached() == true) {
			return $(elementSelector).width();
		} else {
			return 0;
		}
	}

	/**
	 * Get height of element
	 * 
	 * @param elementSelector
	 * @return
	 */
	public int getHeightOfElement(String elementSelector) {
		if (isAttached() == true) {
			return $(elementSelector).height();
		} else {
			return 0;
		}
	}

}
