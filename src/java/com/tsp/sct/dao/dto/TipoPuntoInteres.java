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

public class TipoPuntoInteres implements Serializable
{
	/** 
	 * This attribute maps to the column ID_TIPO_PUNTO in the tipo_punto_interes table.
	 */
	protected int idTipoPunto;

	/** 
	 * This attribute represents whether the attribute idTipoPunto has been modified since being read from the database.
	 */
	protected boolean idTipoPuntoModified = false;

	/** 
	 * This attribute maps to the column NOMBRE_TIPO_PUNTO in the tipo_punto_interes table.
	 */
	protected String nombreTipoPunto;

	/** 
	 * This attribute represents whether the attribute nombreTipoPunto has been modified since being read from the database.
	 */
	protected boolean nombreTipoPuntoModified = false;

	/** 
	 * This attribute maps to the column SUFIJO_IMG_TIPO_PUNTO in the tipo_punto_interes table.
	 */
	protected String sufijoImgTipoPunto;

	/** 
	 * This attribute represents whether the attribute sufijoImgTipoPunto has been modified since being read from the database.
	 */
	protected boolean sufijoImgTipoPuntoModified = false;

	/**
	 * Method 'TipoPuntoInteres'
	 * 
	 */
	public TipoPuntoInteres()
	{
	}

	/**
	 * Method 'getIdTipoPunto'
	 * 
	 * @return int
	 */
	public int getIdTipoPunto()
	{
		return idTipoPunto;
	}

	/**
	 * Method 'setIdTipoPunto'
	 * 
	 * @param idTipoPunto
	 */
	public void setIdTipoPunto(int idTipoPunto)
	{
		this.idTipoPunto = idTipoPunto;
		this.idTipoPuntoModified = true;
	}

	/** 
	 * Sets the value of idTipoPuntoModified
	 */
	public void setIdTipoPuntoModified(boolean idTipoPuntoModified)
	{
		this.idTipoPuntoModified = idTipoPuntoModified;
	}

	/** 
	 * Gets the value of idTipoPuntoModified
	 */
	public boolean isIdTipoPuntoModified()
	{
		return idTipoPuntoModified;
	}

	/**
	 * Method 'getNombreTipoPunto'
	 * 
	 * @return String
	 */
	public String getNombreTipoPunto()
	{
		return nombreTipoPunto;
	}

	/**
	 * Method 'setNombreTipoPunto'
	 * 
	 * @param nombreTipoPunto
	 */
	public void setNombreTipoPunto(String nombreTipoPunto)
	{
		this.nombreTipoPunto = nombreTipoPunto;
		this.nombreTipoPuntoModified = true;
	}

	/** 
	 * Sets the value of nombreTipoPuntoModified
	 */
	public void setNombreTipoPuntoModified(boolean nombreTipoPuntoModified)
	{
		this.nombreTipoPuntoModified = nombreTipoPuntoModified;
	}

	/** 
	 * Gets the value of nombreTipoPuntoModified
	 */
	public boolean isNombreTipoPuntoModified()
	{
		return nombreTipoPuntoModified;
	}

	/**
	 * Method 'getSufijoImgTipoPunto'
	 * 
	 * @return String
	 */
	public String getSufijoImgTipoPunto()
	{
		return sufijoImgTipoPunto;
	}

	/**
	 * Method 'setSufijoImgTipoPunto'
	 * 
	 * @param sufijoImgTipoPunto
	 */
	public void setSufijoImgTipoPunto(String sufijoImgTipoPunto)
	{
		this.sufijoImgTipoPunto = sufijoImgTipoPunto;
		this.sufijoImgTipoPuntoModified = true;
	}

	/** 
	 * Sets the value of sufijoImgTipoPuntoModified
	 */
	public void setSufijoImgTipoPuntoModified(boolean sufijoImgTipoPuntoModified)
	{
		this.sufijoImgTipoPuntoModified = sufijoImgTipoPuntoModified;
	}

	/** 
	 * Gets the value of sufijoImgTipoPuntoModified
	 */
	public boolean isSufijoImgTipoPuntoModified()
	{
		return sufijoImgTipoPuntoModified;
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
		
		if (!(_other instanceof TipoPuntoInteres)) {
			return false;
		}
		
		final TipoPuntoInteres _cast = (TipoPuntoInteres) _other;
		if (idTipoPunto != _cast.idTipoPunto) {
			return false;
		}
		
		if (idTipoPuntoModified != _cast.idTipoPuntoModified) {
			return false;
		}
		
		if (nombreTipoPunto == null ? _cast.nombreTipoPunto != nombreTipoPunto : !nombreTipoPunto.equals( _cast.nombreTipoPunto )) {
			return false;
		}
		
		if (nombreTipoPuntoModified != _cast.nombreTipoPuntoModified) {
			return false;
		}
		
		if (sufijoImgTipoPunto == null ? _cast.sufijoImgTipoPunto != sufijoImgTipoPunto : !sufijoImgTipoPunto.equals( _cast.sufijoImgTipoPunto )) {
			return false;
		}
		
		if (sufijoImgTipoPuntoModified != _cast.sufijoImgTipoPuntoModified) {
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
		_hashCode = 29 * _hashCode + idTipoPunto;
		_hashCode = 29 * _hashCode + (idTipoPuntoModified ? 1 : 0);
		if (nombreTipoPunto != null) {
			_hashCode = 29 * _hashCode + nombreTipoPunto.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (nombreTipoPuntoModified ? 1 : 0);
		if (sufijoImgTipoPunto != null) {
			_hashCode = 29 * _hashCode + sufijoImgTipoPunto.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (sufijoImgTipoPuntoModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return TipoPuntoInteresPk
	 */
	public TipoPuntoInteresPk createPk()
	{
		return new TipoPuntoInteresPk(idTipoPunto);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.tsp.sct.dao.dto.TipoPuntoInteres: " );
		ret.append( "idTipoPunto=" + idTipoPunto );
		ret.append( ", nombreTipoPunto=" + nombreTipoPunto );
		ret.append( ", sufijoImgTipoPunto=" + sufijoImgTipoPunto );
		return ret.toString();
	}

}
