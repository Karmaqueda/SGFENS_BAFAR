/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 578
 */
public class WsItemCotizacion implements Serializable {
    
    
    private int idCotizacion;
    private int idEmpresa;
    private int idCliente;
    private int idProspecto;
    private int consecutivoCotizacion;
    private String folioCotizacion;
    private Date fechaCotizacion;
    private String tipoMoneda;    
    private int tiempoEntregaDias;
    private String comentarios;
    private double descuentoTasa;
    private double descuentoMonto;
    private String descuentoMotivo;
    private double subtotal;
    private double total;
    private short idEstatusCotizacion;
    private double latitud;
    private double longitud;
    private String folioCotizacionMovil;
    private List<WsItemPedidoConcepto> wsItemCotizacionConcepto;
    private List<WsItemPedidoImpuesto> wsItemCotizacionImpuesto;

    public int getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(int idProspecto) {
        this.idProspecto = idProspecto;
    }
    
    public int getConsecutivoCotizacion() {
        return consecutivoCotizacion;
    }

    public void setConsecutivoCotizacion(int consecutivoCotizacion) {
        this.consecutivoCotizacion = consecutivoCotizacion;
    }

    public String getFolioCotizacion() {
        return folioCotizacion;
    }

    public void setFolioCotizacion(String folioCotizacion) {
        this.folioCotizacion = folioCotizacion;
    }

    public Date getFechaCotizacion() {
        return fechaCotizacion;
    }

    public void setFechaCotizacion(Date fechaCotizacion) {
        this.fechaCotizacion = fechaCotizacion;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public int getTiempoEntregaDias() {
        return tiempoEntregaDias;
    }

    public void setTiempoEntregaDias(int tiempoEntregaDias) {
        this.tiempoEntregaDias = tiempoEntregaDias;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public double getDescuentoTasa() {
        return descuentoTasa;
    }

    public void setDescuentoTasa(double descuentoTasa) {
        this.descuentoTasa = descuentoTasa;
    }

    public double getDescuentoMonto() {
        return descuentoMonto;
    }

    public void setDescuentoMonto(double descuentoMonto) {
        this.descuentoMonto = descuentoMonto;
    }

    public String getDescuentoMotivo() {
        return descuentoMotivo;
    }

    public void setDescuentoMotivo(String descuentoMotivo) {
        this.descuentoMotivo = descuentoMotivo;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public short getIdEstatusCotizacion() {
        return idEstatusCotizacion;
    }

    public void setIdEstatusCotizacion(short idEstatusCotizacion) {
        this.idEstatusCotizacion = idEstatusCotizacion;
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

    public String getFolioCotizacionMovil() {
        return folioCotizacionMovil;
    }

    public void setFolioCotizacionMovil(String folioCotizacionMovil) {
        this.folioCotizacionMovil = folioCotizacionMovil;
    }

    public List<WsItemPedidoConcepto> getWsItemCotizacionConcepto() {
        if(wsItemCotizacionConcepto==null)
            wsItemCotizacionConcepto =  new ArrayList<WsItemPedidoConcepto>();
        return wsItemCotizacionConcepto;
    }

    public void setWsItemCotizacionConcepto(List<WsItemPedidoConcepto> wsItemCotizacionConcepto) {
        this.wsItemCotizacionConcepto = wsItemCotizacionConcepto;
    }

    public List<WsItemPedidoImpuesto> getWsItemCotizacionImpuesto() {
        if(wsItemCotizacionImpuesto==null)
            wsItemCotizacionImpuesto =  new ArrayList<WsItemPedidoImpuesto>();
        return wsItemCotizacionImpuesto;
    }

    public void setWsItemCotizacionImpuesto(List<WsItemPedidoImpuesto> wsItemCotizacionImpuesto) {
        this.wsItemCotizacionImpuesto = wsItemCotizacionImpuesto;
    }
        
    
}
