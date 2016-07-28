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
public class UpdateAgendaResponse extends WSResponseInsert {
    private List<WsItemUpdateAgenda> wsItemUpdateAgenda = null;

    /**
     * @return the wsItemUpdateAgenda
     */
    public List<WsItemUpdateAgenda> getWsItemUpdateAgenda() {
        if(wsItemUpdateAgenda == null)
            wsItemUpdateAgenda = new ArrayList<WsItemUpdateAgenda>();
        return wsItemUpdateAgenda;
    }

    /**
     * @param wsItemUpdateAgenda the wsItemUpdateAgenda to set
     */
    public void setWsItemUpdateAgenda(List<WsItemUpdateAgenda> wsItemUpdateAgenda) {
        this.wsItemUpdateAgenda = wsItemUpdateAgenda;
    }
    
    
    
}
