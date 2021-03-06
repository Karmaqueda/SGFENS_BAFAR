/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.tsp.sct.dao.dao;

import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.exceptions.*;

public interface GeocercaDao
{
	/** 
	 * Inserts a new row in the geocerca table.
	 */
	public GeocercaPk insert(Geocerca dto) throws GeocercaDaoException;

	/** 
	 * Updates a single row in the geocerca table.
	 */
	public void update(GeocercaPk pk, Geocerca dto) throws GeocercaDaoException;

	/** 
	 * Deletes a single row in the geocerca table.
	 */
	public void delete(GeocercaPk pk) throws GeocercaDaoException;

	/** 
	 * Returns the rows from the geocerca table that matches the specified primary-key value.
	 */
	public Geocerca findByPrimaryKey(GeocercaPk pk) throws GeocercaDaoException;

	/** 
	 * Returns all rows from the geocerca table that match the criteria 'ID_GEOCERCA = :idGeocerca'.
	 */
	public Geocerca findByPrimaryKey(int idGeocerca) throws GeocercaDaoException;

	/** 
	 * Returns all rows from the geocerca table that match the criteria ''.
	 */
	public Geocerca[] findAll() throws GeocercaDaoException;

	/** 
	 * Returns all rows from the geocerca table that match the criteria 'ID_GEOCERCA = :idGeocerca'.
	 */
	public Geocerca[] findWhereIdGeocercaEquals(int idGeocerca) throws GeocercaDaoException;

	/** 
	 * Returns all rows from the geocerca table that match the criteria 'ID_EMPRESA = :idEmpresa'.
	 */
	public Geocerca[] findWhereIdEmpresaEquals(int idEmpresa) throws GeocercaDaoException;

	/** 
	 * Returns all rows from the geocerca table that match the criteria 'TIPO_GEOCERCA = :tipoGeocerca'.
	 */
	public Geocerca[] findWhereTipoGeocercaEquals(int tipoGeocerca) throws GeocercaDaoException;

	/** 
	 * Returns all rows from the geocerca table that match the criteria 'COORDENADAS = :coordenadas'.
	 */
	public Geocerca[] findWhereCoordenadasEquals(String coordenadas) throws GeocercaDaoException;

	/** 
	 * Returns all rows from the geocerca table that match the criteria 'ID_ESTATUS = :idEstatus'.
	 */
	public Geocerca[] findWhereIdEstatusEquals(int idEstatus) throws GeocercaDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the geocerca table that match the specified arbitrary SQL statement
	 */
	public Geocerca[] findByDynamicSelect(String sql, Object[] sqlParams) throws GeocercaDaoException;

	/** 
	 * Returns all rows from the geocerca table that match the specified arbitrary SQL statement
	 */
	public Geocerca[] findByDynamicWhere(String sql, Object[] sqlParams) throws GeocercaDaoException;

}
