/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class CrearPedidoRequest {
    private WsItemConceptoRequest[] wsItemConceptoRequest;
    private int idClienteReceptor;
    
    private double subtotal=0;
    private double total=0;
    private double abonoInicial=0;
    private boolean ventaGenerica=false;
    private String folioPedidoMovil;
    private String abonoInicialFolioCobranzaMovil;
    private String imagenFirmaBytesBase64;
    private String referencia;
    
    /**
     * Datos de la operación bancaria, en caso de no existir sera nulo.
     * 
     * Dentro del objeto, los datos idEmpresa e idEstatus no se llenan
     */
    private WsBancoOperacionRequest bancoOperacion;
    private WsDatosMetodoPagoRequest wsDatosMetodoPagoRequest;
    
    private Date fechaPedido;
    private Date fechaEntrega;
    private int entregado = 1;//Estatus pedido
    private int idEstatus = 2;//Tipo Entrega  2 - Completa 4 - Parcial
    
    private WsItemImpuestoRequest[] listaImpuestos;
    
    private boolean enviarCorreoPedido=false;
    private boolean enviarCorreoReciboPago=false;
    private String[] listaCorreosDestinatarios= new String[0];
    
    private double bonificacionDevolucion;
    
    private int sincronizacionMicrosip = 0;//Estatus pedido
    
    private int idServidorFolioMovilEmpleado;
    private String folioMovilEmpleadoGenerado;

    public int getIdClienteReceptor() {
        return idClienteReceptor;
    }

    public void setIdClienteReceptor(int idClienteReceptor) {
        this.idClienteReceptor = idClienteReceptor;
    }

    public WsItemConceptoRequest[] getWsItemConceptoRequest() {
        return wsItemConceptoRequest;
    }

    public void setWsItemConceptoRequest(WsItemConceptoRequest[] wsItemConceptoRequest) {
        this.wsItemConceptoRequest = wsItemConceptoRequest;
    }

    public WsBancoOperacionRequest getBancoOperacion() {
        return bancoOperacion;
    }

    public void setBancoOperacion(WsBancoOperacionRequest bancoOperacion) {
        this.bancoOperacion = bancoOperacion;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isVentaGenerica() {
        return ventaGenerica;
    }

    public void setVentaGenerica(boolean ventaGenerica) {
        this.ventaGenerica = ventaGenerica;
    }

    public WsDatosMetodoPagoRequest getWsDatosMetodoPagoRequest() {
        return wsDatosMetodoPagoRequest;
    }

    public void setWsDatosMetodoPagoRequest(WsDatosMetodoPagoRequest wsDatosMetodoPagoRequest) {
        this.wsDatosMetodoPagoRequest = wsDatosMetodoPagoRequest;
    }

    public double getAbonoInicial() {
        return abonoInicial;
    }

    public void setAbonoInicial(double abonoInicial) {
        this.abonoInicial = abonoInicial;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public int getEntregado() {
        return entregado;
    }

    public void setEntregado(int entregado) {
        this.entregado = entregado;
    }    

    public WsItemImpuestoRequest[] getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(WsItemImpuestoRequest[] listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public boolean isEnviarCorreoPedido() {
        return enviarCorreoPedido;
    }

    public void setEnviarCorreoPedido(boolean enviarCorreoPedido) {
        this.enviarCorreoPedido = enviarCorreoPedido;
    }

    public boolean isEnviarCorreoReciboPago() {
        return enviarCorreoReciboPago;
    }

    public void setEnviarCorreoReciboPago(boolean enviarCorreoReciboPago) {
        this.enviarCorreoReciboPago = enviarCorreoReciboPago;
    }

    public String[] getListaCorreosDestinatarios() {
        return listaCorreosDestinatarios;
    }

    public void setListaCorreosDestinatarios(String[] listaCorreosDestinatarios) {
        this.listaCorreosDestinatarios = listaCorreosDestinatarios;
    }

    public String getAbonoInicialFolioCobranzaMovil() {
        return abonoInicialFolioCobranzaMovil;
    }

    public void setAbonoInicialFolioCobranzaMovil(String abonoInicialFolioCobranzaMovil) {
        this.abonoInicialFolioCobranzaMovil = abonoInicialFolioCobranzaMovil;
    }

    public String getFolioPedidoMovil() {
        return folioPedidoMovil;
    }

    public void setFolioPedidoMovil(String folioPedidoMovil) {
        this.folioPedidoMovil = folioPedidoMovil;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    /**
     * @return the imagenFirmaBytesBase64
     */
    public String getImagenFirmaBytesBase64() {
        return imagenFirmaBytesBase64;
    }

    /**
     * @param imagenFirmaBytesBase64 the imagenFirmaBytesBase64 to set
     */
    public void setImagenFirmaBytesBase64(String imagenFirmaBytesBase64) {
        this.imagenFirmaBytesBase64 = imagenFirmaBytesBase64;
    }

    public double getBonificacionDevolucion() {
        return bonificacionDevolucion;
    }

    public void setBonificacionDevolucion(double bonificacionDevolucion) {
        this.bonificacionDevolucion = bonificacionDevolucion;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public int getSincronizacionMicrosip() {
        return sincronizacionMicrosip;
    }

    public void setSincronizacionMicrosip(int sincronizacionMicrosip) {
        this.sincronizacionMicrosip = sincronizacionMicrosip;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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
