/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;

/**
 *
 * @author ISCesar
 */
public class WsListaAlmacenes {
    
    
    private ArrayList<WsItemAlmacen> lista;

    public WsListaAlmacenes(){
        lista = new ArrayList<WsItemAlmacen>();
    }

    public void addItem(WsItemAlmacen data){
        this.lista.add(data);
    }

    public WsItemAlmacen getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemAlmacen> getLista() {
        return lista;
    }
    
    
    
}
