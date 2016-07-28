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
public class AgendaRequest {
    
    private List<WsItemAgenda> wsItemAgenda = null;
    
    public AgendaRequest(){
        
    }

    /**
     * @return the wsItemAgenda
     */
    public List<WsItemAgenda> getWsItemAgenda() {
        if(wsItemAgenda == null)
            wsItemAgenda = new ArrayList<WsItemAgenda>();
        return wsItemAgenda;
    }

    /**
     * @param wsItemAgenda the wsItemAgenda to set
     */
    public void setWsItemAgenda(List<WsItemAgenda> wsItemAgenda) {
        this.wsItemAgenda = wsItemAgenda;
    }
    
}
