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
 * @author ISCesar
 */
public class ConsultaAlmacenesResponse extends WSResponse {
    
    private List<WsItemAlmacen> listaAlmacenes;

    public List<WsItemAlmacen> getListaAlmacenes() {
        if (listaAlmacenes==null)
            listaAlmacenes = new ArrayList<WsItemAlmacen>();
        return listaAlmacenes;     
    }

    public void setListaAlmacenes(List<WsItemAlmacen> listaAlmacenes) {
        this.listaAlmacenes = listaAlmacenes;
    }

}
