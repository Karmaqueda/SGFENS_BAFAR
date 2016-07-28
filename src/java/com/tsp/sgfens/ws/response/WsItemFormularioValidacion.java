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
public class WsItemFormularioValidacion implements Serializable {
    
    protected int idFormularioValidacion;
    protected String nombre;
    protected String descripcion;
    protected String regexJava;
    protected String regexLenguajeExt;
    protected int isCreadoSistema;
    protected int idEmpresa;
    protected int idEstatus;

    /**
     * @return the idFormularioValidacion
     */
    public int getIdFormularioValidacion() {
        return idFormularioValidacion;
    }

    /**
     * @param idFormularioValidacion the idFormularioValidacion to set
     */
    public void setIdFormularioValidacion(int idFormularioValidacion) {
        this.idFormularioValidacion = idFormularioValidacion;
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
     * @return the regexJava
     */
    public String getRegexJava() {
        return regexJava;
    }

    /**
     * @param regexJava the regexJava to set
     */
    public void setRegexJava(String regexJava) {
        this.regexJava = regexJava;
    }

    /**
     * @return the regexLenguajeExt
     */
    public String getRegexLenguajeExt() {
        return regexLenguajeExt;
    }

    /**
     * @param regexLenguajeExt the regexLenguajeExt to set
     */
    public void setRegexLenguajeExt(String regexLenguajeExt) {
        this.regexLenguajeExt = regexLenguajeExt;
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
    
}
