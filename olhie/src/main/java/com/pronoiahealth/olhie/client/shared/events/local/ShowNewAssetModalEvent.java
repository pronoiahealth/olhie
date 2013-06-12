package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;

/**
 * ShowNewAssetModalEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Signals that the app should show the NewAssetDialog<br/>
 * 
 * <p>
 * Fired By: NewBookPage class<br/>
 * Observed By: NewAssetDialog class<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jun 11, 2013
 *
 */
@Local
public class ShowNewAssetModalEvent {

	/**
	 * Constructor
	 *
	 */
	public ShowNewAssetModalEvent() {
	}

}
