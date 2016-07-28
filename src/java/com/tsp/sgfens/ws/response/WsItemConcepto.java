/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesarMartinez
 */
public class WsItemConcepto implements Serializable {
    
    private int idConcepto;
    private String nombre;
    private String descripcion;
    private double precio;
    private String identificacion;
    private int idCategoria;
    private double stock; 
    private int idAlmacen;
    private double stockAlmacen;
    private String marca;
    private double precioCompra;
    private double precioDocena;
    private double precioMedioMayoreo;
    private double precioMayoreo;
    private double precioEspecial;
    private double maxMenudeo;
    private double minMedioMayoreo;
    private double maxMedioMayoreo;
    private double minMayoreo;
    protected double descuentoPorcentaje;
    protected double descuentoMonto;
    private double precioMinimoVenta;
    private byte[] imagen;
    private byte[] video;
    private String caracteristicas;
    
    private double montoComision;
    private double porcentajeComision;
    private String clave;
    private double desglose_piezas;
    
    private String folioConceptoMovil;
    
    private double precioUnitarioGranel;
    private double precioMedioGranel;
    private double precioMayoreoGranel;
    private double precioEspecialGranel;
    
    private double pesoProducto;
    private double pesoStock;
    
    private int idInventarioEmpleado = 0;

    public int getIdInventarioEmpleado() {
        return idInventarioEmpleado;
    }

    public void setIdInventarioEmpleado(int idInventarioEmpleado) {
        this.idInventarioEmpleado = idInventarioEmpleado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public double getStockAlmacen() {
        return stockAlmacen;
    }

    public void setStockAlmacen(double stockAlmacen) {
        this.stockAlmacen = stockAlmacen;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioDocena() {
        return precioDocena;
    }

    public void setPrecioDocena(double precioDocena) {
        this.precioDocena = precioDocena;
    }

    public double getPrecioMedioMayoreo() {
        return precioMedioMayoreo;
    }

    public void setPrecioMedioMayoreo(double precioMedioMayoreo) {
        this.precioMedioMayoreo = precioMedioMayoreo;
    }

    public double getPrecioMayoreo() {
        return precioMayoreo;
    }

    public void setPrecioMayoreo(double precioMayoreo) {
        this.precioMayoreo = precioMayoreo;
    }

    public double getMaxMenudeo() {
        return maxMenudeo;
    }

    public void setMaxMenudeo(double maxMenudeo) {
        this.maxMenudeo = maxMenudeo;
    }

    public double getMinMedioMayoreo() {
        return minMedioMayoreo;
    }

    public void setMinMedioMayoreo(double minMedioMayoreo) {
        this.minMedioMayoreo = minMedioMayoreo;
    }

    public double getMaxMedioMayoreo() {
        return maxMedioMayoreo;
    }

    public void setMaxMedioMayoreo(double maxMedioMayoreo) {
        this.maxMedioMayoreo = maxMedioMayoreo;
    }

    public double getMinMayoreo() {
        return minMayoreo;
    }

    public void setMinMayoreo(double minMayoreo) {
        this.minMayoreo = minMayoreo;
    }

    public double getPrecioEspecial() {
        return precioEspecial;
    }

    public void setPrecioEspecial(double precioEspecial) {
        this.precioEspecial = precioEspecial;
    }

    /**
     * @return the descuentoPorcentaje
     */
    public double getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    /**
     * @param descuentoPorcentaje the descuentoPorcentaje to set
     */
    public void setDescuentoPorcentaje(double descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    /**
     * @return the descuentoMonto
     */
    public double getDescuentoMonto() {
        return descuentoMonto;
    }

    /**
     * @param descuentoMonto the descuentoMonto to set
     */
    public void setDescuentoMonto(double descuentoMonto) {
        this.descuentoMonto = descuentoMonto;
    }

    /**
     * @return the precioMinimoVenta
     */
    public double getPrecioMinimoVenta() {
        return precioMinimoVenta;
    }

    /**
     * @param precioMinimoVenta the precioMinimoVenta to set
     */
    public void setPrecioMinimoVenta(double precioMinimoVenta) {
        this.precioMinimoVenta = precioMinimoVenta;
    }

    /**
     * @return the imagen
     */
    public byte[] getImagen() {
        return imagen;
    }

    /**
     * @param imagen the imagen to set
     */
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    /**
     * @return the video
     */
    public byte[] getVideo() {
        return video;
    }

    /**
     * @param video the video to set
     */
    public void setVideo(byte[] video) {
        this.video = video;
    }

    /**
     * @return the caracteristicas
     */
    public String getCaracteristicas() {
        return caracteristicas;
    }

    /**
     * @param caracteristicas the caracteristicas to set
     */
    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    /**
     * @return the montoComision
     */
    public double getMontoComision() {
        return montoComision;
    }

    /**
     * @param montoComision the montoComision to set
     */
    public void setMontoComision(double montoComision) {
        this.montoComision = montoComision;
    }

    /**
     * @return the porcentajeComision
     */
    public double getPorcentajeComision() {
        return porcentajeComision;
    }

    /**
     * @param porcentajeComision the porcentajeComision to set
     */
    public void setPorcentajeComision(double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public double getDesglose_piezas() {
        return desglose_piezas;
    }

    public void setDesglose_piezas(double desglose_piezas) {
        this.desglose_piezas = desglose_piezas;
    }

    public String getFolioConceptoMovil() {
        return folioConceptoMovil;
    }

    public void setFolioConceptoMovil(String folioConceptoMovil) {
        this.folioConceptoMovil = folioConceptoMovil;
    }

    /**
     * @return the precioUnitarioGranel
     */
    public double getPrecioUnitarioGranel() {
        return precioUnitarioGranel;
    }

    /**
     * @param precioUnitarioGranel the precioUnitarioGranel to set
     */
    public void setPrecioUnitarioGranel(double precioUnitarioGranel) {
        this.precioUnitarioGranel = precioUnitarioGranel;
    }

    /**
     * @return the precioMedioGranel
     */
    public double getPrecioMedioGranel() {
        return precioMedioGranel;
    }

    /**
     * @param precioMedioGranel the precioMedioGranel to set
     */
    public void setPrecioMedioGranel(double precioMedioGranel) {
        this.precioMedioGranel = precioMedioGranel;
    }

    /**
     * @return the precioMayoreoGranel
     */
    public double getPrecioMayoreoGranel() {
        return precioMayoreoGranel;
    }

    /**
     * @param precioMayoreoGranel the precioMayoreoGranel to set
     */
    public void setPrecioMayoreoGranel(double precioMayoreoGranel) {
        this.precioMayoreoGranel = precioMayoreoGranel;
    }

    /**
     * @return the precioEspecialGranel
     */
    public double getPrecioEspecialGranel() {
        return precioEspecialGranel;
    }

    /**
     * @param precioEspecialGranel the precioEspecialGranel to set
     */
    public void setPrecioEspecialGranel(double precioEspecialGranel) {
        this.precioEspecialGranel = precioEspecialGranel;
    }

    /**
     * @return the pesoProducto
     */
    public double getPesoProducto() {
        return pesoProducto;
    }

    /**
     * @param pesoProducto the pesoProducto to set
     */
    public void setPesoProducto(double pesoProducto) {
        this.pesoProducto = pesoProducto;
    }

    /**
     * @return the pesoStock
     */
    public double getPesoStock() {
        return pesoStock;
    }

    /**
     * @param pesoStock the pesoStock to set
     */
    public void setPesoStock(double pesoStock) {
        this.pesoStock = pesoStock;
    }
        
    
    
}
