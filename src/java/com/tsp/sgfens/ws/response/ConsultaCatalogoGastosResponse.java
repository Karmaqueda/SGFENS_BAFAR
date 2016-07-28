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
 * @author HpPyme
 */
public class ConsultaCatalogoGastosResponse extends WSResponse {
    
    private List<WsItemCatalogoGastos> wsItemCatalogoGastos;

    public List<WsItemCatalogoGastos> getWsItemCatalogoGastos() {
        if(wsItemCatalogoGastos == null)
            wsItemCatalogoGastos = new ArrayList<WsItemCatalogoGastos>();
        return wsItemCatalogoGastos;
    }

    public void setWsItemCatalogoGastos(List<WsItemCatalogoGastos> wsItemCatalogoGastos) {
        this.wsItemCatalogoGastos = wsItemCatalogoGastos;
    }
    
    
}
