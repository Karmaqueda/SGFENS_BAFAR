/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemTipoCampo implements Serializable {
    
    protected int idTipoCampo;
    protected String nombre;
    protected String descripcion;
    protected byte[] imgVistaPrevia;
    protected byte[] iconoNombre;
    protected int idEmpresa;
    protected int idEstatus;
    protected int isCreadoSistema;

    /**
     * @return the idTipoCampo
     */
    public int getIdTipoCampo() {
        return idTipoCampo;
    }

    /**
     * @param idTipoCampo the idTipoCampo to set
     */
    public void setIdTipoCampo(int idTipoCampo) {
        this.idTipoCampo = idTipoCampo;
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
     * @return the imgVistaPrevia
     */
    public byte[] getImgVistaPrevia() {
        return imgVistaPrevia;
    }

    /**
     * @param imgVistaPrevia the imgVistaPrevia to set
     */
    public void setImgVistaPrevia(byte[] imgVistaPrevia) {
        this.imgVistaPrevia = imgVistaPrevia;
    }

    /**
     * @return the iconoNombre
     */
    public byte[] getIconoNombre() {
        return iconoNombre;
    }

    /**
     * @param iconoNombre the iconoNombre to set
     */
    public void setIconoNombre(byte[] iconoNombre) {
        this.iconoNombre = iconoNombre;
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
     * @return the isCreadoSistema
     */
    public int getIsCreadoSistema() {
        return isCreadoSistema;
    }

    /**
     * @param isCreadoSistema the isCreadoSistema to set
     */
    public void setIsCreadoSistema(int isCreadoSistema) {
        this.isCreadoSistema = isCreadoSistema;
    }
}
