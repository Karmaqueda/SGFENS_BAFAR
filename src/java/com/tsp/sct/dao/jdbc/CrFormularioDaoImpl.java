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

public class CrFormularioDaoImpl extends AbstractDAO implements CrFormularioDao
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
	protected final String SQL_SELECT = "SELECT id_formulario, id_grupo_formulario, orden_grupo, nombre, descripcion, fecha_hr_creacion, fecha_hr_ultima_edicion, id_usuario_edicion, id_estatus, id_empresa FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( id_formulario, id_grupo_formulario, orden_grupo, nombre, descripcion, fecha_hr_creacion, fecha_hr_ultima_edicion, id_usuario_edicion, id_estatus, id_empresa ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET id_formulario = ?, id_grupo_formulario = ?, orden_grupo = ?, nombre = ?, descripcion = ?, fecha_hr_creacion = ?, fecha_hr_ultima_edicion = ?, id_usuario_edicion = ?, id_estatus = ?, id_empresa = ? WHERE id_formulario = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE id_formulario = ?";

	/** 
	 * Index of column id_formulario
	 */
	protected static final int COLUMN_ID_FORMULARIO = 1;

	/** 
	 * Index of column id_grupo_formulario
	 */
	protected static final int COLUMN_ID_GRUPO_FORMULARIO = 2;

	/** 
	 * Index of column orden_grupo
	 */
	protected static final int COLUMN_ORDEN_GRUPO = 3;

	/** 
	 * Index of column nombre
	 */
	protected static final int COLUMN_NOMBRE = 4;

	/** 
	 * Index of column descripcion
	 */
	protected static final int COLUMN_DESCRIPCION = 5;

	/** 
	 * Index of column fecha_hr_creacion
	 */
	protected static final int COLUMN_FECHA_HR_CREACION = 6;

	/** 
	 * Index of column fecha_hr_ultima_edicion
	 */
	protected static final int COLUMN_FECHA_HR_ULTIMA_EDICION = 7;

	/** 
	 * Index of column id_usuario_edicion
	 */
	protected static final int COLUMN_ID_USUARIO_EDICION = 8;

	/** 
	 * Index of column id_estatus
	 */
	protected static final int COLUMN_ID_ESTATUS = 9;

	/** 
	 * Index of column id_empresa
	 */
	protected static final int COLUMN_ID_EMPRESA = 10;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 10;

	/** 
	 * Index of primary-key column id_formulario
	 */
	protected static final int PK_COLUMN_ID_FORMULARIO = 1;

	/** 
	 * Inserts a new row in the cr_formulario table.
	 */
	public CrFormularioPk insert(CrFormulario dto) throws CrFormularioDaoException
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
			if (dto.isIdFormularioModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "id_formulario" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdGrupoFormularioModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "id_grupo_formulario" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isOrdenGrupoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "orden_grupo" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombreModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "nombre" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDescripcionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "descripcion" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechaHrCreacionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "fecha_hr_creacion" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechaHrUltimaEdicionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "fecha_hr_ultima_edicion" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdUsuarioEdicionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "id_usuario_edicion" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdEstatusModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "id_estatus" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdEmpresaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "id_empresa" );
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
			if (dto.isIdFormularioModified()) {
				stmt.setInt( index++, dto.getIdFormulario() );
			}
		
			if (dto.isIdGrupoFormularioModified()) {
				stmt.setInt( index++, dto.getIdGrupoFormulario() );
			}
		
			if (dto.isOrdenGrupoModified()) {
				if (dto.isOrdenGrupoNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getOrdenGrupo() );
				}
		
			}
		
			if (dto.isNombreModified()) {
				stmt.setString( index++, dto.getNombre() );
			}
		
			if (dto.isDescripcionModified()) {
				stmt.setString( index++, dto.getDescripcion() );
			}
		
			if (dto.isFechaHrCreacionModified()) {
				stmt.setTimestamp(index++, dto.getFechaHrCreacion()==null ? null : new java.sql.Timestamp( dto.getFechaHrCreacion().getTime() ) );
			}
		
			if (dto.isFechaHrUltimaEdicionModified()) {
				stmt.setTimestamp(index++, dto.getFechaHrUltimaEdicion()==null ? null : new java.sql.Timestamp( dto.getFechaHrUltimaEdicion().getTime() ) );
			}
		
			if (dto.isIdUsuarioEdicionModified()) {
				if (dto.isIdUsuarioEdicionNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdUsuarioEdicion() );
				}
		
			}
		
			if (dto.isIdEstatusModified()) {
				stmt.setInt( index++, dto.getIdEstatus() );
			}
		
			if (dto.isIdEmpresaModified()) {
				stmt.setInt( index++, dto.getIdEmpresa() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdFormulario( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new CrFormularioDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the cr_formulario table.
	 */
	public void update(CrFormularioPk pk, CrFormulario dto) throws CrFormularioDaoException
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
			if (dto.isIdFormularioModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "id_formulario=?" );
				modified=true;
			}
		
			if (dto.isIdGrupoFormularioModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "id_grupo_formulario=?" );
				modified=true;
			}
		
			if (dto.isOrdenGrupoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "orden_grupo=?" );
				modified=true;
			}
		
			if (dto.isNombreModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "nombre=?" );
				modified=true;
			}
		
			if (dto.isDescripcionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "descripcion=?" );
				modified=true;
			}
		
			if (dto.isFechaHrCreacionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "fecha_hr_creacion=?" );
				modified=true;
			}
		
			if (dto.isFechaHrUltimaEdicionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "fecha_hr_ultima_edicion=?" );
				modified=true;
			}
		
			if (dto.isIdUsuarioEdicionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "id_usuario_edicion=?" );
				modified=true;
			}
		
			if (dto.isIdEstatusModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "id_estatus=?" );
				modified=true;
			}
		
			if (dto.isIdEmpresaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "id_empresa=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE id_formulario=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdFormularioModified()) {
				stmt.setInt( index++, dto.getIdFormulario() );
			}
		
			if (dto.isIdGrupoFormularioModified()) {
				stmt.setInt( index++, dto.getIdGrupoFormulario() );
			}
		
			if (dto.isOrdenGrupoModified()) {
				if (dto.isOrdenGrupoNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getOrdenGrupo() );
				}
		
			}
		
			if (dto.isNombreModified()) {
				stmt.setString( index++, dto.getNombre() );
			}
		
			if (dto.isDescripcionModified()) {
				stmt.setString( index++, dto.getDescripcion() );
			}
		
			if (dto.isFechaHrCreacionModified()) {
				stmt.setTimestamp(index++, dto.getFechaHrCreacion()==null ? null : new java.sql.Timestamp( dto.getFechaHrCreacion().getTime() ) );
			}
		
			if (dto.isFechaHrUltimaEdicionModified()) {
				stmt.setTimestamp(index++, dto.getFechaHrUltimaEdicion()==null ? null : new java.sql.Timestamp( dto.getFechaHrUltimaEdicion().getTime() ) );
			}
		
			if (dto.isIdUsuarioEdicionModified()) {
				if (dto.isIdUsuarioEdicionNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdUsuarioEdicion() );
				}
		
			}
		
			if (dto.isIdEstatusModified()) {
				stmt.setInt( index++, dto.getIdEstatus() );
			}
		
			if (dto.isIdEmpresaModified()) {
				stmt.setInt( index++, dto.getIdEmpresa() );
			}
		
			stmt.setInt( index++, pk.getIdFormulario() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new CrFormularioDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the cr_formulario table.
	 */
	public void delete(CrFormularioPk pk) throws CrFormularioDaoException
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
			stmt.setInt( 1, pk.getIdFormulario() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new CrFormularioDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the cr_formulario table that matches the specified primary-key value.
	 */
	public CrFormulario findByPrimaryKey(CrFormularioPk pk) throws CrFormularioDaoException
	{
		return findByPrimaryKey( pk.getIdFormulario() );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_formulario = :idFormulario'.
	 */
	public CrFormulario findByPrimaryKey(int idFormulario) throws CrFormularioDaoException
	{
		CrFormulario ret[] = findByDynamicSelect( SQL_SELECT + " WHERE id_formulario = ?", new Object[] {  new Integer(idFormulario) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria ''.
	 */
	public CrFormulario[] findAll() throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY id_formulario", null );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_formulario = :idFormulario'.
	 */
	public CrFormulario[] findWhereIdFormularioEquals(int idFormulario) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE id_formulario = ? ORDER BY id_formulario", new Object[] {  new Integer(idFormulario) } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_grupo_formulario = :idGrupoFormulario'.
	 */
	public CrFormulario[] findWhereIdGrupoFormularioEquals(int idGrupoFormulario) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE id_grupo_formulario = ? ORDER BY id_grupo_formulario", new Object[] {  new Integer(idGrupoFormulario) } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'orden_grupo = :ordenGrupo'.
	 */
	public CrFormulario[] findWhereOrdenGrupoEquals(int ordenGrupo) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE orden_grupo = ? ORDER BY orden_grupo", new Object[] {  new Integer(ordenGrupo) } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'nombre = :nombre'.
	 */
	public CrFormulario[] findWhereNombreEquals(String nombre) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE nombre = ? ORDER BY nombre", new Object[] { nombre } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'descripcion = :descripcion'.
	 */
	public CrFormulario[] findWhereDescripcionEquals(String descripcion) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE descripcion = ? ORDER BY descripcion", new Object[] { descripcion } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'fecha_hr_creacion = :fechaHrCreacion'.
	 */
	public CrFormulario[] findWhereFechaHrCreacionEquals(Date fechaHrCreacion) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE fecha_hr_creacion = ? ORDER BY fecha_hr_creacion", new Object[] { fechaHrCreacion==null ? null : new java.sql.Timestamp( fechaHrCreacion.getTime() ) } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'fecha_hr_ultima_edicion = :fechaHrUltimaEdicion'.
	 */
	public CrFormulario[] findWhereFechaHrUltimaEdicionEquals(Date fechaHrUltimaEdicion) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE fecha_hr_ultima_edicion = ? ORDER BY fecha_hr_ultima_edicion", new Object[] { fechaHrUltimaEdicion==null ? null : new java.sql.Timestamp( fechaHrUltimaEdicion.getTime() ) } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_usuario_edicion = :idUsuarioEdicion'.
	 */
	public CrFormulario[] findWhereIdUsuarioEdicionEquals(int idUsuarioEdicion) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE id_usuario_edicion = ? ORDER BY id_usuario_edicion", new Object[] {  new Integer(idUsuarioEdicion) } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_estatus = :idEstatus'.
	 */
	public CrFormulario[] findWhereIdEstatusEquals(int idEstatus) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE id_estatus = ? ORDER BY id_estatus", new Object[] {  new Integer(idEstatus) } );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_empresa = :idEmpresa'.
	 */
	public CrFormulario[] findWhereIdEmpresaEquals(int idEmpresa) throws CrFormularioDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE id_empresa = ? ORDER BY id_empresa", new Object[] {  new Integer(idEmpresa) } );
	}

	/**
	 * Method 'CrFormularioDaoImpl'
	 * 
	 */
	public CrFormularioDaoImpl()
	{
	}

	/**
	 * Method 'CrFormularioDaoImpl'
	 * 
	 * @param userConn
	 */
	public CrFormularioDaoImpl(final java.sql.Connection userConn)
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
		return "cr_formulario";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected CrFormulario fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			CrFormulario dto = new CrFormulario();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected CrFormulario[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			CrFormulario dto = new CrFormulario();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		CrFormulario ret[] = new CrFormulario[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(CrFormulario dto, ResultSet rs) throws SQLException
	{
		dto.setIdFormulario( rs.getInt( COLUMN_ID_FORMULARIO ) );
		dto.setIdGrupoFormulario( rs.getInt( COLUMN_ID_GRUPO_FORMULARIO ) );
		dto.setOrdenGrupo( rs.getInt( COLUMN_ORDEN_GRUPO ) );
		if (rs.wasNull()) {
			dto.setOrdenGrupoNull( true );
		}
		
		dto.setNombre( rs.getString( COLUMN_NOMBRE ) );
		dto.setDescripcion( rs.getString( COLUMN_DESCRIPCION ) );
		dto.setFechaHrCreacion( rs.getTimestamp(COLUMN_FECHA_HR_CREACION ) );
		dto.setFechaHrUltimaEdicion( rs.getTimestamp(COLUMN_FECHA_HR_ULTIMA_EDICION ) );
		dto.setIdUsuarioEdicion( rs.getInt( COLUMN_ID_USUARIO_EDICION ) );
		if (rs.wasNull()) {
			dto.setIdUsuarioEdicionNull( true );
		}
		
		dto.setIdEstatus( rs.getInt( COLUMN_ID_ESTATUS ) );
		dto.setIdEmpresa( rs.getInt( COLUMN_ID_EMPRESA ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(CrFormulario dto)
	{
		dto.setIdFormularioModified( false );
		dto.setIdGrupoFormularioModified( false );
		dto.setOrdenGrupoModified( false );
		dto.setNombreModified( false );
		dto.setDescripcionModified( false );
		dto.setFechaHrCreacionModified( false );
		dto.setFechaHrUltimaEdicionModified( false );
		dto.setIdUsuarioEdicionModified( false );
		dto.setIdEstatusModified( false );
		dto.setIdEmpresaModified( false );
	}

	/** 
	 * Returns all rows from the cr_formulario table that match the specified arbitrary SQL statement
	 */
	public CrFormulario[] findByDynamicSelect(String sql, Object[] sqlParams) throws CrFormularioDaoException
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
			throw new CrFormularioDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the cr_formulario table that match the specified arbitrary SQL statement
	 */
	public CrFormulario[] findByDynamicWhere(String sql, Object[] sqlParams) throws CrFormularioDaoException
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
			throw new CrFormularioDaoException( "Exception: " + _e.getMessage(), _e );
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
