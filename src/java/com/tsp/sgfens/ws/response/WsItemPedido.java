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
 * @author ISCesar poseidon24@hotmail.com
 * @since 7/10/2014 12:24:10 PM
 */
public class WsItemPedido implements Serializable{

    private int idPedido;
    private int idEmpresa;
    private int idCliente;
    private int consecutivoPedido;
    private String folioPedido;
    private Date fechaPedido;
    private String tipoMoneda;
    private int tiempoEntregaDias;
    private String comentarios;
    private double descuentoTasa;
    private double descuentoMonto;
    private double subtotal;
    private double total;
    private String descuentoMotivo;
    private Date fechaEntrega;
    private Date fechaTentativaPago;
    private double saldoPagado;
    private double adelanto;
    private int idComprobanteFiscal;
    private short idEstatusPedido;
    private double latitud;
    private double longitud;
    private String folioPedidoMovil;
    private List<WsItemPedidoConcepto> wsItemPedidoConcepto;
    private List<WsItemPedidoImpuesto> wsItemPedidoImpuesto;
    private List<WsItemPedidoCobranzaAbono> wsItemCobranzaAbono;
    private int sincronizacionMicrosip;

    //Datos para serie y folio internos para pedido movil (solo informativos)
    private int idServidorFolioMovilEmpleado;
    private String folioMovilEmpleadoGenerado;
    
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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

    public int getConsecutivoPedido() {
        return consecutivoPedido;
    }

    public void setConsecutivoPedido(int consecutivoPedido) {
        this.consecutivoPedido = consecutivoPedido;
    }

    public String getFolioPedido() {
        return folioPedido;
    }

    public void setFolioPedido(String folioPedido) {
        this.folioPedido = folioPedido;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
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

    public String getDescuentoMotivo() {
        return descuentoMotivo;
    }

    public void setDescuentoMotivo(String descuentoMotivo) {
        this.descuentoMotivo = descuentoMotivo;
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

    public double getSaldoPagado() {
        return saldoPagado;
    }

    public void setSaldoPagado(double saldoPagado) {
        this.saldoPagado = saldoPagado;
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

    public short getIdEstatusPedido() {
        return idEstatusPedido;
    }

    public void setIdEstatusPedido(short idEstatusPedido) {
        this.idEstatusPedido = idEstatusPedido;
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

    public List<WsItemPedidoConcepto> getWsItemPedidoConcepto() {
        if (wsItemPedidoConcepto==null)
            wsItemPedidoConcepto = new ArrayList<WsItemPedidoConcepto>();
        return wsItemPedidoConcepto;
    }

    public void setWsItemPedidoConcepto(List<WsItemPedidoConcepto> wsItemPedidoConcepto) {
        this.wsItemPedidoConcepto = wsItemPedidoConcepto;
    }

    public List<WsItemPedidoImpuesto> getWsItemPedidoImpuesto() {
        if (wsItemPedidoImpuesto==null)
            wsItemPedidoImpuesto = new ArrayList<WsItemPedidoImpuesto>();
        return wsItemPedidoImpuesto;
    }

    public void setWsItemPedidoImpuesto(List<WsItemPedidoImpuesto> wsItemPedidoImpuesto) {
        this.wsItemPedidoImpuesto = wsItemPedidoImpuesto;
    }

    public List<WsItemPedidoCobranzaAbono> getWsItemCobranzaAbono() {
        if (wsItemCobranzaAbono==null)
            wsItemCobranzaAbono = new ArrayList<WsItemPedidoCobranzaAbono>();
        return wsItemCobranzaAbono;
    }

    public void setWsItemCobranzaAbono(List<WsItemPedidoCobranzaAbono> wsItemCobranzaAbono) {
        this.wsItemCobranzaAbono = wsItemCobranzaAbono;
    }

    public int getSincronizacionMicrosip() {
        return sincronizacionMicrosip;
    }

    public void setSincronizacionMicrosip(int sincronizacionMicrosip) {
        this.sincronizacionMicrosip = sincronizacionMicrosip;
    }

    public int getIdServidorFolioMovilEmpleado() {
        return idServidorFolioMovilEmpleado;
    }

    public void setIdServidorFolioMovilEmpleado(int idServidorFolioMovilEmpleado) {
        this.idServidorFolioMovilEmpleado = idServidorFolioMovilEmpleado;
    }

    public String getFolioMovilEmpleadoGenerado() {
        return folioMovilEmpleadoGenerado;
    }

    public void setFolioMovilEmpleadoGenerado(String folioMovilEmpleadoGenerado) {
        this.folioMovilEmpleadoGenerado = folioMovilEmpleadoGenerado;
    }
    
}