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

public class NominaBancoDaoImpl extends AbstractDAO implements NominaBancoDao
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
	protected final String SQL_SELECT = "SELECT ID_BANCO, NOMBRE, NOMBRE_O_RAZON_SOCIAL, CLAVE FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( ID_BANCO, NOMBRE, NOMBRE_O_RAZON_SOCIAL, CLAVE ) VALUES ( ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET ID_BANCO = ?, NOMBRE = ?, NOMBRE_O_RAZON_SOCIAL = ?, CLAVE = ? WHERE ID_BANCO = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE ID_BANCO = ?";

	/** 
	 * Index of column ID_BANCO
	 */
	protected static final int COLUMN_ID_BANCO = 1;

	/** 
	 * Index of column NOMBRE
	 */
	protected static final int COLUMN_NOMBRE = 2;

	/** 
	 * Index of column NOMBRE_O_RAZON_SOCIAL
	 */
	protected static final int COLUMN_NOMBRE_O_RAZON_SOCIAL = 3;

	/** 
	 * Index of column CLAVE
	 */
	protected static final int COLUMN_CLAVE = 4;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 4;

	/** 
	 * Index of primary-key column ID_BANCO
	 */
	protected static final int PK_COLUMN_ID_BANCO = 1;

	/** 
	 * Inserts a new row in the nomina_banco table.
	 */
	public NominaBancoPk insert(NominaBanco dto) throws NominaBancoDaoException
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
		
			StringBuffer sql = new StringBuffer();
			StringBuffer values = new StringBuffer();
			sql.append( "INSERT INTO " + getTableName() + " (" );
			int modifiedCount = 0;
			if (dto.isIdBancoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_BANCO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombreModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombreORazonSocialModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRE_O_RAZON_SOCIAL" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isClaveModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "CLAVE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (modifiedCount==0) {
				// nothing to insert
				throw new IllegalStateException( "Nothing to insert" );
			}
		
			sql.append( ") VALUES (" );
			sql.append( values );
			sql.append( ")" );
			stmt = conn.prepareStatement( sql.toString(), Statement.RETURN_GENERATED_KEYS );
			int index = 1;
			if (dto.isIdBancoModified()) {
				stmt.setInt( index++, dto.getIdBanco() );
			}
		
			if (dto.isNombreModified()) {
				stmt.setString( index++, dto.getNombre() );
			}
		
			if (dto.isNombreORazonSocialModified()) {
				stmt.setString( index++, dto.getNombreORazonSocial() );
			}
		
			if (dto.isClaveModified()) {
				stmt.setString( index++, dto.getClave() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdBanco( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new NominaBancoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the nomina_banco table.
	 */
	public void update(NominaBancoPk pk, NominaBanco dto) throws NominaBancoDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			StringBuffer sql = new StringBuffer();
			sql.append( "UPDATE " + getTableName() + " SET " );
			boolean modified = false;
			if (dto.isIdBancoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_BANCO=?" );
				modified=true;
			}
		
			if (dto.isNombreModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRE=?" );
				modified=true;
			}
		
			if (dto.isNombreORazonSocialModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRE_O_RAZON_SOCIAL=?" );
				modified=true;
			}
		
			if (dto.isClaveModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "CLAVE=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE ID_BANCO=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdBancoModified()) {
				stmt.setInt( index++, dto.getIdBanco() );
			}
		
			if (dto.isNombreModified()) {
				stmt.setString( index++, dto.getNombre() );
			}
		
			if (dto.isNombreORazonSocialModified()) {
				stmt.setString( index++, dto.getNombreORazonSocial() );
			}
		
			if (dto.isClaveModified()) {
				stmt.setString( index++, dto.getClave() );
			}
		
			stmt.setInt( index++, pk.getIdBanco() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new NominaBancoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the nomina_banco table.
	 */
	public void delete(NominaBancoPk pk) throws NominaBancoDaoException
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
			stmt.setInt( 1, pk.getIdBanco() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new NominaBancoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the nomina_banco table that matches the specified primary-key value.
	 */
	public NominaBanco findByPrimaryKey(NominaBancoPk pk) throws NominaBancoDaoException
	{
		return findByPrimaryKey( pk.getIdBanco() );
	}

	/** 
	 * Returns all rows from the nomina_banco table that match the criteria 'ID_BANCO = :idBanco'.
	 */
	public NominaBanco findByPrimaryKey(int idBanco) throws NominaBancoDaoException
	{
		NominaBanco ret[] = findByDynamicSelect( SQL_SELECT + " WHERE ID_BANCO = ?", new Object[] {  new Integer(idBanco) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the nomina_banco table that match the criteria ''.
	 */
	public NominaBanco[] findAll() throws NominaBancoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY ID_BANCO", null );
	}

	/** 
	 * Returns all rows from the nomina_banco table that match the criteria 'ID_BANCO = :idBanco'.
	 */
	public NominaBanco[] findWhereIdBancoEquals(int idBanco) throws NominaBancoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_BANCO = ? ORDER BY ID_BANCO", new Object[] {  new Integer(idBanco) } );
	}

	/** 
	 * Returns all rows from the nomina_banco table that match the criteria 'NOMBRE = :nombre'.
	 */
	public NominaBanco[] findWhereNombreEquals(String nombre) throws NominaBancoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRE = ? ORDER BY NOMBRE", new Object[] { nombre } );
	}

	/** 
	 * Returns all rows from the nomina_banco table that match the criteria 'NOMBRE_O_RAZON_SOCIAL = :nombreORazonSocial'.
	 */
	public NominaBanco[] findWhereNombreORazonSocialEquals(String nombreORazonSocial) throws NominaBancoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRE_O_RAZON_SOCIAL = ? ORDER BY NOMBRE_O_RAZON_SOCIAL", new Object[] { nombreORazonSocial } );
	}

	/** 
	 * Returns all rows from the nomina_banco table that match the criteria 'CLAVE = :clave'.
	 */
	public NominaBanco[] findWhereClaveEquals(String clave) throws NominaBancoDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CLAVE = ? ORDER BY CLAVE", new Object[] { clave } );
	}

	/**
	 * Method 'NominaBancoDaoImpl'
	 * 
	 */
	public NominaBancoDaoImpl()
	{
	}

	/**
	 * Method 'NominaBancoDaoImpl'
	 * 
	 * @param userConn
	 */
	public NominaBancoDaoImpl(final java.sql.Connection userConn)
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
		return "nomina_banco";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected NominaBanco fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			NominaBanco dto = new NominaBanco();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected NominaBanco[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			NominaBanco dto = new NominaBanco();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		NominaBanco ret[] = new NominaBanco[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(NominaBanco dto, ResultSet rs) throws SQLException
	{
		dto.setIdBanco( rs.getInt( COLUMN_ID_BANCO ) );
		dto.setNombre( rs.getString( COLUMN_NOMBRE ) );
		dto.setNombreORazonSocial( rs.getString( COLUMN_NOMBRE_O_RAZON_SOCIAL ) );
		dto.setClave( rs.getString( COLUMN_CLAVE ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(NominaBanco dto)
	{
		dto.setIdBancoModified( false );
		dto.setNombreModified( false );
		dto.setNombreORazonSocialModified( false );
		dto.setClaveModified( false );
	}

	/** 
	 * Returns all rows from the nomina_banco table that match the specified arbitrary SQL statement
	 */
	public NominaBanco[] findByDynamicSelect(String sql, Object[] sqlParams) throws NominaBancoDaoException
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
			throw new NominaBancoDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the nomina_banco table that match the specified arbitrary SQL statement
	 */
	public NominaBanco[] findByDynamicWhere(String sql, Object[] sqlParams) throws NominaBancoDaoException
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
			throw new NominaBancoDaoException( "Exception: " + _e.getMessage(), _e );
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
