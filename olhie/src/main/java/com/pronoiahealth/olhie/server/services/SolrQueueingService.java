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

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;
import com.pronoiahealth.olhie.client.shared.events.book.QueueBookEvent;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.Bookasset;
import com.pronoiahealth.olhie.client.shared.vo.Bookassetdescription;
import com.pronoiahealth.olhie.client.shared.vo.User;
import com.pronoiahealth.olhie.server.dataaccess.DAO;
import com.pronoiahealth.olhie.server.security.SecureAccess;
import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;
import com.pronoiahealth.olhie.solr.xml.BookAsset;
import com.pronoiahealth.olhie.solr.xml.ObjectFactory;

/**
 * SolrQueueingService.java<br/>
 * Responsibilities:<br/>
 * 1. Application scoped bean that will response to the QueueBookEvent.<br/>
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
	 * is updated
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
			Book book = bookDAO.getBookById(bookId);

			// Get the user
			String userId = queueBookEvent.getUserId();
			User user = bookDAO.getUserByUserId(userId);

			// create a Book object
			com.pronoiahealth.olhie.solr.xml.Book solrBook = new com.pronoiahealth.olhie.solr.xml.Book();
			solrBook.setId(book.getId());
			solrBook.setBookTitle(book.getBookTitle());
			solrBook.setIntroduction(book.getIntroduction());
			solrBook.setKeyWords(book.getKeywords());
			solrBook.setCategory(book.getCategory());
			solrBook.setCoverName(book.getCoverName());
			solrBook.setCreatedDate(dtFormatHolder.get().format(
					book.getCreatedDate()));
			if (book.getActDate() != null) {
				solrBook.setPublishedDate(dtFormatHolder.get().format(
						book.getActDate()));
			} else {
				solrBook.setPublishedDate(null);
			}
			solrBook.setAuthorId(book.getAuthorId());
			solrBook.setActive(booleanStrVal(book.getActive()));

			// create a User
			com.pronoiahealth.olhie.solr.xml.User solrUser = new com.pronoiahealth.olhie.solr.xml.User();
			solrUser.setId(user.getId());
			solrUser.setFirstName(user.getFirstName());
			solrUser.setLastName(user.getLastName());
			solrUser.setRole(user.getRole());
			solrUser.setResetPwd(null);
			solrBook.getUser().add(solrUser);

			// Iterate over book assets
			List<Bookassetdescription> descs = bookDAO
					.getBookassetdescriptionByBookId(bookId, true);
			if (descs != null && descs.size() > 0) {
				for (Bookassetdescription desc : descs) {
					// create a BookAssetDescription
					com.pronoiahealth.olhie.solr.xml.BookAssetDescription solrBookAssetDescription = new com.pronoiahealth.olhie.solr.xml.BookAssetDescription();
					solrBookAssetDescription.setId(desc.getId());
					solrBookAssetDescription.setDescription(desc
							.getDescription());
					solrBookAssetDescription.setRemoved(booleanStrVal(desc
							.getRemoved()));
					solrBookAssetDescription.setCreatedDate(dtFormatHolder
							.get().format(desc.getCreatedDate()));
					solrBookAssetDescription.setBookId(desc.getBookId());
					solrBook.getBookAssetDescription().add(
							solrBookAssetDescription);

					// create a BookAsset
					if (desc.getBookAssets() != null) {
						Bookasset ba = desc.getBookAssets().get(0);
						BookAsset bookAsset = new BookAsset();
						bookAsset.setId(ba.getId());
						bookAsset.setItemName(ba.getItemName());
						bookAsset.setContentType(ba.getContentType());
						bookAsset.setSize(ba.getSize() == null ? "0" : ba
								.getSize().toString());
						bookAsset.setBase64Data(ba.getBase64Data());
						bookAsset.setBookassetdescriptionId(ba
								.getBookassetdescriptionId());
						solrBook.getBookAsset().add(bookAsset);
					}
				}
			}

			ObjectFactory of = new ObjectFactory();
			JAXBElement<com.pronoiahealth.olhie.solr.xml.Book> element = of
					.createBook(solrBook);

			// generate XML string with JAXB
			StringWriter writer = new StringWriter();
			JAXBContext jaxbContext = JAXBContext
					.newInstance("com.pronoiahealth.olhie.solr.xml");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(element, writer);

			// Create a connection and Message producer
			connection = factory.createConnection("guest", "guestw");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session.createProducer(queue);

			// send text message to queue
			TextMessage message = session.createTextMessage(writer.toString());
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

	private String booleanStrVal(Boolean b) {
		if (b == null || b.booleanValue() == false) {
			return "false";
		} else {
			return "true";
		}
	}
}
