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
import java.util.Date;

public class EmergenciaCredito implements Serializable
{
	/** 
	 * This attribute maps to the column ID_EMERGENCIA in the emergencia_credito table.
	 */
	protected int idEmergencia;

	/** 
	 * This attribute represents whether the attribute idEmergencia has been modified since being read from the database.
	 */
	protected boolean idEmergenciaModified = false;

	/** 
	 * This attribute maps to the column ID_EMPRESA in the emergencia_credito table.
	 */
	protected int idEmpresa;

	/** 
	 * This attribute represents whether the attribute idEmpresa has been modified since being read from the database.
	 */
	protected boolean idEmpresaModified = false;

	/** 
	 * This attribute maps to the column FECHA_INICIO in the emergencia_credito table.
	 */
	protected Date fechaInicio;

	/** 
	 * This attribute represents whether the attribute fechaInicio has been modified since being read from the database.
	 */
	protected boolean fechaInicioModified = false;

	/** 
	 * This attribute maps to the column FECHA_PAGO in the emergencia_credito table.
	 */
	protected Date fechaPago;

	/** 
	 * This attribute represents whether the attribute fechaPago has been modified since being read from the database.
	 */
	protected boolean fechaPagoModified = false;

	/** 
	 * This attribute maps to the column MONTO_PAGADO in the emergencia_credito table.
	 */
	protected double montoPagado;

	/** 
	 * This attribute represents whether the primitive attribute montoPagado is null.
	 */
	protected boolean montoPagadoNull = true;

	/** 
	 * This attribute represents whether the attribute montoPagado has been modified since being read from the database.
	 */
	protected boolean montoPagadoModified = false;

	/** 
	 * This attribute maps to the column CREDITOS_OCUPADOS in the emergencia_credito table.
	 */
	protected int creditosOcupados;

	/** 
	 * This attribute represents whether the primitive attribute creditosOcupados is null.
	 */
	protected boolean creditosOcupadosNull = true;

	/** 
	 * This attribute represents whether the attribute creditosOcupados has been modified since being read from the database.
	 */
	protected boolean creditosOcupadosModified = false;

	/** 
	 * This attribute maps to the column RFC_EMPRESA in the emergencia_credito table.
	 */
	protected String rfcEmpresa;

	/** 
	 * This attribute represents whether the attribute rfcEmpresa has been modified since being read from the database.
	 */
	protected boolean rfcEmpresaModified = false;

	/**
	 * Method 'EmergenciaCredito'
	 * 
	 */
	public EmergenciaCredito()
	{
	}

	/**
	 * Method 'getIdEmergencia'
	 * 
	 * @return int
	 */
	public int getIdEmergencia()
	{
		return idEmergencia;
	}

	/**
	 * Method 'setIdEmergencia'
	 * 
	 * @param idEmergencia
	 */
	public void setIdEmergencia(int idEmergencia)
	{
		this.idEmergencia = idEmergencia;
		this.idEmergenciaModified = true;
	}

	/** 
	 * Sets the value of idEmergenciaModified
	 */
	public void setIdEmergenciaModified(boolean idEmergenciaModified)
	{
		this.idEmergenciaModified = idEmergenciaModified;
	}

	/** 
	 * Gets the value of idEmergenciaModified
	 */
	public boolean isIdEmergenciaModified()
	{
		return idEmergenciaModified;
	}

	/**
	 * Method 'getIdEmpresa'
	 * 
	 * @return int
	 */
	public int getIdEmpresa()
	{
		return idEmpresa;
	}

	/**
	 * Method 'setIdEmpresa'
	 * 
	 * @param idEmpresa
	 */
	public void setIdEmpresa(int idEmpresa)
	{
		this.idEmpresa = idEmpresa;
		this.idEmpresaModified = true;
	}

	/** 
	 * Sets the value of idEmpresaModified
	 */
	public void setIdEmpresaModified(boolean idEmpresaModified)
	{
		this.idEmpresaModified = idEmpresaModified;
	}

	/** 
	 * Gets the value of idEmpresaModified
	 */
	public boolean isIdEmpresaModified()
	{
		return idEmpresaModified;
	}

