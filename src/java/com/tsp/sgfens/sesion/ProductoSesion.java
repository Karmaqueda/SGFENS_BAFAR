/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.dao.dto.Aduana;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class ProductoSesion {
    
    private int idProducto = -1;
    private double cantidad =0;
    private double precio =0;
    private double monto=0;
    private String unidad = "No Aplica";
    private int idImpuesto = -1;
    
    private double descuento_tasa=0;
    
    private double cantidadEntregada = 0;
    private Date fechaEntrega = new Date();
    private int estatus = 0;
    private int idAlmacen = 0;
    
    //variables auxililares para el control de ID, peso y cantidad para los productos a granel
    private double cantidadPeso = 0;
    private double cantidadEntregadaPeso = 0;
    private int idInventarioEmpleado = 0;
    
    private List<Aduana> aduanas = new ArrayList<Aduana>();  
    
    public int getIdInventarioEmpleado() {
        return idInventarioEmpleado;
    }

    public void setIdInventarioEmpleado(int idInventarioEmpleado) {
        this.idInventarioEmpleado = idInventarioEmpleado;
    }

    public double getCantidadPeso() {
        return cantidadPeso;
    }

    public void setCantidadPeso(double cantidadPeso) {
        this.cantidadPeso = cantidadPeso;
    }

    public double getCantidadEntregadaPeso() {
        return cantidadEntregadaPeso;
    }

    public void setCantidadEntregadaPeso(double cantidadEntregadaPeso) {
        this.cantidadEntregadaPeso = cantidadEntregadaPeso;
    }
    
    /**
     * En caso de requerir una descripcion diferente 
     * a la almacenada en base de datos
     */
    private String descripcionAlternativa = null;

    public ProductoSesion() {
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(int idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getDescuento_tasa() {
        return descuento_tasa;
    }

    public void setDescuento_tasa(double descuento_tasa) {
        this.descuento_tasa = descuento_tasa;
    }

    public String getDescripcionAlternativa() {
        return descripcionAlternativa;
    }

    /**
     * En caso de requerir una descripcion diferente 
     * a la almacenada en base de datos.
     * @param descripcionAlternativa  String
     */
    public void setDescripcionAlternativa(String descripcionAlternativa) {
        this.descripcionAlternativa = descripcionAlternativa;
    }

    public double getCantidadEntregada() {
        return cantidadEntregada;
    }

    public void setCantidadEntregada(double cantidadEntregada) {
        this.cantidadEntregada = cantidadEntregada;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    /**
     * @return the aduanas
     */
    public List<Aduana> getAduanas() {
        if (aduanas==null)
            aduanas = new ArrayList<Aduana>();
        return aduanas;
    }

    /**
     * @param aduanas the aduanas to set
     */
    public void setAduanas(List<Aduana> aduanas) {
        this.aduanas = aduanas;
    }
    
    
    
    
}
