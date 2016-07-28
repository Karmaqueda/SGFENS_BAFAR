/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesarMartinez
 */
public class WsItemImpuestoRequest {
    
    private int idImpuesto = -1;
    private String nombre = "";
    private String descripcion = "";
    private double porcentaje = 0;
    private boolean trasladado = false;
    private boolean implocal = false;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(int idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public boolean isImplocal() {
        return implocal;
    }

    public void setImplocal(boolean implocal) {
        this.implocal = implocal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public boolean isTrasladado() {
        return trasladado;
    }

    public void setTrasladado(boolean trasladado) {
        this.trasladado = trasladado;
    } 

    public WsItemImpuestoRequest() {
    }

   
    
}
