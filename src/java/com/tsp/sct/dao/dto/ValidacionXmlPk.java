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
 * This class represents the primary key of the validacion_xml table.
 */
public class ValidacionXmlPk implements Serializable
{
	protected int idValidacion;

	/** 
	 * This attribute represents whether the primitive attribute idValidacion is null.
	 */
	protected boolean idValidacionNull;

	/** 
	 * Sets the value of idValidacion
	 */
	public void setIdValidacion(int idValidacion)
	{
		this.idValidacion = idValidacion;
	}

	/** 
	 * Gets the value of idValidacion
	 */
	public int getIdValidacion()
	{
		return idValidacion;
	}

	/**
	 * Method 'ValidacionXmlPk'
	 * 
	 */
	public ValidacionXmlPk()
	{
	}

	/**
	 * Method 'ValidacionXmlPk'
	 * 
	 * @param idValidacion
	 */
	public ValidacionXmlPk(final int idValidacion)
	{
		this.idValidacion = idValidacion;
	}

	/** 
	 * Sets the value of idValidacionNull
	 */
	public void setIdValidacionNull(boolean idValidacionNull)
	{
		this.idValidacionNull = idValidacionNull;
	}

	/** 
	 * Gets the value of idValidacionNull
	 */
	public boolean isIdValidacionNull()
	{
		return idValidacionNull;
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
		
		if (!(_other instanceof ValidacionXmlPk)) {
			return false;
		}
		
		final ValidacionXmlPk _cast = (ValidacionXmlPk) _other;
		if (idValidacion != _cast.idValidacion) {
			return false;
		}
		
		if (idValidacionNull != _cast.idValidacionNull) {
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
		_hashCode = 29 * _hashCode + idValidacion;
		_hashCode = 29 * _hashCode + (idValidacionNull ? 1 : 0);
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
		ret.append( "com.tsp.sct.dao.dto.ValidacionXmlPk: " );
		ret.append( "idValidacion=" + idValidacion );
		return ret.toString();
	}

}