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

public class ClienteCategoriaDaoImpl extends AbstractDAO implements ClienteCategoriaDao
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
	protected final String SQL_SELECT = "SELECT ID_CATEGORIA, ID_EMPRESA, ID_ESTATUS, NOMBRE_CLASIFICACION, DESCRIPCION FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( ID_CATEGORIA, ID_EMPRESA, ID_ESTATUS, NOMBRE_CLASIFICACION, DESCRIPCION ) VALUES ( ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET ID_CATEGORIA = ?, ID_EMPRESA = ?, ID_ESTATUS = ?, NOMBRE_CLASIFICACION = ?, DESCRIPCION = ? WHERE ID_CATEGORIA = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE ID_CATEGORIA = ?";

	/** 
	 * Index of column ID_CATEGORIA
	 */
	protected static final int COLUMN_ID_CATEGORIA = 1;

	/** 
	 * Index of column ID_EMPRESA
	 */
	protected static final int COLUMN_ID_EMPRESA = 2;

	/** 
	 * Index of column ID_ESTATUS
	 */
	protected static final int COLUMN_ID_ESTATUS = 3;

	/** 
	 * Index of column NOMBRE_CLASIFICACION
	 */
	protected static final int COLUMN_NOMBRE_CLASIFICACION = 4;

	/** 
	 * Index of column DESCRIPCION
	 */
	protected static final int COLUMN_DESCRIPCION = 5;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 5;

	/** 
	 * Index of primary-key column ID_CATEGORIA
	 */
	protected static final int PK_COLUMN_ID_CATEGORIA = 1;

	/** 
	 * Inserts a new row in the cliente_categoria table.
	 */
	public ClienteCategoriaPk insert(ClienteCategoria dto) throws ClienteCategoriaDaoException
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
			if (dto.isIdCategoriaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_CATEGORIA" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdEmpresaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_EMPRESA" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdEstatusModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_ESTATUS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombreClasificacionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRE_CLASIFICACION" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDescripcionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DESCRIPCION" );
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
			if (dto.isIdCategoriaModified()) {
				stmt.setInt( index++, dto.getIdCategoria() );
			}
		
			if (dto.isIdEmpresaModified()) {
				stmt.setInt( index++, dto.getIdEmpresa() );
			}
		
			if (dto.isIdEstatusModified()) {
				if (dto.isIdEstatusNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdEstatus() );
				}
		
			}
		
			if (dto.isNombreClasificacionModified()) {
				stmt.setString( index++, dto.getNombreClasificacion() );
			}
		
			if (dto.isDescripcionModified()) {
				stmt.setString( index++, dto.getDescripcion() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdCategoria( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new ClienteCategoriaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the cliente_categoria table.
	 */
	public void update(ClienteCategoriaPk pk, ClienteCategoria dto) throws ClienteCategoriaDaoException
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
			if (dto.isIdCategoriaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_CATEGORIA=?" );
				modified=true;
			}
		
			if (dto.isIdEmpresaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_EMPRESA=?" );
				modified=true;
			}
		
			if (dto.isIdEstatusModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_ESTATUS=?" );
				modified=true;
			}
		
			if (dto.isNombreClasificacionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRE_CLASIFICACION=?" );
				modified=true;
			}
		
			if (dto.isDescripcionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DESCRIPCION=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE ID_CATEGORIA=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdCategoriaModified()) {
				stmt.setInt( index++, dto.getIdCategoria() );
			}
		
			if (dto.isIdEmpresaModified()) {
				stmt.setInt( index++, dto.getIdEmpresa() );
			}
		
			if (dto.isIdEstatusModified()) {
				if (dto.isIdEstatusNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdEstatus() );
				}
		
			}
		
			if (dto.isNombreClasificacionModified()) {
				stmt.setString( index++, dto.getNombreClasificacion() );
			}
		
			if (dto.isDescripcionModified()) {
				stmt.setString( index++, dto.getDescripcion() );
			}
		
			stmt.setInt( index++, pk.getIdCategoria() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new ClienteCategoriaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the cliente_categoria table.
	 */
	public void delete(ClienteCategoriaPk pk) throws ClienteCategoriaDaoException
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
			stmt.setInt( 1, pk.getIdCategoria() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new ClienteCategoriaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the cliente_categoria table that matches the specified primary-key value.
	 */
	public ClienteCategoria findByPrimaryKey(ClienteCategoriaPk pk) throws ClienteCategoriaDaoException
	{
		return findByPrimaryKey( pk.getIdCategoria() );
	}

	/** 
	 * Returns all rows from the cliente_categoria table that match the criteria 'ID_CATEGORIA = :idCategoria'.
	 */
	public ClienteCategoria findByPrimaryKey(int idCategoria) throws ClienteCategoriaDaoException
	{
		ClienteCategoria ret[] = findByDynamicSelect( SQL_SELECT + " WHERE ID_CATEGORIA = ?", new Object[] {  new Integer(idCategoria) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the cliente_categoria table that match the criteria ''.
	 */
	public ClienteCategoria[] findAll() throws ClienteCategoriaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY ID_CATEGORIA", null );
	}

	/** 
	 * Returns all rows from the cliente_categoria table that match the criteria 'ID_CATEGORIA = :idCategoria'.
	 */
	public ClienteCategoria[] findWhereIdCategoriaEquals(int idCategoria) throws ClienteCategoriaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_CATEGORIA = ? ORDER BY ID_CATEGORIA", new Object[] {  new Integer(idCategoria) } );
	}

	/** 
	 * Returns all rows from the cliente_categoria table that match the criteria 'ID_EMPRESA = :idEmpresa'.
	 */
	public ClienteCategoria[] findWhereIdEmpresaEquals(int idEmpresa) throws ClienteCategoriaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_EMPRESA = ? ORDER BY ID_EMPRESA", new Object[] {  new Integer(idEmpresa) } );
	}

	/** 
	 * Returns all rows from the cliente_categoria table that match the criteria 'ID_ESTATUS = :idEstatus'.
	 */
	public ClienteCategoria[] findWhereIdEstatusEquals(int idEstatus) throws ClienteCategoriaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_ESTATUS = ? ORDER BY ID_ESTATUS", new Object[] {  new Integer(idEstatus) } );
	}

	/** 
	 * Returns all rows from the cliente_categoria table that match the criteria 'NOMBRE_CLASIFICACION = :nombreClasificacion'.
	 */
	public ClienteCategoria[] findWhereNombreClasificacionEquals(String nombreClasificacion) throws ClienteCategoriaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRE_CLASIFICACION = ? ORDER BY NOMBRE_CLASIFICACION", new Object[] { nombreClasificacion } );
	}

	/** 
	 * Returns all rows from the cliente_categoria table that match the criteria 'DESCRIPCION = :descripcion'.
	 */
	public ClienteCategoria[] findWhereDescripcionEquals(String descripcion) throws ClienteCategoriaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DESCRIPCION = ? ORDER BY DESCRIPCION", new Object[] { descripcion } );
	}

	/**
	 * Method 'ClienteCategoriaDaoImpl'
	 * 
	 */
	public ClienteCategoriaDaoImpl()
	{
	}

	/**
	 * Method 'ClienteCategoriaDaoImpl'
	 * 
	 * @param userConn
	 */
	public ClienteCategoriaDaoImpl(final java.sql.Connection userConn)
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
		return "cliente_categoria";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected ClienteCategoria fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			ClienteCategoria dto = new ClienteCategoria();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected ClienteCategoria[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			ClienteCategoria dto = new ClienteCategoria();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		ClienteCategoria ret[] = new ClienteCategoria[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(ClienteCategoria dto, ResultSet rs) throws SQLException
	{
		dto.setIdCategoria( rs.getInt( COLUMN_ID_CATEGORIA ) );
		dto.setIdEmpresa( rs.getInt( COLUMN_ID_EMPRESA ) );
		dto.setIdEstatus( rs.getInt( COLUMN_ID_ESTATUS ) );
		if (rs.wasNull()) {
			dto.setIdEstatusNull( true );
		}
		
		dto.setNombreClasificacion( rs.getString( COLUMN_NOMBRE_CLASIFICACION ) );
		dto.setDescripcion( rs.getString( COLUMN_DESCRIPCION ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(ClienteCategoria dto)
	{
		dto.setIdCategoriaModified( false );
		dto.setIdEmpresaModified( false );
		dto.setIdEstatusModified( false );
		dto.setNombreClasificacionModified( false );
		dto.setDescripcionModified( false );
	}

	/** 
	 * Returns all rows from the cliente_categoria table that match the specified arbitrary SQL statement
	 */
	public ClienteCategoria[] findByDynamicSelect(String sql, Object[] sqlParams) throws ClienteCategoriaDaoException
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
			throw new ClienteCategoriaDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the cliente_categoria table that match the specified arbitrary SQL statement
	 */
	public ClienteCategoria[] findByDynamicWhere(String sql, Object[] sqlParams) throws ClienteCategoriaDaoException
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
			throw new ClienteCategoriaDaoException( "Exception: " + _e.getMessage(), _e );
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
