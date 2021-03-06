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

public class BancoOperacionDaoImpl extends AbstractDAO implements BancoOperacionDao
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
	protected final String SQL_SELECT = "SELECT ID_OPERACION_BANCARIA, ID_EMPRESA, NO_TARJETA, NOMBRE_TITULAR, MONTO, BANCO_AUTH, BANCO_ORDER_ID, BANCO_OPER_FECHA, BANCO_OPER_TYPE, BANCO_OPER_ISSUING_BANK, ID_ESTATUS, NOMBRE_ARCHIVO_IMG_IFE, NOMBRE_ARCHIVO_IMG_TDC, DATA_ARQC, DATA_AID, DATA_TSI, DATA_REF, DATA_EXTRA1, DATA_EXTRA2 FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( ID_OPERACION_BANCARIA, ID_EMPRESA, NO_TARJETA, NOMBRE_TITULAR, MONTO, BANCO_AUTH, BANCO_ORDER_ID, BANCO_OPER_FECHA, BANCO_OPER_TYPE, BANCO_OPER_ISSUING_BANK, ID_ESTATUS, NOMBRE_ARCHIVO_IMG_IFE, NOMBRE_ARCHIVO_IMG_TDC, DATA_ARQC, DATA_AID, DATA_TSI, DATA_REF, DATA_EXTRA1, DATA_EXTRA2 ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET ID_OPERACION_BANCARIA = ?, ID_EMPRESA = ?, NO_TARJETA = ?, NOMBRE_TITULAR = ?, MONTO = ?, BANCO_AUTH = ?, BANCO_ORDER_ID = ?, BANCO_OPER_FECHA = ?, BANCO_OPER_TYPE = ?, BANCO_OPER_ISSUING_BANK = ?, ID_ESTATUS = ?, NOMBRE_ARCHIVO_IMG_IFE = ?, NOMBRE_ARCHIVO_IMG_TDC = ?, DATA_ARQC = ?, DATA_AID = ?, DATA_TSI = ?, DATA_REF = ?, DATA_EXTRA1 = ?, DATA_EXTRA2 = ? WHERE ID_OPERACION_BANCARIA = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE ID_OPERACION_BANCARIA = ?";

	/** 
	 * Index of column ID_OPERACION_BANCARIA
	 */
	protected static final int COLUMN_ID_OPERACION_BANCARIA = 1;

	/** 
	 * Index of column ID_EMPRESA
	 */
	protected static final int COLUMN_ID_EMPRESA = 2;

	/** 
	 * Index of column NO_TARJETA
	 */
	protected static final int COLUMN_NO_TARJETA = 3;

	/** 
	 * Index of column NOMBRE_TITULAR
	 */
	protected static final int COLUMN_NOMBRE_TITULAR = 4;

	/** 
	 * Index of column MONTO
	 */
	protected static final int COLUMN_MONTO = 5;

	/** 
	 * Index of column BANCO_AUTH
	 */
	protected static final int COLUMN_BANCO_AUTH = 6;

	/** 
	 * Index of column BANCO_ORDER_ID
	 */
	protected static final int COLUMN_BANCO_ORDER_ID = 7;

	/** 
	 * Index of column BANCO_OPER_FECHA
	 */
	protected static final int COLUMN_BANCO_OPER_FECHA = 8;

	/** 
	 * Index of column BANCO_OPER_TYPE
	 */
	protected static final int COLUMN_BANCO_OPER_TYPE = 9;

	/** 
	 * Index of column BANCO_OPER_ISSUING_BANK
	 */
	protected static final int COLUMN_BANCO_OPER_ISSUING_BANK = 10;

	/** 
	 * Index of column ID_ESTATUS
	 */
	protected static final int COLUMN_ID_ESTATUS = 11;

	/** 
	 * Index of column NOMBRE_ARCHIVO_IMG_IFE
	 */
	protected static final int COLUMN_NOMBRE_ARCHIVO_IMG_IFE = 12;

	/** 
	 * Index of column NOMBRE_ARCHIVO_IMG_TDC
	 */
	protected static final int COLUMN_NOMBRE_ARCHIVO_IMG_TDC = 13;

	/** 
	 * Index of column DATA_ARQC
	 */
	protected static final int COLUMN_DATA_ARQC = 14;

	/** 
	 * Index of column DATA_AID
	 */
	protected static final int COLUMN_DATA_AID = 15;

	/** 
	 * Index of column DATA_TSI
	 */
	protected static final int COLUMN_DATA_TSI = 16;

	/** 
	 * Index of column DATA_REF
	 */
	protected static final int COLUMN_DATA_REF = 17;

	/** 
	 * Index of column DATA_EXTRA1
	 */
	protected static final int COLUMN_DATA_EXTRA1 = 18;

	/** 
	 * Index of column DATA_EXTRA2
	 */
	protected static final int COLUMN_DATA_EXTRA2 = 19;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 19;

	/** 
	 * Index of primary-key column ID_OPERACION_BANCARIA
	 */
	protected static final int PK_COLUMN_ID_OPERACION_BANCARIA = 1;

	/** 
	 * Inserts a new row in the banco_operacion table.
	 */
	public BancoOperacionPk insert(BancoOperacion dto) throws BancoOperacionDaoException
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
			if (dto.isIdOperacionBancariaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_OPERACION_BANCARIA" );
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
		
			if (dto.isNoTarjetaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NO_TARJETA" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombreTitularModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRE_TITULAR" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isMontoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "MONTO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isBancoAuthModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "BANCO_AUTH" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isBancoOrderIdModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "BANCO_ORDER_ID" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isBancoOperFechaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "BANCO_OPER_FECHA" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isBancoOperTypeModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "BANCO_OPER_TYPE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isBancoOperIssuingBankModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "BANCO_OPER_ISSUING_BANK" );
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
		
			if (dto.isNombreArchivoImgIfeModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRE_ARCHIVO_IMG_IFE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombreArchivoImgTdcModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRE_ARCHIVO_IMG_TDC" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDataArqcModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DATA_ARQC" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDataAidModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DATA_AID" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDataTsiModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DATA_TSI" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDataRefModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DATA_REF" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDataExtra1Modified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DATA_EXTRA1" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDataExtra2Modified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DATA_EXTRA2" );
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
			if (dto.isIdOperacionBancariaModified()) {
				stmt.setInt( index++, dto.getIdOperacionBancaria() );
			}
		
			if (dto.isIdEmpresaModified()) {
				if (dto.isIdEmpresaNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdEmpresa() );
				}
		
			}
		
			if (dto.isNoTarjetaModified()) {
				stmt.setString( index++, dto.getNoTarjeta() );
			}
		
			if (dto.isNombreTitularModified()) {
				stmt.setString( index++, dto.getNombreTitular() );
			}
		
			if (dto.isMontoModified()) {
				if (dto.isMontoNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getMonto() );
				}
		
			}
		
			if (dto.isBancoAuthModified()) {
				stmt.setString( index++, dto.getBancoAuth() );
			}
		
			if (dto.isBancoOrderIdModified()) {
				stmt.setString( index++, dto.getBancoOrderId() );
			}
		
			if (dto.isBancoOperFechaModified()) {
				stmt.setString( index++, dto.getBancoOperFecha() );
			}
		
			if (dto.isBancoOperTypeModified()) {
				stmt.setString( index++, dto.getBancoOperType() );
			}
		
			if (dto.isBancoOperIssuingBankModified()) {
				stmt.setString( index++, dto.getBancoOperIssuingBank() );
			}
		
			if (dto.isIdEstatusModified()) {
				if (dto.isIdEstatusNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdEstatus() );
				}
		
			}
		
			if (dto.isNombreArchivoImgIfeModified()) {
				stmt.setString( index++, dto.getNombreArchivoImgIfe() );
			}
		
			if (dto.isNombreArchivoImgTdcModified()) {
				stmt.setString( index++, dto.getNombreArchivoImgTdc() );
			}
		
			if (dto.isDataArqcModified()) {
				stmt.setString( index++, dto.getDataArqc() );
			}
		
			if (dto.isDataAidModified()) {
				stmt.setString( index++, dto.getDataAid() );
			}
		
			if (dto.isDataTsiModified()) {
				stmt.setString( index++, dto.getDataTsi() );
			}
		
			if (dto.isDataRefModified()) {
				stmt.setString( index++, dto.getDataRef() );
			}
		
			if (dto.isDataExtra1Modified()) {
				stmt.setString( index++, dto.getDataExtra1() );
			}
		
			if (dto.isDataExtra2Modified()) {
				stmt.setString( index++, dto.getDataExtra2() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdOperacionBancaria( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new BancoOperacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the banco_operacion table.
	 */
	public void update(BancoOperacionPk pk, BancoOperacion dto) throws BancoOperacionDaoException
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
			if (dto.isIdOperacionBancariaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_OPERACION_BANCARIA=?" );
				modified=true;
			}
		
			if (dto.isIdEmpresaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_EMPRESA=?" );
				modified=true;
			}
		
			if (dto.isNoTarjetaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NO_TARJETA=?" );
				modified=true;
			}
		
			if (dto.isNombreTitularModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRE_TITULAR=?" );
				modified=true;
			}
		
			if (dto.isMontoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "MONTO=?" );
				modified=true;
			}
		
			if (dto.isBancoAuthModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "BANCO_AUTH=?" );
				modified=true;
			}
		
			if (dto.isBancoOrderIdModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "BANCO_ORDER_ID=?" );
				modified=true;
			}
		
			if (dto.isBancoOperFechaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "BANCO_OPER_FECHA=?" );
				modified=true;
			}
		
			if (dto.isBancoOperTypeModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "BANCO_OPER_TYPE=?" );
				modified=true;
			}
		
			if (dto.isBancoOperIssuingBankModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "BANCO_OPER_ISSUING_BANK=?" );
				modified=true;
			}
		
			if (dto.isIdEstatusModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_ESTATUS=?" );
				modified=true;
			}
		
			if (dto.isNombreArchivoImgIfeModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRE_ARCHIVO_IMG_IFE=?" );
				modified=true;
			}
		
			if (dto.isNombreArchivoImgTdcModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRE_ARCHIVO_IMG_TDC=?" );
				modified=true;
			}
		
			if (dto.isDataArqcModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DATA_ARQC=?" );
				modified=true;
			}
		
			if (dto.isDataAidModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DATA_AID=?" );
				modified=true;
			}
		
			if (dto.isDataTsiModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DATA_TSI=?" );
				modified=true;
			}
		
			if (dto.isDataRefModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DATA_REF=?" );
				modified=true;
			}
		
			if (dto.isDataExtra1Modified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DATA_EXTRA1=?" );
				modified=true;
			}
		
			if (dto.isDataExtra2Modified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DATA_EXTRA2=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE ID_OPERACION_BANCARIA=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdOperacionBancariaModified()) {
				stmt.setInt( index++, dto.getIdOperacionBancaria() );
			}
		
			if (dto.isIdEmpresaModified()) {
				if (dto.isIdEmpresaNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdEmpresa() );
				}
		
			}
		
			if (dto.isNoTarjetaModified()) {
				stmt.setString( index++, dto.getNoTarjeta() );
			}
		
			if (dto.isNombreTitularModified()) {
				stmt.setString( index++, dto.getNombreTitular() );
			}
		
			if (dto.isMontoModified()) {
				if (dto.isMontoNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getMonto() );
				}
		
			}
		
			if (dto.isBancoAuthModified()) {
				stmt.setString( index++, dto.getBancoAuth() );
			}
		
			if (dto.isBancoOrderIdModified()) {
				stmt.setString( index++, dto.getBancoOrderId() );
			}
		
			if (dto.isBancoOperFechaModified()) {
				stmt.setString( index++, dto.getBancoOperFecha() );
			}
		
			if (dto.isBancoOperTypeModified()) {
				stmt.setString( index++, dto.getBancoOperType() );
			}
		
			if (dto.isBancoOperIssuingBankModified()) {
				stmt.setString( index++, dto.getBancoOperIssuingBank() );
			}
		
			if (dto.isIdEstatusModified()) {
				if (dto.isIdEstatusNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdEstatus() );
				}
		
			}
		
			if (dto.isNombreArchivoImgIfeModified()) {
				stmt.setString( index++, dto.getNombreArchivoImgIfe() );
			}
		
			if (dto.isNombreArchivoImgTdcModified()) {
				stmt.setString( index++, dto.getNombreArchivoImgTdc() );
			}
		
			if (dto.isDataArqcModified()) {
				stmt.setString( index++, dto.getDataArqc() );
			}
		
			if (dto.isDataAidModified()) {
				stmt.setString( index++, dto.getDataAid() );
			}
		
			if (dto.isDataTsiModified()) {
				stmt.setString( index++, dto.getDataTsi() );
			}
		
			if (dto.isDataRefModified()) {
				stmt.setString( index++, dto.getDataRef() );
			}
		
			if (dto.isDataExtra1Modified()) {
				stmt.setString( index++, dto.getDataExtra1() );
			}
		
			if (dto.isDataExtra2Modified()) {
				stmt.setString( index++, dto.getDataExtra2() );
			}
		
			stmt.setInt( index++, pk.getIdOperacionBancaria() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new BancoOperacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the banco_operacion table.
	 */
	public void delete(BancoOperacionPk pk) throws BancoOperacionDaoException
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
			stmt.setInt( 1, pk.getIdOperacionBancaria() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new BancoOperacionDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the banco_operacion table that matches the specified primary-key value.
	 */
	public BancoOperacion findByPrimaryKey(BancoOperacionPk pk) throws BancoOperacionDaoException
	{
		return findByPrimaryKey( pk.getIdOperacionBancaria() );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'ID_OPERACION_BANCARIA = :idOperacionBancaria'.
	 */
	public BancoOperacion findByPrimaryKey(int idOperacionBancaria) throws BancoOperacionDaoException
	{
		BancoOperacion ret[] = findByDynamicSelect( SQL_SELECT + " WHERE ID_OPERACION_BANCARIA = ?", new Object[] {  new Integer(idOperacionBancaria) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria ''.
	 */
	public BancoOperacion[] findAll() throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY ID_OPERACION_BANCARIA", null );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'ID_OPERACION_BANCARIA = :idOperacionBancaria'.
	 */
	public BancoOperacion[] findWhereIdOperacionBancariaEquals(int idOperacionBancaria) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_OPERACION_BANCARIA = ? ORDER BY ID_OPERACION_BANCARIA", new Object[] {  new Integer(idOperacionBancaria) } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'ID_EMPRESA = :idEmpresa'.
	 */
	public BancoOperacion[] findWhereIdEmpresaEquals(int idEmpresa) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_EMPRESA = ? ORDER BY ID_EMPRESA", new Object[] {  new Integer(idEmpresa) } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'NO_TARJETA = :noTarjeta'.
	 */
	public BancoOperacion[] findWhereNoTarjetaEquals(String noTarjeta) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NO_TARJETA = ? ORDER BY NO_TARJETA", new Object[] { noTarjeta } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'NOMBRE_TITULAR = :nombreTitular'.
	 */
	public BancoOperacion[] findWhereNombreTitularEquals(String nombreTitular) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRE_TITULAR = ? ORDER BY NOMBRE_TITULAR", new Object[] { nombreTitular } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'MONTO = :monto'.
	 */
	public BancoOperacion[] findWhereMontoEquals(double monto) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE MONTO = ? ORDER BY MONTO", new Object[] {  new Double(monto) } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'BANCO_AUTH = :bancoAuth'.
	 */
	public BancoOperacion[] findWhereBancoAuthEquals(String bancoAuth) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE BANCO_AUTH = ? ORDER BY BANCO_AUTH", new Object[] { bancoAuth } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'BANCO_ORDER_ID = :bancoOrderId'.
	 */
	public BancoOperacion[] findWhereBancoOrderIdEquals(String bancoOrderId) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE BANCO_ORDER_ID = ? ORDER BY BANCO_ORDER_ID", new Object[] { bancoOrderId } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'BANCO_OPER_FECHA = :bancoOperFecha'.
	 */
	public BancoOperacion[] findWhereBancoOperFechaEquals(String bancoOperFecha) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE BANCO_OPER_FECHA = ? ORDER BY BANCO_OPER_FECHA", new Object[] { bancoOperFecha } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'BANCO_OPER_TYPE = :bancoOperType'.
	 */
	public BancoOperacion[] findWhereBancoOperTypeEquals(String bancoOperType) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE BANCO_OPER_TYPE = ? ORDER BY BANCO_OPER_TYPE", new Object[] { bancoOperType } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'BANCO_OPER_ISSUING_BANK = :bancoOperIssuingBank'.
	 */
	public BancoOperacion[] findWhereBancoOperIssuingBankEquals(String bancoOperIssuingBank) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE BANCO_OPER_ISSUING_BANK = ? ORDER BY BANCO_OPER_ISSUING_BANK", new Object[] { bancoOperIssuingBank } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'ID_ESTATUS = :idEstatus'.
	 */
	public BancoOperacion[] findWhereIdEstatusEquals(int idEstatus) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_ESTATUS = ? ORDER BY ID_ESTATUS", new Object[] {  new Integer(idEstatus) } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'NOMBRE_ARCHIVO_IMG_IFE = :nombreArchivoImgIfe'.
	 */
	public BancoOperacion[] findWhereNombreArchivoImgIfeEquals(String nombreArchivoImgIfe) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRE_ARCHIVO_IMG_IFE = ? ORDER BY NOMBRE_ARCHIVO_IMG_IFE", new Object[] { nombreArchivoImgIfe } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'NOMBRE_ARCHIVO_IMG_TDC = :nombreArchivoImgTdc'.
	 */
	public BancoOperacion[] findWhereNombreArchivoImgTdcEquals(String nombreArchivoImgTdc) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRE_ARCHIVO_IMG_TDC = ? ORDER BY NOMBRE_ARCHIVO_IMG_TDC", new Object[] { nombreArchivoImgTdc } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'DATA_ARQC = :dataArqc'.
	 */
	public BancoOperacion[] findWhereDataArqcEquals(String dataArqc) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DATA_ARQC = ? ORDER BY DATA_ARQC", new Object[] { dataArqc } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'DATA_AID = :dataAid'.
	 */
	public BancoOperacion[] findWhereDataAidEquals(String dataAid) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DATA_AID = ? ORDER BY DATA_AID", new Object[] { dataAid } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'DATA_TSI = :dataTsi'.
	 */
	public BancoOperacion[] findWhereDataTsiEquals(String dataTsi) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DATA_TSI = ? ORDER BY DATA_TSI", new Object[] { dataTsi } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'DATA_REF = :dataRef'.
	 */
	public BancoOperacion[] findWhereDataRefEquals(String dataRef) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DATA_REF = ? ORDER BY DATA_REF", new Object[] { dataRef } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'DATA_EXTRA1 = :dataExtra1'.
	 */
	public BancoOperacion[] findWhereDataExtra1Equals(String dataExtra1) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DATA_EXTRA1 = ? ORDER BY DATA_EXTRA1", new Object[] { dataExtra1 } );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the criteria 'DATA_EXTRA2 = :dataExtra2'.
	 */
	public BancoOperacion[] findWhereDataExtra2Equals(String dataExtra2) throws BancoOperacionDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DATA_EXTRA2 = ? ORDER BY DATA_EXTRA2", new Object[] { dataExtra2 } );
	}

	/**
	 * Method 'BancoOperacionDaoImpl'
	 * 
	 */
	public BancoOperacionDaoImpl()
	{
	}

	/**
	 * Method 'BancoOperacionDaoImpl'
	 * 
	 * @param userConn
	 */
	public BancoOperacionDaoImpl(final java.sql.Connection userConn)
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
		return "banco_operacion";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected BancoOperacion fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			BancoOperacion dto = new BancoOperacion();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected BancoOperacion[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			BancoOperacion dto = new BancoOperacion();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		BancoOperacion ret[] = new BancoOperacion[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(BancoOperacion dto, ResultSet rs) throws SQLException
	{
		dto.setIdOperacionBancaria( rs.getInt( COLUMN_ID_OPERACION_BANCARIA ) );
		dto.setIdEmpresa( rs.getInt( COLUMN_ID_EMPRESA ) );
		if (rs.wasNull()) {
			dto.setIdEmpresaNull( true );
		}
		
		dto.setNoTarjeta( rs.getString( COLUMN_NO_TARJETA ) );
		dto.setNombreTitular( rs.getString( COLUMN_NOMBRE_TITULAR ) );
		dto.setMonto( rs.getDouble( COLUMN_MONTO ) );
		if (rs.wasNull()) {
			dto.setMontoNull( true );
		}
		
		dto.setBancoAuth( rs.getString( COLUMN_BANCO_AUTH ) );
		dto.setBancoOrderId( rs.getString( COLUMN_BANCO_ORDER_ID ) );
		dto.setBancoOperFecha( rs.getString( COLUMN_BANCO_OPER_FECHA ) );
		dto.setBancoOperType( rs.getString( COLUMN_BANCO_OPER_TYPE ) );
		dto.setBancoOperIssuingBank( rs.getString( COLUMN_BANCO_OPER_ISSUING_BANK ) );
		dto.setIdEstatus( rs.getInt( COLUMN_ID_ESTATUS ) );
		if (rs.wasNull()) {
			dto.setIdEstatusNull( true );
		}
		
		dto.setNombreArchivoImgIfe( rs.getString( COLUMN_NOMBRE_ARCHIVO_IMG_IFE ) );
		dto.setNombreArchivoImgTdc( rs.getString( COLUMN_NOMBRE_ARCHIVO_IMG_TDC ) );
		dto.setDataArqc( rs.getString( COLUMN_DATA_ARQC ) );
		dto.setDataAid( rs.getString( COLUMN_DATA_AID ) );
		dto.setDataTsi( rs.getString( COLUMN_DATA_TSI ) );
		dto.setDataRef( rs.getString( COLUMN_DATA_REF ) );
		dto.setDataExtra1( rs.getString( COLUMN_DATA_EXTRA1 ) );
		dto.setDataExtra2( rs.getString( COLUMN_DATA_EXTRA2 ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(BancoOperacion dto)
	{
		dto.setIdOperacionBancariaModified( false );
		dto.setIdEmpresaModified( false );
		dto.setNoTarjetaModified( false );
		dto.setNombreTitularModified( false );
		dto.setMontoModified( false );
		dto.setBancoAuthModified( false );
		dto.setBancoOrderIdModified( false );
		dto.setBancoOperFechaModified( false );
		dto.setBancoOperTypeModified( false );
		dto.setBancoOperIssuingBankModified( false );
		dto.setIdEstatusModified( false );
		dto.setNombreArchivoImgIfeModified( false );
		dto.setNombreArchivoImgTdcModified( false );
		dto.setDataArqcModified( false );
		dto.setDataAidModified( false );
		dto.setDataTsiModified( false );
		dto.setDataRefModified( false );
		dto.setDataExtra1Modified( false );
		dto.setDataExtra2Modified( false );
	}

	/** 
	 * Returns all rows from the banco_operacion table that match the specified arbitrary SQL statement
	 */
	public BancoOperacion[] findByDynamicSelect(String sql, Object[] sqlParams) throws BancoOperacionDaoException
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
			throw new BancoOperacionDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the banco_operacion table that match the specified arbitrary SQL statement
	 */
	public BancoOperacion[] findByDynamicWhere(String sql, Object[] sqlParams) throws BancoOperacionDaoException
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
			throw new BancoOperacionDaoException( "Exception: " + _e.getMessage(), _e );
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
