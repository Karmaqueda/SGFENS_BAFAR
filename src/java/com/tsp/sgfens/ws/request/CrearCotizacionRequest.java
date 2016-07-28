/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author 578
 */
public class CrearCotizacionRequest {
    
    // Encabezado cotizacion    
    private int idClienteReceptor;
    private int idProspectoReceptor;
    private int estatus;
    
    private double total=0;        
    private double subtotal=0;
       
    private String folioCotizacionMovil;    
    
    private Date fechaCotizacion;
    
    //Productos cotizacion
    private WsItemConceptoRequest[] wsItemConceptoRequest;
    
    //Impuestos cotizacion
    private WsItemImpuestoRequest[] listaImpuestos;
    //Envio de correo
    private boolean enviarCorreoCotizacion=false;   
    private String[] listaCorreosDestinatarios= new String[0];
    
    

    public int getIdClienteReceptor() {
        return idClienteReceptor;
    }

    public void setIdClienteReceptor(int idClienteReceptor) {
        this.idClienteReceptor = idClienteReceptor;
    }

    public int getIdProspectoReceptor() {
        return idProspectoReceptor;
    }

    public void setIdProspectoReceptor(int idProspectoReceptor) {
        this.idProspectoReceptor = idProspectoReceptor;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }    

    public String getFolioCotizacionMovil() {
        return folioCotizacionMovil;
    }

    public void setFolioCotizacionMovil(String folioCotizacionMovil) {
        this.folioCotizacionMovil = folioCotizacionMovil;
    }

    public Date getFechaCotizacion() {
        return fechaCotizacion;
    }

    public void setFechaCotizacion(Date fechaCotizacion) {
        this.fechaCotizacion = fechaCotizacion;
    }

    public WsItemConceptoRequest[] getWsItemConceptoRequest() {
        return wsItemConceptoRequest;
    }

    public void setWsItemConceptoRequest(WsItemConceptoRequest[] wsItemConceptoRequest) {
        this.wsItemConceptoRequest = wsItemConceptoRequest;
    }

    public WsItemImpuestoRequest[] getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(WsItemImpuestoRequest[] listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public boolean isEnviarCorreoCotizacion() {
        return enviarCorreoCotizacion;
    }

    public void setEnviarCorreoCotizacion(boolean enviarCorreoCotizacion) {
        this.enviarCorreoCotizacion = enviarCorreoCotizacion;
    }   

    public String[] getListaCorreosDestinatarios() {
        return listaCorreosDestinatarios;
    }

    public void setListaCorreosDestinatarios(String[] listaCorreosDestinatarios) {
        this.listaCorreosDestinatarios = listaCorreosDestinatarios;
    }
    
    
    
    
    
    
}
