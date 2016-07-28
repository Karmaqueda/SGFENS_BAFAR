/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.bo.SGEstatusPedidoBO;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class PedidoSesion extends FormatoSesion {
    
    private int idPedido = -1;
    
    private double adelanto = 0;
    private double saldoPagado = 0;
    
    private int idComprobanteFiscal = 0;
    
    private Date fechaEntrega = null;
    private Date fechaTentativaPago = null;
    
    private int idEstatus = SGEstatusPedidoBO.ESTATUS_PENDIENTE;
    
    private double latitud=0;
    private double longitud=0;
    
    private String folioPedidoMovil = null;
    
    private String rutaImagen = "";
    
    private int consigna = 0;
    
    private int sincronizacionMicrosip = 0;
    
    private int idCotizacionOrigen = 0;
    
    private int idUsuarioVendedor = 0;

    public PedidoSesion() {
        super();
    }
    
    
    public double getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(double adelanto) {
        this.adelanto = adelanto;
    }

    public int getIdComprobanteFiscal() {
        return idComprobanteFiscal;
    }

    public void setIdComprobanteFiscal(int idComprobanteFiscal) {
        this.idComprobanteFiscal = idComprobanteFiscal;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public double getSaldoPagado() {
        return saldoPagado;
    }

    public void setSaldoPagado(double saldoPagado) {
        this.saldoPagado = saldoPagado;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaTentativaPago() {
        return fechaTentativaPago;
    }

    public void setFechaTentativaPago(Date fechaTentativaPago) {
        this.fechaTentativaPago = fechaTentativaPago;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFolioPedidoMovil() {
        return folioPedidoMovil;
    }

    public void setFolioPedidoMovil(String folioPedidoMovil) {
        this.folioPedidoMovil = folioPedidoMovil;
    }

    /**
     * @return the rutaImagen
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    /**
     * @param rutaImagen the rutaImagen to set
     */
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public int getConsigna() {
        return consigna;
    }

    public void setConsigna(int consigna) {
        this.consigna = consigna;
    }

    public int getSincronizacionMicrosip() {
        return sincronizacionMicrosip;
    }

    public void setSincronizacionMicrosip(int sincronizacionMicrosip) {
        this.sincronizacionMicrosip = sincronizacionMicrosip;
    }

    public int getIdCotizacionOrigen() {
        return idCotizacionOrigen;
    }

    public void setIdCotizacionOrigen(int idCotizacionOrigen) {
        this.idCotizacionOrigen = idCotizacionOrigen;
    }

    public int getIdUsuarioVendedor() {
        return idUsuarioVendedor;
    }

    public void setIdUsuarioVendedor(int idUsuarioVendedor) {
        this.idUsuarioVendedor = idUsuarioVendedor;
    }
    
    
       
    
}
