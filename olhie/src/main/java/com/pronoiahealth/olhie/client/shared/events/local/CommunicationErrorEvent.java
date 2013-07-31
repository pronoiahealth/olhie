package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;
import org.jboss.errai.bus.client.api.TransportError;

/**
 * CommunicationErrorEvent.java<br/>
 * Responsibilities:<br/>
 * 1. Emitted to the system when a TransportError occurs.
 * 
 * <p>
 * Fired By: Olhie class on a TransportError<br/>
 * Observed By: ErrorDisplayDialog<br/>
 * </p>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jul 30, 2013
 * 
 */
@Local
public class CommunicationErrorEvent {
	private String msg;
	private TransportError error;
	private boolean reloadWindow;

	/**
	 * Constructor
	 * 
	 */
	public CommunicationErrorEvent() {
	}

	/**
	 * Constructor
	 * 
	 * @param msg
	 * @param error
	 */
	public CommunicationErrorEvent(String msg, TransportError error,
			boolean reloadWindow) {
		super();
		this.msg = msg;
		this.error = error;
		this.reloadWindow = reloadWindow;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public TransportError getError() {
		return error;
	}

	public void setError(TransportError error) {
		this.error = error;
	}

	public boolean isReloadWindow() {
		return reloadWindow;
	}

	public void setReloadWindow(boolean reloadWindow) {
		this.reloadWindow = reloadWindow;
	}
}
