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
 * This class represents the primary key of the iedu table.
 */
public class IeduPk implements Serializable
{
	protected int idIedu;

	/** 
	 * This attribute represents whether the primitive attribute idIedu is null.
	 */
	protected boolean idIeduNull;

	/** 
	 * Sets the value of idIedu
	 */
	public void setIdIedu(int idIedu)
	{
		this.idIedu = idIedu;
	}

	/** 
	 * Gets the value of idIedu
	 */
	public int getIdIedu()
	{
		return idIedu;
	}

	/**
	 * Method 'IeduPk'
	 * 
	 */
	public IeduPk()
	{
	}

	/**
	 * Method 'IeduPk'
	 * 
	 * @param idIedu
	 */
	public IeduPk(final int idIedu)
	{
		this.idIedu = idIedu;
	}

	/** 
	 * Sets the value of idIeduNull
	 */
	public void setIdIeduNull(boolean idIeduNull)
	{
		this.idIeduNull = idIeduNull;
	}

	/** 
	 * Gets the value of idIeduNull
	 */
	public boolean isIdIeduNull()
	{
		return idIeduNull;
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
		
		if (!(_other instanceof IeduPk)) {
			return false;
		}
		
		final IeduPk _cast = (IeduPk) _other;
		if (idIedu != _cast.idIedu) {
			return false;
		}
		
		if (idIeduNull != _cast.idIeduNull) {
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
		_hashCode = 29 * _hashCode + idIedu;
		_hashCode = 29 * _hashCode + (idIeduNull ? 1 : 0);
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
		ret.append( "com.tsp.sct.dao.dto.IeduPk: " );
		ret.append( "idIedu=" + idIedu );
		return ret.toString();
	}

}