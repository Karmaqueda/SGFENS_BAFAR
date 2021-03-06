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
 * This class represents the primary key of the roles table.
 */
public class RolesPk implements Serializable
{
	protected int idRoles;

	/** 
	 * This attribute represents whether the primitive attribute idRoles is null.
	 */
	protected boolean idRolesNull;

	/** 
	 * Sets the value of idRoles
	 */
	public void setIdRoles(int idRoles)
	{
		this.idRoles = idRoles;
	}

	/** 
	 * Gets the value of idRoles
	 */
	public int getIdRoles()
	{
		return idRoles;
	}

	/**
	 * Method 'RolesPk'
	 * 
	 */
	public RolesPk()
	{
	}

	/**
	 * Method 'RolesPk'
	 * 
	 * @param idRoles
	 */
	public RolesPk(final int idRoles)
	{
		this.idRoles = idRoles;
	}

	/** 
	 * Sets the value of idRolesNull
	 */
	public void setIdRolesNull(boolean idRolesNull)
	{
		this.idRolesNull = idRolesNull;
	}

	/** 
	 * Gets the value of idRolesNull
	 */
	public boolean isIdRolesNull()
	{
		return idRolesNull;
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
		
		if (!(_other instanceof RolesPk)) {
			return false;
		}
		
		final RolesPk _cast = (RolesPk) _other;
		if (idRoles != _cast.idRoles) {
			return false;
		}
		
		if (idRolesNull != _cast.idRolesNull) {
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
		_hashCode = 29 * _hashCode + idRoles;
		_hashCode = 29 * _hashCode + (idRolesNull ? 1 : 0);
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
		ret.append( "com.tsp.sct.dao.dto.RolesPk: " );
		ret.append( "idRoles=" + idRoles );
		return ret.toString();
	}

}
