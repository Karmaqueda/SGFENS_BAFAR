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
public class ConsultaAgendaResponse extends WSResponse implements Serializable{
    private List<WsItemAgendaResponse> wsItemAgenda;

    /**
     * @return the wsItemAgenda
     */
    public List<WsItemAgendaResponse> getWsItemAgenda() {
        if(wsItemAgenda == null)
            wsItemAgenda = new ArrayList<WsItemAgendaResponse>();
        return wsItemAgenda;
    }

    /**
     * @param wsItemAgenda the wsItemAgenda to set
     */
    public void setWsItemAgenda(List<WsItemAgendaResponse> wsItemAgenda) {
        this.wsItemAgenda = wsItemAgenda;
    }
}
