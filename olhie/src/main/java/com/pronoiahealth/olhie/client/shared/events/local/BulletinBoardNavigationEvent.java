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
package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;

/**
 * BulletinBoardNavigationEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Fired by the Default page (BulletinBoardPage) when hiding or showing<br>
 * 
 * <p>
 * Fired By: BulletinBoardPage<br/>
 * Observed By: MainPage<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 19, 2013
 * 
 */
@Local
public class BulletinBoardNavigationEvent {
	public enum VisibleStateEnum {
		HIDING, SHOWING
	};

	private VisibleStateEnum visibleState = VisibleStateEnum.HIDING;

	/**
	 * Constructor
	 * 
	 */
	public BulletinBoardNavigationEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param visibleState
	 */
	public BulletinBoardNavigationEvent(VisibleStateEnum visibleState) {
		super();
		this.visibleState = visibleState;
	}

	public VisibleStateEnum getVisibleState() {
		return visibleState;
	}

	public void setVisibleState(VisibleStateEnum visibleState) {
		this.visibleState = visibleState;
	}
}
