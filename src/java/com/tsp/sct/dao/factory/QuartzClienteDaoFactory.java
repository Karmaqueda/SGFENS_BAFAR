/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.tsp.sct.dao.factory;

import java.sql.Connection;
import com.tsp.sct.dao.dao.*;
import com.tsp.sct.dao.jdbc.*;

public class QuartzClienteDaoFactory
{
	/**
	 * Method 'create'
	 * 
	 * @return QuartzClienteDao
	 */
	public static QuartzClienteDao create()
	{
		return new QuartzClienteDaoImpl();
	}

	/**
	 * Method 'create'
	 * 
	 * @param conn
	 * @return QuartzClienteDao
	 */
	public static QuartzClienteDao create(Connection conn)
	{
		return new QuartzClienteDaoImpl( conn );
	}

}
