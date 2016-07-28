/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemScore {
    
    protected int idScore;
    protected String nombre;
    protected String descripcion;
    protected Date fechaHrCreacion;
    protected Date fechaHrUltimaEdicion;
    protected int idUsuarioEdicion;
    protected int idEmpresa;
    protected int idEstatus;
    protected List<WsItemScoreDetalle> wsItemScore;

    /**
     * @return the idScore
     */
    public int getIdScore() {
        return idScore;
    }

    /**
     * @param idScore the idScore to set
     */
    public void setIdScore(int idScore) {
        this.idScore = idScore;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
     * @return the fechaHrUltimaEdicion
     */
    public Date getFechaHrUltimaEdicion() {
        return fechaHrUltimaEdicion;
    }

    /**
     * @param fechaHrUltimaEdicion the fechaHrUltimaEdicion to set
     */
    public void setFechaHrUltimaEdicion(Date fechaHrUltimaEdicion) {
        this.fechaHrUltimaEdicion = fechaHrUltimaEdicion;
    }

    /**
     * @return the idUsuarioEdicion
     */
    public int getIdUsuarioEdicion() {
        return idUsuarioEdicion;
    }

    /**
     * @param idUsuarioEdicion the idUsuarioEdicion to set
     */
    public void setIdUsuarioEdicion(int idUsuarioEdicion) {
        this.idUsuarioEdicion = idUsuarioEdicion;
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
     * @return the wsItemScore
     */
    public List<WsItemScoreDetalle> getWsItemScore() {
        if(wsItemScore == null)
            wsItemScore = new ArrayList<WsItemScoreDetalle>();
        return wsItemScore;
    }

    /**
     * @param wsItemScore the wsItemScore to set
     */
    public void setWsItemScore(List<WsItemScoreDetalle> wsItemScore) {
        this.wsItemScore = wsItemScore;
    }
    
}
