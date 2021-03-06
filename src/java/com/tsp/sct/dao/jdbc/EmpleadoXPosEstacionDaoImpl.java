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
import java.util.Date;
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

public class EmpleadoXPosEstacionDaoImpl extends AbstractDAO implements EmpleadoXPosEstacionDao
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
	protected final String SQL_SELECT = "SELECT id_empleado, id_pos_estacion, fecha_hr_alta FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( id_empleado, id_pos_estacion, fecha_hr_alta ) VALUES ( ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET id_empleado = ?, id_pos_estacion = ?, fecha_hr_alta = ? WHERE id_empleado = ? AND id_pos_estacion = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE id_empleado = ? AND id_pos_estacion = ?";

	/** 
	 * Index of column id_empleado
	 */
	protected static final int COLUMN_ID_EMPLEADO = 1;

	/** 
	 * Index of column id_pos_estacion
	 */
	protected static final int COLUMN_ID_POS_ESTACION = 2;

	/** 
	 * Index of column fecha_hr_alta
	 */
	protected static final int COLUMN_FECHA_HR_ALTA = 3;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 3;

	/** 
	 * Index of primary-key column id_empleado
	 */
	protected static final int PK_COLUMN_ID_EMPLEADO = 1;

	/** 
	 * Index of primary-key column id_pos_estacion
	 */
	protected static final int PK_COLUMN_ID_POS_ESTACION = 2;

	/** 
	 * Inserts a new row in the empleado_x_pos_estacion table.
	 */
	public EmpleadoXPosEstacionPk insert(EmpleadoXPosEstacion dto) throws EmpleadoXPosEstacionDaoException
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
			if (dto.isIdEmpleadoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "id_empleado" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdPosEstacionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "id_pos_estacion" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechaHrAltaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "fecha_hr_alta" );
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
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdEmpleadoModified()) {
				stmt.setInt( index++, dto.getIdEmpleado() );
			}
		
			if (dto.isIdPosEstacionModified()) {
				stmt.setInt( index++, dto.getIdPosEstacion() );
			}
		
			if (dto.isFechaHrAltaModified()) {
				stmt.setTimestamp(index++, dto.getFechaHrAlta()==null ? null : new java.sql.Timestamp( dto.getFechaHrAlta().getTime() ) );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EmpleadoXPosEstacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the empleado_x_pos_estacion table.
	 */
	public void update(EmpleadoXPosEstacionPk pk, EmpleadoXPosEstacion dto) throws EmpleadoXPosEstacionDaoException
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
			if (dto.isIdEmpleadoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "id_empleado=?" );
				modified=true;
			}
		
			if (dto.isIdPosEstacionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "id_pos_estacion=?" );
				modified=true;
			}
		
			if (dto.isFechaHrAltaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "fecha_hr_alta=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE id_empleado=? AND id_pos_estacion=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdEmpleadoModified()) {
				stmt.setInt( index++, dto.getIdEmpleado() );
			}
		
			if (dto.isIdPosEstacionModified()) {
				stmt.setInt( index++, dto.getIdPosEstacion() );
			}
		
			if (dto.isFechaHrAltaModified()) {
				stmt.setTimestamp(index++, dto.getFechaHrAlta()==null ? null : new java.sql.Timestamp( dto.getFechaHrAlta().getTime() ) );
			}
		
			stmt.setInt( index++, pk.getIdEmpleado() );
			stmt.setInt( index++, pk.getIdPosEstacion() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EmpleadoXPosEstacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the empleado_x_pos_estacion table.
	 */
	public void delete(EmpleadoXPosEstacionPk pk) throws EmpleadoXPosEstacionDaoException
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
			stmt.setInt( 1, pk.getIdEmpleado() );
			stmt.setInt( 2, pk.getIdPosEstacion() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EmpleadoXPosEstacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the empleado_x_pos_estacion table that matches the specified primary-key value.
	 */
	public EmpleadoXPosEstacion findByPrimaryKey(EmpleadoXPosEstacionPk pk) throws EmpleadoXPosEstacionDaoException
	{
		return findByPrimaryKey( pk.getIdEmpleado(), pk.getIdPosEstacion() );
	}

	/** 
	 * Returns all rows from the empleado_x_pos_estacion table that match the criteria 'id_empleado = :idEmpleado AND id_pos_estacion = :idPosEstacion'.
	 */
	public EmpleadoXPosEstacion findByPrimaryKey(int idEmpleado, int idPosEstacion) throws EmpleadoXPosEstacionDaoException
	{
		EmpleadoXPosEstacion ret[] = findByDynamicSelect( SQL_SELECT + " WHERE id_empleado = ? AND id_pos_estacion = ?", new Object[] {  new Integer(idEmpleado),  new Integer(idPosEstacion) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the empleado_x_pos_estacion table that match the criteria ''.
	 */
	public EmpleadoXPosEstacion[] findAll() throws EmpleadoXPosEstacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY id_empleado, id_pos_estacion", null );
	}

	/** 
	 * Returns all rows from the empleado_x_pos_estacion table that match the criteria 'id_empleado = :idEmpleado'.
	 */
	public EmpleadoXPosEstacion[] findWhereIdEmpleadoEquals(int idEmpleado) throws EmpleadoXPosEstacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE id_empleado = ? ORDER BY id_empleado", new Object[] {  new Integer(idEmpleado) } );
	}

	/** 
	 * Returns all rows from the empleado_x_pos_estacion table that match the criteria 'id_pos_estacion = :idPosEstacion'.
	 */
	public EmpleadoXPosEstacion[] findWhereIdPosEstacionEquals(int idPosEstacion) throws EmpleadoXPosEstacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE id_pos_estacion = ? ORDER BY id_pos_estacion", new Object[] {  new Integer(idPosEstacion) } );
	}

	/** 
	 * Returns all rows from the empleado_x_pos_estacion table that match the criteria 'fecha_hr_alta = :fechaHrAlta'.
	 */
	public EmpleadoXPosEstacion[] findWhereFechaHrAltaEquals(Date fechaHrAlta) throws EmpleadoXPosEstacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE fecha_hr_alta = ? ORDER BY fecha_hr_alta", new Object[] { fechaHrAlta==null ? null : new java.sql.Timestamp( fechaHrAlta.getTime() ) } );
	}

	/**
	 * Method 'EmpleadoXPosEstacionDaoImpl'
	 * 
	 */
	public EmpleadoXPosEstacionDaoImpl()
	{
	}

	/**
	 * Method 'EmpleadoXPosEstacionDaoImpl'
	 * 
	 * @param userConn
	 */
	public EmpleadoXPosEstacionDaoImpl(final java.sql.Connection userConn)
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
		return "empleado_x_pos_estacion";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected EmpleadoXPosEstacion fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			EmpleadoXPosEstacion dto = new EmpleadoXPosEstacion();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected EmpleadoXPosEstacion[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			EmpleadoXPosEstacion dto = new EmpleadoXPosEstacion();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		EmpleadoXPosEstacion ret[] = new EmpleadoXPosEstacion[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(EmpleadoXPosEstacion dto, ResultSet rs) throws SQLException
	{
		dto.setIdEmpleado( rs.getInt( COLUMN_ID_EMPLEADO ) );
		dto.setIdPosEstacion( rs.getInt( COLUMN_ID_POS_ESTACION ) );
		dto.setFechaHrAlta( rs.getTimestamp(COLUMN_FECHA_HR_ALTA ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(EmpleadoXPosEstacion dto)
	{
		dto.setIdEmpleadoModified( false );
		dto.setIdPosEstacionModified( false );
		dto.setFechaHrAltaModified( false );
	}

	/** 
	 * Returns all rows from the empleado_x_pos_estacion table that match the specified arbitrary SQL statement
	 */
	public EmpleadoXPosEstacion[] findByDynamicSelect(String sql, Object[] sqlParams) throws EmpleadoXPosEstacionDaoException
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
			throw new EmpleadoXPosEstacionDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the empleado_x_pos_estacion table that match the specified arbitrary SQL statement
	 */
	public EmpleadoXPosEstacion[] findByDynamicWhere(String sql, Object[] sqlParams) throws EmpleadoXPosEstacionDaoException
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
			throw new EmpleadoXPosEstacionDaoException( "Exception: " + _e.getMessage(), _e );
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
