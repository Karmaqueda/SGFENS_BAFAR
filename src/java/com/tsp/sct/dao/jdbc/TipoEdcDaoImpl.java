/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.tsp.sct.dao.jdbc;

import com.tsp.sct.dao.dao.*;
import com.tsp.sct.dao.factory.*;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.exceptions.*;
import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class TipoEdcDaoImpl extends AbstractDAO implements TipoEdcDao
{
	/** 
	 * The factory class for this DAO has two versions of the create() method - one that
takes no arguments and one that takes a Connection argument. If the Connection version
is chosen then the connection will be stored in this attribute and will be used by all
calls to this DAO, otherwise a new Connection will be allocated for each operation.
	 */
	protected java.sql.Connection userConn;

	/** 
	 * All finder methods in this class use this SELECT constant to build their queries
	 */
	protected final String SQL_SELECT = "SELECT ID_TIPO_EDC, DESC_TIPO_EDC FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( ID_TIPO_EDC, DESC_TIPO_EDC ) VALUES ( ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET ID_TIPO_EDC = ?, DESC_TIPO_EDC = ? WHERE ID_TIPO_EDC = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE ID_TIPO_EDC = ?";

	/** 
	 * Index of column ID_TIPO_EDC
	 */
	protected static final int COLUMN_ID_TIPO_EDC = 1;

	/** 
	 * Index of column DESC_TIPO_EDC
	 */
	protected static final int COLUMN_DESC_TIPO_EDC = 2;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 2;

	/** 
	 * Index of primary-key column ID_TIPO_EDC
	 */
	protected static final int PK_COLUMN_ID_TIPO_EDC = 1;

	/** 
	 * Inserts a new row in the tipo_edc table.
	 */
	public TipoEdcPk insert(TipoEdc dto) throws TipoEdcDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			stmt = conn.prepareStatement( SQL_INSERT );
			int index = 1;
			stmt.setInt( index++, dto.getIdTipoEdc() );
			stmt.setString( index++, dto.getDescTipoEdc() );
			System.out.println( "Executing " + SQL_INSERT + " with DTO: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TipoEdcDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the tipo_edc table.
	 */
	public void update(TipoEdcPk pk, TipoEdc dto) throws TipoEdcDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_UPDATE + " with DTO: " + dto );
			stmt = conn.prepareStatement( SQL_UPDATE );
			int index=1;
			stmt.setInt( index++, dto.getIdTipoEdc() );
			stmt.setString( index++, dto.getDescTipoEdc() );
			stmt.setInt( 3, pk.getIdTipoEdc() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TipoEdcDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the tipo_edc table.
	 */
	public void delete(TipoEdcPk pk) throws TipoEdcDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_DELETE + " with PK: " + pk );
			stmt = conn.prepareStatement( SQL_DELETE );
			stmt.setInt( 1, pk.getIdTipoEdc() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TipoEdcDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the tipo_edc table that matches the specified primary-key value.
	 */
	public TipoEdc findByPrimaryKey(TipoEdcPk pk) throws TipoEdcDaoException
	{
		return findByPrimaryKey( pk.getIdTipoEdc() );
	}

	/** 
	 * Returns all rows from the tipo_edc table that match the criteria 'ID_TIPO_EDC = :idTipoEdc'.
	 */
	public TipoEdc findByPrimaryKey(int idTipoEdc) throws TipoEdcDaoException
	{
		TipoEdc ret[] = findByDynamicSelect( SQL_SELECT + " WHERE ID_TIPO_EDC = ?", new Object[] {  new Integer(idTipoEdc) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the tipo_edc table that match the criteria ''.
	 */
	public TipoEdc[] findAll() throws TipoEdcDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY ID_TIPO_EDC", null );
	}

	/** 
	 * Returns all rows from the tipo_edc table that match the criteria 'ID_TIPO_EDC = :idTipoEdc'.
	 */
	public TipoEdc[] findWhereIdTipoEdcEquals(int idTipoEdc) throws TipoEdcDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_TIPO_EDC = ? ORDER BY ID_TIPO_EDC", new Object[] {  new Integer(idTipoEdc) } );
	}

	/** 
	 * Returns all rows from the tipo_edc table that match the criteria 'DESC_TIPO_EDC = :descTipoEdc'.
	 */
	public TipoEdc[] findWhereDescTipoEdcEquals(String descTipoEdc) throws TipoEdcDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DESC_TIPO_EDC = ? ORDER BY DESC_TIPO_EDC", new Object[] { descTipoEdc } );
	}

	/**
	 * Method 'TipoEdcDaoImpl'
	 * 
	 */
	public TipoEdcDaoImpl()
	{
	}

	/**
	 * Method 'TipoEdcDaoImpl'
	 * 
	 * @param userConn
	 */
	public TipoEdcDaoImpl(final java.sql.Connection userConn)
	{
		this.userConn = userConn;
	}

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows)
	{
		this.maxRows = maxRows;
	}

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * Method 'getTableName'
	 * 
	 * @return String
	 */
	public String getTableName()
	{
		return "TIPO_EDC";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected TipoEdc fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			TipoEdc dto = new TipoEdc();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected TipoEdc[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			TipoEdc dto = new TipoEdc();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		TipoEdc ret[] = new TipoEdc[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(TipoEdc dto, ResultSet rs) throws SQLException
	{
		dto.setIdTipoEdc( rs.getInt( COLUMN_ID_TIPO_EDC ) );
		dto.setDescTipoEdc( rs.getString( COLUMN_DESC_TIPO_EDC ) );
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(TipoEdc dto)
	{
	}

	/** 
	 * Returns all rows from the tipo_edc table that match the specified arbitrary SQL statement
	 */
	public TipoEdc[] findByDynamicSelect(String sql, Object[] sqlParams) throws TipoEdcDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TipoEdcDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns all rows from the tipo_edc table that match the specified arbitrary SQL statement
	 */
	public TipoEdc[] findByDynamicWhere(String sql, Object[] sqlParams) throws TipoEdcDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = SQL_SELECT + " WHERE " + sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TipoEdcDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

}
