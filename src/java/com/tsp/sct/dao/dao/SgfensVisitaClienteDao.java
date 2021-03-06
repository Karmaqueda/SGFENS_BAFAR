/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.tsp.sct.dao.dao;

import java.util.Date;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.exceptions.*;

public interface SgfensVisitaClienteDao
{
	/** 
	 * Inserts a new row in the sgfens_visita_cliente table.
	 */
	public SgfensVisitaClientePk insert(SgfensVisitaCliente dto) throws SgfensVisitaClienteDaoException;

	/** 
	 * Updates a single row in the sgfens_visita_cliente table.
	 */
	public void update(SgfensVisitaClientePk pk, SgfensVisitaCliente dto) throws SgfensVisitaClienteDaoException;

	/** 
	 * Deletes a single row in the sgfens_visita_cliente table.
	 */
	public void delete(SgfensVisitaClientePk pk) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns the rows from the sgfens_visita_cliente table that matches the specified primary-key value.
	 */
	public SgfensVisitaCliente findByPrimaryKey(SgfensVisitaClientePk pk) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'ID_VISITA = :idVisita'.
	 */
	public SgfensVisitaCliente findByPrimaryKey(int idVisita) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria ''.
	 */
	public SgfensVisitaCliente[] findAll() throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'ID_VISITA = :idVisita'.
	 */
	public SgfensVisitaCliente[] findWhereIdVisitaEquals(int idVisita) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'ID_EMPRESA = :idEmpresa'.
	 */
	public SgfensVisitaCliente[] findWhereIdEmpresaEquals(int idEmpresa) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'ID_EMPLEADO_VENDEDOR = :idEmpleadoVendedor'.
	 */
	public SgfensVisitaCliente[] findWhereIdEmpleadoVendedorEquals(int idEmpleadoVendedor) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'ID_CLIENTE = :idCliente'.
	 */
	public SgfensVisitaCliente[] findWhereIdClienteEquals(int idCliente) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'FECHA_HORA = :fechaHora'.
	 */
	public SgfensVisitaCliente[] findWhereFechaHoraEquals(Date fechaHora) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'LATITUD = :latitud'.
	 */
	public SgfensVisitaCliente[] findWhereLatitudEquals(double latitud) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'LONGITUD = :longitud'.
	 */
	public SgfensVisitaCliente[] findWhereLongitudEquals(double longitud) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'ID_OPCION = :idOpcion'.
	 */
	public SgfensVisitaCliente[] findWhereIdOpcionEquals(int idOpcion) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the criteria 'COMENTARIOS = :comentarios'.
	 */
	public SgfensVisitaCliente[] findWhereComentariosEquals(String comentarios) throws SgfensVisitaClienteDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the specified arbitrary SQL statement
	 */
	public SgfensVisitaCliente[] findByDynamicSelect(String sql, Object[] sqlParams) throws SgfensVisitaClienteDaoException;

	/** 
	 * Returns all rows from the sgfens_visita_cliente table that match the specified arbitrary SQL statement
	 */
	public SgfensVisitaCliente[] findByDynamicWhere(String sql, Object[] sqlParams) throws SgfensVisitaClienteDaoException;

}
