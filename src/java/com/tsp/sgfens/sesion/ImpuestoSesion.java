/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

/**
 *
 * @author ISCesarMartinez
 */
public class ImpuestoSesion {
    
    private int idImpuesto = -1;
    private String nombre = "";
    private String descripcion = "";
    private double porcentaje = 0;
    private boolean trasladado = false;
    private boolean implocal = false;
    private double monto = 0;
    
    private double montoImpuestoXConcepto = 0;

    public double getMontoImpuestoXConcepto() {
        return montoImpuestoXConcepto;
    }

    public void setMontoImpuestoXConcepto(double montoImpuestoXConcepto) {
        this.montoImpuestoXConcepto = montoImpuestoXConcepto;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }   

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

    public ImpuestoSesion() {
    }

   
    
}
