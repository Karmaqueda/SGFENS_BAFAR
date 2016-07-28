/*
 * To change this template; choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class CrearClienteRequest {

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
    
    private int perioricidad;
    private Date fechaUltimaVisita;
    
    private String nombreComercial;
    private int permisoVentaCredito;
    
    private int idCategoria;
    
    private List<WsItemClienteCampoContenidoRequest> listaContenido;
    
    private Date fechaRegistro;
    
    
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

    public String getLada() {
        return lada;
    }

    public void setLada(String lada) {
        this.lada = lada;
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
    * @return the listaContenido
    */
    public List<WsItemClienteCampoContenidoRequest> getListaContenido() {
            if(listaContenido == null)
                    listaContenido = new ArrayList<WsItemClienteCampoContenidoRequest>();
            return listaContenido;
    }

    /**
     * @param listaContenido the listaContenido to set
     */
    public void setListaContenido(List<WsItemClienteCampoContenidoRequest> listaContenido) {
            this.listaContenido = listaContenido;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    
    
}
