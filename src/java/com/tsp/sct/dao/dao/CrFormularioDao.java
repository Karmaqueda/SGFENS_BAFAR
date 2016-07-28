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

public interface CrFormularioDao
{
	/** 
	 * Inserts a new row in the cr_formulario table.
	 */
	public CrFormularioPk insert(CrFormulario dto) throws CrFormularioDaoException;

	/** 
	 * Updates a single row in the cr_formulario table.
	 */
	public void update(CrFormularioPk pk, CrFormulario dto) throws CrFormularioDaoException;

	/** 
	 * Deletes a single row in the cr_formulario table.
	 */
	public void delete(CrFormularioPk pk) throws CrFormularioDaoException;

	/** 
	 * Returns the rows from the cr_formulario table that matches the specified primary-key value.
	 */
	public CrFormulario findByPrimaryKey(CrFormularioPk pk) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_formulario = :idFormulario'.
	 */
	public CrFormulario findByPrimaryKey(int idFormulario) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria ''.
	 */
	public CrFormulario[] findAll() throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_formulario = :idFormulario'.
	 */
	public CrFormulario[] findWhereIdFormularioEquals(int idFormulario) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_grupo_formulario = :idGrupoFormulario'.
	 */
	public CrFormulario[] findWhereIdGrupoFormularioEquals(int idGrupoFormulario) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'orden_grupo = :ordenGrupo'.
	 */
	public CrFormulario[] findWhereOrdenGrupoEquals(int ordenGrupo) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'nombre = :nombre'.
	 */
	public CrFormulario[] findWhereNombreEquals(String nombre) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'descripcion = :descripcion'.
	 */
	public CrFormulario[] findWhereDescripcionEquals(String descripcion) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'fecha_hr_creacion = :fechaHrCreacion'.
	 */
	public CrFormulario[] findWhereFechaHrCreacionEquals(Date fechaHrCreacion) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'fecha_hr_ultima_edicion = :fechaHrUltimaEdicion'.
	 */
	public CrFormulario[] findWhereFechaHrUltimaEdicionEquals(Date fechaHrUltimaEdicion) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_usuario_edicion = :idUsuarioEdicion'.
	 */
	public CrFormulario[] findWhereIdUsuarioEdicionEquals(int idUsuarioEdicion) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_estatus = :idEstatus'.
	 */
	public CrFormulario[] findWhereIdEstatusEquals(int idEstatus) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the criteria 'id_empresa = :idEmpresa'.
	 */
	public CrFormulario[] findWhereIdEmpresaEquals(int idEmpresa) throws CrFormularioDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the cr_formulario table that match the specified arbitrary SQL statement
	 */
	public CrFormulario[] findByDynamicSelect(String sql, Object[] sqlParams) throws CrFormularioDaoException;

	/** 
	 * Returns all rows from the cr_formulario table that match the specified arbitrary SQL statement
	 */
	public CrFormulario[] findByDynamicWhere(String sql, Object[] sqlParams) throws CrFormularioDaoException;

}