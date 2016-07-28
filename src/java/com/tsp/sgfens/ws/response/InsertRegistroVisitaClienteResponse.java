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
public class InsertRegistroVisitaClienteResponse extends WSResponseInsert  {
    protected List<WsItemRegistroVisitaCliente> wsItemRegistroVisita = null;

    /**
     * @return the wsItemRegistroVisita
     */
    public List<WsItemRegistroVisitaCliente> getWsItemRegistroVisita() {
        if(wsItemRegistroVisita == null)
            wsItemRegistroVisita = new ArrayList<WsItemRegistroVisitaCliente>();
        return wsItemRegistroVisita;
    }

    /**
     * @param wsItemRegistroVisita the wsItemRegistroVisita to set
     */
    public void setWsItemRegistroVisita(List<WsItemRegistroVisitaCliente> wsItemRegistroVisita) {
        this.wsItemRegistroVisita = wsItemRegistroVisita;
    }
    
}
