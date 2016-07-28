/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.sesion;

/**
 *
 * @author leonardo
 */
public class DeduccionSesion {
    
    private int idDeduccion = -1;
    private int idNominaTipoDeduccion = -1;
    private String clave = "";
    private String descipcionConcepto = "";
    private String unidad = "SERVICIO";
    private double importeGravado = 0;
    private double importeExento = 0;
    private double cantidad =0;
    
    private String descripcionConceptoAlterna = null;

    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }    
    public int getIdDeduccion() {
        return idDeduccion;
    }
    public void setIdDeduccion(int idDeduccion) {
        this.idDeduccion = idDeduccion;
    }
    public int getIdNominaTipoDeduccion() {
        return idNominaTipoDeduccion;
    }
    public void setIdNominaTipoDeduccion(int idNominaTipoDeduccion) {
        this.idNominaTipoDeduccion = idNominaTipoDeduccion;
    }
    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
    public String getDescipcionConcepto() {
        return descipcionConcepto;
    }
    public void setDescipcionConcepto(String descipcionConcepto) {
        this.descipcionConcepto = descipcionConcepto;
    }
    public String getUnidad() {
        return unidad;
    }
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    public double getImporteExento() {
        return importeExento;
    }
    public void setImporteExento(double importeExento) {
        this.importeExento = importeExento;
    }
    public double getImporteGravado() {
        return importeGravado;
    }
    public void setImporteGravado(double importeGravado) {
        this.importeGravado = importeGravado;
    }
    public String getDescripcionConceptoAlterna() {
        return descripcionConceptoAlterna;
    }
    public void setDescripcionConceptoAlterna(String descripcionConceptoAlterna) {
        this.descripcionConceptoAlterna = descripcionConceptoAlterna;
    }   
    
}
