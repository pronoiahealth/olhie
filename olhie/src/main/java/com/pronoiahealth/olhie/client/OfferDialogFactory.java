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
package com.pronoiahealth.olhie.client;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.ioc.client.container.IOC;
import org.jboss.errai.ioc.client.container.IOCBeanDef;

import com.pronoiahealth.olhie.client.widgets.chat.AcceptOfferDialog;
import com.pronoiahealth.olhie.client.widgets.chat.AcceptOfferDialogCloseHandler;
import com.pronoiahealth.olhie.client.widgets.chat.ChatDialog;
import com.pronoiahealth.olhie.client.widgets.chat.ChatDialogCloseHandler;

/**
 * OfferDialogFactory.java<br/>
 * Responsibilities:<br/>
 * 1. Application scoped factory to create new Chat and Accept dialogs
 *
 * @author John DeStefano
 * @version 1.0
 * @since Jul 25, 2013
 *
 */
@ApplicationScoped
public class OfferDialogFactory {

	/**
	 * Constructor
	 *
	 */
	public OfferDialogFactory() {
	}

	public ChatDialog createChatDialog(String caption, String channelId,
			ChatDialogCloseHandler closeHandler) {
		IOCBeanDef<ChatDialog> cdd = IOC.getBeanManager().lookupBean(
				ChatDialog.class, (Annotation[]) null);
		ChatDialog cd = cdd.newInstance();
		cd.initDialog(caption, channelId, closeHandler);
		return cd;
	}

	public AcceptOfferDialog createAcceptOfferDialog(String channelId,
			String offererName, AcceptOfferDialogCloseHandler closeHandler) {
		IOCBeanDef<AcceptOfferDialog> cdd = IOC.getBeanManager().lookupBean(
				AcceptOfferDialog.class, (Annotation[]) null);
		AcceptOfferDialog cd = cdd.newInstance();
		cd.init(channelId, offererName, closeHandler);
		return cd;
	}

}
