package com.pronoiahealth.olhie.server.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import com.pronoiahealth.olhie.client.shared.events.errors.ServiceErrorEvent;

/**
 * CDIObserverServerErrorExceptionInterceptor.java<br/>
 * Responsibilities:<br/>
 * 1. Wrap exception on methods that are CDI observers
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Mar 9, 2014
 * 
 */
@CDIObserverServerError
@Interceptor
public class CDIObserverServerErrorExceptionInterceptor {
	@Inject
	private Logger log;

	@Inject
	private Event<ServiceErrorEvent> serviceErrorEvent;

	public CDIObserverServerErrorExceptionInterceptor() {
	}

	/**
	 * Wrapp the execution of a CDI observer method and throw an exception if an
	 * error occurs
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object handleException(InvocationContext ctx) throws Exception {
		try {
			// Check to make sure that the first parameter of the method is
			// annotated with @Observable
			Object obj = ctx.getParameters()[0];
			Annotation annotation = obj.getClass()
					.getAnnotation(Observes.class);
			if (annotation != null) {
				return ctx.proceed();
			}
		} catch (Exception e) {
			String errMsg = e.getMessage();
			log.log(Level.SEVERE, errMsg, e);
			serviceErrorEvent.fire(new ServiceErrorEvent(errMsg));
		}

		// Exception was encountered
		return null;
	}
}
