/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemPuntoInteresEvc {
    protected int idPunto;
    protected String nombre;
    protected String descripcion;
    protected String latitud;
    protected String longitud;
    protected int idTipoPunto;
    protected String direccion;

    /**
     * @return the idPunto
     */
    public int getIdPunto() {
        return idPunto;
    }

    /**
     * @param idPunto the idPunto to set
     */
    public void setIdPunto(int idPunto) {
        this.idPunto = idPunto;
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
     * @return the latitud
     */
    public String getLatitud() {
        return latitud;
    }

    /**
     * @param latitud the latitud to set
     */
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    /**
     * @return the longitud
     */
    public String getLongitud() {
        return longitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    /**
     * @return the idTipoPunto
     */
    public int getIdTipoPunto() {
        return idTipoPunto;
    }

    /**
     * @param idTipoPunto the idTipoPunto to set
     */
    public void setIdTipoPunto(int idTipoPunto) {
        this.idTipoPunto = idTipoPunto;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
