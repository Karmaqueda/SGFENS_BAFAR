/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author MOVILPYME
 */
public class WsItemPaqueteConcepto implements Serializable{
    
    protected int idPaqueteConcepto;
    protected int idEstatus;
    protected int idPaquete;
    protected int idConcepto;
    protected double cantidad;
    protected double precio;

    /**
     * @return the idPaqueteConcepto
     */
    public int getIdPaqueteConcepto() {
        return idPaqueteConcepto;
    }

    /**
     * @param idPaqueteConcepto the idPaqueteConcepto to set
     */
    public void setIdPaqueteConcepto(int idPaqueteConcepto) {
        this.idPaqueteConcepto = idPaqueteConcepto;
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
     * @return the idConcepto
     */
    public int getIdConcepto() {
        return idConcepto;
    }

    /**
     * @param idConcepto the idConcepto to set
     */
    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }

    /**
     * @return the cantidad
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    
}
