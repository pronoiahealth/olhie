package com.pronoiahealth.olhie.server.services.dbaccess.orient;

import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.pronoiahealth.olhie.client.shared.vo.LoggedInSession;
import com.pronoiahealth.olhie.server.services.dbaccess.StartupDAO;

/**
 * OrientStartupDAOImpl.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 15, 2013
 * 
 */
public class OrientStartupDAOImpl extends OrientBaseDBFactoryDAO implements StartupDAO {
	/**
	 * Constructor
	 * 
	 */
	public OrientStartupDAOImpl() {
	}

	/**
	 * Called when loading application to clear any active records
	 * 
	 * @param ooDbTx
	 * @param handleTransaction
	 * @throws Exception
	 */
	@Override
	public void inactivateAllActive() throws Exception {
		OObjectDatabaseTx ooDbTx = this.getConnection();
		ooDbTx.begin(TXTYPE.OPTIMISTIC);
		try {
			List<LoggedInSession> sesses = getAllActiveSessions();
			if (sesses != null && sesses.size() > 0) {
				for (LoggedInSession sess : sesses) {
					sess.setActive(false);
					ooDbTx.save(sess);
				}
			}
			ooDbTx.commit();
		} catch (Exception e) {
			if (ooDbTx != null) {
				ooDbTx.rollback();
			}
			throw e;
		} finally {
			closeConnection(ooDbTx);
		}
	}

	/**
	 * Get all rows that are active
	 * 
	 * @param ooDbTx
	 * @return
	 */
	@Override
	public List<LoggedInSession> getAllActiveSessions() throws Exception {
		OObjectDatabaseTx ooDbTx = this.getConnection();
		try {
			OSQLSynchQuery<LoggedInSession> baQuery = null;
			baQuery = new OSQLSynchQuery<LoggedInSession>(
					"select from LoggedInSession where active = true");
			HashMap<String, String> baparams = new HashMap<String, String>();
			List<LoggedInSession> baResult = ooDbTx.command(baQuery).execute(
					baparams);
			return baResult;
		} catch (Exception e) {
			throw e;
		} finally {
			closeConnection(ooDbTx);
		}
	}

}
