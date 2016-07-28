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
public class WsListaProveedorConceptosServiciosProveedores {
    
    private ArrayList<WsItemProveedorConceptoServicio> lista;

    public WsListaProveedorConceptosServiciosProveedores(){
        lista = new ArrayList<WsItemProveedorConceptoServicio>();
    }

    public void addItem(WsItemProveedorConceptoServicio data){
        this.lista.add(data);
    }

    public WsItemProveedorConceptoServicio getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemProveedorConceptoServicio> getLista() {
        return lista;
    }
    
}