	/**
	 * Method 'getFechaInicio'
	 * 
	 * @return Date
	 */
	public Date getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * Method 'setFechaInicio'
	 * 
	 * @param fechaInicio
	 */
	public void setFechaInicio(Date fechaInicio)
	{
		this.fechaInicio = fechaInicio;
		this.fechaInicioModified = true;
	}

	/** 
	 * Sets the value of fechaInicioModified
	 */
	public void setFechaInicioModified(boolean fechaInicioModified)
	{
		this.fechaInicioModified = fechaInicioModified;
	}

	/** 
	 * Gets the value of fechaInicioModified
	 */
	public boolean isFechaInicioModified()
	{
		return fechaInicioModified;
	}

	/**
	 * Method 'getFechaPago'
	 * 
	 * @return Date
	 */
	public Date getFechaPago()
	{
		return fechaPago;
	}

	/**
	 * Method 'setFechaPago'
	 * 
	 * @param fechaPago
	 */
	public void setFechaPago(Date fechaPago)
	{
		this.fechaPago = fechaPago;
		this.fechaPagoModified = true;
	}

	/** 
	 * Sets the value of fechaPagoModified
	 */
	public void setFechaPagoModified(boolean fechaPagoModified)
	{
		this.fechaPagoModified = fechaPagoModified;
	}

	/** 
	 * Gets the value of fechaPagoModified
	 */
	public boolean isFechaPagoModified()
	{
		return fechaPagoModified;
	}

	/**
	 * Method 'getMontoPagado'
	 * 
	 * @return double
	 */
	public double getMontoPagado()
	{
		return montoPagado;
	}

	/**
	 * Method 'setMontoPagado'
	 * 
	 * @param montoPagado
	 */
	public void setMontoPagado(double montoPagado)
	{
		this.montoPagado = montoPagado;
		this.montoPagadoNull = false;
		this.montoPagadoModified = true;
	}

	/**
	 * Method 'setMontoPagadoNull'
	 * 
	 * @param value
	 */
	public void setMontoPagadoNull(boolean value)
	{
		this.montoPagadoNull = value;
		this.montoPagadoModified = true;
	}

	/**
	 * Method 'isMontoPagadoNull'
	 * 
	 * @return boolean
	 */
	public boolean isMontoPagadoNull()
	{
		return montoPagadoNull;
	}

	/** 
	 * Sets the value of montoPagadoModified
	 */
	public void setMontoPagadoModified(boolean montoPagadoModified)
	{
		this.montoPagadoModified = montoPagadoModified;
	}

	/** 
	 * Gets the value of montoPagadoModified
	 */
	public boolean isMontoPagadoModified()
	{
		return montoPagadoModified;
	}

	/**
	 * Method 'getCreditosOcupados'
	 * 
	 * @return int
	 */
	public int getCreditosOcupados()
	{
		return creditosOcupados;
	}

	/**
	 * Method 'setCreditosOcupados'
	 * 
	 * @param creditosOcupados
	 */
	public void setCreditosOcupados(int creditosOcupados)
	{
		this.creditosOcupados = creditosOcupados;
		this.creditosOcupadosNull = false;
		this.creditosOcupadosModified = true;
	}

	/**
	 * Method 'setCreditosOcupadosNull'
	 * 
	 * @param value
	 */
	public void setCreditosOcupadosNull(boolean value)
	{
		this.creditosOcupadosNull = value;
		this.creditosOcupadosModified = true;
	}

	/**
	 * Method 'isCreditosOcupadosNull'
	 * 
	 * @return boolean
	 */
	public boolean isCreditosOcupadosNull()
	{
		return creditosOcupadosNull;
	}

	/** 
	 * Sets the value of creditosOcupadosModified
	 */
	public void setCreditosOcupadosModified(boolean creditosOcupadosModified)
	{
		this.creditosOcupadosModified = creditosOcupadosModified;
	}

	/** 
	 * Gets the value of creditosOcupadosModified
	 */
	public boolean isCreditosOcupadosModified()
	{
		return creditosOcupadosModified;
	}

	/**
	 * Method 'getRfcEmpresa'
	 * 
	 * @return String
	 */
	public String getRfcEmpresa()
	{
		return rfcEmpresa;
	}

	/**
	 * Method 'setRfcEmpresa'
	 * 
	 * @param rfcEmpresa
	 */
	public void setRfcEmpresa(String rfcEmpresa)
	{
		this.rfcEmpresa = rfcEmpresa;
		this.rfcEmpresaModified = true;
	}

