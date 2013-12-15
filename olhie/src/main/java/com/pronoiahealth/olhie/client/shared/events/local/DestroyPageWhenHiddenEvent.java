package com.pronoiahealth.olhie.client.shared.events.local;

import org.jboss.errai.bus.client.api.Local;
import org.jboss.errai.common.client.api.annotations.NonPortable;

import com.pronoiahealth.olhie.client.shared.constants.NavEnum;

@Local
@NonPortable
public class DestroyPageWhenHiddenEvent {
	private NavEnum pageNav;

	public DestroyPageWhenHiddenEvent() {
	}

	public DestroyPageWhenHiddenEvent(NavEnum pageNav) {
		super();
		this.pageNav = pageNav;
	}

	public NavEnum getPageNav() {
		return pageNav;
	}

	public void setPageNav(NavEnum pageNav) {
		this.pageNav = pageNav;
	}
}
