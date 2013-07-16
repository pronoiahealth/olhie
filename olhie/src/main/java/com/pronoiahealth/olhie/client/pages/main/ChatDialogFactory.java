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
package com.pronoiahealth.olhie.client.pages.main;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.ioc.client.container.IOC;
import org.jboss.errai.ioc.client.container.IOCBeanDef;

@ApplicationScoped
public class ChatDialogFactory {

	public ChatDialogFactory() {
	}

	public ChatDialog createChatDialog(String caption, String channelId,
			ChatDialogCloseHandler closeHandler) {
		IOCBeanDef<ChatDialog> cdd = IOC.getBeanManager()
				.lookupBean(ChatDialog.class, (Annotation[]) null);
		ChatDialog cd = cdd.newInstance();
		cd.initDialog(caption, channelId, closeHandler);
		return cd;
	}

}
