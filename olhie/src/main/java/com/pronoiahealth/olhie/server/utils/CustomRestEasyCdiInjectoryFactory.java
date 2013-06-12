package com.pronoiahealth.olhie.server.utils;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.jboss.resteasy.cdi.CdiInjectorFactory;

public class CustomRestEasyCdiInjectoryFactory extends CdiInjectorFactory {

	@Override
	protected BeanManager lookupBeanManager() {
		return BeanManagerProvider.getInstance().getBeanManager();
	}
}