	/** 
	 * Sets the value of rfcEmpresaModified
	 */
	public void setRfcEmpresaModified(boolean rfcEmpresaModified)
	{
		this.rfcEmpresaModified = rfcEmpresaModified;
	}

	/** 
	 * Gets the value of rfcEmpresaModified
	 */
	public boolean isRfcEmpresaModified()
	{
		return rfcEmpresaModified;
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
		
		if (!(_other instanceof EmergenciaCredito)) {
			return false;
		}
		
		final EmergenciaCredito _cast = (EmergenciaCredito) _other;
		if (idEmergencia != _cast.idEmergencia) {
			return false;
		}
		
		if (idEmergenciaModified != _cast.idEmergenciaModified) {
			return false;
		}
		
		if (idEmpresa != _cast.idEmpresa) {
			return false;
		}
		
		if (idEmpresaModified != _cast.idEmpresaModified) {
			return false;
		}
		
		if (fechaInicio == null ? _cast.fechaInicio != fechaInicio : !fechaInicio.equals( _cast.fechaInicio )) {
			return false;
		}
		
		if (fechaInicioModified != _cast.fechaInicioModified) {
			return false;
		}
		
		if (fechaPago == null ? _cast.fechaPago != fechaPago : !fechaPago.equals( _cast.fechaPago )) {
			return false;
		}
		
		if (fechaPagoModified != _cast.fechaPagoModified) {
			return false;
		}
		
		if (montoPagado != _cast.montoPagado) {
			return false;
		}
		
		if (montoPagadoNull != _cast.montoPagadoNull) {
			return false;
		}
		
		if (montoPagadoModified != _cast.montoPagadoModified) {
			return false;
		}
		
		if (creditosOcupados != _cast.creditosOcupados) {
			return false;
		}
		
		if (creditosOcupadosNull != _cast.creditosOcupadosNull) {
			return false;
		}
		
		if (creditosOcupadosModified != _cast.creditosOcupadosModified) {
			return false;
		}
		
		if (rfcEmpresa == null ? _cast.rfcEmpresa != rfcEmpresa : !rfcEmpresa.equals( _cast.rfcEmpresa )) {
			return false;
		}
		
		if (rfcEmpresaModified != _cast.rfcEmpresaModified) {
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
		_hashCode = 29 * _hashCode + idEmergencia;
		_hashCode = 29 * _hashCode + (idEmergenciaModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idEmpresa;
		_hashCode = 29 * _hashCode + (idEmpresaModified ? 1 : 0);
		if (fechaInicio != null) {
			_hashCode = 29 * _hashCode + fechaInicio.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaInicioModified ? 1 : 0);
		if (fechaPago != null) {
			_hashCode = 29 * _hashCode + fechaPago.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaPagoModified ? 1 : 0);
		long temp_montoPagado = Double.doubleToLongBits(montoPagado);
		_hashCode = 29 * _hashCode + (int) (temp_montoPagado ^ (temp_montoPagado >>> 32));
		_hashCode = 29 * _hashCode + (montoPagadoNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (montoPagadoModified ? 1 : 0);
		_hashCode = 29 * _hashCode + creditosOcupados;
		_hashCode = 29 * _hashCode + (creditosOcupadosNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (creditosOcupadosModified ? 1 : 0);
		if (rfcEmpresa != null) {
			_hashCode = 29 * _hashCode + rfcEmpresa.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (rfcEmpresaModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return EmergenciaCreditoPk
	 */
	public EmergenciaCreditoPk createPk()
	{
		return new EmergenciaCreditoPk(idEmergencia);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.tsp.sct.dao.dto.EmergenciaCredito: " );
		ret.append( "idEmergencia=" + idEmergencia );
		ret.append( ", idEmpresa=" + idEmpresa );
		ret.append( ", fechaInicio=" + fechaInicio );
		ret.append( ", fechaPago=" + fechaPago );
		ret.append( ", montoPagado=" + montoPagado );
		ret.append( ", creditosOcupados=" + creditosOcupados );
		ret.append( ", rfcEmpresa=" + rfcEmpresa );
		return ret.toString();
	}

}
