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
package com.pronoiahealth.olhie.server.services;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.QueueBookEvent;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;

/**
 * SolrQueueingService.java<br/>
 * Responsibilities:<br/>
 * 1. Application scoped bean that will respond to the QueueBookEvent.<br/>
 * 
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Dec 5, 2013
 * 
 */
@ApplicationScoped
public class SolrQueueingService {

	/**
	 * ThreadLocal date formatter
	 */
	private static ThreadLocal<SimpleDateFormat> dtFormatHolder = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy");
		}
	};

	@Inject
	private Logger log;

	@Inject
	@DAO
	private BookDAO bookDAO;

	@Inject
	@ConfigProperty(name = "SOLR_JMS_USERID", defaultValue = "guest")
	private String jmxUserid;

	@Inject
	@ConfigProperty(name = "SOLR_JMS_USERPWD", defaultValue = "guestw")
	private String jmsPwd;

	@Resource(mappedName = "java:/ConnectionFactory")
	ConnectionFactory factory;

	@Resource(mappedName = "java:/queue/solr")
	Queue queue;

	/**
	 * Constructor
	 * 
	 */
	public SolrQueueingService() {
	}

	/**
	 * Send the book data to the solr queue. This should happen whenever a book
	 * is updated (new also)
	 * 
	 * 
	 * @param queueBookEvent
	 */
	@SecureAccess({ SecurityRoleEnum.ADMIN, SecurityRoleEnum.AUTHOR })
	protected void observesQueueBookEvent(
			@Observes QueueBookEvent queueBookEvent) {

		String bookId = null;
		Connection connection = null;
		Session session = null;

		try {
			// Get the Book
			bookId = queueBookEvent.getBookId();

			// Get the user
			String userId = queueBookEvent.getUserId();

			// Create a connection and Message producer
			connection = factory.createConnection("guest", "guestw");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session.createProducer(queue);

			// send text message to queue
			TextMessage message = session.createTextMessage(bookId + "|"
					+ userId);
			sender.send(message);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e1) {
					log.log(Level.SEVERE, e1.getMessage(), e1);
				}
			}
		}
	}
}
