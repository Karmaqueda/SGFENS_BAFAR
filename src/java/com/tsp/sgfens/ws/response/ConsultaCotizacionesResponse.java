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
 * @author 578
 */
public class ConsultaCotizacionesResponse extends WSResponse implements Serializable {
    
    private List<WsItemCotizacion> wsItemCotizacion;

    public List<WsItemCotizacion> getWsItemCotizacion() {
        if (wsItemCotizacion==null)
            wsItemCotizacion = new ArrayList<WsItemCotizacion>();
        return wsItemCotizacion;
    }

    public void setWsItemCotizacion(List<WsItemCotizacion> wsItemCotizacion) {
        this.wsItemCotizacion = wsItemCotizacion;
    }
    
}
