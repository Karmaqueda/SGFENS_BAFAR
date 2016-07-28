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

public interface InventarioHistoricoVendedorDao
{
	/** 
	 * Inserts a new row in the inventario_historico_vendedor table.
	 */
	public void insert(InventarioHistoricoVendedor dto) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the criteria ''.
	 */
	public InventarioHistoricoVendedor[] findAll() throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the criteria 'ID_EMPLEADO = :idEmpleado'.
	 */
	public InventarioHistoricoVendedor[] findWhereIdEmpleadoEquals(int idEmpleado) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the criteria 'ID_CONCEPTO = :idConcepto'.
	 */
	public InventarioHistoricoVendedor[] findWhereIdConceptoEquals(int idConcepto) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the criteria 'CANTIDAD_ASIGNADA = :cantidadAsignada'.
	 */
	public InventarioHistoricoVendedor[] findWhereCantidadAsignadaEquals(double cantidadAsignada) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the criteria 'CANTIDAD_TERMINNO = :cantidadTerminno'.
	 */
	public InventarioHistoricoVendedor[] findWhereCantidadTerminnoEquals(double cantidadTerminno) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the criteria 'FECHA_REGISTRO = :fechaRegistro'.
	 */
	public InventarioHistoricoVendedor[] findWhereFechaRegistroEquals(Date fechaRegistro) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the criteria 'FECHA_INICIAL_CORTE = :fechaInicialCorte'.
	 */
	public InventarioHistoricoVendedor[] findWhereFechaInicialCorteEquals(Date fechaInicialCorte) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the criteria 'FECHA_FINAL_CORTE = :fechaFinalCorte'.
	 */
	public InventarioHistoricoVendedor[] findWhereFechaFinalCorteEquals(Date fechaFinalCorte) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the specified arbitrary SQL statement
	 */
	public InventarioHistoricoVendedor[] findByDynamicSelect(String sql, Object[] sqlParams) throws InventarioHistoricoVendedorDaoException;

	/** 
	 * Returns all rows from the inventario_historico_vendedor table that match the specified arbitrary SQL statement
	 */
	public InventarioHistoricoVendedor[] findByDynamicWhere(String sql, Object[] sqlParams) throws InventarioHistoricoVendedorDaoException;

}