/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class WsItemCliente implements Serializable{
    
    private int idCliente;
    private String rfcCliente;
    private String nombreCliente;
    private String apellidoPaternoCliente;
    private String apellidoMaternoCliente;
    private String razonSocial;
    private String calle;
    private String numero;
    private String numeroInterior;
    private String colonia;
    private String codigoPostal;
    private String pais;
    private String estado;
    private String municipio;
    private String lada;
    private String telefono;
    private String extension;
    private String celular;
    private String correo;
    private String contacto;
    
    private String latitud;
    private String longitud;
    
    private String folioClienteMovil;
    
    private String diasVisita;
    private double saldoCliente;
    
    private Date fechaLimiteReasignacion;
    
    private int perioricidad;
    private Date fechaUltimaVisita;
    
    private String nombreComercial;
    private int permisoVentaCredito;
    
    private int idCategoria;
    private String categoria;
    
    private WsListaPreciosCliente listaPreciosEspeciales;
    private WsListaClienteCampoContenido listaCampoContenido;
    
    private Date fechaRegistro;
    
    protected int ventaConsigna;
    protected double comisionConsigna;
    
    private double creditoMontoMax;
    private int creditoDiasMax;

    public String getApellidoMaternoCliente() {
        return apellidoMaternoCliente;
    }

    public void setApellidoMaternoCliente(String apellidoMaternoCliente) {
        this.apellidoMaternoCliente = apellidoMaternoCliente;
    }

    public String getApellidoPaternoCliente() {
        return apellidoPaternoCliente;
    }

    public void setApellidoPaternoCliente(String apellidoPaternoCliente) {
        this.apellidoPaternoCliente = apellidoPaternoCliente;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getLada() {
        return lada;
    }

    public void setLada(String lada) {
        this.lada = lada;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRfcCliente() {
        return rfcCliente;
    }

    public void setRfcCliente(String rfcCliente) {
        this.rfcCliente = rfcCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    
    public String getFolioClienteMovil() {
        return folioClienteMovil;
    }

    public void setFolioClienteMovil(String folioClienteMovil) {
        this.folioClienteMovil = folioClienteMovil;
    }

    public String getDiasVisita() {
        return diasVisita;
    }

    public void setDiasVisita(String diasVisita) {
        this.diasVisita = diasVisita;
    }

    public double getSaldoCliente() {
        return saldoCliente;
    }

    public void setSaldoCliente(double saldoCliente) {
        this.saldoCliente = saldoCliente;
    }

    /**
     * @return the fechaLimiteReasignacion
     */
    public Date getFechaLimiteReasignacion() {
        return fechaLimiteReasignacion;
    }

    /**
     * @param fechaLimiteReasignacion the fechaLimiteReasignacion to set
     */
    public void setFechaLimiteReasignacion(Date fechaLimiteReasignacion) {
        this.fechaLimiteReasignacion = fechaLimiteReasignacion;
    }

    /**
     * @return the perioricidad
     */
    public int getPerioricidad() {
        return perioricidad;
    }

    /**
     * @param perioricidad the perioricidad to set
     */
    public void setPerioricidad(int perioricidad) {
        this.perioricidad = perioricidad;
    }

    /**
     * @return the fechaUltimaVisita
     */
    public Date getFechaUltimaVisita() {
        return fechaUltimaVisita;
    }

    /**
     * @param fechaUltimaVisita the fechaUltimaVisita to set
     */
    public void setFechaUltimaVisita(Date fechaUltimaVisita) {
        this.fechaUltimaVisita = fechaUltimaVisita;
    }

    /**
     * @return the nombreComercial
     */
    public String getNombreComercial() {
        return nombreComercial;
    }

    /**
     * @param nombreComercial the nombreComercial to set
     */
    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    /**
     * @return the permisoVentaCredito
     */
    public int getPermisoVentaCredito() {
        return permisoVentaCredito;
    }

    /**
     * @param permisoVentaCredito the permisoVentaCredito to set
     */
    public void setPermisoVentaCredito(int permisoVentaCredito) {
        this.permisoVentaCredito = permisoVentaCredito;
    }

    /*
     * Precios Especiales Cliente
     */
    public WsListaPreciosCliente getListaPreciosEspeciales() {
        return listaPreciosEspeciales;
    }

    public void setListaPreciosEspeciales(WsListaPreciosCliente listaPreciosEspeciales) {
        this.listaPreciosEspeciales = listaPreciosEspeciales;
    }

    /**
     * @return the idCategoria
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * @param idCategoria the idCategoria to set
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * @return the categoria
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the listaCampoContenido
     */
    public WsListaClienteCampoContenido getListaCampoContenido() {
        return listaCampoContenido;
    }

    /**
     * @param listaCampoContenido the listaCampoContenido to set
     */
    public void setListaCampoContenido(WsListaClienteCampoContenido listaCampoContenido) {
        this.listaCampoContenido = listaCampoContenido;
    }
    
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * @return the ventaConsigna
     */
    public int getVentaConsigna() {
        return ventaConsigna;
    }

    /**
     * @param ventaConsigna the ventaConsigna to set
     */
    public void setVentaConsigna(int ventaConsigna) {
        this.ventaConsigna = ventaConsigna;
    }

    /**
     * @return the comisionConsigna
     */
    public double getComisionConsigna() {
        return comisionConsigna;
    }

    /**
     * @param comisionConsigna the comisionConsigna to set
     */
    public void setComisionConsigna(double comisionConsigna) {
        this.comisionConsigna = comisionConsigna;
    }

    public double getCreditoMontoMax() {
        return creditoMontoMax;
    }

    public void setCreditoMontoMax(double creditoMontoMax) {
        this.creditoMontoMax = creditoMontoMax;
    }

    public int getCreditoDiasMax() {
        return creditoDiasMax;
    }

    public void setCreditoDiasMax(int creditoDiasMax) {
        this.creditoDiasMax = creditoDiasMax;
    }
    
}