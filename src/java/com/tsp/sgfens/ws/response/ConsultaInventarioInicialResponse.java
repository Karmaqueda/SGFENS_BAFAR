/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class ConsultaInventarioInicialResponse  extends WSResponse implements Serializable {
    private List<WsItemInventarioInicialResponse> wsItemInventarioInicial = null;

    /**
     * @return the wsItemInventarioInicial
     */
    public List<WsItemInventarioInicialResponse> getWsItemInventarioInicial() {
        if(wsItemInventarioInicial == null)
            wsItemInventarioInicial = new ArrayList<WsItemInventarioInicialResponse>();
        return wsItemInventarioInicial;
    }

    /**
     * @param wsItemInventarioInicial the wsItemInventarioInicial to set
     */
    public void setWsItemInventarioInicial(List<WsItemInventarioInicialResponse> wsItemInventarioInicial) {
        this.wsItemInventarioInicial = wsItemInventarioInicial;
    }
}
