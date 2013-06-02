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
package com.pronoiahealth.olhie.client.clientfactories;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * PingFireTime.java<br/>
 * Responsibilities:<br/>
 * 1. Annotates the pingFireTime interval.<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Jun 1, 2013
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD  })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface PingFireTime {
}
