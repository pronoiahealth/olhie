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
package com.pronoiahealth.olhie.server.dataaccess.orient;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

/**
 * OrientDb object factory<br/>
 * 
 * @author John DeStefano
 * @version 1.0
 * @since May 25, 2013
 * 
 */
// TODO: Test embedded config
@ApplicationScoped
public class OrientFactory {
	@Inject
	private Logger log;

	@Inject
	@ConfigProperty(name = "dbUser", defaultValue = "admin")
	private String dbUserName;

	@Inject
	@ConfigProperty(name = "dbPwd", defaultValue = "admin")
	private String dbPwd;

	@Inject
	@ConfigProperty(name = "dbConnectURL", defaultValue = "remote:localhost/olhie")
	private String dbConStr;

	@Inject
	@ConfigProperty(name = "dbMode", defaultValue = "remote")
	private String dbMode;

	@Inject
	@DBValueObjects
	private String[] dbValObjs;

	private OServer server = null;

	public OrientFactory() {
	}

	/**
	 * Initialize the embedded database if using and register any required
	 * classes with the entity manager.
	 */
	@PostConstruct
	private void postConstruct() {
		if (dbMode.equals("embedded") == true) {
			startEmbeddedServer();
			registerClasses();
		}

		if (dbValObjs != null && dbValObjs.length > 0) {
			registerClasses();
		}
	}

	/**
	 * Start the embedded server when called for. The dbMode would need to be
	 * "embedded".
	 */
	private void startEmbeddedServer() {
		try {
			server = OServerMain.create();
			server.startup(getClass().getResourceAsStream("db.config.xml"));
			server.activate();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error configuring Orient DB!", e);
		}
	}

	private void registerClasses() {
		try {
			OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(
					dbConStr, dbUserName, dbPwd);
			for (String str : dbValObjs) {
				str = str.trim();
				Class clazz = Thread.currentThread().getContextClassLoader()
						.loadClass(str);
				db.getEntityManager().registerEntityClass(clazz);
			}
			db.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error configuring Orient DB!", e);
		}
	}

	/**
	 * Close embedded server if using embedded
	 */
	@PreDestroy
	private void preDestroy() {
		if (dbMode.equals("embedded") == true) {
			server.shutdown();
		}
	}

	/**
	 * Get a transacted connection from the database connection pool
	 * 
	 * @return
	 */
	@Produces
	@OODbTx
	public OObjectDatabaseTx createOObjTx(InjectionPoint ip) {
		OObjectDatabaseTx ooDbTx = OObjectDatabasePool.global()
				.acquire(dbConStr, dbUserName, dbPwd);
		
		log.log(Level.INFO, "Aquired connection " + ooDbTx.hashCode() + " for bean " + ip.toString());
		
		return ooDbTx;
	}

	/**
	 * When the object that acquired the transacted connection releases it
	 * (probably object garbage collected) then return the connection to the
	 * database pool.
	 * 
	 * @param ooDbTx
	 */
	public void disposeOObjTx(@Disposes @OODbTx OObjectDatabaseTx ooDbTx) {
		
		log.log(Level.INFO, "Released connection " + ooDbTx.hashCode());
		
		ooDbTx.close();
	}
}
