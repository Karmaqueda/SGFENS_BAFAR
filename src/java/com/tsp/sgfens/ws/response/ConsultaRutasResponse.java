/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 28-mar-2013 
 */
public class ConsultaRutasResponse extends WSResponse{

    private List<WsItemRuta> wsItemRuta;

    public List<WsItemRuta> getWsItemRuta() {
        if (wsItemRuta==null)
            wsItemRuta = new ArrayList<WsItemRuta>();
        return wsItemRuta;
    }

    public void setWsItemRuta(List<WsItemRuta> wsItemRuta) {
        this.wsItemRuta = wsItemRuta;
    }
    
}
