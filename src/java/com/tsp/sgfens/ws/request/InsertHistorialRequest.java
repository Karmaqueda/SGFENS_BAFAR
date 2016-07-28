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
 * @author Ing. Roberto Trejo
 */
public class InsertHistorialRequest {
     private List<WsItemHistorial> wsItemHistorial = null;

    /**
     * @return the wsItemHistorial
     */
    public List<WsItemHistorial  > getWsItemHistorial() {
        if(wsItemHistorial == null)
            wsItemHistorial = new ArrayList<WsItemHistorial>();
        return wsItemHistorial;
    }

    /**
     * @param wsItemHistorial the wsItemHistorial to set
     */
    public void setWsItemHistorial(List<WsItemHistorial  > wsItemHistorial) {
        this.wsItemHistorial = wsItemHistorial;
    }
    
}
