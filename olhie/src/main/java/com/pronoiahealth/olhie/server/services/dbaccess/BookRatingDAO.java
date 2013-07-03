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
package com.pronoiahealth.olhie.server.services.dbaccess;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public class BookRatingDAO {

	public static int getUserBookRating(String userId, String bookId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		return 3;
	}
	
	public static int getAvgBookRating(String bookId,
			OObjectDatabaseTx ooDbTx) throws Exception {
		return 3;
	}

}
