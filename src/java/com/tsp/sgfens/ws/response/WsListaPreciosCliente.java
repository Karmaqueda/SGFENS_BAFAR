/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.util.ArrayList;

/**
 *
 * @author HpPyme
 */
public class WsListaPreciosCliente {
    
    private ArrayList<WsItemPrecioCliente> lista;

    public WsListaPreciosCliente(){
        lista = new ArrayList<WsItemPrecioCliente>();
    }

    public void addItem(WsItemPrecioCliente data){
        this.lista.add(data);
    }

    public WsItemPrecioCliente getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemPrecioCliente> getLista() {
        return lista;
    }

    public void setLista(ArrayList<WsItemPrecioCliente> lista) {
        this.lista = lista;
    }
    
    
    
}
