/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MOVILPYME
 */
public class WsItemPaquete implements Serializable{
    
    protected int idPaquete;
    protected int idEstatus;
    protected int idEmpresa;
    protected String nombre;
    protected String descripcion;
    protected List<WsItemPaqueteConcepto> wsItemPaqueteConcepto;

    /**
     * @return the idPaquete
     */
    public int getIdPaquete() {
        return idPaquete;
    }

    /**
     * @param idPaquete the idPaquete to set
     */
    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
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
     * @return the wsItemPaqueteConcepto
     */
    public List<WsItemPaqueteConcepto> getWsItemPaqueteConcepto() {
        if(wsItemPaqueteConcepto == null)
            wsItemPaqueteConcepto = new ArrayList<WsItemPaqueteConcepto>();
        return wsItemPaqueteConcepto;
    }

    /**
     * @param wsItemPaqueteConcepto the wsItemPaqueteConcepto to set
     */
    public void setWsItemPaqueteConcepto(List<WsItemPaqueteConcepto> wsItemPaqueteConcepto) {
        this.wsItemPaqueteConcepto = wsItemPaqueteConcepto;
    }
    
}
