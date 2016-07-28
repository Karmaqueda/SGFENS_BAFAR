/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemFormulario implements Serializable{
    
    protected int idFormulario;
    protected int idGrupoFormulario;
    protected int ordenGrupo;
    protected String nombre;
    protected String descripcion;
    protected Date fechaHrCreacion;
    protected Date fechaHrUltimaEdicion;
    protected int idEstatus;
    protected int idEmpresa;
    protected List<WsItemFormularioCampo> wsItemFormularioCampo;
    protected List<WsItemFormularioRespuesta> wsItemFormularioRespuesta;

    /**
     * @return the idFormulario
     */
    public int getIdFormulario() {
        return idFormulario;
    }

    /**
     * @param idFormulario the idFormulario to set
     */
    public void setIdFormulario(int idFormulario) {
        this.idFormulario = idFormulario;
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
     * @return the ordenGrupo
     */
    public int getOrdenGrupo() {
        return ordenGrupo;
    }

    /**
     * @param ordenGrupo the ordenGrupo to set
     */
    public void setOrdenGrupo(int ordenGrupo) {
        this.ordenGrupo = ordenGrupo;
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
     * @return the wsItemFormularioCampo
     */
    public List<WsItemFormularioCampo> getWsItemFormularioCampo() {
        if(wsItemFormularioCampo == null)
            wsItemFormularioCampo = new ArrayList<WsItemFormularioCampo>();
        return wsItemFormularioCampo;
    }

    /**
     * @param wsItemFormularioCampo the wsItemFormularioCampo to set
     */
    public void setWsItemFormularioCampo(List<WsItemFormularioCampo> wsItemFormularioCampo) {
        this.wsItemFormularioCampo = wsItemFormularioCampo;
    }

    /**
     * @return the wsItemFormularioRespuesta
     */
    public List<WsItemFormularioRespuesta> getWsItemFormularioRespuesta() {
        if(wsItemFormularioRespuesta == null)
            wsItemFormularioRespuesta = new ArrayList<WsItemFormularioRespuesta>();
        return wsItemFormularioRespuesta;
    }

    /**
     * @param wsItemFormularioRespuesta the wsItemFormularioRespuesta to set
     */
    public void setWsItemFormularioRespuesta(List<WsItemFormularioRespuesta> wsItemFormularioRespuesta) {
        this.wsItemFormularioRespuesta = wsItemFormularioRespuesta;
    }
    
}
