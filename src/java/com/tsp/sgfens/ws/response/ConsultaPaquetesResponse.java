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
 * @author MOVILPYME
 */
public class ConsultaPaquetesResponse extends WSResponse implements Serializable {
    protected List<WsItemPaquete> wsItemPaquete;

    /**
     * @return the wsItemPaquete
     */
    public List<WsItemPaquete> getWsItemPaquete() {
        if(wsItemPaquete == null)
            wsItemPaquete = new ArrayList<WsItemPaquete>();
        return wsItemPaquete;
    }

    /**
     * @param wsItemPaquete the wsItemPaquete to set
     */
    public void setWsItemPaquete(List<WsItemPaquete> wsItemPaquete) {
        this.wsItemPaquete = wsItemPaquete;
    }
    
}
