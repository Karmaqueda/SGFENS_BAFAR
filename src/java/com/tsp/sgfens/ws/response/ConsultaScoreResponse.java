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
public class ConsultaScoreResponse extends WSResponse {
    
    protected List<WsItemScore> wsItemScore;

    /**
     * @return the wsItemScore
     */
    public List<WsItemScore> getWsItemScore() {
        if(wsItemScore == null)
            wsItemScore = new ArrayList<WsItemScore>();
        return wsItemScore;
    }

    /**
     * @param wsItemScore the wsItemScore to set
     */
    public void setWsItemScore(List<WsItemScore> wsItemScore) {
        this.wsItemScore = wsItemScore;
    }
    
}
