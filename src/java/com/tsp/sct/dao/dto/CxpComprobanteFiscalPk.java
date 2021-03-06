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
 * This class represents the primary key of the cxp_comprobante_fiscal table.
 */
public class CxpComprobanteFiscalPk implements Serializable
{
	protected int idCxpComprobanteFiscal;

	/** 
	 * This attribute represents whether the primitive attribute idCxpComprobanteFiscal is null.
	 */
	protected boolean idCxpComprobanteFiscalNull;

	/** 
	 * Sets the value of idCxpComprobanteFiscal
	 */
	public void setIdCxpComprobanteFiscal(int idCxpComprobanteFiscal)
	{
		this.idCxpComprobanteFiscal = idCxpComprobanteFiscal;
	}

	/** 
	 * Gets the value of idCxpComprobanteFiscal
	 */
	public int getIdCxpComprobanteFiscal()
	{
		return idCxpComprobanteFiscal;
	}

	/**
	 * Method 'CxpComprobanteFiscalPk'
	 * 
	 */
	public CxpComprobanteFiscalPk()
	{
	}

	/**
	 * Method 'CxpComprobanteFiscalPk'
	 * 
	 * @param idCxpComprobanteFiscal
	 */
	public CxpComprobanteFiscalPk(final int idCxpComprobanteFiscal)
	{
		this.idCxpComprobanteFiscal = idCxpComprobanteFiscal;
	}

	/** 
	 * Sets the value of idCxpComprobanteFiscalNull
	 */
	public void setIdCxpComprobanteFiscalNull(boolean idCxpComprobanteFiscalNull)
	{
		this.idCxpComprobanteFiscalNull = idCxpComprobanteFiscalNull;
	}

	/** 
	 * Gets the value of idCxpComprobanteFiscalNull
	 */
	public boolean isIdCxpComprobanteFiscalNull()
	{
		return idCxpComprobanteFiscalNull;
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
		
		if (!(_other instanceof CxpComprobanteFiscalPk)) {
			return false;
		}
		
		final CxpComprobanteFiscalPk _cast = (CxpComprobanteFiscalPk) _other;
		if (idCxpComprobanteFiscal != _cast.idCxpComprobanteFiscal) {
			return false;
		}
		
		if (idCxpComprobanteFiscalNull != _cast.idCxpComprobanteFiscalNull) {
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
		_hashCode = 29 * _hashCode + idCxpComprobanteFiscal;
		_hashCode = 29 * _hashCode + (idCxpComprobanteFiscalNull ? 1 : 0);
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
		ret.append( "com.tsp.sct.dao.dto.CxpComprobanteFiscalPk: " );
		ret.append( "idCxpComprobanteFiscal=" + idCxpComprobanteFiscal );
		return ret.toString();
	}

}
