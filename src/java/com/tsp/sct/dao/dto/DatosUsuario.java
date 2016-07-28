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

public class DatosUsuario implements Serializable
{
	/** 
	 * This attribute maps to the column ID_DATOS_USUARIO in the datos_usuario table.
	 */
	protected int idDatosUsuario;

	/** 
	 * This attribute maps to the column NOMBRE in the datos_usuario table.
	 */
	protected String nombre;

	/** 
	 * This attribute maps to the column APELLIDO_PAT in the datos_usuario table.
	 */
	protected String apellidoPat;

	/** 
	 * This attribute maps to the column APELLIDO_MAT in the datos_usuario table.
	 */
	protected String apellidoMat;

	/** 
	 * This attribute maps to the column DIRECCION in the datos_usuario table.
	 */
	protected String direccion;

	/** 
	 * This attribute maps to the column LADA in the datos_usuario table.
	 */
	protected String lada;

	/** 
	 * This attribute maps to the column TELEFONO in the datos_usuario table.
	 */
	protected String telefono;

	/** 
	 * This attribute maps to the column EXTENSION in the datos_usuario table.
	 */
	protected String extension;

	/** 
	 * This attribute maps to the column CELULAR in the datos_usuario table.
	 */
	protected String celular;

	/** 
	 * This attribute maps to the column CORREO in the datos_usuario table.
	 */
	protected String correo;

	/**
	 * Method 'DatosUsuario'
	 * 
	 */
	public DatosUsuario()
	{
	}

	/**
	 * Method 'getIdDatosUsuario'
	 * 
	 * @return int
	 */
	public int getIdDatosUsuario()
	{
		return idDatosUsuario;
	}

	/**
	 * Method 'setIdDatosUsuario'
	 * 
	 * @param idDatosUsuario
	 */
	public void setIdDatosUsuario(int idDatosUsuario)
	{
		this.idDatosUsuario = idDatosUsuario;
	}

	/**
	 * Method 'getNombre'
	 * 
	 * @return String
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Method 'setNombre'
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Method 'getApellidoPat'
	 * 
	 * @return String
	 */
	public String getApellidoPat()
	{
		return apellidoPat;
	}

	/**
	 * Method 'setApellidoPat'
	 * 
	 * @param apellidoPat
	 */
	public void setApellidoPat(String apellidoPat)
	{
		this.apellidoPat = apellidoPat;
	}

	/**
	 * Method 'getApellidoMat'
	 * 
	 * @return String
	 */
	public String getApellidoMat()
	{
		return apellidoMat;
	}

	/**
	 * Method 'setApellidoMat'
	 * 
	 * @param apellidoMat
	 */
	public void setApellidoMat(String apellidoMat)
	{
		this.apellidoMat = apellidoMat;
	}

	/**
	 * Method 'getDireccion'
	 * 
	 * @return String
	 */
	public String getDireccion()
	{
		return direccion;
	}

	/**
	 * Method 'setDireccion'
	 * 
	 * @param direccion
	 */
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}

	/**
	 * Method 'getLada'
	 * 
	 * @return String
	 */
	public String getLada()
	{
		return lada;
	}

	/**
	 * Method 'setLada'
	 * 
	 * @param lada
	 */
	public void setLada(String lada)
	{
		this.lada = lada;
	}

	/**
	 * Method 'getTelefono'
	 * 
	 * @return String
	 */
	public String getTelefono()
	{
		return telefono;
	}

	/**
	 * Method 'setTelefono'
	 * 
	 * @param telefono
	 */
	public void setTelefono(String telefono)
	{
		this.telefono = telefono;
	}

	/**
	 * Method 'getExtension'
	 * 
	 * @return String
	 */
	public String getExtension()
	{
		return extension;
	}

	/**
	 * Method 'setExtension'
	 * 
	 * @param extension
	 */
	public void setExtension(String extension)
	{
		this.extension = extension;
	}

	/**
	 * Method 'getCelular'
	 * 
	 * @return String
	 */
	public String getCelular()
	{
		return celular;
	}

	/**
	 * Method 'setCelular'
	 * 
	 * @param celular
	 */
	public void setCelular(String celular)
	{
		this.celular = celular;
	}

	/**
	 * Method 'getCorreo'
	 * 
	 * @return String
	 */
	public String getCorreo()
	{
		return correo;
	}

	/**
	 * Method 'setCorreo'
	 * 
	 * @param correo
	 */
	public void setCorreo(String correo)
	{
		this.correo = correo;
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
		
		if (!(_other instanceof DatosUsuario)) {
			return false;
		}
		
		final DatosUsuario _cast = (DatosUsuario) _other;
		if (idDatosUsuario != _cast.idDatosUsuario) {
			return false;
		}
		
		if (nombre == null ? _cast.nombre != nombre : !nombre.equals( _cast.nombre )) {
			return false;
		}
		
		if (apellidoPat == null ? _cast.apellidoPat != apellidoPat : !apellidoPat.equals( _cast.apellidoPat )) {
			return false;
		}
		
		if (apellidoMat == null ? _cast.apellidoMat != apellidoMat : !apellidoMat.equals( _cast.apellidoMat )) {
			return false;
		}
		
		if (direccion == null ? _cast.direccion != direccion : !direccion.equals( _cast.direccion )) {
			return false;
		}
		
		if (lada == null ? _cast.lada != lada : !lada.equals( _cast.lada )) {
			return false;
		}
		
		if (telefono == null ? _cast.telefono != telefono : !telefono.equals( _cast.telefono )) {
			return false;
		}
		
		if (extension == null ? _cast.extension != extension : !extension.equals( _cast.extension )) {
			return false;
		}
		
		if (celular == null ? _cast.celular != celular : !celular.equals( _cast.celular )) {
			return false;
		}
		
		if (correo == null ? _cast.correo != correo : !correo.equals( _cast.correo )) {
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
		_hashCode = 29 * _hashCode + idDatosUsuario;
		if (nombre != null) {
			_hashCode = 29 * _hashCode + nombre.hashCode();
		}
		
		if (apellidoPat != null) {
			_hashCode = 29 * _hashCode + apellidoPat.hashCode();
		}
		
		if (apellidoMat != null) {
			_hashCode = 29 * _hashCode + apellidoMat.hashCode();
		}
		
		if (direccion != null) {
			_hashCode = 29 * _hashCode + direccion.hashCode();
		}
		
		if (lada != null) {
			_hashCode = 29 * _hashCode + lada.hashCode();
		}
		
		if (telefono != null) {
			_hashCode = 29 * _hashCode + telefono.hashCode();
		}
		
		if (extension != null) {
			_hashCode = 29 * _hashCode + extension.hashCode();
		}
		
		if (celular != null) {
			_hashCode = 29 * _hashCode + celular.hashCode();
		}
		
		if (correo != null) {
			_hashCode = 29 * _hashCode + correo.hashCode();
		}
		
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return DatosUsuarioPk
	 */
	public DatosUsuarioPk createPk()
	{
		return new DatosUsuarioPk(idDatosUsuario);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.tsp.sct.dao.dto.DatosUsuario: " );
		ret.append( "idDatosUsuario=" + idDatosUsuario );
		ret.append( ", nombre=" + nombre );
		ret.append( ", apellidoPat=" + apellidoPat );
		ret.append( ", apellidoMat=" + apellidoMat );
		ret.append( ", direccion=" + direccion );
		ret.append( ", lada=" + lada );
		ret.append( ", telefono=" + telefono );
		ret.append( ", extension=" + extension );
		ret.append( ", celular=" + celular );
		ret.append( ", correo=" + correo );
		return ret.toString();
	}

}