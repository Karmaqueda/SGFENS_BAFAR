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
 * @author HpPyme
 */
public class ConsultaGastosResponse extends WSResponse {
    
    private List<WsItemGastosEvc> listaGastos;

    public List<WsItemGastosEvc> getListaGastos() {
        if (listaGastos==null)
            listaGastos = new ArrayList<WsItemGastosEvc>();
        return listaGastos;     
    }

    public void setListaGastos(List<WsItemGastosEvc> listaGastos) {
        this.listaGastos = listaGastos;
    }

    

    
    
}
