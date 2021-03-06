/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.tsp.sct.dao.dto;

import com.tsp.sct.dao.dao.*;
import com.tsp.sct.dao.factory.*;
import com.tsp.sct.dao.exceptions.*;
import java.io.Serializable;
import java.util.*;

public class AduanaTerceros implements Serializable
{
	/** 
	 * This attribute maps to the column ID_ADUANA_TERCEROS in the aduana_terceros table.
	 */
	protected int idAduanaTerceros;

	/** 
	 * This attribute maps to the column ID_ADUANA in the aduana_terceros table.
	 */
	protected int idAduana;

	/** 
	 * This attribute maps to the column ID_CUENTA_TERCEROS in the aduana_terceros table.
	 */
	protected int idCuentaTerceros;

	/**
	 * Method 'AduanaTerceros'
	 * 
	 */
	public AduanaTerceros()
	{
	}

	/**
	 * Method 'getIdAduanaTerceros'
	 * 
	 * @return int
	 */
	public int getIdAduanaTerceros()
	{
		return idAduanaTerceros;
	}

	/**
	 * Method 'setIdAduanaTerceros'
	 * 
	 * @param idAduanaTerceros
	 */
	public void setIdAduanaTerceros(int idAduanaTerceros)
	{
		this.idAduanaTerceros = idAduanaTerceros;
	}

	/**
	 * Method 'getIdAduana'
	 * 
	 * @return int
	 */
	public int getIdAduana()
	{
		return idAduana;
	}

	/**
	 * Method 'setIdAduana'
	 * 
	 * @param idAduana
	 */
	public void setIdAduana(int idAduana)
	{
		this.idAduana = idAduana;
	}

	/**
	 * Method 'getIdCuentaTerceros'
	 * 
	 * @return int
	 */
	public int getIdCuentaTerceros()
	{
		return idCuentaTerceros;
	}

	/**
	 * Method 'setIdCuentaTerceros'
	 * 
	 * @param idCuentaTerceros
	 */
	public void setIdCuentaTerceros(int idCuentaTerceros)
	{
		this.idCuentaTerceros = idCuentaTerceros;
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
		
		if (!(_other instanceof AduanaTerceros)) {
			return false;
		}
		
		final AduanaTerceros _cast = (AduanaTerceros) _other;
		if (idAduanaTerceros != _cast.idAduanaTerceros) {
			return false;
		}
		
		if (idAduana != _cast.idAduana) {
			return false;
		}
		
		if (idCuentaTerceros != _cast.idCuentaTerceros) {
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
		_hashCode = 29 * _hashCode + idAduanaTerceros;
		_hashCode = 29 * _hashCode + idAduana;
		_hashCode = 29 * _hashCode + idCuentaTerceros;
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
		ret.append( "com.tsp.sct.dao.dto.AduanaTerceros: " );
		ret.append( "idAduanaTerceros=" + idAduanaTerceros );
		ret.append( ", idAduana=" + idAduana );
		ret.append( ", idCuentaTerceros=" + idCuentaTerceros );
		return ret.toString();
	}

}
