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

public interface ComprobanteDescripcionPersonalizadaDao
{
	/** 
	 * Inserts a new row in the comprobante_descripcion_personalizada table.
	 */
	public ComprobanteDescripcionPersonalizadaPk insert(ComprobanteDescripcionPersonalizada dto) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Updates a single row in the comprobante_descripcion_personalizada table.
	 */
	public void update(ComprobanteDescripcionPersonalizadaPk pk, ComprobanteDescripcionPersonalizada dto) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Deletes a single row in the comprobante_descripcion_personalizada table.
	 */
	public void delete(ComprobanteDescripcionPersonalizadaPk pk) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Returns the rows from the comprobante_descripcion_personalizada table that matches the specified primary-key value.
	 */
	public ComprobanteDescripcionPersonalizada findByPrimaryKey(ComprobanteDescripcionPersonalizadaPk pk) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Returns all rows from the comprobante_descripcion_personalizada table that match the criteria 'ID_COMPROBANTE_DESCRIPCION_PERSONALIZADA = :idComprobanteDescripcionPersonalizada'.
	 */
	public ComprobanteDescripcionPersonalizada findByPrimaryKey(int idComprobanteDescripcionPersonalizada) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Returns all rows from the comprobante_descripcion_personalizada table that match the criteria ''.
	 */
	public ComprobanteDescripcionPersonalizada[] findAll() throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Returns all rows from the comprobante_descripcion_personalizada table that match the criteria 'ID_COMPROBANTE_DESCRIPCION_PERSONALIZADA = :idComprobanteDescripcionPersonalizada'.
	 */
	public ComprobanteDescripcionPersonalizada[] findWhereIdComprobanteDescripcionPersonalizadaEquals(int idComprobanteDescripcionPersonalizada) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Returns all rows from the comprobante_descripcion_personalizada table that match the criteria 'ID_COMPROBANTE_FISCAL = :idComprobanteFiscal'.
	 */
	public ComprobanteDescripcionPersonalizada[] findWhereIdComprobanteFiscalEquals(int idComprobanteFiscal) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Returns all rows from the comprobante_descripcion_personalizada table that match the criteria 'DATOS_DE_PERSONALIZACION = :datosDePersonalizacion'.
	 */
	public ComprobanteDescripcionPersonalizada[] findWhereDatosDePersonalizacionEquals(String datosDePersonalizacion) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the comprobante_descripcion_personalizada table that match the specified arbitrary SQL statement
	 */
	public ComprobanteDescripcionPersonalizada[] findByDynamicSelect(String sql, Object[] sqlParams) throws ComprobanteDescripcionPersonalizadaDaoException;

	/** 
	 * Returns all rows from the comprobante_descripcion_personalizada table that match the specified arbitrary SQL statement
	 */
	public ComprobanteDescripcionPersonalizada[] findByDynamicWhere(String sql, Object[] sqlParams) throws ComprobanteDescripcionPersonalizadaDaoException;

}
