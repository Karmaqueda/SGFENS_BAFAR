/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class RegistrarAbonoRequest {
    
    private int idServerPedido;
    
    private double montoAbono;
    
    private WsDatosMetodoPagoRequest wsDatosMetodoPagoRequest;
     
    /**
     * Datos de la operación bancaria, en caso de no existir sera nulo.
     * 
     * Dentro del objeto, los datos idEmpresa e idEstatus no se llenan
     */
    private WsBancoOperacionRequest bancoOperacion;

    private boolean enviarCorreoReciboPago=false;
    
    private String[] listaCorreosDestinatarios= new String[0];
    
    private double latitud = 0;
    private double longitud = 0;
    
    private String folioCobranzaMovil=null;
    
    private Date fechaCobranza = null;
    
    private String referencia = null;

    public WsBancoOperacionRequest getBancoOperacion() {
        return bancoOperacion;
    }

    public void setBancoOperacion(WsBancoOperacionRequest bancoOperacion) {
        this.bancoOperacion = bancoOperacion;
    }

    public boolean isEnviarCorreoReciboPago() {
        return enviarCorreoReciboPago;
    }

    public void setEnviarCorreoReciboPago(boolean enviarCorreoReciboPago) {
        this.enviarCorreoReciboPago = enviarCorreoReciboPago;
    }

    public int getIdServerPedido() {
        return idServerPedido;
    }

    public void setIdServerPedido(int idServerPedido) {
        this.idServerPedido = idServerPedido;
    }

    public String[] getListaCorreosDestinatarios() {
        return listaCorreosDestinatarios;
    }

    public void setListaCorreosDestinatarios(String[] listaCorreosDestinatarios) {
        this.listaCorreosDestinatarios = listaCorreosDestinatarios;
    }

    public double getMontoAbono() {
        return montoAbono;
    }

    public void setMontoAbono(double montoAbono) {
        this.montoAbono = montoAbono;
    }

    public WsDatosMetodoPagoRequest getWsDatosMetodoPagoRequest() {
        return wsDatosMetodoPagoRequest;
    }

    public void setWsDatosMetodoPagoRequest(WsDatosMetodoPagoRequest wsDatosMetodoPagoRequest) {
        this.wsDatosMetodoPagoRequest = wsDatosMetodoPagoRequest;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFolioCobranzaMovil() {
        return folioCobranzaMovil;
    }

    public void setFolioCobranzaMovil(String folioCobranzaMovil) {
        this.folioCobranzaMovil = folioCobranzaMovil;
    }

    public Date getFechaCobranza() {
        return fechaCobranza;
    }

    public void setFechaCobranza(Date fechaCobranza) {
        this.fechaCobranza = fechaCobranza;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    
    
}
