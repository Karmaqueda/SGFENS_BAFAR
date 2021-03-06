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

public interface ValidacionXmlDao
{
	/** 
	 * Inserts a new row in the validacion_xml table.
	 */
	public ValidacionXmlPk insert(ValidacionXml dto) throws ValidacionXmlDaoException;

	/** 
	 * Updates a single row in the validacion_xml table.
	 */
	public void update(ValidacionXmlPk pk, ValidacionXml dto) throws ValidacionXmlDaoException;

	/** 
	 * Deletes a single row in the validacion_xml table.
	 */
	public void delete(ValidacionXmlPk pk) throws ValidacionXmlDaoException;

	/** 
	 * Returns the rows from the validacion_xml table that matches the specified primary-key value.
	 */
	public ValidacionXml findByPrimaryKey(ValidacionXmlPk pk) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'ID_VALIDACION = :idValidacion'.
	 */
	public ValidacionXml findByPrimaryKey(int idValidacion) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria ''.
	 */
	public ValidacionXml[] findAll() throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'ID_VALIDACION = :idValidacion'.
	 */
	public ValidacionXml[] findWhereIdValidacionEquals(int idValidacion) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'ID_EMPRESA = :idEmpresa'.
	 */
	public ValidacionXml[] findWhereIdEmpresaEquals(int idEmpresa) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'NOMBRE_ARCHIVO = :nombreArchivo'.
	 */
	public ValidacionXml[] findWhereNombreArchivoEquals(String nombreArchivo) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'VERSION_COMPROBANTE = :versionComprobante'.
	 */
	public ValidacionXml[] findWhereVersionComprobanteEquals(String versionComprobante) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'CADENA_ORIGINAL = :cadenaOriginal'.
	 */
	public ValidacionXml[] findWhereCadenaOriginalEquals(String cadenaOriginal) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'COMPROBANTE_VALIDO = :comprobanteValido'.
	 */
	public ValidacionXml[] findWhereComprobanteValidoEquals(int comprobanteValido) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'SELLO_EMISOR_VALIDO = :selloEmisorValido'.
	 */
	public ValidacionXml[] findWhereSelloEmisorValidoEquals(int selloEmisorValido) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'TIMBRE_FISCAL = :timbreFiscal'.
	 */
	public ValidacionXml[] findWhereTimbreFiscalEquals(String timbreFiscal) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the criteria 'MENSAJES_ERROR = :mensajesError'.
	 */
	public ValidacionXml[] findWhereMensajesErrorEquals(String mensajesError) throws ValidacionXmlDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the validacion_xml table that match the specified arbitrary SQL statement
	 */
	public ValidacionXml[] findByDynamicSelect(String sql, Object[] sqlParams) throws ValidacionXmlDaoException;

	/** 
	 * Returns all rows from the validacion_xml table that match the specified arbitrary SQL statement
	 */
	public ValidacionXml[] findByDynamicWhere(String sql, Object[] sqlParams) throws ValidacionXmlDaoException;

}
