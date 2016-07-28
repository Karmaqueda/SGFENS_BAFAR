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
 * This class represents the primary key of the preto_modulo table.
 */
public class PretoModuloPk implements Serializable
{
	protected int idPretoModulo;

	/** 
	 * This attribute represents whether the primitive attribute idPretoModulo is null.
	 */
	protected boolean idPretoModuloNull;

	/** 
	 * Sets the value of idPretoModulo
	 */
	public void setIdPretoModulo(int idPretoModulo)
	{
		this.idPretoModulo = idPretoModulo;
	}

	/** 
	 * Gets the value of idPretoModulo
	 */
	public int getIdPretoModulo()
	{
		return idPretoModulo;
	}

	/**
	 * Method 'PretoModuloPk'
	 * 
	 */
	public PretoModuloPk()
	{
	}

	/**
	 * Method 'PretoModuloPk'
	 * 
	 * @param idPretoModulo
	 */
	public PretoModuloPk(final int idPretoModulo)
	{
		this.idPretoModulo = idPretoModulo;
	}

	/** 
	 * Sets the value of idPretoModuloNull
	 */
	public void setIdPretoModuloNull(boolean idPretoModuloNull)
	{
		this.idPretoModuloNull = idPretoModuloNull;
	}

	/** 
	 * Gets the value of idPretoModuloNull
	 */
	public boolean isIdPretoModuloNull()
	{
		return idPretoModuloNull;
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
		
		if (!(_other instanceof PretoModuloPk)) {
			return false;
		}
		
		final PretoModuloPk _cast = (PretoModuloPk) _other;
		if (idPretoModulo != _cast.idPretoModulo) {
			return false;
		}
		
		if (idPretoModuloNull != _cast.idPretoModuloNull) {
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
		_hashCode = 29 * _hashCode + idPretoModulo;
		_hashCode = 29 * _hashCode + (idPretoModuloNull ? 1 : 0);
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
		ret.append( "com.tsp.sct.dao.dto.PretoModuloPk: " );
		ret.append( "idPretoModulo=" + idPretoModulo );
		return ret.toString();
	}

}