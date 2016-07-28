/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class CrearCrFormularioEventoRequest {
    
    private int idGrupoFormulario;
    private Date fechaHrCreacion;
    private double latitud;
    private double longitud;
    private String folioMovil;
    protected int idEntidad;
    
    protected WsItemFrmEventoSolicitudRequest eventoSolicitud;
    
    private WsItemCrFormularioRespuestaRequest[] listaCrFormularioRespuestaRequest;

    public int getIdGrupoFormulario() {
        return idGrupoFormulario;
    }

    public void setIdGrupoFormulario(int idGrupoFormulario) {
        this.idGrupoFormulario = idGrupoFormulario;
    }

    public Date getFechaHrCreacion() {
        return fechaHrCreacion;
    }

    public void setFechaHrCreacion(Date fechaHrCreacion) {
        this.fechaHrCreacion = fechaHrCreacion;
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

    public String getFolioMovil() {
        return folioMovil;
    }

    public void setFolioMovil(String folioMovil) {
        this.folioMovil = folioMovil;
    }

    public WsItemCrFormularioRespuestaRequest[] getListaCrFormularioRespuestaRequest() {
        return listaCrFormularioRespuestaRequest;
    }

    public void setListaCrFormularioRespuestaRequest(WsItemCrFormularioRespuestaRequest[] listaCrFormularioRespuestaRequest) {
        this.listaCrFormularioRespuestaRequest = listaCrFormularioRespuestaRequest;
    }

    /**
     * @return the eventoSolicitud
     */
    public WsItemFrmEventoSolicitudRequest getEventoSolicitud() {
        return eventoSolicitud;
    }

    /**
     * @param eventoSolicitud the eventoSolicitud to set
     */
    public void setEventoSolicitud(WsItemFrmEventoSolicitudRequest eventoSolicitud) {
        this.eventoSolicitud = eventoSolicitud;
    }

    /**
     * @return the idEntidad
     */
    public int getIdEntidad() {
        return idEntidad;
    }

    /**
     * @param idEntidad the idEntidad to set
     */
    public void setIdEntidad(int idEntidad) {
        this.idEntidad = idEntidad;
    }
    
}
