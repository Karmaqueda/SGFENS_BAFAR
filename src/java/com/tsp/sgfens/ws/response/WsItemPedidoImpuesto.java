/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 7/10/2014 01:36:38 PM
 */
public class WsItemPedidoImpuesto implements Serializable{

    private int idImpuesto;
    private String nombre;
    private String descripcion;
    private double porcentaje;
    private int traslado;
    private int idEstatus;
    private int impuestoLocal;

    public int getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(int idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getTraslado() {
        return traslado;
    }

    public void setTraslado(int traslado) {
        this.traslado = traslado;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public int getImpuestoLocal() {
        return impuestoLocal;
    }

    public void setImpuestoLocal(int impuestoLocal) {
        this.impuestoLocal = impuestoLocal;
    }
    
    
    
}
