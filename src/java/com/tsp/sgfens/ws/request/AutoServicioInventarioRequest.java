/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesar
 */
public class AutoServicioInventarioRequest {
    
    /**
     * tipoMovimiento
     * 1 : Carga
     * 2 : Devolucion
     * 3 : Merma
     */
    private int tipoMovimiento;
    private int idAlmacen;

    /**
     * Listado de productos en movimiento
     * 
     * Solo se utilizan 2 datos por concepto:
     *  idConcepto y cantidad
     *  
     * posiblemente: idInventarioEmpleado , para saber exactamente a que producto se refiere
     *  en la tabla empleado_inventario_repartidor
     */
    private WsItemConceptoRequest[] wsItemConceptoRequest;
    
    private boolean activarInventarioInicial = false;
    
    public int getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(int tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public WsItemConceptoRequest[] getWsItemConceptoRequest() {
        return wsItemConceptoRequest;
    }

    public void setWsItemConceptoRequest(WsItemConceptoRequest[] wsItemConceptoRequest) {
        this.wsItemConceptoRequest = wsItemConceptoRequest;
    }

    public boolean isActivarInventarioInicial() {
        return activarInventarioInicial;
    }

    public void setActivarInventarioInicial(boolean activarInventarioInicial) {
        this.activarInventarioInicial = activarInventarioInicial;
    }

}
