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
public class ItemEstatusMensajeMovilRequest {
    
    /**
     * Variable para poder rastrear en la respuesta
     * que mensajes si fueron recibidos satisfactoriamente
     * por el servidor
     */
    private long rastreadorMensaje;
    private Date fechaHoraEstatus;
    private String mensajeEstatus;
    private double latitud;
    private double longitud;
    private String imagenEstatusBytesBase64;

    public Date getFechaHoraEstatus() {
        return fechaHoraEstatus;
    }

    public void setFechaHoraEstatus(Date fechaHoraEstatus) {
        this.fechaHoraEstatus = fechaHoraEstatus;
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

    public String getMensajeEstatus() {
        return mensajeEstatus;
    }

    public void setMensajeEstatus(String mensajeEstatus) {
        this.mensajeEstatus = mensajeEstatus;
    }

    public long getRastreadorMensaje() {
        return rastreadorMensaje;
    }

    public void setRastreadorMensaje(long rastreadorMensaje) {
        this.rastreadorMensaje = rastreadorMensaje;
    }

    public String getImagenEstatusBytesBase64() {
        return imagenEstatusBytesBase64;
    }

    public void setImagenEstatusBytesBase64(String imagenEstatusBytesBase64) {
        this.imagenEstatusBytesBase64 = imagenEstatusBytesBase64;
    }
    
}
