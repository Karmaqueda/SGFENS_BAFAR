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

public class CxpComprobanteFiscal implements Serializable
{
	/** 
	 * This attribute maps to the column ID_CXP_COMPROBANTE_FISCAL in the cxp_comprobante_fiscal table.
	 */
	protected int idCxpComprobanteFiscal;

	/** 
	 * This attribute represents whether the attribute idCxpComprobanteFiscal has been modified since being read from the database.
	 */
	protected boolean idCxpComprobanteFiscalModified = false;

	/** 
	 * This attribute maps to the column ID_EMPRESA in the cxp_comprobante_fiscal table.
	 */
	protected int idEmpresa;

	/** 
	 * This attribute represents whether the attribute idEmpresa has been modified since being read from the database.
	 */
	protected boolean idEmpresaModified = false;

	/** 
	 * This attribute maps to the column ID_VALIDACION in the cxp_comprobante_fiscal table.
	 */
	protected int idValidacion;

	/** 
	 * This attribute represents whether the attribute idValidacion has been modified since being read from the database.
	 */
	protected boolean idValidacionModified = false;

	/** 
	 * This attribute maps to the column EMISOR_RFC in the cxp_comprobante_fiscal table.
	 */
	protected String emisorRfc;

	/** 
	 * This attribute represents whether the attribute emisorRfc has been modified since being read from the database.
	 */
	protected boolean emisorRfcModified = false;

	/** 
	 * This attribute maps to the column EMISOR_NOMBRE in the cxp_comprobante_fiscal table.
	 */
	protected String emisorNombre;

	/** 
	 * This attribute represents whether the attribute emisorNombre has been modified since being read from the database.
	 */
	protected boolean emisorNombreModified = false;

	/** 
	 * This attribute maps to the column SERIE in the cxp_comprobante_fiscal table.
	 */
	protected String serie;

	/** 
	 * This attribute represents whether the attribute serie has been modified since being read from the database.
	 */
	protected boolean serieModified = false;

	/** 
	 * This attribute maps to the column FOLIO in the cxp_comprobante_fiscal table.
	 */
	protected String folio;

	/** 
	 * This attribute represents whether the attribute folio has been modified since being read from the database.
	 */
	protected boolean folioModified = false;

	/** 
	 * This attribute maps to the column TOTAL in the cxp_comprobante_fiscal table.
	 */
	protected double total;

	/** 
	 * This attribute represents whether the attribute total has been modified since being read from the database.
	 */
	protected boolean totalModified = false;

	/** 
	 * This attribute maps to the column IMPORTE_PAGADO in the cxp_comprobante_fiscal table.
	 */
	protected double importePagado;

	/** 
	 * This attribute represents whether the attribute importePagado has been modified since being read from the database.
	 */
	protected boolean importePagadoModified = false;

	/** 
	 * This attribute maps to the column FECHA_HORA_CAPTURA in the cxp_comprobante_fiscal table.
	 */
	protected Date fechaHoraCaptura;

	/** 
	 * This attribute represents whether the attribute fechaHoraCaptura has been modified since being read from the database.
	 */
	protected boolean fechaHoraCapturaModified = false;

	/** 
	 * This attribute maps to the column FECHA_HORA_SELLO in the cxp_comprobante_fiscal table.
	 */
	protected Date fechaHoraSello;

	/** 
	 * This attribute represents whether the attribute fechaHoraSello has been modified since being read from the database.
	 */
	protected boolean fechaHoraSelloModified = false;

	/** 
	 * This attribute maps to the column FECHA_TENTATIVA_PAGO in the cxp_comprobante_fiscal table.
	 */
	protected Date fechaTentativaPago;

	/** 
	 * This attribute represents whether the attribute fechaTentativaPago has been modified since being read from the database.
	 */
	protected boolean fechaTentativaPagoModified = false;

	/** 
	 * This attribute maps to the column SELLO_EMISOR in the cxp_comprobante_fiscal table.
	 */
	protected String selloEmisor;

