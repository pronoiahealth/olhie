package com.pronoiahealth.olhie.client.widgets;

import org.jboss.errai.bus.client.api.BusLifecycleListener;

public interface BusStatus extends BusLifecycleListener {
	public BusStatusEnum getCurrentBusStatus();
}
