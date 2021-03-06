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
 * This class represents the primary key of the divisas table.
 */
public class DivisasPk implements Serializable
{
	protected int idDivisas;

	/** 
	 * This attribute represents whether the primitive attribute idDivisas is null.
	 */
	protected boolean idDivisasNull;

	/** 
	 * Sets the value of idDivisas
	 */
	public void setIdDivisas(int idDivisas)
	{
		this.idDivisas = idDivisas;
	}

	/** 
	 * Gets the value of idDivisas
	 */
	public int getIdDivisas()
	{
		return idDivisas;
	}

	/**
	 * Method 'DivisasPk'
	 * 
	 */
	public DivisasPk()
	{
	}

	/**
	 * Method 'DivisasPk'
	 * 
	 * @param idDivisas
	 */
	public DivisasPk(final int idDivisas)
	{
		this.idDivisas = idDivisas;
	}

	/** 
	 * Sets the value of idDivisasNull
	 */
	public void setIdDivisasNull(boolean idDivisasNull)
	{
		this.idDivisasNull = idDivisasNull;
	}

	/** 
	 * Gets the value of idDivisasNull
	 */
	public boolean isIdDivisasNull()
	{
		return idDivisasNull;
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
		
		if (!(_other instanceof DivisasPk)) {
			return false;
		}
		
		final DivisasPk _cast = (DivisasPk) _other;
		if (idDivisas != _cast.idDivisas) {
			return false;
		}
		
		if (idDivisasNull != _cast.idDivisasNull) {
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
		_hashCode = 29 * _hashCode + idDivisas;
		_hashCode = 29 * _hashCode + (idDivisasNull ? 1 : 0);
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
		ret.append( "com.tsp.sct.dao.dto.DivisasPk: " );
		ret.append( "idDivisas=" + idDivisas );
		return ret.toString();
	}

}