	/** 
	 * This attribute represents whether the attribute selloEmisor has been modified since being read from the database.
	 */
	protected boolean selloEmisorModified = false;

	/** 
	 * This attribute maps to the column CFDI_UUID in the cxp_comprobante_fiscal table.
	 */
	protected String cfdiUuid;

	/** 
	 * This attribute represents whether the attribute cfdiUuid has been modified since being read from the database.
	 */
	protected boolean cfdiUuidModified = false;

	/** 
	 * This attribute maps to the column ID_ESTATUS in the cxp_comprobante_fiscal table.
	 */
	protected int idEstatus;

	/** 
	 * This attribute represents whether the primitive attribute idEstatus is null.
	 */
	protected boolean idEstatusNull = true;

	/** 
	 * This attribute represents whether the attribute idEstatus has been modified since being read from the database.
	 */
	protected boolean idEstatusModified = false;

	/**
	 * Method 'CxpComprobanteFiscal'
	 * 
	 */
	public CxpComprobanteFiscal()
	{
	}

	/**
	 * Method 'getIdCxpComprobanteFiscal'
	 * 
	 * @return int
	 */
	public int getIdCxpComprobanteFiscal()
	{
		return idCxpComprobanteFiscal;
	}

	/**
	 * Method 'setIdCxpComprobanteFiscal'
	 * 
	 * @param idCxpComprobanteFiscal
	 */
	public void setIdCxpComprobanteFiscal(int idCxpComprobanteFiscal)
	{
		this.idCxpComprobanteFiscal = idCxpComprobanteFiscal;
		this.idCxpComprobanteFiscalModified = true;
	}

	/** 
	 * Sets the value of idCxpComprobanteFiscalModified
	 */
	public void setIdCxpComprobanteFiscalModified(boolean idCxpComprobanteFiscalModified)
	{
		this.idCxpComprobanteFiscalModified = idCxpComprobanteFiscalModified;
	}

