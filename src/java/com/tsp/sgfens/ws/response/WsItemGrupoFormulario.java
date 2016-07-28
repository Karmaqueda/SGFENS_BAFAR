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
public class WsItemGrupoFormulario implements Serializable {
    
    protected int idGrupoFormulario;
    protected String nombre;
    protected String descripcion;
    protected Date fechaHrCreacion;
    protected int idEstatus;
    protected int idEmpresa;
    protected List<WsItemFormulario> listaWsItemFormulario;//wsItemFormulario;
    protected List<WsItemFormularioEvento> wsItemFormularioEvento;

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
     * @return the wsItemFormulario
     */
//    public List<WsItemFormulario> getWsItemFormulario() {
//        if(wsItemFormulario == null)
//            wsItemFormulario = new ArrayList<WsItemFormulario>();
//        return wsItemFormulario;
//    }
//
//    /**
//     * @param wsItemFormulario the wsItemFormulario to set
//     */
//    public void setWsItemFormulario(List<WsItemFormulario> wsItemFormulario) {
//        this.wsItemFormulario = wsItemFormulario;
//    }

    /**
     * @return the wsItemFormularioEvento
     */
    public List<WsItemFormularioEvento> getWsItemFormularioEvento() {
        if(wsItemFormularioEvento == null)
            wsItemFormularioEvento = new ArrayList<WsItemFormularioEvento>();
        return wsItemFormularioEvento;
    }

    /**
     * @param wsItemFormularioEvento the wsItemFormularioEvento to set
     */
    public void setWsItemFormularioEvento(List<WsItemFormularioEvento> wsItemFormularioEvento) {
        this.wsItemFormularioEvento = wsItemFormularioEvento;
    }

    public List<WsItemFormulario> getListaWsItemFormulario() {
        if (listaWsItemFormulario==null)
            listaWsItemFormulario = new ArrayList<WsItemFormulario>();
        return listaWsItemFormulario;
    }

    public void setListaWsItemFormulario(List<WsItemFormulario> listaWsItemFormulario) {
        this.listaWsItemFormulario = listaWsItemFormulario;
    }
    
}
