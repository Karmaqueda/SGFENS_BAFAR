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
 * This class represents the primary key of the cr_formulario_campo table.
 */
public class CrFormularioCampoPk implements Serializable
{
	protected int idFormularioCampo;

	/** 
	 * This attribute represents whether the primitive attribute idFormularioCampo is null.
	 */
	protected boolean idFormularioCampoNull;

	/** 
	 * Sets the value of idFormularioCampo
	 */
	public void setIdFormularioCampo(int idFormularioCampo)
	{
		this.idFormularioCampo = idFormularioCampo;
	}

	/** 
	 * Gets the value of idFormularioCampo
	 */
	public int getIdFormularioCampo()
	{
		return idFormularioCampo;
	}

	/**
	 * Method 'CrFormularioCampoPk'
	 * 
	 */
	public CrFormularioCampoPk()
	{
	}

	/**
	 * Method 'CrFormularioCampoPk'
	 * 
	 * @param idFormularioCampo
	 */
	public CrFormularioCampoPk(final int idFormularioCampo)
	{
		this.idFormularioCampo = idFormularioCampo;
	}

	/** 
	 * Sets the value of idFormularioCampoNull
	 */
	public void setIdFormularioCampoNull(boolean idFormularioCampoNull)
	{
		this.idFormularioCampoNull = idFormularioCampoNull;
	}

	/** 
	 * Gets the value of idFormularioCampoNull
	 */
	public boolean isIdFormularioCampoNull()
	{
		return idFormularioCampoNull;
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
		
		if (!(_other instanceof CrFormularioCampoPk)) {
			return false;
		}
		
		final CrFormularioCampoPk _cast = (CrFormularioCampoPk) _other;
		if (idFormularioCampo != _cast.idFormularioCampo) {
			return false;
		}
		
		if (idFormularioCampoNull != _cast.idFormularioCampoNull) {
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
		_hashCode = 29 * _hashCode + idFormularioCampo;
		_hashCode = 29 * _hashCode + (idFormularioCampoNull ? 1 : 0);
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
		ret.append( "com.tsp.sct.dao.dto.CrFormularioCampoPk: " );
		ret.append( "idFormularioCampo=" + idFormularioCampo );
		return ret.toString();
	}

}