	/** 
	 * Gets the value of idCxpComprobanteFiscalModified
	 */
	public boolean isIdCxpComprobanteFiscalModified()
	{
		return idCxpComprobanteFiscalModified;
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
	 * Method 'getIdValidacion'
	 * 
	 * @return int
	 */
	public int getIdValidacion()
	{
		return idValidacion;
	}

	/**
	 * Method 'setIdValidacion'
	 * 
	 * @param idValidacion
	 */
	public void setIdValidacion(int idValidacion)
	{
		this.idValidacion = idValidacion;
		this.idValidacionModified = true;
	}

	/** 
	 * Sets the value of idValidacionModified
	 */
	public void setIdValidacionModified(boolean idValidacionModified)
	{
		this.idValidacionModified = idValidacionModified;
	}

	/** 
	 * Gets the value of idValidacionModified
	 */
	public boolean isIdValidacionModified()
	{
		return idValidacionModified;
	}

	/**
	 * Method 'getEmisorRfc'
	 * 
	 * @return String
	 */
	public String getEmisorRfc()
	{
		return emisorRfc;
	}

	/**
	 * Method 'setEmisorRfc'
	 * 
	 * @param emisorRfc
	 */
	public void setEmisorRfc(String emisorRfc)
	{
		this.emisorRfc = emisorRfc;
		this.emisorRfcModified = true;
	}

	/** 
	 * Sets the value of emisorRfcModified
	 */
	public void setEmisorRfcModified(boolean emisorRfcModified)
	{
		this.emisorRfcModified = emisorRfcModified;
	}

	/** 
	 * Gets the value of emisorRfcModified
	 */
	public boolean isEmisorRfcModified()
	{
		return emisorRfcModified;
	}

	/**
	 * Method 'getEmisorNombre'
	 * 
	 * @return String
	 */
	public String getEmisorNombre()
	{
		return emisorNombre;
	}

	/**
	 * Method 'setEmisorNombre'
	 * 
	 * @param emisorNombre
	 */
	public void setEmisorNombre(String emisorNombre)
	{
		this.emisorNombre = emisorNombre;
		this.emisorNombreModified = true;
	}

	/** 
	 * Sets the value of emisorNombreModified
	 */
	public void setEmisorNombreModified(boolean emisorNombreModified)
	{
		this.emisorNombreModified = emisorNombreModified;
	}

	/** 
	 * Gets the value of emisorNombreModified
	 */
	public boolean isEmisorNombreModified()
	{
		return emisorNombreModified;
	}

	/**
	 * Method 'getSerie'
	 * 
	 * @return String
	 */
	public String getSerie()
	{
		return serie;
	}

	/**
	 * Method 'setSerie'
	 * 
	 * @param serie
	 */
	public void setSerie(String serie)
	{
		this.serie = serie;
		this.serieModified = true;
	}

	/** 
	 * Sets the value of serieModified
	 */
	public void setSerieModified(boolean serieModified)
	{
		this.serieModified = serieModified;
	}

	/** 
	 * Gets the value of serieModified
	 */
	public boolean isSerieModified()
	{
		return serieModified;
	}

	/**
	 * Method 'getFolio'
	 * 
	 * @return String
	 */
	public String getFolio()
	{
		return folio;
	}

	/**
	 * Method 'setFolio'
	 * 
	 * @param folio
	 */
	public void setFolio(String folio)
	{
		this.folio = folio;
		this.folioModified = true;
	}

	/** 
	 * Sets the value of folioModified
	 */
	public void setFolioModified(boolean folioModified)
	{
		this.folioModified = folioModified;
	}

	/** 
	 * Gets the value of folioModified
	 */
	public boolean isFolioModified()
	{
		return folioModified;
	}

	/**
	 * Method 'getTotal'
	 * 
	 * @return double
	 */
	public double getTotal()
	{
		return total;
	}

	/**
	 * Method 'setTotal'
	 * 
	 * @param total
	 */
	public void setTotal(double total)
	{
		this.total = total;
		this.totalModified = true;
	}

	/** 
	 * Sets the value of totalModified
	 */
	public void setTotalModified(boolean totalModified)
	{
		this.totalModified = totalModified;
	}

	/** 
	 * Gets the value of totalModified
	 */
	public boolean isTotalModified()
	{
		return totalModified;
	}

	/**
	 * Method 'getImportePagado'
	 * 
	 * @return double
	 */
	public double getImportePagado()
	{
		return importePagado;
	}

	/**
	 * Method 'setImportePagado'
	 * 
	 * @param importePagado
	 */
	public void setImportePagado(double importePagado)
	{
		this.importePagado = importePagado;
		this.importePagadoModified = true;
	}

	/** 
	 * Sets the value of importePagadoModified
	 */
	public void setImportePagadoModified(boolean importePagadoModified)
	{
		this.importePagadoModified = importePagadoModified;
	}

	/** 
	 * Gets the value of importePagadoModified
	 */
	public boolean isImportePagadoModified()
	{
		return importePagadoModified;
	}

	/**
	 * Method 'getFechaHoraCaptura'
	 * 
	 * @return Date
	 */
	public Date getFechaHoraCaptura()
	{
		return fechaHoraCaptura;
	}

	/**
	 * Method 'setFechaHoraCaptura'
	 * 
	 * @param fechaHoraCaptura
	 */
	public void setFechaHoraCaptura(Date fechaHoraCaptura)
	{
		this.fechaHoraCaptura = fechaHoraCaptura;
		this.fechaHoraCapturaModified = true;
	}

	/** 
	 * Sets the value of fechaHoraCapturaModified
	 */
	public void setFechaHoraCapturaModified(boolean fechaHoraCapturaModified)
	{
		this.fechaHoraCapturaModified = fechaHoraCapturaModified;
	}

	/** 
	 * Gets the value of fechaHoraCapturaModified
	 */
	public boolean isFechaHoraCapturaModified()
	{
		return fechaHoraCapturaModified;
	}

	/**
	 * Method 'getFechaHoraSello'
	 * 
	 * @return Date
	 */
	public Date getFechaHoraSello()
	{
		return fechaHoraSello;
	}

	/**
	 * Method 'setFechaHoraSello'
	 * 
	 * @param fechaHoraSello
	 */
	public void setFechaHoraSello(Date fechaHoraSello)
	{
		this.fechaHoraSello = fechaHoraSello;
		this.fechaHoraSelloModified = true;
	}

	/** 
	 * Sets the value of fechaHoraSelloModified
	 */
	public void setFechaHoraSelloModified(boolean fechaHoraSelloModified)
	{
		this.fechaHoraSelloModified = fechaHoraSelloModified;
	}

	/** 
	 * Gets the value of fechaHoraSelloModified
	 */
	public boolean isFechaHoraSelloModified()
	{
		return fechaHoraSelloModified;
	}

	/**
	 * Method 'getFechaTentativaPago'
	 * 
	 * @return Date
	 */
	public Date getFechaTentativaPago()
	{
		return fechaTentativaPago;
	}

	/**
	 * Method 'setFechaTentativaPago'
	 * 
	 * @param fechaTentativaPago
	 */
	public void setFechaTentativaPago(Date fechaTentativaPago)
	{
		this.fechaTentativaPago = fechaTentativaPago;
		this.fechaTentativaPagoModified = true;
	}

	/** 
	 * Sets the value of fechaTentativaPagoModified
	 */
	public void setFechaTentativaPagoModified(boolean fechaTentativaPagoModified)
	{
		this.fechaTentativaPagoModified = fechaTentativaPagoModified;
	}

	/** 
	 * Gets the value of fechaTentativaPagoModified
	 */
	public boolean isFechaTentativaPagoModified()
	{
		return fechaTentativaPagoModified;
	}

	/**
	 * Method 'getSelloEmisor'
	 * 
	 * @return String
	 */
	public String getSelloEmisor()
	{
		return selloEmisor;
	}

	/**
	 * Method 'setSelloEmisor'
	 * 
	 * @param selloEmisor
	 */
	public void setSelloEmisor(String selloEmisor)
	{
		this.selloEmisor = selloEmisor;
		this.selloEmisorModified = true;
	}

	/** 
	 * Sets the value of selloEmisorModified
	 */
	public void setSelloEmisorModified(boolean selloEmisorModified)
	{
		this.selloEmisorModified = selloEmisorModified;
	}

	/** 
	 * Gets the value of selloEmisorModified
	 */
	public boolean isSelloEmisorModified()
	{
		return selloEmisorModified;
	}

	/**
	 * Method 'getCfdiUuid'
	 * 
	 * @return String
	 */
	public String getCfdiUuid()
	{
		return cfdiUuid;
	}

	/**
	 * Method 'setCfdiUuid'
	 * 
	 * @param cfdiUuid
	 */
	public void setCfdiUuid(String cfdiUuid)
	{
		this.cfdiUuid = cfdiUuid;
		this.cfdiUuidModified = true;
	}

	/** 
	 * Sets the value of cfdiUuidModified
	 */
	public void setCfdiUuidModified(boolean cfdiUuidModified)
	{
		this.cfdiUuidModified = cfdiUuidModified;
	}

	/** 
	 * Gets the value of cfdiUuidModified
	 */
	public boolean isCfdiUuidModified()
	{
		return cfdiUuidModified;
	}

	/**
	 * Method 'getIdEstatus'
	 * 
	 * @return int
	 */
	public int getIdEstatus()
	{
		return idEstatus;
	}

	/**
	 * Method 'setIdEstatus'
	 * 
	 * @param idEstatus
	 */
	public void setIdEstatus(int idEstatus)
	{
		this.idEstatus = idEstatus;
		this.idEstatusNull = false;
		this.idEstatusModified = true;
	}

	/**
	 * Method 'setIdEstatusNull'
	 * 
	 * @param value
	 */
	public void setIdEstatusNull(boolean value)
	{
		this.idEstatusNull = value;
		this.idEstatusModified = true;
	}

	/**
	 * Method 'isIdEstatusNull'
	 * 
	 * @return boolean
	 */
	public boolean isIdEstatusNull()
	{
		return idEstatusNull;
	}

	/** 
	 * Sets the value of idEstatusModified
	 */
	public void setIdEstatusModified(boolean idEstatusModified)
	{
		this.idEstatusModified = idEstatusModified;
	}

	/** 
	 * Gets the value of idEstatusModified
	 */
	public boolean isIdEstatusModified()
	{
		return idEstatusModified;
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
		
		if (!(_other instanceof CxpComprobanteFiscal)) {
			return false;
		}
		
		final CxpComprobanteFiscal _cast = (CxpComprobanteFiscal) _other;
		if (idCxpComprobanteFiscal != _cast.idCxpComprobanteFiscal) {
			return false;
		}
		
		if (idCxpComprobanteFiscalModified != _cast.idCxpComprobanteFiscalModified) {
			return false;
		}
		
		if (idEmpresa != _cast.idEmpresa) {
			return false;
		}
		
		if (idEmpresaModified != _cast.idEmpresaModified) {
			return false;
		}
		
		if (idValidacion != _cast.idValidacion) {
			return false;
		}
		
		if (idValidacionModified != _cast.idValidacionModified) {
			return false;
		}
		
		if (emisorRfc == null ? _cast.emisorRfc != emisorRfc : !emisorRfc.equals( _cast.emisorRfc )) {
			return false;
		}
		
		if (emisorRfcModified != _cast.emisorRfcModified) {
			return false;
		}
		
		if (emisorNombre == null ? _cast.emisorNombre != emisorNombre : !emisorNombre.equals( _cast.emisorNombre )) {
			return false;
		}
		
		if (emisorNombreModified != _cast.emisorNombreModified) {
			return false;
		}
		
		if (serie == null ? _cast.serie != serie : !serie.equals( _cast.serie )) {
			return false;
		}
		
		if (serieModified != _cast.serieModified) {
			return false;
		}
		
		if (folio == null ? _cast.folio != folio : !folio.equals( _cast.folio )) {
			return false;
		}
		
		if (folioModified != _cast.folioModified) {
			return false;
		}
		
		if (total != _cast.total) {
			return false;
		}
		
		if (totalModified != _cast.totalModified) {
			return false;
		}
		
		if (importePagado != _cast.importePagado) {
			return false;
		}
		
		if (importePagadoModified != _cast.importePagadoModified) {
			return false;
		}
		
		if (fechaHoraCaptura == null ? _cast.fechaHoraCaptura != fechaHoraCaptura : !fechaHoraCaptura.equals( _cast.fechaHoraCaptura )) {
			return false;
		}
		
		if (fechaHoraCapturaModified != _cast.fechaHoraCapturaModified) {
			return false;
		}
		
		if (fechaHoraSello == null ? _cast.fechaHoraSello != fechaHoraSello : !fechaHoraSello.equals( _cast.fechaHoraSello )) {
			return false;
		}
		
		if (fechaHoraSelloModified != _cast.fechaHoraSelloModified) {
			return false;
		}
		
		if (fechaTentativaPago == null ? _cast.fechaTentativaPago != fechaTentativaPago : !fechaTentativaPago.equals( _cast.fechaTentativaPago )) {
			return false;
		}
		
		if (fechaTentativaPagoModified != _cast.fechaTentativaPagoModified) {
			return false;
		}
		
		if (selloEmisor == null ? _cast.selloEmisor != selloEmisor : !selloEmisor.equals( _cast.selloEmisor )) {
			return false;
		}
		
		if (selloEmisorModified != _cast.selloEmisorModified) {
			return false;
		}
		
		if (cfdiUuid == null ? _cast.cfdiUuid != cfdiUuid : !cfdiUuid.equals( _cast.cfdiUuid )) {
			return false;
		}
		
		if (cfdiUuidModified != _cast.cfdiUuidModified) {
			return false;
		}
		
		if (idEstatus != _cast.idEstatus) {
			return false;
		}
		
		if (idEstatusNull != _cast.idEstatusNull) {
			return false;
		}
		
		if (idEstatusModified != _cast.idEstatusModified) {
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
		_hashCode = 29 * _hashCode + (idCxpComprobanteFiscalModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idEmpresa;
		_hashCode = 29 * _hashCode + (idEmpresaModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idValidacion;
		_hashCode = 29 * _hashCode + (idValidacionModified ? 1 : 0);
		if (emisorRfc != null) {
			_hashCode = 29 * _hashCode + emisorRfc.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (emisorRfcModified ? 1 : 0);
		if (emisorNombre != null) {
			_hashCode = 29 * _hashCode + emisorNombre.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (emisorNombreModified ? 1 : 0);
		if (serie != null) {
			_hashCode = 29 * _hashCode + serie.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (serieModified ? 1 : 0);
		if (folio != null) {
			_hashCode = 29 * _hashCode + folio.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (folioModified ? 1 : 0);
		long temp_total = Double.doubleToLongBits(total);
		_hashCode = 29 * _hashCode + (int) (temp_total ^ (temp_total >>> 32));
		_hashCode = 29 * _hashCode + (totalModified ? 1 : 0);
		long temp_importePagado = Double.doubleToLongBits(importePagado);
		_hashCode = 29 * _hashCode + (int) (temp_importePagado ^ (temp_importePagado >>> 32));
		_hashCode = 29 * _hashCode + (importePagadoModified ? 1 : 0);
		if (fechaHoraCaptura != null) {
			_hashCode = 29 * _hashCode + fechaHoraCaptura.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaHoraCapturaModified ? 1 : 0);
		if (fechaHoraSello != null) {
			_hashCode = 29 * _hashCode + fechaHoraSello.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaHoraSelloModified ? 1 : 0);
		if (fechaTentativaPago != null) {
			_hashCode = 29 * _hashCode + fechaTentativaPago.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaTentativaPagoModified ? 1 : 0);
		if (selloEmisor != null) {
			_hashCode = 29 * _hashCode + selloEmisor.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (selloEmisorModified ? 1 : 0);
		if (cfdiUuid != null) {
			_hashCode = 29 * _hashCode + cfdiUuid.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (cfdiUuidModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idEstatus;
		_hashCode = 29 * _hashCode + (idEstatusNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (idEstatusModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return CxpComprobanteFiscalPk
	 */
	public CxpComprobanteFiscalPk createPk()
	{
		return new CxpComprobanteFiscalPk(idCxpComprobanteFiscal);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.tsp.sct.dao.dto.CxpComprobanteFiscal: " );
		ret.append( "idCxpComprobanteFiscal=" + idCxpComprobanteFiscal );
		ret.append( ", idEmpresa=" + idEmpresa );
		ret.append( ", idValidacion=" + idValidacion );
		ret.append( ", emisorRfc=" + emisorRfc );
		ret.append( ", emisorNombre=" + emisorNombre );
		ret.append( ", serie=" + serie );
		ret.append( ", folio=" + folio );
		ret.append( ", total=" + total );
		ret.append( ", importePagado=" + importePagado );
		ret.append( ", fechaHoraCaptura=" + fechaHoraCaptura );
		ret.append( ", fechaHoraSello=" + fechaHoraSello );
		ret.append( ", fechaTentativaPago=" + fechaTentativaPago );
		ret.append( ", selloEmisor=" + selloEmisor );
		ret.append( ", cfdiUuid=" + cfdiUuid );
		ret.append( ", idEstatus=" + idEstatus );
		return ret.toString();
	}

}
