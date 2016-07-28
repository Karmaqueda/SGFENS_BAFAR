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
 * @author Ing. Roberto Trejo
 */
public class WsItemFormularioEvento implements Serializable {
    
    protected int idFormularioEvento;
    protected int idGrupoFormulario;
    protected Date fechaHrCreacion;
    protected Date fechaHrEdicion;
    protected int idUsuarioCapturo;
    protected String tipoEntidadRespondio;
    protected int idEntidadRespondio;
    protected int idEmpresa;
    protected int idEstatus;
    protected double latitud;
    protected double longitud;
    protected String folioMovil;
    protected WsItemCrFrmEventoSolicitud eventoSolicitud;

    /**
     * @return the idFormularioEvento
     */
    public int getIdFormularioEvento() {
        return idFormularioEvento;
    }

    /**
     * @param idFormularioEvento the idFormularioEvento to set
     */
    public void setIdFormularioEvento(int idFormularioEvento) {
        this.idFormularioEvento = idFormularioEvento;
    }

    /**
     * @return the idGrupoFormulario
     */
    public int getIdGrupoFormulario() {
        return idGrupoFormulario;
    }

    /**
     * @param idGrupoFormulario the idGrupoFormulario to set
     */
    public void setIdGrupoFormulario(int idGrupoFormulario) {
        this.idGrupoFormulario = idGrupoFormulario;
    }

    /**
     * @return the fechaHrCreacion
     */
    public Date getFechaHrCreacion() {
        return fechaHrCreacion;
    }

    /**
     * @param fechaHrCreacion the fechaHrCreacion to set
     */
    public void setFechaHrCreacion(Date fechaHrCreacion) {
        this.fechaHrCreacion = fechaHrCreacion;
    }

    /**
     * @return the fechaHrEdicion
     */
    public Date getFechaHrEdicion() {
        return fechaHrEdicion;
    }

    /**
     * @param fechaHrEdicion the fechaHrEdicion to set
     */
    public void setFechaHrEdicion(Date fechaHrEdicion) {
        this.fechaHrEdicion = fechaHrEdicion;
    }

    /**
     * @return the idUsuarioCapturo
     */
    public int getIdUsuarioCapturo() {
        return idUsuarioCapturo;
    }

    /**
     * @param idUsuarioCapturo the idUsuarioCapturo to set
     */
    public void setIdUsuarioCapturo(int idUsuarioCapturo) {
        this.idUsuarioCapturo = idUsuarioCapturo;
    }

    /**
     * @return the tipoEntidadRespondio
     */
    public String getTipoEntidadRespondio() {
        return tipoEntidadRespondio;
    }

    /**
     * @param tipoEntidadRespondio the tipoEntidadRespondio to set
     */
    public void setTipoEntidadRespondio(String tipoEntidadRespondio) {
        this.tipoEntidadRespondio = tipoEntidadRespondio;
    }

    /**
     * @return the idEntidadRespondio
     */
    public int getIdEntidadRespondio() {
        return idEntidadRespondio;
    }

    /**
     * @param idEntidadRespondio the idEntidadRespondio to set
     */
    public void setIdEntidadRespondio(int idEntidadRespondio) {
        this.idEntidadRespondio = idEntidadRespondio;
    }

    /**
     * @return the idEmpresa
     */
    public int getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * @param idEmpresa the idEmpresa to set
     */
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    /**
     * @return the idEstatus
     */
    public int getIdEstatus() {
        return idEstatus;
    }

    /**
     * @param idEstatus the idEstatus to set
     */
    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    /**
     * @return the latitud
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * @param latitud the latitud to set
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /**
     * @return the longitud
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /**
     * @return the folioMovil
     */
    public String getFolioMovil() {
        return folioMovil;
    }

    /**
     * @param folioMovil the folioMovil to set
     */
    public void setFolioMovil(String folioMovil) {
        this.folioMovil = folioMovil;
    }

    /**
     * @return the eventoSolicitud
     */
    public WsItemCrFrmEventoSolicitud getEventoSolicitud() {
        return eventoSolicitud;
    }

    /**
     * @param eventoSolicitud the eventoSolicitud to set
     */
    public void setEventoSolicitud(WsItemCrFrmEventoSolicitud eventoSolicitud) {
        this.eventoSolicitud = eventoSolicitud;
    }
    
}
