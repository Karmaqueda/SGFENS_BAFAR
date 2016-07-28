/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roberto Trejo
 * 
 */
public class DevolucionesDtoRequest {
    
    List<WsItemPedidoDevolucionCambioRequest> wsITemDevolucion = null;
    
    public DevolucionesDtoRequest(){
    }

    /**
     * @return the wsITemDevolucion
     */
    public List<WsItemPedidoDevolucionCambioRequest> getWsITemDevolucion() {
        if(wsITemDevolucion == null){
            wsITemDevolucion = new ArrayList<WsItemPedidoDevolucionCambioRequest>();
        }
        return wsITemDevolucion;
    }

    /**
     * @param wsITemDevolucion the wsITemDevolucion to set
     */
    public void setWsITemDevolucion(List<WsItemPedidoDevolucionCambioRequest> wsITemDevolucion) {
        this.wsITemDevolucion = wsITemDevolucion;
    }
    
    
}
