/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.util.Date;

/**
 *
 * @author leonardo
 */
public class WsItemProveedorConceptoServicio {
    
    private int idProveedorProducto;
    private int idProductoServicio;
    private int idProveedor;
    private double volumenDisponible;
    private Date caducidad;
    protected Date caducidadDate;
    private Date fechaDisponibilidad;
    protected Date fechaDisponibilidadDate;
    private String unidad;
    private double precioMedioMayoreo;
    private double minMedioMayoreo;
    private double maxMedioMayoreo;
    private double precioMenudeo;
    private double maxMenudeo;
    private double precioMayoreo;
    private double minMayoreo;
    private byte[] imagen;
    private int idCategoria;
    private String imagenProveedorProductoBytesBase64;

    /**
     * @return the idProveedorProducto
     */
    public int getIdProveedorProducto() {
        return idProveedorProducto;
    }

    /**
     * @param idProveedorProducto the idProveedorProducto to set
     */
    public void setIdProveedorProducto(int idProveedorProducto) {
        this.idProveedorProducto = idProveedorProducto;
    }

    /**
     * @return the idProductoServicio
     */
    public int getIdProductoServicio() {
        return idProductoServicio;
    }

    /**
     * @param idProductoServicio the idProductoServicio to set
     */
    public void setIdProductoServicio(int idProductoServicio) {
        this.idProductoServicio = idProductoServicio;
    }

    /**
     * @return the volumenDisponible
     */
    public double getVolumenDisponible() {
        return volumenDisponible;
    }

    /**
     * @param volumenDisponible the volumenDisponible to set
     */
    public void setVolumenDisponible(double volumenDisponible) {
        this.volumenDisponible = volumenDisponible;
    }

    /**
     * @return the caducidad
     */
    public Date getCaducidad() {
        return caducidad;
    }

    /**
     * @param caducidad the caducidad to set
     */
    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    /**
     * @return the fechaDisponibilidad
     */
    public Date getFechaDisponibilidad() {
        return fechaDisponibilidad;
    }

    /**
     * @param fechaDisponibilidad the fechaDisponibilidad to set
     */
    public void setFechaDisponibilidad(Date fechaDisponibilidad) {
        this.fechaDisponibilidad = fechaDisponibilidad;
    }

    /**
     * @return the unidad
     */
    public String getUnidad() {
        return unidad;
    }

    /**
     * @param unidad the unidad to set
     */
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    /**
     * @return the precioMedioMayoreo
     */
    public double getPrecioMedioMayoreo() {
        return precioMedioMayoreo;
    }

    /**
     * @param precioMedioMayoreo the precioMedioMayoreo to set
     */
    public void setPrecioMedioMayoreo(double precioMedioMayoreo) {
        this.precioMedioMayoreo = precioMedioMayoreo;
    }

    /**
     * @return the minMedioMayoreo
     */
    public double getMinMedioMayoreo() {
        return minMedioMayoreo;
    }

    /**
     * @param minMedioMayoreo the minMedioMayoreo to set
     */
    public void setMinMedioMayoreo(double minMedioMayoreo) {
        this.minMedioMayoreo = minMedioMayoreo;
    }

    /**
     * @return the maxMedioMayoreo
     */
    public double getMaxMedioMayoreo() {
        return maxMedioMayoreo;
    }

    /**
     * @param maxMedioMayoreo the maxMedioMayoreo to set
     */
    public void setMaxMedioMayoreo(double maxMedioMayoreo) {
        this.maxMedioMayoreo = maxMedioMayoreo;
    }

    /**
     * @return the precioMenudeo
     */
    public double getPrecioMenudeo() {
        return precioMenudeo;
    }

    /**
     * @param precioMenudeo the precioMenudeo to set
     */
    public void setPrecioMenudeo(double precioMenudeo) {
        this.precioMenudeo = precioMenudeo;
    }

    /**
     * @return the maxMenudeo
     */
    public double getMaxMenudeo() {
        return maxMenudeo;
    }

    /**
     * @param maxMenudeo the maxMenudeo to set
     */
    public void setMaxMenudeo(double maxMenudeo) {
        this.maxMenudeo = maxMenudeo;
    }

    /**
     * @return the precioMayoreo
     */
    public double getPrecioMayoreo() {
        return precioMayoreo;
    }

    /**
     * @param precioMayoreo the precioMayoreo to set
     */
    public void setPrecioMayoreo(double precioMayoreo) {
        this.precioMayoreo = precioMayoreo;
    }

    /**
     * @return the minMayoreo
     */
    public double getMinMayoreo() {
        return minMayoreo;
    }

    /**
     * @param minMayoreo the minMayoreo to set
     */
    public void setMinMayoreo(double minMayoreo) {
        this.minMayoreo = minMayoreo;
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
     * @return the idCategoria
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * @param idCategoria the idCategoria to set
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * @return the idProveedor
     */
    public int getIdProveedor() {
        return idProveedor;
    }

    /**
     * @param idProveedor the idProveedor to set
     */
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    /**
     * @return the imagenProveedorProductoBytesBase64
     */
    public String getImagenProveedorProductoBytesBase64() {
        return imagenProveedorProductoBytesBase64;
    }

    /**
     * @param imagenProveedorProductoBytesBase64 the imagenProveedorProductoBytesBase64 to set
     */
    public void setImagenProveedorProductoBytesBase64(String imagenProveedorProductoBytesBase64) {
        this.imagenProveedorProductoBytesBase64 = imagenProveedorProductoBytesBase64;
    }

    /**
     * @return the caducidadDate
     */
    public Date getCaducidadDate() {
        return caducidadDate;
    }

    /**
     * @param caducidadDate the caducidadDate to set
     */
    public void setCaducidadDate(Date caducidadDate) {
        this.caducidadDate = caducidadDate;
    }

    /**
     * @return the fechaDisponibilidadDate
     */
    public Date getFechaDisponibilidadDate() {
        return fechaDisponibilidadDate;
    }

    /**
     * @param fechaDisponibilidadDate the fechaDisponibilidadDate to set
     */
    public void setFechaDisponibilidadDate(Date fechaDisponibilidadDate) {
        this.fechaDisponibilidadDate = fechaDisponibilidadDate;
    }
    
}
