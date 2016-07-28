/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.reports.dto;

/**
 *
 * @author HpPyme
 */
public class TablaContado {
    //Reuso este objeto para todas las tablas
    //solo seter datos que se van a usar
    
    private int numeroSecuencial;
    private String folioPedido;
    private String serieFactura;
    private String folioFactura;
    private String nombreCliente;
    private double monto;
    private String nombreBanco;
    private String numeroReferencia;
    private String producto;//Cambios y dev
    private double piezas;//Cambios y dev
    private double cantidad;//Cambios y dev
    private String txtLibre;
    

    public int getNumeroSecuencial() {
        return numeroSecuencial;
    }

    public void setNumeroSecuencial(int numeroSecuencial) {
        this.numeroSecuencial = numeroSecuencial;
    }

    public String getSerieFactura() {
        return serieFactura;
    }

    public void setSerieFactura(String serieFactura) {
        this.serieFactura = serieFactura;
    }

    public String getFolioFactura() {
        return folioFactura;
    }

    public void setFolioFactura(String folioFactura) {
        this.folioFactura = folioFactura;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFolioPedido() {
        return folioPedido;
    }

    public void setFolioPedido(String folioPedido) {
        this.folioPedido = folioPedido;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public double getPiezas() {
        return piezas;
    }

    public void setPiezas(double piezas) {
        this.piezas = piezas;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getTxtLibre() {
        return txtLibre;
    }

    public void setTxtLibre(String txtLibre) {
        this.txtLibre = txtLibre;
    }
    
        
    
}
