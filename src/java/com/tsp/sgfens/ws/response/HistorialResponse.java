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
public class HistorialResponse extends WSResponseInsert {
    private List<WSResponseInsert> wsInsertResponse = null;

    /**
     * @return the wsInsertResponse
     */
    public List<WSResponseInsert> getWsInsertResponse() {
        if(wsInsertResponse == null)
            wsInsertResponse = new ArrayList<WSResponseInsert>();
        return wsInsertResponse;
    }

    /**
     * @param wsInsertResponse the wsInsertResponse to set
     */
    public void setWsInsertResponse(List<WSResponseInsert> wsInsertResponse) {
        this.wsInsertResponse = wsInsertResponse;
    }
    
}
