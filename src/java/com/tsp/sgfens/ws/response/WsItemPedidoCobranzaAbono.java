/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 7/10/2014 01:41:23 PM
 */
public class WsItemPedidoCobranzaAbono implements Serializable{
    
    private int idCobranzaAbono;
    private Date fechaAbono;
    private double montoAbono;
    private int idEstatus;
    private int idCobranzaMetodoPago;
    private String identificadorOperacion;
    private String comentarios;
    private double latitud;
    private double longitud;
    private String folioCobranzaMovil;
    private WsItemBancoOperacion wsItemBancoOperacion = null;
    
    public WsItemBancoOperacion getWsItemBancoOperacion() {
        return wsItemBancoOperacion;
    }

    public void setWsItemBancoOperacion(WsItemBancoOperacion wsItemBancoOperacion) {
        this.wsItemBancoOperacion = wsItemBancoOperacion;
    }

    public int getIdCobranzaAbono() {
        return idCobranzaAbono;
    }

    public void setIdCobranzaAbono(int idCobranzaAbono) {
        this.idCobranzaAbono = idCobranzaAbono;
    }

    public Date getFechaAbono() {
        return fechaAbono;
    }

    public void setFechaAbono(Date fechaAbono) {
        this.fechaAbono = fechaAbono;
    }

    public double getMontoAbono() {
        return montoAbono;
    }

    public void setMontoAbono(double montoAbono) {
        this.montoAbono = montoAbono;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public int getIdCobranzaMetodoPago() {
        return idCobranzaMetodoPago;
    }

    public void setIdCobranzaMetodoPago(int idCobranzaMetodoPago) {
        this.idCobranzaMetodoPago = idCobranzaMetodoPago;
    }

    public String getIdentificadorOperacion() {
        return identificadorOperacion;
    }

    public void setIdentificadorOperacion(String identificadorOperacion) {
        this.identificadorOperacion = identificadorOperacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
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

}
