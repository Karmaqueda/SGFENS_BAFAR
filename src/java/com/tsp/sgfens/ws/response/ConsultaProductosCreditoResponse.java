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
 * @author Ing. Roberto Trejo
 */
public class ConsultaProductosCreditoResponse extends WSResponse {
    
    protected List<WsItemProductoCredito> wsItemProductoCredito;

    /**
     * @return the wsItemProductoCredito
     */
    public List<WsItemProductoCredito> getWsItemProductoCredito() {
        if(wsItemProductoCredito == null)
            wsItemProductoCredito = new ArrayList<WsItemProductoCredito>();
        return wsItemProductoCredito;
    }

    /**
     * @param wsItemProductoCredito the wsItemProductoCredito to set
     */
    public void setWsItemProductoCredito(List<WsItemProductoCredito> wsItemProductoCredito) {
        this.wsItemProductoCredito = wsItemProductoCredito;
    }
    
}
