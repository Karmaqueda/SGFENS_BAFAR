/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.tsp.sct.dao.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 
 * This class represents the primary key of the cr_estado_solicitud table.
 */
public class CrEstadoSolicitudPk implements Serializable
{
	protected int idEstadoSolicitud;

	/** 
	 * This attribute represents whether the primitive attribute idEstadoSolicitud is null.
	 */
	protected boolean idEstadoSolicitudNull;

	/** 
	 * Sets the value of idEstadoSolicitud
	 */
	public void setIdEstadoSolicitud(int idEstadoSolicitud)
	{
		this.idEstadoSolicitud = idEstadoSolicitud;
	}

	/** 
	 * Gets the value of idEstadoSolicitud
	 */
	public int getIdEstadoSolicitud()
	{
		return idEstadoSolicitud;
	}

	/**
	 * Method 'CrEstadoSolicitudPk'
	 * 
	 */
	public CrEstadoSolicitudPk()
	{
	}

	/**
	 * Method 'CrEstadoSolicitudPk'
	 * 
	 * @param idEstadoSolicitud
	 */
	public CrEstadoSolicitudPk(final int idEstadoSolicitud)
	{
		this.idEstadoSolicitud = idEstadoSolicitud;
	}

	/** 
	 * Sets the value of idEstadoSolicitudNull
	 */
	public void setIdEstadoSolicitudNull(boolean idEstadoSolicitudNull)
	{
		this.idEstadoSolicitudNull = idEstadoSolicitudNull;
	}

	/** 
	 * Gets the value of idEstadoSolicitudNull
	 */
	public boolean isIdEstadoSolicitudNull()
	{
		return idEstadoSolicitudNull;
	}

	/**
	 * Method 'equals'
	 * 
	 * @param _other
	 * @return boolean
	 */
	public boolean equals(Object _other)
	{
		if (_other == null) {
			return false;
		}
		
		if (_other == this) {
			return true;
		}
		
		if (!(_other instanceof CrEstadoSolicitudPk)) {
			return false;
		}
		
		final CrEstadoSolicitudPk _cast = (CrEstadoSolicitudPk) _other;
		if (idEstadoSolicitud != _cast.idEstadoSolicitud) {
			return false;
		}
		
		if (idEstadoSolicitudNull != _cast.idEstadoSolicitudNull) {
			return false;
		}
		
		return true;
	}

	/**
	 * Method 'hashCode'
	 * 
	 * @return int
	 */
	public int hashCode()
	{
		int _hashCode = 0;
		_hashCode = 29 * _hashCode + idEstadoSolicitud;
		_hashCode = 29 * _hashCode + (idEstadoSolicitudNull ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.tsp.sct.dao.dto.CrEstadoSolicitudPk: " );
		ret.append( "idEstadoSolicitud=" + idEstadoSolicitud );
		return ret.toString();
	}

}
