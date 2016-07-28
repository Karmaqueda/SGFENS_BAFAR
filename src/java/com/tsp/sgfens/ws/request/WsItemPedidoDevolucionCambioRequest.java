/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author MOVILPYME
 */
public class WsItemPedidoDevolucionCambioRequest implements Serializable{
    
    private int idTipo;
    private int idEstatus;
    private int idClasificacion;
    private int idConcepto;
    private String descripcionClasificacion;
    private double aptoParaVenta;
    private double noAptoParaVenta;
    private double montoResultante;
    private int diferenciaFavor;
    private Date fecha;
    private int idConceptoEntregado;
    private int idPedido;
    private int idPedidoDevolucionCambio;
    private double cantidadDevuelta;
    private String folioMovil;
    private double devolucionEfectivo;

    /**
     * @return the idTipo
     */
    public int getIdTipo() {
        return idTipo;
    }

    /**
     * @param idTipo the idTipo to set
     */
    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
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
     * @return the idClasificacion
     */
    public int getIdClasificacion() {
        return idClasificacion;
    }

    /**
     * @param idClasificacion the idClasificacion to set
     */
    public void setIdClasificacion(int idClasificacion) {
        this.idClasificacion = idClasificacion;
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
     * @return the descripcionClasificacion
     */
    public String getDescripcionClasificacion() {
        return descripcionClasificacion;
    }

    /**
     * @param descripcionClasificacion the descripcionClasificacion to set
     */
    public void setDescripcionClasificacion(String descripcionClasificacion) {
        this.descripcionClasificacion = descripcionClasificacion;
    }

    /**
     * @return the aptoParaVenta
     */
    public double getAptoParaVenta() {
        return aptoParaVenta;
    }

    /**
     * @param aptoParaVenta the aptoParaVenta to set
     */
    public void setAptoParaVenta(double aptoParaVenta) {
        this.aptoParaVenta = aptoParaVenta;
    }

    /**
     * @return the noAptoParaVenta
     */
    public double getNoAptoParaVenta() {
        return noAptoParaVenta;
    }

    /**
     * @param noAptoParaVenta the noAptoParaVenta to set
     */
    public void setNoAptoParaVenta(double noAptoParaVenta) {
        this.noAptoParaVenta = noAptoParaVenta;
    }

    /**
     * @return the montoResultante
     */
    public double getMontoResultante() {
        return montoResultante;
    }

    /**
     * @param montoResultante the montoResultante to set
     */
    public void setMontoResultante(double montoResultante) {
        this.montoResultante = montoResultante;
    }

    /**
     * @return the diferenciaFavor
     */
    public int getDiferenciaFavor() {
        return diferenciaFavor;
    }

    /**
     * @param diferenciaFavor the diferenciaFavor to set
     */
    public void setDiferenciaFavor(int diferenciaFavor) {
        this.diferenciaFavor = diferenciaFavor;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the idConceptoEntregado
     */
    public int getIdConceptoEntregado() {
        return idConceptoEntregado;
    }

    /**
     * @param idConceptoEntregado the idConceptoEntregado to set
     */
    public void setIdConceptoEntregado(int idConceptoEntregado) {
        this.idConceptoEntregado = idConceptoEntregado;
    }

    /**
     * @return the idPedido
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * @param idPedido the idPedido to set
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * @return the idPedidoDevolucionCambio
     */
    public int getIdPedidoDevolucionCambio() {
        return idPedidoDevolucionCambio;
    }

    /**
     * @param idPedidoDevolucionCambio the idPedidoDevolucionCambio to set
     */
    public void setIdPedidoDevolucionCambio(int idPedidoDevolucionCambio) {
        this.idPedidoDevolucionCambio = idPedidoDevolucionCambio;
    }

    /**
     * @return the cantidadDevuelta
     */
    public double getCantidadDevuelta() {
        return cantidadDevuelta;
    }

    /**
     * @param cantidadDevuelta the cantidadDevuelta to set
     */
    public void setCantidadDevuelta(double cantidadDevuelta) {
        this.cantidadDevuelta = cantidadDevuelta;
    }

    public String getFolioMovil() {
        return folioMovil;
    }

    public void setFolioMovil(String folioMovil) {
        this.folioMovil = folioMovil;
    }

    public double getDevolucionEfectivo() {
        return devolucionEfectivo;
    }

    public void setDevolucionEfectivo(double devolucionEfectivo) {
        this.devolucionEfectivo = devolucionEfectivo;
    }
    
    
}
