/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.util.ArrayList;

/**
 *
 * @author leonardo
 */
public class WsListaConceptosServiciosProveedores {
    
    private ArrayList<WsItemConceptoServicio> lista;

    public WsListaConceptosServiciosProveedores(){
        lista = new ArrayList<WsItemConceptoServicio>();
    }

    public void addItem(WsItemConceptoServicio data){
        this.lista.add(data);
    }

    public WsItemConceptoServicio getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemConceptoServicio> getLista() {
        return lista;
    }
    
}
