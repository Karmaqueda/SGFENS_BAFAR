/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MOVILPYME
 */
public class InsertPedidoDevolucionesResponse extends WSResponse {
    private List<WsItemPedidosDevoluciones> wsItemPedidoDevoluciones = null;

    /**
     * @return the wsItemPedidoDevoluciones
     */
    public List<WsItemPedidosDevoluciones> getWsItemPedidoDevoluciones() {
        if(wsItemPedidoDevoluciones == null)
            wsItemPedidoDevoluciones = new ArrayList<WsItemPedidosDevoluciones>();
        return wsItemPedidoDevoluciones;
    }

    /**
     * @param wsItemPedidoDevoluciones the wsItemPedidoDevoluciones to set
     */
    public void setWsItemPedidoDevoluciones(List<WsItemPedidosDevoluciones> wsItemPedidoDevoluciones) {
        this.wsItemPedidoDevoluciones = wsItemPedidoDevoluciones;
    }
    
    
    
}
