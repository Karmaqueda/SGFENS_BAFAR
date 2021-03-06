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

public class BitacoraCreditosOperacionDaoImpl extends AbstractDAO implements BitacoraCreditosOperacionDao
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
	protected final String SQL_SELECT = "SELECT ID_BITACORA_CREDITOS_OPERACION, ID_EMPRESA, ID_ESTATUS, TIPO, ID_USER_REGISTRA, CANTIDAD, FECHA_HORA, ID_CLIENTE, ID_PROSPECTO, MONTO_OPERACION, COMENTARIOS, FOLIO_MOVIL, ID_OPERACION_BANCARIA FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( ID_BITACORA_CREDITOS_OPERACION, ID_EMPRESA, ID_ESTATUS, TIPO, ID_USER_REGISTRA, CANTIDAD, FECHA_HORA, ID_CLIENTE, ID_PROSPECTO, MONTO_OPERACION, COMENTARIOS, FOLIO_MOVIL, ID_OPERACION_BANCARIA ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET ID_BITACORA_CREDITOS_OPERACION = ?, ID_EMPRESA = ?, ID_ESTATUS = ?, TIPO = ?, ID_USER_REGISTRA = ?, CANTIDAD = ?, FECHA_HORA = ?, ID_CLIENTE = ?, ID_PROSPECTO = ?, MONTO_OPERACION = ?, COMENTARIOS = ?, FOLIO_MOVIL = ?, ID_OPERACION_BANCARIA = ? WHERE ID_BITACORA_CREDITOS_OPERACION = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE ID_BITACORA_CREDITOS_OPERACION = ?";

	/** 
	 * Index of column ID_BITACORA_CREDITOS_OPERACION
	 */
	protected static final int COLUMN_ID_BITACORA_CREDITOS_OPERACION = 1;

	/** 
	 * Index of column ID_EMPRESA
	 */
	protected static final int COLUMN_ID_EMPRESA = 2;

	/** 
	 * Index of column ID_ESTATUS
	 */
	protected static final int COLUMN_ID_ESTATUS = 3;

	/** 
	 * Index of column TIPO
	 */
	protected static final int COLUMN_TIPO = 4;

	/** 
	 * Index of column ID_USER_REGISTRA
	 */
	protected static final int COLUMN_ID_USER_REGISTRA = 5;

	/** 
	 * Index of column CANTIDAD
	 */
	protected static final int COLUMN_CANTIDAD = 6;

	/** 
	 * Index of column FECHA_HORA
	 */
	protected static final int COLUMN_FECHA_HORA = 7;

	/** 
	 * Index of column ID_CLIENTE
	 */
	protected static final int COLUMN_ID_CLIENTE = 8;

	/** 
	 * Index of column ID_PROSPECTO
	 */
	protected static final int COLUMN_ID_PROSPECTO = 9;

	/** 
	 * Index of column MONTO_OPERACION
	 */
	protected static final int COLUMN_MONTO_OPERACION = 10;

	/** 
	 * Index of column COMENTARIOS
	 */
	protected static final int COLUMN_COMENTARIOS = 11;

	/** 
	 * Index of column FOLIO_MOVIL
	 */
	protected static final int COLUMN_FOLIO_MOVIL = 12;

	/** 
	 * Index of column ID_OPERACION_BANCARIA
	 */
	protected static final int COLUMN_ID_OPERACION_BANCARIA = 13;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 13;

	/** 
	 * Index of primary-key column ID_BITACORA_CREDITOS_OPERACION
	 */
	protected static final int PK_COLUMN_ID_BITACORA_CREDITOS_OPERACION = 1;

	/** 
	 * Inserts a new row in the bitacora_creditos_operacion table.
	 */
	public BitacoraCreditosOperacionPk insert(BitacoraCreditosOperacion dto) throws BitacoraCreditosOperacionDaoException
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
			if (dto.isIdBitacoraCreditosOperacionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_BITACORA_CREDITOS_OPERACION" );
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
		
			if (dto.isTipoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TIPO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdUserRegistraModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_USER_REGISTRA" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isCantidadModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "CANTIDAD" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechaHoraModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "FECHA_HORA" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdClienteModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_CLIENTE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdProspectoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_PROSPECTO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isMontoOperacionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "MONTO_OPERACION" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isComentariosModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "COMENTARIOS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFolioMovilModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "FOLIO_MOVIL" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdOperacionBancariaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_OPERACION_BANCARIA" );
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
			if (dto.isIdBitacoraCreditosOperacionModified()) {
				stmt.setInt( index++, dto.getIdBitacoraCreditosOperacion() );
			}
		
			if (dto.isIdEmpresaModified()) {
				stmt.setInt( index++, dto.getIdEmpresa() );
			}
		
			if (dto.isIdEstatusModified()) {
				stmt.setInt( index++, dto.getIdEstatus() );
			}
		
			if (dto.isTipoModified()) {
				stmt.setInt( index++, dto.getTipo() );
			}
		
			if (dto.isIdUserRegistraModified()) {
				stmt.setInt( index++, dto.getIdUserRegistra() );
			}
		
			if (dto.isCantidadModified()) {
				stmt.setInt( index++, dto.getCantidad() );
			}
		
			if (dto.isFechaHoraModified()) {
				stmt.setTimestamp(index++, dto.getFechaHora()==null ? null : new java.sql.Timestamp( dto.getFechaHora().getTime() ) );
			}
		
			if (dto.isIdClienteModified()) {
				if (dto.isIdClienteNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdCliente() );
				}
		
			}
		
			if (dto.isIdProspectoModified()) {
				if (dto.isIdProspectoNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdProspecto() );
				}
		
			}
		
			if (dto.isMontoOperacionModified()) {
				if (dto.isMontoOperacionNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getMontoOperacion() );
				}
		
			}
		
			if (dto.isComentariosModified()) {
				stmt.setString( index++, dto.getComentarios() );
			}
		
			if (dto.isFolioMovilModified()) {
				stmt.setString( index++, dto.getFolioMovil() );
			}
		
			if (dto.isIdOperacionBancariaModified()) {
				if (dto.isIdOperacionBancariaNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdOperacionBancaria() );
				}
		
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdBitacoraCreditosOperacion( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new BitacoraCreditosOperacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the bitacora_creditos_operacion table.
	 */
	public void update(BitacoraCreditosOperacionPk pk, BitacoraCreditosOperacion dto) throws BitacoraCreditosOperacionDaoException
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
			if (dto.isIdBitacoraCreditosOperacionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_BITACORA_CREDITOS_OPERACION=?" );
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
		
			if (dto.isTipoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TIPO=?" );
				modified=true;
			}
		
			if (dto.isIdUserRegistraModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_USER_REGISTRA=?" );
				modified=true;
			}
		
			if (dto.isCantidadModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "CANTIDAD=?" );
				modified=true;
			}
		
			if (dto.isFechaHoraModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "FECHA_HORA=?" );
				modified=true;
			}
		
			if (dto.isIdClienteModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_CLIENTE=?" );
				modified=true;
			}
		
			if (dto.isIdProspectoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_PROSPECTO=?" );
				modified=true;
			}
		
			if (dto.isMontoOperacionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "MONTO_OPERACION=?" );
				modified=true;
			}
		
			if (dto.isComentariosModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "COMENTARIOS=?" );
				modified=true;
			}
		
			if (dto.isFolioMovilModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "FOLIO_MOVIL=?" );
				modified=true;
			}
		
			if (dto.isIdOperacionBancariaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_OPERACION_BANCARIA=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE ID_BITACORA_CREDITOS_OPERACION=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdBitacoraCreditosOperacionModified()) {
				stmt.setInt( index++, dto.getIdBitacoraCreditosOperacion() );
			}
		
			if (dto.isIdEmpresaModified()) {
				stmt.setInt( index++, dto.getIdEmpresa() );
			}
		
			if (dto.isIdEstatusModified()) {
				stmt.setInt( index++, dto.getIdEstatus() );
			}
		
			if (dto.isTipoModified()) {
				stmt.setInt( index++, dto.getTipo() );
			}
		
			if (dto.isIdUserRegistraModified()) {
				stmt.setInt( index++, dto.getIdUserRegistra() );
			}
		
			if (dto.isCantidadModified()) {
				stmt.setInt( index++, dto.getCantidad() );
			}
		
			if (dto.isFechaHoraModified()) {
				stmt.setTimestamp(index++, dto.getFechaHora()==null ? null : new java.sql.Timestamp( dto.getFechaHora().getTime() ) );
			}
		
			if (dto.isIdClienteModified()) {
				if (dto.isIdClienteNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdCliente() );
				}
		
			}
		
			if (dto.isIdProspectoModified()) {
				if (dto.isIdProspectoNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdProspecto() );
				}
		
			}
		
			if (dto.isMontoOperacionModified()) {
				if (dto.isMontoOperacionNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getMontoOperacion() );
				}
		
			}
		
			if (dto.isComentariosModified()) {
				stmt.setString( index++, dto.getComentarios() );
			}
		
			if (dto.isFolioMovilModified()) {
				stmt.setString( index++, dto.getFolioMovil() );
			}
		
			if (dto.isIdOperacionBancariaModified()) {
				if (dto.isIdOperacionBancariaNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdOperacionBancaria() );
				}
		
			}
		
			stmt.setInt( index++, pk.getIdBitacoraCreditosOperacion() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new BitacoraCreditosOperacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the bitacora_creditos_operacion table.
	 */
	public void delete(BitacoraCreditosOperacionPk pk) throws BitacoraCreditosOperacionDaoException
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
			stmt.setInt( 1, pk.getIdBitacoraCreditosOperacion() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new BitacoraCreditosOperacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the bitacora_creditos_operacion table that matches the specified primary-key value.
	 */
	public BitacoraCreditosOperacion findByPrimaryKey(BitacoraCreditosOperacionPk pk) throws BitacoraCreditosOperacionDaoException
	{
		return findByPrimaryKey( pk.getIdBitacoraCreditosOperacion() );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'ID_BITACORA_CREDITOS_OPERACION = :idBitacoraCreditosOperacion'.
	 */
	public BitacoraCreditosOperacion findByPrimaryKey(int idBitacoraCreditosOperacion) throws BitacoraCreditosOperacionDaoException
	{
		BitacoraCreditosOperacion ret[] = findByDynamicSelect( SQL_SELECT + " WHERE ID_BITACORA_CREDITOS_OPERACION = ?", new Object[] {  new Integer(idBitacoraCreditosOperacion) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria ''.
	 */
	public BitacoraCreditosOperacion[] findAll() throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY ID_BITACORA_CREDITOS_OPERACION", null );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'ID_BITACORA_CREDITOS_OPERACION = :idBitacoraCreditosOperacion'.
	 */
	public BitacoraCreditosOperacion[] findWhereIdBitacoraCreditosOperacionEquals(int idBitacoraCreditosOperacion) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_BITACORA_CREDITOS_OPERACION = ? ORDER BY ID_BITACORA_CREDITOS_OPERACION", new Object[] {  new Integer(idBitacoraCreditosOperacion) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'ID_EMPRESA = :idEmpresa'.
	 */
	public BitacoraCreditosOperacion[] findWhereIdEmpresaEquals(int idEmpresa) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_EMPRESA = ? ORDER BY ID_EMPRESA", new Object[] {  new Integer(idEmpresa) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'ID_ESTATUS = :idEstatus'.
	 */
	public BitacoraCreditosOperacion[] findWhereIdEstatusEquals(int idEstatus) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_ESTATUS = ? ORDER BY ID_ESTATUS", new Object[] {  new Integer(idEstatus) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'TIPO = :tipo'.
	 */
	public BitacoraCreditosOperacion[] findWhereTipoEquals(int tipo) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TIPO = ? ORDER BY TIPO", new Object[] {  new Integer(tipo) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'ID_USER_REGISTRA = :idUserRegistra'.
	 */
	public BitacoraCreditosOperacion[] findWhereIdUserRegistraEquals(int idUserRegistra) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_USER_REGISTRA = ? ORDER BY ID_USER_REGISTRA", new Object[] {  new Integer(idUserRegistra) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'CANTIDAD = :cantidad'.
	 */
	public BitacoraCreditosOperacion[] findWhereCantidadEquals(int cantidad) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CANTIDAD = ? ORDER BY CANTIDAD", new Object[] {  new Integer(cantidad) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'FECHA_HORA = :fechaHora'.
	 */
	public BitacoraCreditosOperacion[] findWhereFechaHoraEquals(Date fechaHora) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE FECHA_HORA = ? ORDER BY FECHA_HORA", new Object[] { fechaHora==null ? null : new java.sql.Timestamp( fechaHora.getTime() ) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'ID_CLIENTE = :idCliente'.
	 */
	public BitacoraCreditosOperacion[] findWhereIdClienteEquals(int idCliente) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_CLIENTE = ? ORDER BY ID_CLIENTE", new Object[] {  new Integer(idCliente) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'ID_PROSPECTO = :idProspecto'.
	 */
	public BitacoraCreditosOperacion[] findWhereIdProspectoEquals(int idProspecto) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_PROSPECTO = ? ORDER BY ID_PROSPECTO", new Object[] {  new Integer(idProspecto) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'MONTO_OPERACION = :montoOperacion'.
	 */
	public BitacoraCreditosOperacion[] findWhereMontoOperacionEquals(double montoOperacion) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE MONTO_OPERACION = ? ORDER BY MONTO_OPERACION", new Object[] {  new Double(montoOperacion) } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'COMENTARIOS = :comentarios'.
	 */
	public BitacoraCreditosOperacion[] findWhereComentariosEquals(String comentarios) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE COMENTARIOS = ? ORDER BY COMENTARIOS", new Object[] { comentarios } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'FOLIO_MOVIL = :folioMovil'.
	 */
	public BitacoraCreditosOperacion[] findWhereFolioMovilEquals(String folioMovil) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE FOLIO_MOVIL = ? ORDER BY FOLIO_MOVIL", new Object[] { folioMovil } );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the criteria 'ID_OPERACION_BANCARIA = :idOperacionBancaria'.
	 */
	public BitacoraCreditosOperacion[] findWhereIdOperacionBancariaEquals(int idOperacionBancaria) throws BitacoraCreditosOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_OPERACION_BANCARIA = ? ORDER BY ID_OPERACION_BANCARIA", new Object[] {  new Integer(idOperacionBancaria) } );
	}

	/**
	 * Method 'BitacoraCreditosOperacionDaoImpl'
	 * 
	 */
	public BitacoraCreditosOperacionDaoImpl()
	{
	}

	/**
	 * Method 'BitacoraCreditosOperacionDaoImpl'
	 * 
	 * @param userConn
	 */
	public BitacoraCreditosOperacionDaoImpl(final java.sql.Connection userConn)
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
		return "bitacora_creditos_operacion";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected BitacoraCreditosOperacion fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			BitacoraCreditosOperacion dto = new BitacoraCreditosOperacion();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected BitacoraCreditosOperacion[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			BitacoraCreditosOperacion dto = new BitacoraCreditosOperacion();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		BitacoraCreditosOperacion ret[] = new BitacoraCreditosOperacion[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(BitacoraCreditosOperacion dto, ResultSet rs) throws SQLException
	{
		dto.setIdBitacoraCreditosOperacion( rs.getInt( COLUMN_ID_BITACORA_CREDITOS_OPERACION ) );
		dto.setIdEmpresa( rs.getInt( COLUMN_ID_EMPRESA ) );
		dto.setIdEstatus( rs.getInt( COLUMN_ID_ESTATUS ) );
		dto.setTipo( rs.getInt( COLUMN_TIPO ) );
		dto.setIdUserRegistra( rs.getInt( COLUMN_ID_USER_REGISTRA ) );
		dto.setCantidad( rs.getInt( COLUMN_CANTIDAD ) );
		dto.setFechaHora( rs.getTimestamp(COLUMN_FECHA_HORA ) );
		dto.setIdCliente( rs.getInt( COLUMN_ID_CLIENTE ) );
		if (rs.wasNull()) {
			dto.setIdClienteNull( true );
		}
		
		dto.setIdProspecto( rs.getInt( COLUMN_ID_PROSPECTO ) );
		if (rs.wasNull()) {
			dto.setIdProspectoNull( true );
		}
		
		dto.setMontoOperacion( rs.getDouble( COLUMN_MONTO_OPERACION ) );
		if (rs.wasNull()) {
			dto.setMontoOperacionNull( true );
		}
		
		dto.setComentarios( rs.getString( COLUMN_COMENTARIOS ) );
		dto.setFolioMovil( rs.getString( COLUMN_FOLIO_MOVIL ) );
		dto.setIdOperacionBancaria( rs.getInt( COLUMN_ID_OPERACION_BANCARIA ) );
		if (rs.wasNull()) {
			dto.setIdOperacionBancariaNull( true );
		}
		
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(BitacoraCreditosOperacion dto)
	{
		dto.setIdBitacoraCreditosOperacionModified( false );
		dto.setIdEmpresaModified( false );
		dto.setIdEstatusModified( false );
		dto.setTipoModified( false );
		dto.setIdUserRegistraModified( false );
		dto.setCantidadModified( false );
		dto.setFechaHoraModified( false );
		dto.setIdClienteModified( false );
		dto.setIdProspectoModified( false );
		dto.setMontoOperacionModified( false );
		dto.setComentariosModified( false );
		dto.setFolioMovilModified( false );
		dto.setIdOperacionBancariaModified( false );
	}

	/** 
	 * Returns all rows from the bitacora_creditos_operacion table that match the specified arbitrary SQL statement
	 */
	public BitacoraCreditosOperacion[] findByDynamicSelect(String sql, Object[] sqlParams) throws BitacoraCreditosOperacionDaoException
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
			throw new BitacoraCreditosOperacionDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the bitacora_creditos_operacion table that match the specified arbitrary SQL statement
	 */
	public BitacoraCreditosOperacion[] findByDynamicWhere(String sql, Object[] sqlParams) throws BitacoraCreditosOperacionDaoException
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
			throw new BitacoraCreditosOperacionDaoException( "Exception: " + _e.getMessage(), _e );
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
