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

public interface CrGrupoFormularioDao
{
	/** 
	 * Inserts a new row in the cr_grupo_formulario table.
	 */
	public CrGrupoFormularioPk insert(CrGrupoFormulario dto) throws CrGrupoFormularioDaoException;

	/** 
	 * Updates a single row in the cr_grupo_formulario table.
	 */
	public void update(CrGrupoFormularioPk pk, CrGrupoFormulario dto) throws CrGrupoFormularioDaoException;

	/** 
	 * Deletes a single row in the cr_grupo_formulario table.
	 */
	public void delete(CrGrupoFormularioPk pk) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns the rows from the cr_grupo_formulario table that matches the specified primary-key value.
	 */
	public CrGrupoFormulario findByPrimaryKey(CrGrupoFormularioPk pk) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the criteria 'id_grupo_formulario = :idGrupoFormulario'.
	 */
	public CrGrupoFormulario findByPrimaryKey(int idGrupoFormulario) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the criteria ''.
	 */
	public CrGrupoFormulario[] findAll() throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the criteria 'id_grupo_formulario = :idGrupoFormulario'.
	 */
	public CrGrupoFormulario[] findWhereIdGrupoFormularioEquals(int idGrupoFormulario) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the criteria 'nombre = :nombre'.
	 */
	public CrGrupoFormulario[] findWhereNombreEquals(String nombre) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the criteria 'descripcion = :descripcion'.
	 */
	public CrGrupoFormulario[] findWhereDescripcionEquals(String descripcion) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the criteria 'fecha_hr_creacion = :fechaHrCreacion'.
	 */
	public CrGrupoFormulario[] findWhereFechaHrCreacionEquals(Date fechaHrCreacion) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the criteria 'id_estatus = :idEstatus'.
	 */
	public CrGrupoFormulario[] findWhereIdEstatusEquals(int idEstatus) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the criteria 'id_empresa = :idEmpresa'.
	 */
	public CrGrupoFormulario[] findWhereIdEmpresaEquals(int idEmpresa) throws CrGrupoFormularioDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the specified arbitrary SQL statement
	 */
	public CrGrupoFormulario[] findByDynamicSelect(String sql, Object[] sqlParams) throws CrGrupoFormularioDaoException;

	/** 
	 * Returns all rows from the cr_grupo_formulario table that match the specified arbitrary SQL statement
	 */
	public CrGrupoFormulario[] findByDynamicWhere(String sql, Object[] sqlParams) throws CrGrupoFormularioDaoException;

}