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
package com.pronoiahealth.olhie.server.dataaccess;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import com.pronoiahealth.olhie.server.services.dbaccess.BookDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.CalendarEventDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.LoggedInSessionDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.NewsItemDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.OfferDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.StartupDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.UserDAO;
import com.pronoiahealth.olhie.server.services.dbaccess.orient.OrientBookDAOImpl;
import com.pronoiahealth.olhie.server.services.dbaccess.orient.OrientCalendarEventDAOImpl;
import com.pronoiahealth.olhie.server.services.dbaccess.orient.OrientLoggedInSessionDAOImpl;
import com.pronoiahealth.olhie.server.services.dbaccess.orient.OrientNewsItemDAOImpl;
import com.pronoiahealth.olhie.server.services.dbaccess.orient.OrientOfferDAOImpl;
import com.pronoiahealth.olhie.server.services.dbaccess.orient.OrientStartupDAOImpl;
import com.pronoiahealth.olhie.server.services.dbaccess.orient.OrientUserDAOImpl;

/**
 * DAOProducer.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 12, 2013
 * 
 */
@ApplicationScoped
public class DAOProducer {
	@Inject
	private Logger log;

	@Inject
	@ConfigProperty(name = "dbImpl", defaultValue = "orient")
	private String dbImpl;

	public DAOProducer() {
	}

	/**
	 * Get appropriate BookDAO
	 * 
	 * @return
	 * @throws Exception
	 */
	@Produces
	@DAO
	public BookDAO getBookDAO() throws Exception {
		if (dbImpl.equals("orient")) {
			// Must return an injected bean and not one create with "new".
			return BeanProvider.getContextualReference(OrientBookDAOImpl.class,
					true);
		} else {
			log.log(Level.SEVERE, "Could not produce a BookDAO implementation");
			throw new DAOException("Could not produce a BookDAO");
		}
	}

	/**
	 * UserBookRelationshipDAO
	 * 
	 * @return
	 * @throws Exception
	 */
	@Produces
	@DAO
	public LoggedInSessionDAO getUserBookRelationshipDAO() throws Exception {
		if (dbImpl.equals("orient")) {
			// Must return an injected bean and not one create with "new".
			return BeanProvider.getContextualReference(
					OrientLoggedInSessionDAOImpl.class, true);
		} else {
			log.log(Level.SEVERE,
					"Could not produce a LoggedInSessionDAO implementation");
			throw new DAOException("Could not produce a LoggedInSessionDAO");
		}
	}

	/**
	 * User DAO
	 * 
	 * @return
	 * @throws Exception
	 */
	@Produces
	@DAO
	public UserDAO getUserDAO() throws Exception {
		if (dbImpl.equals("orient")) {
			// Must return an injected bean and not one create with "new".
			return BeanProvider.getContextualReference(OrientUserDAOImpl.class,
					true);
		} else {
			log.log(Level.SEVERE, "Could not produce a UserDAO implementation");
			throw new DAOException("Could not produce a UserDAO");
		}
	}

	/**
	 * Offer DAO
	 * 
	 * @return
	 * @throws Exception
	 */
	@Produces
	@DAO
	public OfferDAO getOfferDAO() throws Exception {
		if (dbImpl.equals("orient")) {
			// Must return an injected bean and not one create with "new".
			return BeanProvider.getContextualReference(
					OrientOfferDAOImpl.class, true);
		} else {
			log.log(Level.SEVERE,
					"Could not produce a OrientOfferDAOImpl implementation");
			throw new DAOException("Could not produce a OfferDAO");
		}
	}

	/**
	 * Startup DAO
	 * 
	 * @return
	 * @throws Exception
	 */
	@Produces
	@DAO
	public StartupDAO getStartupDAO() throws Exception {
		if (dbImpl.equals("orient")) {
			// Must return an injected bean and not one create with "new".
			OrientStartupDAOImpl impl = BeanProvider.getContextualReference(
					OrientStartupDAOImpl.class, true);
			return impl;
		} else {
			log.log(Level.SEVERE,
					"Could not produce a OrientStartupDAOImpl implementation");
			throw new DAOException("Could not produce a StartupDAO");
		}
	}

	/**
	 * NewsItem DAO
	 * 
	 * @return
	 * @throws Exception
	 */
	@Produces
	@DAO
	public NewsItemDAO getNewsItemDAO() throws Exception {
		if (dbImpl.equals("orient")) {
			// Must return an injected bean and not one create with "new".
			OrientNewsItemDAOImpl impl = BeanProvider.getContextualReference(
					OrientNewsItemDAOImpl.class, true);
			return impl;
		} else {
			log.log(Level.SEVERE,
					"Could not produce a OrientNewsItemDAOImpl implementation");
			throw new DAOException("Could not produce a NewsItemDAO");
		}
	}
	
	/**
	 * CalendarEvent DAO
	 * 
	 * @return
	 * @throws Exception
	 */
	@Produces
	@DAO
	public CalendarEventDAO getCalendarEventDAO() throws Exception {
		if (dbImpl.equals("orient")) {
			// Must return an injected bean and not one create with "new".
			OrientCalendarEventDAOImpl impl = BeanProvider.getContextualReference(
					OrientCalendarEventDAOImpl.class, true);
			return impl;
		} else {
			log.log(Level.SEVERE,
					"Could not produce a OrientCalendarEventDAOImpl implementation");
			throw new DAOException("Could not produce a CalendarEventDAO");
		}
	}


}
