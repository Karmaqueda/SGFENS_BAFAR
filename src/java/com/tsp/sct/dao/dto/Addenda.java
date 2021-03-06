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

public class Addenda implements Serializable
{
	/** 
	 * This attribute maps to the column ID_ADDENDA in the addenda table.
	 */
	protected int idAddenda;

	/** 
	 * This attribute maps to the column ID_FACTURA in the addenda table.
	 */
	protected int idFactura;

	/**
	 * Method 'Addenda'
	 * 
	 */
	public Addenda()
	{
	}

	/**
	 * Method 'getIdAddenda'
	 * 
	 * @return int
	 */
	public int getIdAddenda()
	{
		return idAddenda;
	}

	/**
	 * Method 'setIdAddenda'
	 * 
	 * @param idAddenda
	 */
	public void setIdAddenda(int idAddenda)
	{
		this.idAddenda = idAddenda;
	}

	/**
	 * Method 'getIdFactura'
	 * 
	 * @return int
	 */
	public int getIdFactura()
	{
		return idFactura;
	}

	/**
	 * Method 'setIdFactura'
	 * 
	 * @param idFactura
	 */
	public void setIdFactura(int idFactura)
	{
		this.idFactura = idFactura;
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
		
		if (!(_other instanceof Addenda)) {
			return false;
		}
		
		final Addenda _cast = (Addenda) _other;
		if (idAddenda != _cast.idAddenda) {
			return false;
		}
		
		if (idFactura != _cast.idFactura) {
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
		_hashCode = 29 * _hashCode + idAddenda;
		_hashCode = 29 * _hashCode + idFactura;
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return AddendaPk
	 */
	public AddendaPk createPk()
	{
		return new AddendaPk(idAddenda);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.tsp.sct.dao.dto.Addenda: " );
		ret.append( "idAddenda=" + idAddenda );
		ret.append( ", idFactura=" + idFactura );
		return ret.toString();
	}

}
