/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author ISCesar
 */
public class MovimientoSesion {
    
    private int idProducto=-1;
    private double cantidad = 0;
    private double precioVenta = 0;
    private double total = 0; //cantidad * precioVenta
    private String codigoBarras = "";
    private double cantidadValidacion = 0;

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public double getCantidadValidacion() {
        return cantidadValidacion;
    }

    public void setCantidadValidacion(double cantidadValidacion) {
        this.cantidadValidacion = cantidadValidacion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public BigDecimal calculaTotal(){
        BigDecimal totalBigD = BigDecimal.ZERO;
        
        BigDecimal cantidadBigD = new BigDecimal(getCantidad()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal precioVentaBigD = new BigDecimal(getPrecioVenta()).setScale(2, RoundingMode.HALF_UP);
            
        totalBigD = cantidadBigD.multiply(precioVentaBigD).setScale(2, RoundingMode.HALF_UP);;
        
        this.total = totalBigD.doubleValue();
        
        return totalBigD;
    }
    
}
