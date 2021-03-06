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

public class ClienteCampoContenido implements Serializable
{
	/** 
	 * This attribute maps to the column ID_CONTENIDO in the cliente_campo_contenido table.
	 */
	protected int idContenido;

	/** 
	 * This attribute represents whether the attribute idContenido has been modified since being read from the database.
	 */
	protected boolean idContenidoModified = false;

	/** 
	 * This attribute maps to the column ID_CLIENTE_CAMPO in the cliente_campo_contenido table.
	 */
	protected int idClienteCampo;

	/** 
	 * This attribute represents whether the attribute idClienteCampo has been modified since being read from the database.
	 */
	protected boolean idClienteCampoModified = false;

	/** 
	 * This attribute maps to the column ID_CLIENTE in the cliente_campo_contenido table.
	 */
	protected int idCliente;

	/** 
	 * This attribute represents whether the attribute idCliente has been modified since being read from the database.
	 */
	protected boolean idClienteModified = false;

	/** 
	 * This attribute maps to the column VALOR_LABEL in the cliente_campo_contenido table.
	 */
	protected String valorLabel;

	/** 
	 * This attribute represents whether the attribute valorLabel has been modified since being read from the database.
	 */
	protected boolean valorLabelModified = false;

	/**
	 * Method 'ClienteCampoContenido'
	 * 
	 */
	public ClienteCampoContenido()
	{
	}

	/**
	 * Method 'getIdContenido'
	 * 
	 * @return int
	 */
	public int getIdContenido()
	{
		return idContenido;
	}

	/**
	 * Method 'setIdContenido'
	 * 
	 * @param idContenido
	 */
	public void setIdContenido(int idContenido)
	{
		this.idContenido = idContenido;
		this.idContenidoModified = true;
	}

	/** 
	 * Sets the value of idContenidoModified
	 */
	public void setIdContenidoModified(boolean idContenidoModified)
	{
		this.idContenidoModified = idContenidoModified;
	}

	/** 
	 * Gets the value of idContenidoModified
	 */
	public boolean isIdContenidoModified()
	{
		return idContenidoModified;
	}

	/**
	 * Method 'getIdClienteCampo'
	 * 
	 * @return int
	 */
	public int getIdClienteCampo()
	{
		return idClienteCampo;
	}

	/**
	 * Method 'setIdClienteCampo'
	 * 
	 * @param idClienteCampo
	 */
	public void setIdClienteCampo(int idClienteCampo)
	{
		this.idClienteCampo = idClienteCampo;
		this.idClienteCampoModified = true;
	}

	/** 
	 * Sets the value of idClienteCampoModified
	 */
	public void setIdClienteCampoModified(boolean idClienteCampoModified)
	{
		this.idClienteCampoModified = idClienteCampoModified;
	}

	/** 
	 * Gets the value of idClienteCampoModified
	 */
	public boolean isIdClienteCampoModified()
	{
		return idClienteCampoModified;
	}

	/**
	 * Method 'getIdCliente'
	 * 
	 * @return int
	 */
	public int getIdCliente()
	{
		return idCliente;
	}

	/**
	 * Method 'setIdCliente'
	 * 
	 * @param idCliente
	 */
	public void setIdCliente(int idCliente)
	{
		this.idCliente = idCliente;
		this.idClienteModified = true;
	}

	/** 
	 * Sets the value of idClienteModified
	 */
	public void setIdClienteModified(boolean idClienteModified)
	{
		this.idClienteModified = idClienteModified;
	}

	/** 
	 * Gets the value of idClienteModified
	 */
	public boolean isIdClienteModified()
	{
		return idClienteModified;
	}

	/**
	 * Method 'getValorLabel'
	 * 
	 * @return String
	 */
	public String getValorLabel()
	{
		return valorLabel;
	}

	/**
	 * Method 'setValorLabel'
	 * 
	 * @param valorLabel
	 */
	public void setValorLabel(String valorLabel)
	{
		this.valorLabel = valorLabel;
		this.valorLabelModified = true;
	}

	/** 
	 * Sets the value of valorLabelModified
	 */
	public void setValorLabelModified(boolean valorLabelModified)
	{
		this.valorLabelModified = valorLabelModified;
	}

	/** 
	 * Gets the value of valorLabelModified
	 */
	public boolean isValorLabelModified()
	{
		return valorLabelModified;
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
		
		if (!(_other instanceof ClienteCampoContenido)) {
			return false;
		}
		
		final ClienteCampoContenido _cast = (ClienteCampoContenido) _other;
		if (idContenido != _cast.idContenido) {
			return false;
		}
		
		if (idContenidoModified != _cast.idContenidoModified) {
			return false;
		}
		
		if (idClienteCampo != _cast.idClienteCampo) {
			return false;
		}
		
		if (idClienteCampoModified != _cast.idClienteCampoModified) {
			return false;
		}
		
		if (idCliente != _cast.idCliente) {
			return false;
		}
		
		if (idClienteModified != _cast.idClienteModified) {
			return false;
		}
		
		if (valorLabel == null ? _cast.valorLabel != valorLabel : !valorLabel.equals( _cast.valorLabel )) {
			return false;
		}
		
		if (valorLabelModified != _cast.valorLabelModified) {
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
		_hashCode = 29 * _hashCode + idContenido;
		_hashCode = 29 * _hashCode + (idContenidoModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idClienteCampo;
		_hashCode = 29 * _hashCode + (idClienteCampoModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idCliente;
		_hashCode = 29 * _hashCode + (idClienteModified ? 1 : 0);
		if (valorLabel != null) {
			_hashCode = 29 * _hashCode + valorLabel.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (valorLabelModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return ClienteCampoContenidoPk
	 */
	public ClienteCampoContenidoPk createPk()
	{
		return new ClienteCampoContenidoPk(idContenido);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.tsp.sct.dao.dto.ClienteCampoContenido: " );
		ret.append( "idContenido=" + idContenido );
		ret.append( ", idClienteCampo=" + idClienteCampo );
		ret.append( ", idCliente=" + idCliente );
		ret.append( ", valorLabel=" + valorLabel );
		return ret.toString();
	}

}
